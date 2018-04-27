// SPDX-License-Identifier: MIT

package guitests.ingredients;

import static mealplaner.commons.NonnegativeFraction.wholeNumber;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.recipes.Ingredient.ingredientWithUuid;
import static mealplaner.model.recipes.IngredientType.FLUID;
import static mealplaner.model.recipes.Measure.TEASPOON;
import static testcommons.CommonFunctions.getIngredient1;
import static testcommons.CommonFunctions.getIngredient2;
import static testcommons.CommonFunctions.getIngredient3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import guitests.helpers.AssertJMealplanerTestCase;
import mealplaner.commons.NonnegativeFraction;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.Recipe;

public class IngredientsEditDatabase extends AssertJMealplanerTestCase {
  public IngredientsEditDatabase() {
    super("src/test/resources/mealsXmlV2.xml",
        "src/test/resources/save.xml",
        "src/test/resources/ingredientsXmlV2.xml");
  }

  @Test
  public void canChangeAllAspectOfAnIngredient() {
    Ingredient newIngredient = ingredientWithUuid(getIngredient1().getId(), "New name", FLUID,
        TEASPOON);
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(newIngredient);
    ingredients.add(getIngredient2());
    ingredients.add(getIngredient3());

    windowHelpers.getIngredientsPane()
        .changeName(0, newIngredient.getName())
        .changeType(0, newIngredient.getType())
        .changeMeasure(0, newIngredient.getMeasure())
        .compareIngredientsInTable(ingredients);
  }

  @Test
  public void changingIngredientsAndCancellingWorksForAllAspects() {
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(getIngredient1());
    ingredients.add(getIngredient2());
    ingredients.add(getIngredient3());

    windowHelpers.getIngredientsPane()
        .changeName(0, "New name")
        .clickCancelButtonMakingSureItIsEnabled()
        .changeType(0, FLUID)
        .clickCancelButtonMakingSureItIsEnabled()
        .changeMeasure(0, TEASPOON)
        .clickCancelButtonMakingSureItIsEnabled()
        .compareIngredientsInTable(ingredients);
  }

  @Test
  public void removingIngredientReplacesIfIngredientInUse() {
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(getIngredient1());
    ingredients.add(getIngredient3());

    Map<Ingredient, NonnegativeFraction> recipeIngredients = new HashMap<>();
    recipeIngredients.put(getIngredient1(), wholeNumber(nonNegative(300)));
    Recipe expectedNewRecipe = Recipe.from(nonNegative(2), recipeIngredients);

    windowHelpers.getIngredientsPane()
        .removeIngredients(1)
        .saveAndReplaceIngredientsWithDefaults()
        .compareIngredientsInTable(ingredients)
        .saveButton().requireDisabled();

    windowHelpers.getMealsPane()
        .assertRecipeIn(1, expectedNewRecipe);
  }

  @Test
  public void editingNameReplacesNameInRecipe() {
    Ingredient newIngredient = ingredientWithUuid(getIngredient1().getId(), "New name",
        getIngredient1().getType(),
        getIngredient1().getMeasure());
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(newIngredient);
    ingredients.add(getIngredient2());
    ingredients.add(getIngredient3());

    Map<Ingredient, NonnegativeFraction> recipeIngredients = new HashMap<>();
    recipeIngredients.put(newIngredient, wholeNumber(nonNegative(100)));
    recipeIngredients.put(getIngredient2(), wholeNumber(nonNegative(200)));
    Recipe expectedNewRecipe = Recipe.from(nonNegative(2), recipeIngredients);

    windowHelpers.getIngredientsPane()
        .changeName(0, newIngredient.getName())
        .changeType(0, newIngredient.getType())
        .changeMeasure(0, newIngredient.getMeasure())
        .compareIngredientsInTable(ingredients)
        .saveButton().click();

    windowHelpers.getMealsPane()
        .assertRecipeIn(1, expectedNewRecipe);
  }

  @Test
  public void removingIngredientDoesNotRemoveOnRequestIfIngredientInUse() {
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(getIngredient1());
    ingredients.add(getIngredient2());
    ingredients.add(getIngredient3());

    windowHelpers.getIngredientsPane()
        .removeIngredients(1)
        .saveButDoNotRemoveUsedIngredients()
        .compareIngredientsInTable(ingredients)
        .saveButton().requireDisabled();
  }
}
