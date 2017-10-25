package mealplaner.model;

import static java.time.LocalDate.now;
import static java.util.Collections.unmodifiableList;
import static mealplaner.io.XMLHelpers.createTextNode;
import static mealplaner.io.XMLHelpers.parseDate;
import static mealplaner.io.XMLHelpers.parseMealList;
import static mealplaner.io.XMLHelpers.readBoolean;
import static mealplaner.io.XMLHelpers.writeDate;
import static mealplaner.io.XMLHelpers.writeMealList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Proposal {
	private static final Logger logger = LoggerFactory.getLogger(Proposal.class);

	private List<Meal> mealList;
	private LocalDate date;
	private boolean includeToday;

	public Proposal(List<Meal> mealList, LocalDate date, boolean includeToday) {
		this.mealList = mealList;
		this.date = date;
		this.includeToday = includeToday;
	}

	public static Proposal createProposal() {
		return new Proposal(new ArrayList<>(), now(), false);
	}

	public static Proposal from(boolean includeToday) {
		return new Proposal(new ArrayList<>(), now(), includeToday);
	}

	public static Proposal from(boolean includeToday, List<Meal> meals) {
		return new Proposal(meals, now(), includeToday);
	}

	public static Proposal from(boolean includeToday, List<Meal> meals, LocalDate date) {
		return new Proposal(meals, date, includeToday);
	}

	public List<Meal> getProposalList() {
		return unmodifiableList(mealList);
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

	public static Element writeProposal(Document saveFileContent, Proposal proposal,
			String elementName) {
		Element proposalNode = saveFileContent.createElement(elementName);
		proposalNode
				.appendChild(writeMealList(saveFileContent, proposal.mealList, "proposalList"));
		proposalNode.appendChild(writeDate(saveFileContent, proposal.date, "timeOfProposal"));
		proposalNode.appendChild(createTextNode(saveFileContent, "includesToday",
				() -> Boolean.toString(proposal.includeToday)));
		return proposalNode;
	}

	public static Proposal readProposal(Element proposalNode) {
		List<Meal> meals = new ArrayList<>();
		Node mealsNode = proposalNode.getElementsByTagName("proposalList").item(0);
		if (mealsNode.getNodeType() == Node.ELEMENT_NODE) {
			meals = parseMealList((Element) mealsNode);
		} else {
			logger.warn("List of meals in Proposal of" + proposalNode.toString()
					+ " could not be found");
		}
		LocalDate date = parseDate(proposalNode);
		boolean includesToday = readBoolean(false, proposalNode, "includesToday");
		return new Proposal(meals, date, includesToday);
	}

	@Override
	public String toString() {
		return "Proposal [mealList=" + mealList + ", calendar=" + date + ", includeToday="
				+ includeToday + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + (includeToday ? 1231 : 1237);
		result = prime * result + ((mealList == null) ? 0 : mealList.hashCode());
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
				|| !mealList.equals(other.mealList)) {
			return false;
		}
		return true;
	}
}