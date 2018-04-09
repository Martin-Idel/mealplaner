package mealplaner.recipes.model;

import static mealplaner.commons.NonnegativeFraction.fraction;
import static mealplaner.commons.NonnegativeFraction.wholeNumber;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.recipes.model.QuantitativeIngredient.create;
import static mealplaner.recipes.model.Recipe.from;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import mealplaner.commons.NonnegativeFraction;
import testcommons.CommonFunctions;

public class RecipeTest {
  private Ingredient anIngredient1;
  private Ingredient anIngredient2;
  private QuantitativeIngredient quantitativeIngredient1;
  private QuantitativeIngredient quantitativeIngredient2;
  private QuantitativeIngredient quantitativeIngredient3;

  @Before
  public void setUp() {
    anIngredient1 = CommonFunctions.getIngredient1();
    anIngredient2 = CommonFunctions.getIngredient2();
    quantitativeIngredient1 = create(anIngredient1, wholeNumber(nonNegative(1)));
    quantitativeIngredient2 = create(anIngredient2, wholeNumber(nonNegative(10)));
    quantitativeIngredient3 = create(anIngredient1, wholeNumber(nonNegative(10)));
  }

  @Test
  public void initialiserCreatesCorrectMapWithDistinctEntries() {
    List<QuantitativeIngredient> ingredientList = new ArrayList<>();
    ingredientList.add(quantitativeIngredient1);
    ingredientList.add(quantitativeIngredient2);

    Recipe recipe = Recipe.from(nonNegative(1), ingredientList);

    Map<Ingredient, NonnegativeFraction> ingredients = new HashMap<>();
    ingredients.put(anIngredient1, wholeNumber(nonNegative(1)));
    ingredients.put(anIngredient2, wholeNumber(nonNegative(10)));
    assertThat(recipe.getIngredientsAsIs()).containsAllEntriesOf(ingredients);
    assertThat(ingredients).containsAllEntriesOf(recipe.getIngredientsAsIs());
  }

  @Test
  public void initialiserCreatesAddedUpAmountsForEqualEntries() {
    List<QuantitativeIngredient> ingredientList = new ArrayList<>();
    ingredientList.add(quantitativeIngredient1);
    ingredientList.add(quantitativeIngredient3);

    Recipe recipe = Recipe.from(nonNegative(1), ingredientList);

    Map<Ingredient, NonnegativeFraction> ingredients = new HashMap<>();
    ingredients.put(anIngredient1, wholeNumber(nonNegative(11)));
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

    Map<Ingredient, NonnegativeFraction> ingredients = new HashMap<>();
    ingredients.put(anIngredient1, wholeNumber(nonNegative(11)));
    ingredients.put(anIngredient2, wholeNumber(nonNegative(10)));
    assertThat(recipe.getIngredientsAsIs()).containsAllEntriesOf(ingredients);
    assertThat(ingredients).containsAllEntriesOf(recipe.getIngredientsAsIs());
  }

  @Test
  public void getIngredientsListReturnsDoubleAmountsForDoubleNumberOfPeople() {
    Map<Ingredient, NonnegativeFraction> ingredients = new HashMap<>();
    ingredients.put(anIngredient1, wholeNumber(nonNegative(100)));
    ingredients.put(anIngredient2, wholeNumber(nonNegative(300)));
    Recipe recipe = from(nonNegative(2), ingredients);

    List<QuantitativeIngredient> ingredientListFor = recipe
        .getIngredientListFor(nonNegative(4));

    assertThat(ingredientListFor).containsExactlyInAnyOrder(
        create(anIngredient1, wholeNumber(nonNegative(200))),
        create(anIngredient2, wholeNumber(nonNegative(600))));
  }

  @Test
  public void getIngredientsListReturnsCorrectAmountsForHalfTheNumberOfPeople() {
    Map<Ingredient, NonnegativeFraction> ingredients = new HashMap<>();
    ingredients.put(anIngredient1, wholeNumber(nonNegative(100)));
    ingredients.put(anIngredient2, wholeNumber(nonNegative(300)));
    Recipe recipe = from(nonNegative(3), ingredients);

    List<QuantitativeIngredient> ingredientListFor = recipe
        .getIngredientListFor(nonNegative(1));

    assertThat(ingredientListFor).containsExactlyInAnyOrder(
        create(anIngredient1, fraction(100, 3)),
        create(anIngredient2, wholeNumber(nonNegative(100))));
  }
}
