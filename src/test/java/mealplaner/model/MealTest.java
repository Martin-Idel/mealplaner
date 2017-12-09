package mealplaner.model;

import static java.util.Optional.empty;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.io.XMLHelpers.createTextNode;
import static mealplaner.model.Meal.createMeal;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.createDocument;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;
import static testcommons.MealAssert.assertThat;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;
import testcommons.BundlesInitialization;

public class MealTest {
	@Rule
	public final BundlesInitialization bundlesInitialization = new BundlesInitialization();

	private Meal sut;

	@Before
	public void setup() throws MealException {
		sut = createMeal("Test", CookingTime.SHORT, Sidedish.PASTA, ObligatoryUtensil.POT,
				CookingPreference.NO_PREFERENCE, nonNegative(5), "", empty());
	}

	@Test
	public void valuesWithLimitationsWorkCorrectly() throws MealException {
		assertThat(sut.getName()).isEqualTo("Test");
		assertThat(sut.getDaysPassed()).isEqualTo(nonNegative(5));
	}

	@Test(expected = MealException.class)
	public void setNameWithOnlyWhitespace() throws MealException {
		sut = createMeal("", CookingTime.SHORT, Sidedish.PASTA, ObligatoryUtensil.POT,
				CookingPreference.NO_PREFERENCE, nonNegative(5), "", empty());

		assertThat(sut.getName()).isEqualTo("Test");
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

		sut = Meal.readMeal(Meal.writeMeal(saveFileContent, meal, "meal"));

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

		sut = Meal.readMeal(mealNode);

		Meal expectedMeal = createMeal(meal.getName(), meal.getCookingTime(), Sidedish.NONE,
				ObligatoryUtensil.POT, CookingPreference.NO_PREFERENCE, nonNegative(0),
				meal.getComment(), empty());

		assertThat(sut).isEqualTo(expectedMeal);
	}
}