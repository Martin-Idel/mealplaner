package mealplaner.model;

import static mealplaner.model.Proposal.getFromXml;
import static mealplaner.model.Proposal.saveToXml;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;

import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;

public class ProposalTest {

	@Test
	public void test() throws ParserConfigurationException {
		Meal meal1 = new Meal("Test", CookingTime.SHORT, Sidedish.NONE, ObligatoryUtensil.POT,
				CookingPreference.NO_PREFERENCE, 5, "");
		Meal meal2 = new Meal("Test2", CookingTime.SHORT, Sidedish.NONE,
				ObligatoryUtensil.POT, CookingPreference.NO_PREFERENCE, 1, "");
		List<Meal> meals = new ArrayList<>();
		meals.add(meal1);
		meals.add(meal2);
		Calendar cal = Calendar.getInstance();
		long calTime = 1000000000;
		cal.setTimeInMillis(calTime);
		Proposal expected = new Proposal(meals, cal, true);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();
		Document saveFileContent = documentBuilder.newDocument();

		Proposal actual = getFromXml(saveToXml(saveFileContent, expected));

		compareTwoProposals(actual, expected);
	}

	private void compareTwoProposals(Proposal expected, Proposal actual) {
		for (int i = 0; i < expected.getProposalList().size(); i++) {
			compareTwoMeals(expected.getItem(i), actual.getItem(i));
		}
		assertEquals(expected.getCalendar().getTimeInMillis(),
				actual.getCalendar().getTimeInMillis());
		assertEquals(expected.isToday(), actual.isToday());
	}

	private void compareTwoMeals(Meal expected, Meal actual) {
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getComment(), actual.getComment());
		assertEquals(expected.getCookingPreference(), actual.getCookingPreference());
		assertEquals(expected.getCookingTime(), actual.getCookingTime());
		assertEquals(expected.getSidedish(), actual.getSidedish());
		assertEquals(expected.getObligatoryUtensil(), actual.getObligatoryUtensil());
		assertEquals(expected.getDaysPassed(), actual.getDaysPassed());
	}
}
