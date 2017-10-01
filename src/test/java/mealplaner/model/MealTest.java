package mealplaner.model;

import static org.junit.Assert.assertEquals;

import java.util.function.Supplier;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import mealplaner.errorhandling.MealException;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;

public class MealTest {

	private Meal sut;

	@Before
	public void setup() throws MealException {
		sut = new Meal("Test", CookingTime.SHORT, Sidedish.PASTA, ObligatoryUtensil.POT,
				CookingPreference.NO_PREFERENCE, 5, "");
	}

	@Test
	public void setNameWithCorrectName() throws MealException {

		sut.setName("New Name");

		assertEquals("New Name", sut.getName());
	}

	@Test(expected = MealException.class)
	public void setNameWithOnlyWhitespace() throws MealException {

		sut.setName("  ");

		assertEquals("Test", sut.getName());
	}

	@Test
	public void setDaysPassedWithPositiveNumber() throws MealException {

		sut.setDaysPassed(154);

		assertEquals(154, sut.getDaysPassed());
	}

	@Test(expected = MealException.class)
	public void setDaysPassedWithNegativeNumber() throws MealException {

		sut.setDaysPassed(-5);

		assertEquals(5, sut.getDaysPassed());
	}

	@Test
	public void compareToWithName() throws MealException {
		Meal compareMeal = new Meal("Test2", CookingTime.SHORT, Sidedish.NONE,
				ObligatoryUtensil.POT, CookingPreference.NO_PREFERENCE, 1, "");

		int compareResult = sut.compareTo(compareMeal);

		assertEquals(-1, compareResult);
	}

	@Test
	public void saveAndReadFromXmlNode() throws ParserConfigurationException {
		Meal meal = new Meal("Test2", CookingTime.SHORT, Sidedish.NONE,
				ObligatoryUtensil.POT, CookingPreference.NO_PREFERENCE, 1, "");

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();
		Document saveFileContent = documentBuilder.newDocument();

		sut = Meal.loadFromXml(Meal.generateXml(saveFileContent, meal));

		compareTwoMeals(meal, sut);
	}

	@Test
	public void readFromXmlNode() throws ParserConfigurationException {
		Meal meal = new Meal("Test1", CookingTime.SHORT, Sidedish.PASTA,
				ObligatoryUtensil.PAN, CookingPreference.VERY_POPULAR, 5, "no comment");

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();
		Document saveFileContent = documentBuilder.newDocument();
		Element mealNode = saveFileContent.createElement("meal");
		mealNode.setAttribute("name", meal.getName());
		mealNode.appendChild(node(saveFileContent, "comment", () -> meal.getComment()));
		mealNode.appendChild(node(saveFileContent,
				"cookingTime",
				() -> meal.getCookingTime().name()));

		sut = Meal.loadFromXml(mealNode);

		Meal expectedMeal = new Meal(meal.getName(), meal.getCookingTime(), Sidedish.NONE,
				ObligatoryUtensil.POT, CookingPreference.NO_PREFERENCE, 0, meal.getComment());

		compareTwoMeals(expectedMeal, sut);
	}

	private static Element node(Document doc, String name,
			Supplier<String> stringRepresentationOfField) {
		Element mealFieldNode = doc.createElement(name);
		mealFieldNode.appendChild(doc.createTextNode(stringRepresentationOfField.get()));
		return mealFieldNode;
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