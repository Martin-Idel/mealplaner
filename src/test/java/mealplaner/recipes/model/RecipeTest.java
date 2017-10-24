package mealplaner.recipes.model;

import static mealplaner.recipes.model.Recipe.writeRecipe;
import static mealplaner.recipes.model.Recipe.loadRecipe;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;

import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.IngredientType;
import mealplaner.recipes.model.Measure;
import mealplaner.recipes.model.Recipe;
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

		Recipe actual = loadRecipe(writeRecipe(doc, expected, "recipe"));

		assertThat(actual).isEqualTo(expected);
	}
}
