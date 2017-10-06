package mealplaner.recepies.model;

import static mealplaner.recepies.model.Ingredient.generateXml;
import static mealplaner.recepies.model.Ingredient.loadFromXml;
import static org.assertj.core.api.Assertions.assertThat;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;

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
