package mealplaner.model;

import static mealplaner.io.XMLHelpers.createTextNode;
import static mealplaner.io.XMLHelpers.getCalendarFromXml;
import static mealplaner.io.XMLHelpers.getMealListFromXml;
import static mealplaner.io.XMLHelpers.readBoolean;
import static mealplaner.io.XMLHelpers.saveCalendarToXml;
import static mealplaner.io.XMLHelpers.saveMealsToXml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import mealplaner.errorhandling.Logger;

public class Proposal implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Meal> mealList;
	private Calendar calendar;
	private boolean includeToday;

	public Proposal() {
		this(new ArrayList<Meal>(), Calendar.getInstance(), false);
	}

	public Proposal(Calendar calendar, boolean includeToday) {
		this(new ArrayList<Meal>(), calendar, includeToday);
	}

	public Proposal(List<Meal> mealList, Calendar calendar, boolean includeToday) {
		this.mealList = mealList;
		this.calendar = calendar;
		this.includeToday = includeToday;
	}

	public static Proposal prepareProposal(boolean includeToday) {
		return new Proposal(Calendar.getInstance(), includeToday);
	}

	public List<Meal> getProposalList() {
		return mealList;
	}

	public void setProposalList(ArrayList<Meal> proposal) {
		mealList = proposal;
	}

	public void clearProposalList() {
		mealList.clear();
	}

	public void addItemToProposalList(Meal meal) {
		mealList.add(meal);
	}

	public int getSize() {
		return mealList.size();
	}

	public Meal getItem(int index) {
		return mealList.get(index);
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calender) {
		this.calendar = calender;
	}

	public boolean isToday() {
		return includeToday;
	}

	public void setToday(boolean today) {
		this.includeToday = today;
	}

	public Date getTime() {
		return calendar.getTime();
	}

	public void setDate(Date time) {
		calendar.setTime(time);
	}

	public static Element saveToXml(Document saveFileContent, Proposal proposal) {
		Element proposalNode = saveFileContent.createElement("proposal");
		proposalNode
				.appendChild(saveMealsToXml(saveFileContent, proposal.mealList, "proposalList"));
		proposalNode.appendChild(saveCalendarToXml(saveFileContent, proposal.calendar));
		proposalNode.appendChild(createTextNode(saveFileContent, "includesToday",
				() -> Boolean.toString(proposal.includeToday)));
		return proposalNode;
	}

	public static Proposal getFromXml(Element proposalNode) {
		List<Meal> meals = new ArrayList<>();
		Node mealsNode = proposalNode.getElementsByTagName("proposalList").item(0);
		if (mealsNode.getNodeType() == Node.ELEMENT_NODE) {
			meals = getMealListFromXml((Element) mealsNode);
		} else {
			Logger.logParsingError(
					"List of meals in " + proposalNode.toString() + " could not be found");
		}
		Calendar calendar = getCalendarFromXml(proposalNode);
		boolean includesToday = readBoolean(false, proposalNode, "includesToday");
		return new Proposal(meals, calendar, includesToday);
	}

}