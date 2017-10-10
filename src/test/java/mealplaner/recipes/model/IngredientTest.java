package mealplaner.recipes.model;

import static mealplaner.recipes.model.Ingredient.generateXml;
import static mealplaner.recipes.model.Ingredient.loadFromXml;
import static org.assertj.core.api.Assertions.assertThat;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;

import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.IngredientType;
import mealplaner.recipes.model.Measure;
import testcommons.CommonFunctions;

public class IngredientTest {

	@Test
	public void testCorrectXMLSavingAndLoading() throws ParserConfigurationException {
		Ingredient expected = new Ingredient("Test", IngredientType.FRESH_FRUIT, Measure.GRAM);
		Document doc = CommonFunctions.createDocument();

		Ingredient actual = loadFromXml(generateXml(doc, expected, "ingredient"));

		assertThat(actual).isEqualToComparingFieldByField(expected);
	}
}
