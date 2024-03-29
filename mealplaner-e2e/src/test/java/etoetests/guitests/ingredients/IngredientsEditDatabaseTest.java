// SPDX-License-Identifier: MIT

package etoetests.guitests.ingredients;

import static etoetests.CommonFunctions.getIngredient1;
import static etoetests.CommonFunctions.getIngredient2;
import static etoetests.CommonFunctions.getIngredient3;
import static mealplaner.commons.NonnegativeFraction.wholeNumber;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.recipes.IngredientBuilder.ingredient;
import static mealplaner.model.recipes.IngredientType.FLUID;
import static mealplaner.model.recipes.Measure.TEASPOON;
import static mealplaner.model.recipes.Measures.createMeasures;
import static mealplaner.model.recipes.QuantitativeIngredient.createQuantitativeIngredient;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import etoetests.guitests.helpers.AssertJMealplanerTestCase;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.IngredientBuilder;
import mealplaner.model.recipes.QuantitativeIngredient;
import mealplaner.model.recipes.Recipe;

public class IngredientsEditDatabaseTest extends AssertJMealplanerTestCase {
  public IngredientsEditDatabaseTest() {
    super("src/test/resources/mealsXmlV3.xml",
        "src/test/resources/save.xml",
        "src/test/resources/ingredientsXmlV3.xml");
  }

  @Test
  public void canChangeAllAspectOfAnIngredient() {
    var newIngredient = ingredient()
        .withUuid(getIngredient1().getId())
        .withName("New name")
        .withType(FLUID)
        .withMeasures(createMeasures(TEASPOON))
        .create();
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(newIngredient);
    ingredients.add(getIngredient2());
    ingredients.add(getIngredient3());

    windowHelpers.getIngredientsPane()
        .changeName(0, newIngredient.getName())
        .changeType(0, newIngredient.getType())
        .changeMeasure(0, newIngredient.getPrimaryMeasure())
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

    List<QuantitativeIngredient> recipeIngredients = new ArrayList<>();
    recipeIngredients.add(createQuantitativeIngredient(getIngredient1(), wholeNumber(nonNegative(100))));
    recipeIngredients.add(createQuantitativeIngredient(getIngredient1(), wholeNumber(nonNegative(200))));
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
    Ingredient newIngredient = IngredientBuilder.from(getIngredient1()).withName("New name").create();
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(newIngredient);
    ingredients.add(getIngredient2());
    ingredients.add(getIngredient3());

    List<QuantitativeIngredient> recipeIngredients = new ArrayList<>();
    recipeIngredients.add(createQuantitativeIngredient(newIngredient, wholeNumber(nonNegative(100))));
    recipeIngredients.add(createQuantitativeIngredient(getIngredient2(), wholeNumber(nonNegative(200))));
    Recipe expectedNewRecipe = Recipe.from(nonNegative(2), recipeIngredients);

    windowHelpers.getIngredientsPane()
        .changeName(0, newIngredient.getName())
        .changeType(0, newIngredient.getType())
        .changeMeasure(0, newIngredient.getPrimaryMeasure())
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
