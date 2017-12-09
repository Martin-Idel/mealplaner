package mealplaner.model;

import static java.time.LocalDate.now;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
import static mealplaner.commons.Pair.of;
import static mealplaner.io.XMLHelpers.createTextNode;
import static mealplaner.io.XMLHelpers.parseDate;
import static mealplaner.io.XMLHelpers.parseMealList;
import static mealplaner.io.XMLHelpers.parseSettingsList;
import static mealplaner.io.XMLHelpers.readBoolean;
import static mealplaner.io.XMLHelpers.writeDate;
import static mealplaner.io.XMLHelpers.writeMealList;
import static mealplaner.io.XMLHelpers.writeSettingsList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import mealplaner.commons.Pair;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.settings.Settings;

public class Proposal {
	private static final Logger logger = LoggerFactory.getLogger(Proposal.class);

	private List<Meal> mealList;
	private List<Settings> settingsList;
	private LocalDate date;
	private boolean includeToday;

	private Proposal(List<Meal> mealList, List<Settings> settingsList, LocalDate date,
			boolean includeToday) {
		this.mealList = mealList;
		this.settingsList = settingsList;
		this.date = date;
		this.includeToday = includeToday;
	}

	public static Proposal createProposal() {
		return new Proposal(new ArrayList<>(), new ArrayList<>(), now(), false);
	}

	public static Proposal from(boolean includeToday, List<Meal> meals, List<Settings> settings) {
		if (meals.size() != settings.size()) {
			throw new MealException("List of Settings and Meals must have the same size");
		}
		return new Proposal(meals, settings, now(), includeToday);
	}

	public static Proposal from(boolean includeToday, List<Meal> meals, List<Settings> settings,
			LocalDate date) {
		return new Proposal(meals, settings, date, includeToday);
	}

	public List<Meal> getProposalList() {
		return unmodifiableList(mealList);
	}

	public List<Settings> getSettingsList() {
		return unmodifiableList(settingsList);
	}

	public List<Pair<Meal, Settings>> getMealsAndSettings() {
		return unmodifiableList(IntStream.range(0, mealList.size())
				.mapToObj(number -> of(mealList.get(number), settingsList.get(number)))
				.collect(toList()));
	}

	public int getSize() {
		return mealList.size();
	}

	public Meal getItem(int index) {
		return mealList.get(index);
	}

	public boolean isToday() {
		return includeToday;
	}

	public LocalDate getTime() {
		return date;
	}

	public LocalDate getDateOfFirstProposedItem() {
		return includeToday ? date : date.plusDays(1);
	}

	@Override
	public String toString() {
		return "Proposal [mealList=" + mealList
				+ ", settings=" + settingsList
				+ ", calendar=" + date
				+ ", includeToday=" + includeToday + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + (includeToday ? 1231 : 1237);
		result = prime * result + ((mealList == null) ? 0 : mealList.hashCode());
		result = prime * result + ((settingsList == null) ? 0 : settingsList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Proposal other = (Proposal) obj;
		if (!date.equals(other.date)
				|| includeToday != other.includeToday
				|| !mealList.equals(other.mealList)
				|| !settingsList.equals(other.settingsList)) {
			return false;
		}
		return true;
	}

	public static Element writeProposal(Document saveFileContent, Proposal proposal,
			String elementName) {
		Element proposalNode = saveFileContent.createElement(elementName);
		proposalNode
				.appendChild(writeMealList(saveFileContent, proposal.mealList, "proposalList"));
		proposalNode.appendChild(
				writeSettingsList(saveFileContent, proposal.settingsList, "settingsList"));
		proposalNode.appendChild(writeDate(saveFileContent, proposal.date, "timeOfProposal"));
		proposalNode.appendChild(createTextNode(saveFileContent, "includesToday",
				() -> Boolean.toString(proposal.includeToday)));
		return proposalNode;
	}

	public static Proposal readProposal(Element proposalNode) {
		List<Meal> meals = new ArrayList<>();
		Node mealsNode = proposalNode.getElementsByTagName("proposalList").item(0);
		if (mealsNode != null && mealsNode.getNodeType() == Node.ELEMENT_NODE) {
			meals = parseMealList((Element) mealsNode);
		} else {
			logger.warn("List of meals in Proposal of" + proposalNode.toString()
					+ " could not be found");
		}
		List<Settings> settings = new ArrayList<>();
		Node settingsNode = proposalNode.getElementsByTagName("settingsList").item(0);
		if (settingsNode != null && settingsNode.getNodeType() == Node.ELEMENT_NODE) {
			settings = parseSettingsList((Element) settingsNode);
		} else {
			logger.warn("List of settings in Proposal of" + proposalNode.toString()
					+ " could not be found");
		}
		while (settings.size() < meals.size()) {
			settings.add(Settings.createSettings());
		}
		while (meals.size() < settings.size()) {
			meals.add(Meal.EMPTY_MEAL);
		}

		LocalDate date = parseDate(proposalNode);
		boolean includesToday = readBoolean(false, proposalNode, "includesToday");
		return new Proposal(meals, settings, date, includesToday);
	}
}