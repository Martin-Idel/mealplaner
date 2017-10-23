package mealplaner;

import static java.lang.Math.toIntExact;
import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.stream.Collectors.toList;
import static mealplaner.DataStoreEventType.DATABASE_EDITED;
import static mealplaner.DataStoreEventType.DATE_UPDATED;
import static mealplaner.DataStoreEventType.PROPOSAL_ADDED;
import static mealplaner.DataStoreEventType.SETTINGS_CHANGED;
import static mealplaner.io.XMLHelpers.getFirstNode;
import static mealplaner.io.XMLHelpers.logFailedXmlRetrieval;
import static mealplaner.io.XMLHelpers.parseDate;
import static mealplaner.io.XMLHelpers.saveMealsToXml;
import static mealplaner.io.XMLHelpers.writeDate;
import static mealplaner.model.Meal.addDaysPassed;
import static mealplaner.model.Meal.setDaysPassed;
import static mealplaner.model.Proposal.writeProposal;
import static mealplaner.model.settings.DefaultSettings.createDefaultSettings;
import static mealplaner.model.settings.DefaultSettings.writeDefaultSettings;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import mealplaner.io.XMLHelpers;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;
import mealplaner.model.settings.DefaultSettings;

// TODO: Error handling and ResourceBundles
public class MealplanerData implements DataStore {
	private List<Meal> meals;
	private DefaultSettings defaultSettings;
	private LocalDate date;
	private Proposal proposal;

	private List<DataStoreListener> listeners = new ArrayList<>();

	public MealplanerData() {
		meals = new ArrayList<Meal>();
		date = now();
		defaultSettings = createDefaultSettings();
		proposal = new Proposal();
	}

	public MealplanerData(
			List<Meal> meals,
			LocalDate date,
			DefaultSettings defaultSettings,
			Proposal proposal) {
		this.meals = meals;
		this.date = date;
		this.defaultSettings = defaultSettings;
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
	public DefaultSettings getDefaultSettings() {
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

	public void setDefaultSettings(DefaultSettings defaultSettings) {
		this.defaultSettings = defaultSettings;
		listeners.forEach(listener -> listener.updateData(SETTINGS_CHANGED));
	}

	public void setLastProposal(Proposal proposal) {
		this.proposal = proposal;
		listeners.forEach(listener -> listener.updateData(PROPOSAL_ADDED));
	}

	public void update(List<Meal> mealsCookedLast, LocalDate now) {
		long daysSinceLastUpdate = DAYS.between(date, now);
		meals = meals.stream().map(meal -> mealsCookedLast.contains(meal)
				? setDaysPassed(mealsCookedLast.size() - mealsCookedLast.indexOf(meal) - 1, meal)
				: addDaysPassed(toIntExact(daysSinceLastUpdate), meal))
				.collect(toList());
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
		DefaultSettings defaultSettings = settings.isPresent()
				&& settings.get().getNodeType() == Node.ELEMENT_NODE
						? DefaultSettings.parseDefaultSettings((Element) settings.get())
						: logFailedXmlRetrieval(DefaultSettings.createDefaultSettings(), "Calendar",
								mealPlanerNode);
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

	public static Element generateXml(Document saveFileContent, MealplanerData mealplanerData) {
		Element mealplanerDataNode = saveFileContent.createElement("mealplaner");
		mealplanerDataNode.appendChild(saveMealsToXml(
				saveFileContent,
				mealplanerData.meals,
				"mealList"));
		mealplanerDataNode.appendChild(writeDefaultSettings(
				saveFileContent,
				mealplanerData.defaultSettings,
				"defaultSettings"));
		mealplanerDataNode.appendChild(writeDate(
				saveFileContent,
				mealplanerData.date, "date"));
		mealplanerDataNode.appendChild(writeProposal(
				saveFileContent,
				mealplanerData.proposal,
				"proposal"));
		return mealplanerDataNode;
	}
}