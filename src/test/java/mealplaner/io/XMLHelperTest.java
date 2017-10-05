package mealplaner.io;

import static mealplaner.io.XMLHelpers.getCalendarFromXml;
import static mealplaner.io.XMLHelpers.getMealListFromXml;
import static mealplaner.io.XMLHelpers.saveCalendarToXml;
import static mealplaner.io.XMLHelpers.saveMealsToXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.createDocument;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import mealplaner.model.Meal;

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

		assertThat(actual).isEqualByComparingTo(TestEnum.NON_DEFAULT);
	}

	@Test
	public void getEnumFromTextNodeAlthoughItDoesNotExist() throws ParserConfigurationException {
		Document saveDocument = createDocument();
		Element testNode = saveDocument.createElement("testNode");
		Element enumNode = saveDocument.createElement("EnumNode");
		testNode.appendChild(enumNode);

		TestEnum actual = XMLHelpers.readEnum(TestEnum.DEFAULT, TestEnum::valueOf, testNode,
				"EnumNode");

		assertThat(actual).isEqualByComparingTo(TestEnum.DEFAULT);
	}

	@Test
	public void readBoolean() throws ParserConfigurationException {
		Document saveDocument = createDocument();
		Element testNode = saveDocument.createElement("testNode");
		Element booleanNode = saveDocument.createElement("BOOL");
		booleanNode.appendChild(saveDocument.createTextNode(Boolean.toString(true)));
		testNode.appendChild(booleanNode);

		boolean actual = XMLHelpers.readBoolean(false, testNode, "BOOL");

		assertThat(actual).isTrue();
	}

	@Test
	public void readBooleanFromTextNodeAlthoughItDoesNotExist()
			throws ParserConfigurationException {
		Document saveDocument = createDocument();
		Element testNode = saveDocument.createElement("testNode");
		Element booleanNode = saveDocument.createElement("BOOL");
		testNode.appendChild(booleanNode);

		boolean actual = XMLHelpers.readBoolean(false, testNode, "BOOL");

		assertThat(actual).isFalse();
	}

	@Test
	public void calendarCanBeSavedAndReadToXml() throws ParserConfigurationException {
		Calendar expected = Calendar.getInstance();
		expected.setTimeInMillis(5000000);
		Document saveDocument = createDocument();

		Calendar actual = getCalendarFromXml(saveCalendarToXml(saveDocument, expected));

		assertThat(actual.getTimeInMillis()).isEqualTo(expected.getTimeInMillis());
	}

	@Test
	public void getMealListFromXmlWorksCorrectly() throws ParserConfigurationException {
		Document saveDocument = createDocument();
		List<Meal> meals = new ArrayList<>();
		meals.add(getMeal1());
		meals.add(getMeal2());

		List<Meal> actualMeals = getMealListFromXml(
				saveMealsToXml(saveDocument, meals, "mealList"));

		assertThat(meals).asList().containsAll(actualMeals);
	}

	private enum TestEnum {
		DEFAULT, NON_DEFAULT;
	}
}
