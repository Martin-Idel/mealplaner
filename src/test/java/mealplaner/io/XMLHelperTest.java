package mealplaner.io;

import static mealplaner.io.XMLHelpers.writeDate;
import static mealplaner.io.XMLHelpers.parseMealList;
import static mealplaner.io.XMLHelpers.parseDate;
import static mealplaner.io.XMLHelpers.writeMealList;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.createDocument;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
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
	public void getMealListFromXmlWorksCorrectly() throws ParserConfigurationException {
		Document saveDocument = createDocument();
		List<Meal> meals = new ArrayList<>();
		meals.add(getMeal1());
		meals.add(getMeal2());

		List<Meal> actualMeals = parseMealList(
				writeMealList(saveDocument, meals, "mealList"));

		assertThat(meals).asList().containsAll(actualMeals);
	}

	@Test
	public void getLocalDateFromXmlWorksCorrectly() throws ParserConfigurationException {
		Document saveDocument = createDocument();
		LocalDate date = LocalDate.of(2017, Month.JULY, 5);

		LocalDate actualDate = parseDate(writeDate(saveDocument, date, "date"));

		assertThat(actualDate).isAfterOrEqualTo(date);
		assertThat(actualDate).isBeforeOrEqualTo(date);
	}

	private enum TestEnum {
		DEFAULT, NON_DEFAULT;
	}
}
