package mealplaner.model;

import static mealplaner.io.XMLHelpers.createTextNode;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.createDocument;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;
import static testcommons.MealAssert.assertThat;

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

		assertThat(sut.getName()).isEqualTo("New Name");
	}

	@Test(expected = MealException.class)
	public void setNameWithOnlyWhitespace() throws MealException {

		sut.setName("  ");

		assertThat(sut.getName()).isEqualTo("Test");
	}

	@Test
	public void setDaysPassedWithPositiveNumber() throws MealException {

		sut.setDaysPassed(154);

		assertThat(sut.getDaysPassed()).isEqualTo(154);
	}

	@Test(expected = MealException.class)
	public void setDaysPassedWithNegativeNumber() throws MealException {

		sut.setDaysPassed(-5);

		assertThat(sut.getDaysPassed()).isEqualTo(5);
	}

	@Test
	public void compareToWithName() throws MealException {
		Meal compareMeal = getMeal2();

		int compareResult = sut.compareTo(compareMeal);

		assertThat(compareResult).isEqualTo(-1);
	}

	@Test
	public void saveAndReadFromXmlNode() throws ParserConfigurationException {
		Meal meal = getMeal2();
		Document saveFileContent = createDocument();

		sut = Meal.loadFromXml(Meal.generateXml(saveFileContent, meal, "meal"));

		assertThat(sut).isEqualTo(meal);
	}

	@Test
	public void readFromXmlNode() throws ParserConfigurationException {
		Meal meal = getMeal1();

		Document saveFileContent = createDocument();
		Element mealNode = saveFileContent.createElement("meal");
		mealNode.setAttribute("name", meal.getName());
		mealNode.appendChild(createTextNode(saveFileContent, "comment", () -> meal.getComment()));
		mealNode.appendChild(createTextNode(saveFileContent,
				"cookingTime",
				() -> meal.getCookingTime().name()));

		sut = Meal.loadFromXml(mealNode);

		Meal expectedMeal = new Meal(meal.getName(), meal.getCookingTime(), Sidedish.NONE,
				ObligatoryUtensil.POT, CookingPreference.NO_PREFERENCE, 0, meal.getComment());

		assertThat(sut).isEqualTo(expectedMeal);
	}
}