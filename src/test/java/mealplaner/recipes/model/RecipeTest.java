package mealplaner.recipes.model;

import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.recipes.model.QuantitativeIngredientBuilder.builder;
import static mealplaner.recipes.model.Recipe.from;
import static mealplaner.recipes.model.Recipe.loadRecipe;
import static mealplaner.recipes.model.Recipe.writeRecipe;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Document;

import testcommons.BundlesInitialization;
import testcommons.CommonFunctions;

public class RecipeTest {
	@Rule
	public final BundlesInitialization bundlesInitialization = new BundlesInitialization();
	private Ingredient anIngredient1;
	private Ingredient anIngredient2;
	private QuantitativeIngredient quantitativeIngredient1;
	private QuantitativeIngredient quantitativeIngredient2;
	private QuantitativeIngredient quantitativeIngredient3;

	@Before
	public void setUp() {
		ingredient1();
		ingredient2();
		quantitativeIngredient1();
		quantitativeIngredient2();
		quantitativeIngredient3();
	}

	@Test
	public void testXMlLoadAndSave() throws ParserConfigurationException {
		Map<Ingredient, Integer> ingredients = new HashMap<>();
		ingredients.put(anIngredient1, 100);
		ingredients.put(anIngredient2, 300);
		Recipe expected = from(2, ingredients);
		Document doc = CommonFunctions.createDocument();

		Recipe actual = loadRecipe(writeRecipe(doc, expected, "recipe"));

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void initialiserCreatesCorrectMapWithDistinctEntries() {
		List<QuantitativeIngredient> ingredientList = new ArrayList<>();
		ingredientList.add(quantitativeIngredient1);
		ingredientList.add(quantitativeIngredient2);

		Recipe recipe = Recipe.from(1, ingredientList);

		Map<Ingredient, Integer> ingredients = new HashMap<>();
		ingredients.put(anIngredient1, 1);
		ingredients.put(anIngredient2, 10);
		assertThat(recipe.getIngredientsAsIs()).containsAllEntriesOf(ingredients);
		assertThat(ingredients).containsAllEntriesOf(recipe.getIngredientsAsIs());
	}

	@Test
	public void initialiserCreatesAddedUpAmountsForEqualEntries() {
		List<QuantitativeIngredient> ingredientList = new ArrayList<>();
		ingredientList.add(quantitativeIngredient1);
		ingredientList.add(quantitativeIngredient3);

		Recipe recipe = Recipe.from(1, ingredientList);

		Map<Ingredient, Integer> ingredients = new HashMap<>();
		ingredients.put(anIngredient1, 11);
		assertThat(recipe.getIngredientsAsIs()).containsAllEntriesOf(ingredients);
		assertThat(ingredients).containsAllEntriesOf(recipe.getIngredientsAsIs());
	}

	@Test
	public void initialiserCreatesAddedUpAmountsForSeveralEntries() {
		List<QuantitativeIngredient> ingredientList = new ArrayList<>();
		ingredientList.add(quantitativeIngredient1);
		ingredientList.add(quantitativeIngredient2);
		ingredientList.add(quantitativeIngredient3);

		Recipe recipe = Recipe.from(1, ingredientList);

		Map<Ingredient, Integer> ingredients = new HashMap<>();
		ingredients.put(anIngredient1, 11);
		ingredients.put(anIngredient2, 10);
		assertThat(recipe.getIngredientsAsIs()).containsAllEntriesOf(ingredients);
		assertThat(ingredients).containsAllEntriesOf(recipe.getIngredientsAsIs());
	}

	@Test
	public void getIngredientsListReturnsDoubleAmountsForDoubleNumberOfPeople() {
		Map<Ingredient, Integer> ingredients = new HashMap<>();
		ingredients.put(anIngredient1, 100);
		ingredients.put(anIngredient2, 300);
		Recipe recipe = from(2, ingredients);

		List<QuantitativeIngredient> ingredientListFor = recipe.getIngredientListFor(4);

		assertThat(ingredientListFor).containsExactlyInAnyOrder(
				builder().ingredient(anIngredient1)
						.amount(nonNegative(200))
						.forPeople(nonNegative(1))
						.build(),
				builder().ingredient(anIngredient2)
						.amount(nonNegative(600))
						.forPeople(nonNegative(1))
						.build());
	}

	@Test
	public void getIngredientsListReturnsCorrectAmountsForHalfTheNumberOfPeople() {
		Map<Ingredient, Integer> ingredients = new HashMap<>();
		ingredients.put(anIngredient1, 100);
		ingredients.put(anIngredient2, 300);
		Recipe recipe = from(3, ingredients);

		List<QuantitativeIngredient> ingredientListFor = recipe.getIngredientListFor(1);

		assertThat(ingredientListFor).containsExactlyInAnyOrder(
				builder().ingredient(anIngredient1)
						.amount(nonNegative(33))
						.forPeople(nonNegative(1))
						.build(),
				builder().ingredient(anIngredient2)
						.amount(nonNegative(100))
						.forPeople(nonNegative(1))
						.build());
	}

	private void ingredient1() {
		anIngredient1 = new Ingredient("Test1", IngredientType.FRESH_FRUIT,
				Measure.GRAM);
	}

	private void ingredient2() {
		anIngredient2 = new Ingredient("Test2", IngredientType.BAKING_GOODS,
				Measure.MILLILITRE);
	}

	private void quantitativeIngredient1() {
		quantitativeIngredient1 = builder().ingredient(anIngredient1)
				.amount(nonNegative(1))
				.forPeople(nonNegative(1))
				.build();
	}

	private void quantitativeIngredient2() {
		quantitativeIngredient2 = builder().ingredient(anIngredient2)
				.amount(nonNegative(10))
				.forPeople(nonNegative(1))
				.build();
	}

	private void quantitativeIngredient3() {
		quantitativeIngredient3 = builder().ingredient(anIngredient1)
				.amount(nonNegative(10))
				.forPeople(nonNegative(1))
				.build();
	}
}
