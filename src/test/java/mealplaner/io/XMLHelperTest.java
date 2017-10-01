package mealplaner.io;

import static mealplaner.io.XMLHelpers.getCalendarFromXml;
import static mealplaner.io.XMLHelpers.getMealListFromXml;
import static mealplaner.io.XMLHelpers.saveCalendarToXml;
import static mealplaner.io.XMLHelpers.saveMealsToXml;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import mealplaner.model.Meal;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;

public class XMLHelperTest {

	@Test
	public void getEnumFromTextNode() throws ParserConfigurationException {
		Document saveDocument = createDocument();
		Element testNode = saveDocument.createElement("testNode");
		Element enumNode = saveDocument.createElement("EnumNode");
		enumNode.appendChild(saveDocument.createTextNode(TestEnum.NON_DEFAULT.name()));
		testNode.appendChild(enumNode);

		TestEnum actual = XMLHelpers.readEnum(TestEnum.DEFAULT, TestEnum::valueOf, testNode,
				"EnumNode");

		assertEquals(TestEnum.NON_DEFAULT, actual);
	}

	@Test
	public void getEnumFromTextNodeAlthoughItDoesNotExist() throws ParserConfigurationException {
		Document saveDocument = createDocument();
		Element testNode = saveDocument.createElement("testNode");
		Element enumNode = saveDocument.createElement("EnumNode");
		testNode.appendChild(enumNode);

		TestEnum actual = XMLHelpers.readEnum(TestEnum.DEFAULT, TestEnum::valueOf, testNode,
				"EnumNode");

		assertEquals(TestEnum.DEFAULT, actual);
	}

	@Test
	public void readBoolean() throws ParserConfigurationException {
		Document saveDocument = createDocument();
		Element testNode = saveDocument.createElement("testNode");
		Element booleanNode = saveDocument.createElement("BOOL");
		booleanNode.appendChild(saveDocument.createTextNode(Boolean.toString(true)));
		testNode.appendChild(booleanNode);

		boolean actual = XMLHelpers.readBoolean(false, testNode, "BOOL");

		assertTrue(actual);
	}

	@Test
	public void readBooleanFromTextNodeAlthoughItDoesNotExist()
			throws ParserConfigurationException {
		Document saveDocument = createDocument();
		Element testNode = saveDocument.createElement("testNode");
		Element booleanNode = saveDocument.createElement("BOOL");
		testNode.appendChild(booleanNode);

		boolean actual = XMLHelpers.readBoolean(false, testNode, "BOOL");

		assertFalse(actual);
	}

	@Test
	public void calendarCanBeSavedAndReadToXml() throws ParserConfigurationException {
		Calendar expected = Calendar.getInstance();
		expected.setTimeInMillis(5000000);
		Document saveDocument = createDocument();

		Calendar actual = getCalendarFromXml(saveCalendarToXml(saveDocument, expected));

		assertEquals(expected.getTimeInMillis(), actual.getTimeInMillis());
	}

	@Test
	public void getMealListFromXmlWorksCorrectly() throws ParserConfigurationException {
		Document saveDocument = createDocument();
		Meal meal1 = new Meal("Test", CookingTime.SHORT, Sidedish.NONE, ObligatoryUtensil.POT,
				CookingPreference.NO_PREFERENCE, 5, "");
		Meal meal2 = new Meal("Test2", CookingTime.SHORT, Sidedish.NONE,
				ObligatoryUtensil.POT, CookingPreference.NO_PREFERENCE, 1, "");
		List<Meal> meals = new ArrayList<>();
		meals.add(meal1);
		meals.add(meal2);

		List<Meal> actualMeals = getMealListFromXml(
				saveMealsToXml(saveDocument, meals, "mealList"));

		compareTwoMealLists(meals, actualMeals);
	}

	private Document createDocument() throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();
		Document saveDocument = documentBuilder.newDocument();
		return saveDocument;
	}

	private enum TestEnum {
		DEFAULT, NON_DEFAULT;
	}

	private void compareTwoMealLists(List<Meal> expected, List<Meal> actual) {
		for (int i = 0; i < expected.size(); i++) {
			compareTwoMeals(expected.get(i), actual.get(i));
		}
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
