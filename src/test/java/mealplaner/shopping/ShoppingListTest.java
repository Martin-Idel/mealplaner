package mealplaner.shopping;

import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.recipes.model.QuantitativeIngredient.create;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getIngredient1;
import static testcommons.CommonFunctions.getIngredient2;
import static testcommons.CommonFunctions.getIngredient3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.Pair;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.Recipe;
import testcommons.BundlesInitialization;
import testcommons.CommonFunctions;

public class ShoppingListTest {
  @Rule
  public final BundlesInitialization bundlesInitialization = new BundlesInitialization();

  @Test
  public void addsRecipesTogetherFaithfully() {
    Recipe recipe1 = CommonFunctions.getRecipe1();

    Map<Ingredient, NonnegativeInteger> ingredients = new HashMap<>();
    ingredients.put(getIngredient1(), nonNegative(100));
    ingredients.put(getIngredient3(), nonNegative(400));
    Recipe recipe2 = Recipe.from(nonNegative(2), ingredients);

    List<Pair<Recipe, NonnegativeInteger>> recipes = new ArrayList<>();
    recipes.add(Pair.of(recipe1, nonNegative(4)));
    recipes.add(Pair.of(recipe2, nonNegative(2)));

    final ShoppingList shoppingList = ShoppingList.from(recipes);

    Map<Ingredient, NonnegativeInteger> expected = new HashMap<>();
    expected.put(getIngredient1(), nonNegative(300));
    expected.put(getIngredient2(), nonNegative(400));
    expected.put(getIngredient3(), nonNegative(400));
    assertThat(shoppingList.getMap()).containsAllEntriesOf(expected);
    assertThat(expected).containsAllEntriesOf(shoppingList.getMap());
  }

  @Test
  public void getListReturnsGoodList() {
    Recipe recipe1 = CommonFunctions.getRecipe1();

    Map<Ingredient, NonnegativeInteger> ingredients = new HashMap<>();
    ingredients.put(getIngredient1(), nonNegative(100));
    ingredients.put(getIngredient3(), nonNegative(400));
    Recipe recipe2 = Recipe.from(nonNegative(2), ingredients);

    List<Pair<Recipe, NonnegativeInteger>> recipes = new ArrayList<>();
    recipes.add(Pair.of(recipe1, nonNegative(4)));
    recipes.add(Pair.of(recipe2, nonNegative(2)));

    ShoppingList shoppingList = ShoppingList.from(recipes);

    // Order depends on IngredientType
    assertThat(shoppingList.getList()).containsExactly(
        create(getIngredient1(), nonNegative(300)),
        create(getIngredient3(), nonNegative(400)),
        create(getIngredient2(), nonNegative(400)));
  }
}
