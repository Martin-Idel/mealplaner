// SPDX-License-Identifier: MIT

package mealplaner.model.shoppinglist;

import static mealplaner.commons.NonnegativeFraction.fraction;
import static mealplaner.commons.NonnegativeFraction.wholeNumber;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.recipes.QuantitativeIngredient.createQuantitativeIngredient;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommonsmodel.CommonBaseFunctions.getIngredient1;
import static testcommonsmodel.CommonBaseFunctions.getIngredient2;
import static testcommonsmodel.CommonBaseFunctions.getIngredient3;
import static testcommonsmodel.CommonBaseFunctions.getRecipe1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.Pair;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.QuantitativeIngredient;
import mealplaner.model.recipes.Recipe;
import mealplaner.model.settings.DefaultSettings;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class ShoppingListTest {

  @Test
  public void addsRecipesTogetherFaithfully() {
    List<Pair<Recipe, NonnegativeInteger>> recipes = getRecipeListForShoppingList();
    Map<Ingredient, NonnegativeFraction> expected = new HashMap<>();
    expected.put(getIngredient1(), wholeNumber(nonNegative(300)));
    expected.put(getIngredient2(), wholeNumber(nonNegative(400)));
    expected.put(getIngredient3(), wholeNumber(nonNegative(400)));

    ShoppingList shoppingList = ShoppingList.from(recipes);

    assertThat(shoppingList.getMap()).containsAllEntriesOf(expected);
    assertThat(expected).containsAllEntriesOf(shoppingList.getMap());
  }

  @Test
  public void getListReturnsGoodList() {
    List<Pair<Recipe, NonnegativeInteger>> recipes = getRecipeListForShoppingList();

    ShoppingList shoppingList = ShoppingList.from(recipes);

    // Order depends on IngredientType
    assertThat(shoppingList.getList()).containsExactly(
        createQuantitativeIngredient(
            getIngredient1(), wholeNumber(nonNegative(300))),
        createQuantitativeIngredient(
            getIngredient3(), wholeNumber(nonNegative(400))),
        createQuantitativeIngredient(
            getIngredient2(), wholeNumber(nonNegative(400))));
  }

  @Test
  public void addsRecipesTogetherFaithfullyIncludingFractions() {
    List<QuantitativeIngredient> ingredients = new ArrayList<>();
    ingredients.add(createQuantitativeIngredient(
        getIngredient1(), fraction(10, 3)));
    ingredients.add(createQuantitativeIngredient(
        getIngredient3(), fraction(4, 5)));
    Recipe recipe1 = Recipe.from(nonNegative(2), ingredients);

    List<QuantitativeIngredient> ingredients2 = new ArrayList<>();
    ingredients2.add(createQuantitativeIngredient(
        getIngredient1(), fraction(4, 5)));
    ingredients2.add(createQuantitativeIngredient(
        getIngredient3(), fraction(2, 1)));
    Recipe recipe2 = Recipe.from(nonNegative(2), ingredients2);
    List<Pair<Recipe, NonnegativeInteger>> recipes = addRecipesToShoppingList(recipe1, recipe2);

    ShoppingList shoppingList = ShoppingList.from(recipes);

    Map<Ingredient, NonnegativeFraction> expected = new HashMap<>();
    expected.put(getIngredient1(), fraction(112, 15)); // 2*10*5 from recipe1 + 3*4 from recipe2
    expected.put(getIngredient3(), fraction(18, 5)); // 2*4 from recipe1 + 2*5 from recipe2
    assertThat(shoppingList.getMap()).containsAllEntriesOf(expected);
    assertThat(expected).containsAllEntriesOf(shoppingList.getMap());
  }

  @Test
  public void equalsContract() {
    EqualsVerifier.forClass(ShoppingList.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }

  private List<Pair<Recipe, NonnegativeInteger>> getRecipeListForShoppingList() {
    Recipe recipe1 = getRecipe1();
    Recipe recipe2 = getRecipeForShoppingList();
    return addRecipesToShoppingList(recipe1, recipe2);
  }

  private List<Pair<Recipe, NonnegativeInteger>> addRecipesToShoppingList(Recipe recipe1,
      Recipe recipe2) {
    List<Pair<Recipe, NonnegativeInteger>> recipes = new ArrayList<>();
    recipes.add(Pair.of(recipe1, nonNegative(4)));
    recipes.add(Pair.of(recipe2, nonNegative(2)));
    return recipes;
  }

  private Recipe getRecipeForShoppingList() {
    List<QuantitativeIngredient> ingredients = new ArrayList<>();
    ingredients.add(createQuantitativeIngredient(
        getIngredient1(), wholeNumber(nonNegative(100))));
    ingredients.add(createQuantitativeIngredient(
        getIngredient3(), wholeNumber(nonNegative(400))));
    return Recipe.from(nonNegative(2), ingredients);
  }
}
