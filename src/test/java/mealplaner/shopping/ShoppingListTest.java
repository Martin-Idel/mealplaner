package mealplaner.shopping;

import static mealplaner.commons.NonnegativeFraction.fraction;
import static mealplaner.commons.NonnegativeFraction.wholeNumber;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.recipes.model.QuantitativeIngredient.create;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getIngredient1;
import static testcommons.CommonFunctions.getIngredient2;
import static testcommons.CommonFunctions.getIngredient3;
import static testcommons.CommonFunctions.getRecipe1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.Pair;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.Recipe;

public class ShoppingListTest {

  @Test
  public void addsRecipesTogetherFaithfully() {
    List<Pair<Recipe, NonnegativeInteger>> recipes = getRecipeListForShoppingList();

    ShoppingList shoppingList = ShoppingList.from(recipes);

    Map<Ingredient, NonnegativeFraction> expected = new HashMap<>();
    expected.put(getIngredient1(), wholeNumber(nonNegative(300)));
    expected.put(getIngredient2(), wholeNumber(nonNegative(400)));
    expected.put(getIngredient3(), wholeNumber(nonNegative(400)));
    assertThat(shoppingList.getMap()).containsAllEntriesOf(expected);
    assertThat(expected).containsAllEntriesOf(shoppingList.getMap());
  }

  @Test
  public void getListReturnsGoodList() {
    List<Pair<Recipe, NonnegativeInteger>> recipes = getRecipeListForShoppingList();

    ShoppingList shoppingList = ShoppingList.from(recipes);

    // Order depends on IngredientType
    assertThat(shoppingList.getList()).containsExactly(
        create(getIngredient1(), wholeNumber(nonNegative(300))),
        create(getIngredient3(), wholeNumber(nonNegative(400))),
        create(getIngredient2(), wholeNumber(nonNegative(400))));
  }

  @Test
  public void addsRecipesTogetherFaithfullyIncludingFractions() {
    Map<Ingredient, NonnegativeFraction> ingredients = new HashMap<>();
    ingredients.put(getIngredient1(), fraction(10, 3));
    ingredients.put(getIngredient3(), fraction(4, 5));
    Recipe recipe1 = Recipe.from(nonNegative(2), ingredients);

    Map<Ingredient, NonnegativeFraction> ingredients2 = new HashMap<>();
    ingredients2.put(getIngredient1(), fraction(4, 5));
    ingredients2.put(getIngredient3(), fraction(2, 1));
    Recipe recipe2 = Recipe.from(nonNegative(2), ingredients2);

    List<Pair<Recipe, NonnegativeInteger>> recipes = addRecipesToShoppingList(recipe1, recipe2);

    ShoppingList shoppingList = ShoppingList.from(recipes);

    Map<Ingredient, NonnegativeFraction> expected = new HashMap<>();
    expected.put(getIngredient1(), fraction(112, 15)); // 2*10*5 from recipe1 + 3*4 from recipe2
    expected.put(getIngredient3(), fraction(18, 5)); // 2*4 from recipe1 + 2*5 from recipe2
    assertThat(shoppingList.getMap()).containsAllEntriesOf(expected);
    assertThat(expected).containsAllEntriesOf(shoppingList.getMap());
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
    Map<Ingredient, NonnegativeFraction> ingredients = new HashMap<>();
    ingredients.put(getIngredient1(), wholeNumber(nonNegative(100)));
    ingredients.put(getIngredient3(), wholeNumber(nonNegative(400)));
    return Recipe.from(nonNegative(2), ingredients);
  }
}
