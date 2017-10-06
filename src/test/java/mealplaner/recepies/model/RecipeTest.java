package mealplaner.recepies.model;

import static mealplaner.recepies.model.Recipe.generateXml;
import static mealplaner.recepies.model.Recipe.loadFromXml;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;

import testcommons.CommonFunctions;

public class RecipeTest {

	@Test
	public void testXMlLoadAndSave() throws ParserConfigurationException {
		Ingredient anIngredient1 = new Ingredient("Test1", IngredientType.FRESH_FRUIT,
				Measure.GRAM);
		Ingredient anIngredient2 = new Ingredient("Test2", IngredientType.BAKING_GOODS,
				Measure.MILLILITRE);
		Map<Ingredient, Integer> ingredients = new HashMap<>();
		ingredients.put(anIngredient1, 100);
		ingredients.put(anIngredient2, 300);
		Recipe expected = new Recipe(2, ingredients);
		Document doc = CommonFunctions.createDocument();

		Recipe actual = loadFromXml(generateXml(doc, expected, "recipe"));

		assertThat(actual).isEqualTo(expected);
	}
}
