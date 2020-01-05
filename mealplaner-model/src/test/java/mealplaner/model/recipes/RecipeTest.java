// SPDX-License-Identifier: MIT

package mealplaner.model.recipes;

import static mealplaner.commons.NonnegativeFraction.fraction;
import static mealplaner.commons.NonnegativeFraction.wholeNumber;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.recipes.QuantitativeIngredient.createQuantitativeIngredient;
import static mealplaner.model.recipes.Recipe.from;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommonsmodel.CommonBaseFunctions.getIngredient1;
import static testcommonsmodel.CommonBaseFunctions.getIngredient2;
import static testcommonsmodel.CommonBaseFunctions.getIngredient4;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class RecipeTest {
  private Ingredient anIngredient1;
  private Ingredient anIngredient2;
  private Ingredient anIngredient3;
  private QuantitativeIngredient quantitativeIngredient1;
  private QuantitativeIngredient quantitativeIngredient2;

  @Before
  public void setUp() {
    anIngredient1 = getIngredient1();
    anIngredient2 = getIngredient2();
    anIngredient3 = getIngredient4();
    quantitativeIngredient1 = createQuantitativeIngredient(anIngredient1, wholeNumber(nonNegative(1)));
    quantitativeIngredient2 = createQuantitativeIngredient(anIngredient2, wholeNumber(nonNegative(10)));
  }

  @Test
  public void initialiserCreatesCorrectMapWithDistinctEntries() {
    List<QuantitativeIngredient> ingredientList = new ArrayList<>();
    ingredientList.add(quantitativeIngredient1);
    ingredientList.add(quantitativeIngredient2);

    Recipe recipe = Recipe.from(nonNegative(1), ingredientList);

    assertThat(recipe.getIngredientsAsIs()).containsExactlyInAnyOrder(
        createQuantitativeIngredient(anIngredient1, wholeNumber(nonNegative(1))),
        createQuantitativeIngredient(anIngredient2, wholeNumber(nonNegative(10))));
  }

  @Test
  public void getIngredientsListReturnsDoubleAmountsForDoubleNumberOfPeople() {
    List<QuantitativeIngredient> ingredients = new ArrayList<>();
    ingredients.add(createQuantitativeIngredient(anIngredient1, wholeNumber(nonNegative(100))));
    ingredients.add(createQuantitativeIngredient(anIngredient2, wholeNumber(nonNegative(300))));
    Recipe recipe = from(nonNegative(2), ingredients);

    List<QuantitativeIngredient> ingredientListFor = recipe
        .getIngredientListFor(nonNegative(4));

    assertThat(ingredientListFor).containsExactlyInAnyOrder(
        createQuantitativeIngredient(anIngredient1, wholeNumber(nonNegative(200))),
        createQuantitativeIngredient(anIngredient2, wholeNumber(nonNegative(600))));
  }

  @Test
  public void getIngredientsListReturnsHalfAmountsForHalfTheNumberOfPeople() {
    List<QuantitativeIngredient> ingredients = new ArrayList<>();
    ingredients.add(createQuantitativeIngredient(anIngredient1, wholeNumber(nonNegative(100))));
    ingredients.add(createQuantitativeIngredient(anIngredient2, wholeNumber(nonNegative(300))));
    Recipe recipe = from(nonNegative(3), ingredients);

    List<QuantitativeIngredient> ingredientListFor = recipe
        .getIngredientListFor(nonNegative(1));

    assertThat(ingredientListFor).containsExactlyInAnyOrder(
        createQuantitativeIngredient(anIngredient1, fraction(100, 3)),
        createQuantitativeIngredient(anIngredient2, wholeNumber(nonNegative(100))));
  }

  @Test
  public void getIngredientsWithPrimaryMeasureForReturnsAmountInPrimaryMeasure() {
    List<QuantitativeIngredient> ingredients = new ArrayList<>();
    ingredients.add(createQuantitativeIngredient(anIngredient1, wholeNumber(nonNegative(100))));
    ingredients.add(createQuantitativeIngredient(
        anIngredient3, Measure.TEASPOON, wholeNumber(nonNegative(300))));
    Recipe recipe = from(nonNegative(2), ingredients);

    List<QuantitativeIngredient> ingredientsForOnePerson = recipe
        .getIngredientsWithPrimaryMeasureFor(nonNegative(1));

    assertThat(ingredientsForOnePerson).containsExactlyInAnyOrder(
        createQuantitativeIngredient(anIngredient1, fraction(100, 2)),
        createQuantitativeIngredient(anIngredient3, fraction(300, 2 * 2)));
  }
}