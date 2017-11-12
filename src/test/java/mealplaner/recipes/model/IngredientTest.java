package mealplaner.recipes.model;

import static mealplaner.recipes.model.Ingredient.generateXml;
import static mealplaner.recipes.model.Ingredient.loadFromXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.createDocument;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Document;

import testcommons.BundlesInitialization;

public class IngredientTest {
	@Rule
	public final BundlesInitialization bundlesInitialization = new BundlesInitialization();

	@Test
	public void testCorrectXMLSavingAndLoading() throws ParserConfigurationException {
		Ingredient expected = new Ingredient("Test", IngredientType.FRESH_FRUIT, Measure.GRAM);
		Document doc = createDocument();

		Ingredient actual = loadFromXml(generateXml(doc, expected, "ingredient"));

		assertThat(actual).isEqualToComparingFieldByField(expected);
	}
}
