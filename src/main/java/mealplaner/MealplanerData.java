package mealplaner;

import static java.lang.Math.toIntExact;
import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static mealplaner.DataStoreEventType.DATABASE_EDITED;
import static mealplaner.DataStoreEventType.DATE_UPDATED;
import static mealplaner.DataStoreEventType.PROPOSAL_ADDED;
import static mealplaner.DataStoreEventType.SETTINGS_CHANGED;
import static mealplaner.io.XMLHelpers.generateDateXml;
import static mealplaner.io.XMLHelpers.getFirstNode;
import static mealplaner.io.XMLHelpers.logFailedXmlRetrieval;
import static mealplaner.io.XMLHelpers.parseDate;
import static mealplaner.model.settings.Settings.defaultSetting;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import mealplaner.errorhandling.MealException;
import mealplaner.io.XMLHelpers;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;
import mealplaner.model.settings.Settings;

// TODO: Error handling and ResourceBundles
public class MealplanerData implements DataStore {
	private static final Logger logger = LoggerFactory.getLogger(Settings.class);

	private List<Meal> meals;
	private Settings[] defaultSettings;
	private LocalDate date;
	private Proposal proposal;

	private List<DataStoreListener> listeners = new ArrayList<>();

	public MealplanerData() {
		meals = new ArrayList<Meal>();
		date = now();
		defaultSettings = new Settings[7];
		for (int i = 0; i < defaultSettings.length; i++) {
			defaultSettings[i] = defaultSetting();
		}
		proposal = new Proposal();
	}

	public MealplanerData(List<Meal> meals, LocalDate date,
			Settings[] defaultSettings, Proposal proposal) throws MealException {
		this.meals = meals;
		this.date = date;
		setDefaultSettings(defaultSettings);
		this.proposal = proposal;
	}

	@Override
	public int getDaysPassed() {
		return toIntExact(DAYS.between(date, now()));
	}

	@Override
	public LocalDate getTime() {
		return date;
	}

	@Override
	public Settings[] getDefaultSettings() {
		return defaultSettings;
	}

	@Override
	public Proposal getLastProposal() {
		return proposal;
	}

	@Override
	public List<Meal> getMeals() {
		return meals;
	}

	public void setMeals(List<Meal> meals) {
		this.meals = meals;
		listeners.forEach(listener -> listener.updateData(DATABASE_EDITED));
	}

	public void addMeal(Meal neu) {
		meals.add(neu);
		meals.sort((meal1, meal2) -> meal1.compareTo(meal2));
		listeners.forEach(listener -> listener.updateData(DATABASE_EDITED));
	}

	public void setDefaultSettings(Settings[] defaultSettings) throws MealException {
		if (defaultSettings.length == 7) {
			for (Settings setting : defaultSettings) {
				if (setting == null) {
					throw new MealException("One of the default settings is null");
				}
			}
			this.defaultSettings = defaultSettings;
			listeners.forEach(listener -> listener.updateData(SETTINGS_CHANGED));
		} else {
			throw new MealException("Default settings not of size 7");
		}
	}

	public void setLastProposal(Proposal proposal) {
		this.proposal = proposal;
		listeners.forEach(listener -> listener.updateData(PROPOSAL_ADDED));
	}

	public void update(List<Meal> mealsCookedLast, LocalDate now) {
		long daysSinceLastUpdate = DAYS.between(date, now);
		meals.forEach(meal -> meal.setDaysPassed(mealsCookedLast.contains(meal)
				? mealsCookedLast.size() - mealsCookedLast.indexOf(meal) - 1
				: meal.getDaysPassed() + toIntExact(daysSinceLastUpdate)));
		listeners.forEach(listener -> listener.updateData(DATE_UPDATED));
		listeners.forEach(listener -> listener.updateData(DATABASE_EDITED));
	}

	@Override
	public void register(DataStoreListener listener) {
		listeners.add(listener);
	}

	public static MealplanerData readXml(Element mealPlanerNode) {
		Optional<Node> mealList = getFirstNode(mealPlanerNode, "mealList");
		List<Meal> meals = mealList.isPresent() && mealList.get().getNodeType() == Node.ELEMENT_NODE
				? XMLHelpers.getMealListFromXml((Element) mealList.get())
				: logFailedXmlRetrieval(new ArrayList<>(), "Proposal", mealPlanerNode);
		Optional<Node> settings = getFirstNode(mealPlanerNode, "defaultSettings");
		Settings[] defaultSettings = readDefaultSettings(mealPlanerNode, settings);
		Optional<Node> calendarNode = getFirstNode(mealPlanerNode, "date");
		LocalDate date = calendarNode.isPresent()
				&& calendarNode.get().getNodeType() == Node.ELEMENT_NODE
						? parseDate((Element) calendarNode.get())
						: logFailedXmlRetrieval(now(), "Calendar",
								mealPlanerNode);
		Optional<Node> proposalNode = getFirstNode(mealPlanerNode, "proposal");
		Proposal proposal = proposalNode.isPresent()
				&& proposalNode.get().getNodeType() == Node.ELEMENT_NODE
						? Proposal.getFromXml((Element) proposalNode.get())
						: logFailedXmlRetrieval(new Proposal(), "Proposal", mealPlanerNode);
		return new MealplanerData(meals, date, defaultSettings, proposal);
	}

	private static Settings[] readDefaultSettings(Element mealPlanerNode, Optional<Node> settings) {
		Settings[] defaultSettings = new Settings[7];
		if (settings.isPresent() && settings.get().getNodeType() == Node.ELEMENT_NODE) {
			Element settingsNode = (Element) settings.get();
			NodeList elementsByTagName = settingsNode.getElementsByTagName("setting");
			for (int i = 0; i < elementsByTagName.getLength(); i++) {
				int dayofWeek = Integer.parseInt(elementsByTagName
						.item(i).getAttributes().getNamedItem("dayOfWeek").getTextContent());
				defaultSettings[dayofWeek] = elementsByTagName.item(i)
						.getNodeType() == Node.ELEMENT_NODE
								? Settings.loadFromXml((Element) elementsByTagName.item(i))
								: logFailedXmlRetrieval(defaultSetting(), "Settings " + i,
										settingsNode);
			}
		} else {
			logger.warn("Settings node not found.");
		}
		for (int i = 0; i < defaultSettings.length; i++) {
			if (defaultSettings[i] == null) {
				defaultSettings[i] = defaultSetting();
				logger.warn("Setting " + i + " not correctly read in default settings");
			}
		}
		return defaultSettings;
	}

	public static Element generateXml(Document saveFileContent, MealplanerData mealplanerData) {
		Element mealplanerDataNode = saveFileContent.createElement("mealplaner");
		mealplanerDataNode
				.appendChild(XMLHelpers.saveMealsToXml(saveFileContent, mealplanerData.meals,
						"mealList"));
		Element defaultSettingsNode = saveFileContent.createElement("defaultSettings");
		for (int i = 0; i < mealplanerData.defaultSettings.length; i++) {
			defaultSettingsNode
					.appendChild(Settings.generateXml(saveFileContent,
							mealplanerData.defaultSettings[i], i, "setting"));
		}
		mealplanerDataNode.appendChild(defaultSettingsNode);
		mealplanerDataNode
				.appendChild(generateDateXml(saveFileContent, mealplanerData.date, "date"));
		mealplanerDataNode
				.appendChild(
						Proposal.saveToXml(saveFileContent, mealplanerData.proposal, "proposal"));
		return mealplanerDataNode;
	}
}