package mealplaner.recipes.model;

import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.recipes.model.QuantitativeIngredient.create;
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

import mealplaner.commons.NonnegativeInteger;
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
    anIngredient1 = new Ingredient("Test1", IngredientType.FRESH_FRUIT,
        Measure.GRAM);
    anIngredient2 = new Ingredient("Test2", IngredientType.BAKING_GOODS,
        Measure.MILLILITRE);
    quantitativeIngredient1 = create(anIngredient1, nonNegative(1));
    quantitativeIngredient2 = create(anIngredient2, nonNegative(10));
    quantitativeIngredient3 = create(anIngredient1, nonNegative(10));
  }

  @Test
  public void testXMlLoadAndSave() throws ParserConfigurationException {
    Map<Ingredient, NonnegativeInteger> ingredients = new HashMap<>();
    ingredients.put(anIngredient1, nonNegative(100));
    ingredients.put(anIngredient2, nonNegative(300));
    Recipe expected = from(nonNegative(2), ingredients);
    Document doc = CommonFunctions.createDocument();

    Recipe actual = loadRecipe(writeRecipe(doc, expected, "recipe"));

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void initialiserCreatesCorrectMapWithDistinctEntries() {
    List<QuantitativeIngredient> ingredientList = new ArrayList<>();
    ingredientList.add(quantitativeIngredient1);
    ingredientList.add(quantitativeIngredient2);

    Recipe recipe = Recipe.from(nonNegative(1), ingredientList);

    Map<Ingredient, NonnegativeInteger> ingredients = new HashMap<>();
    ingredients.put(anIngredient1, nonNegative(1));
    ingredients.put(anIngredient2, nonNegative(10));
    assertThat(recipe.getIngredientsAsIs()).containsAllEntriesOf(ingredients);
    assertThat(ingredients).containsAllEntriesOf(recipe.getIngredientsAsIs());
  }

  @Test
  public void initialiserCreatesAddedUpAmountsForEqualEntries() {
    List<QuantitativeIngredient> ingredientList = new ArrayList<>();
    ingredientList.add(quantitativeIngredient1);
    ingredientList.add(quantitativeIngredient3);

    Recipe recipe = Recipe.from(nonNegative(1), ingredientList);

    Map<Ingredient, NonnegativeInteger> ingredients = new HashMap<>();
    ingredients.put(anIngredient1, nonNegative(11));
    assertThat(recipe.getIngredientsAsIs()).containsAllEntriesOf(ingredients);
    assertThat(ingredients).containsAllEntriesOf(recipe.getIngredientsAsIs());
  }

  @Test
  public void initialiserCreatesAddedUpAmountsForSeveralEntries() {
    List<QuantitativeIngredient> ingredientList = new ArrayList<>();
    ingredientList.add(quantitativeIngredient1);
    ingredientList.add(quantitativeIngredient2);
    ingredientList.add(quantitativeIngredient3);

    Recipe recipe = Recipe.from(nonNegative(1), ingredientList);

    Map<Ingredient, NonnegativeInteger> ingredients = new HashMap<>();
    ingredients.put(anIngredient1, nonNegative(11));
    ingredients.put(anIngredient2, nonNegative(10));
    assertThat(recipe.getIngredientsAsIs()).containsAllEntriesOf(ingredients);
    assertThat(ingredients).containsAllEntriesOf(recipe.getIngredientsAsIs());
  }

  @Test
  public void getIngredientsListReturnsDoubleAmountsForDoubleNumberOfPeople() {
    Map<Ingredient, NonnegativeInteger> ingredients = new HashMap<>();
    ingredients.put(anIngredient1, nonNegative(100));
    ingredients.put(anIngredient2, nonNegative(300));
    Recipe recipe = from(nonNegative(2), ingredients);

    List<QuantitativeIngredient> ingredientListFor = recipe
        .getIngredientListFor(nonNegative(4));

    assertThat(ingredientListFor).containsExactlyInAnyOrder(
        create(anIngredient1, nonNegative(200)),
        create(anIngredient2, nonNegative(600)));
  }

  @Test
  public void getIngredientsListReturnsCorrectAmountsForHalfTheNumberOfPeople() {
    Map<Ingredient, NonnegativeInteger> ingredients = new HashMap<>();
    ingredients.put(anIngredient1, nonNegative(100));
    ingredients.put(anIngredient2, nonNegative(300));
    Recipe recipe = from(nonNegative(3), ingredients);

    List<QuantitativeIngredient> ingredientListFor = recipe
        .getIngredientListFor(nonNegative(1));

    assertThat(ingredientListFor).containsExactlyInAnyOrder(
        create(anIngredient1, nonNegative(33)),
        create(anIngredient2, nonNegative(100)));
  }
}
