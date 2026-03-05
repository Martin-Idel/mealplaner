// SPDX-License-Identifier: MIT

package etoetests.nativeguitests.ingredients;

import static etoetests.CommonFunctions.getIngredient1;
import static etoetests.CommonFunctions.getIngredient2;
import static etoetests.CommonFunctions.getIngredient3;
import static mealplaner.model.recipes.IngredientBuilder.ingredient;
import static mealplaner.model.recipes.IngredientType.FLUID;
import static mealplaner.model.recipes.Measure.TEASPOON;
import static mealplaner.model.recipes.Measures.createMeasures;
import static mealplaner.model.recipes.QuantitativeIngredient.createQuantitativeIngredient;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import etoetests.guitests.constants.ComponentNames;
import etoetests.guitests.helpers.MealplanerTestCase;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.IngredientBuilder;

@Tag("guitest")
public class IngredientsEditDatabaseTest extends MealplanerTestCase {
  public IngredientsEditDatabaseTest() {
    super("src/test/resources/mealsXmlV3.xml",
        "src/test/resources/save.xml",
        "src/test/resources/ingredientsXmlV3.xml");
  }

  @Test
  public void canChangeAllAspectOfAnIngredient() throws Exception {
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
  public void changingIngredientsAndCancellingWorksForAllAspects() throws Exception {
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(getIngredient1());
    ingredients.add(getIngredient2());
    ingredients.add(getIngredient3());

    windowHelpers.getIngredientsPane()
        .changeName(0, "New name");
    clickButtonAndWaitForDisabledThenAssert(mainFrame, ComponentNames.BUTTON_INGREDIENTSEDIT_CANCEL);

    windowHelpers.getIngredientsPane()
        .changeType(0, FLUID);
    clickButtonAndWaitForDisabledThenAssert(mainFrame, ComponentNames.BUTTON_INGREDIENTSEDIT_CANCEL);

    windowHelpers.getIngredientsPane()
        .changeMeasure(0, TEASPOON);
    clickButtonAndWaitForDisabledThenAssert(mainFrame, ComponentNames.BUTTON_INGREDIENTSEDIT_CANCEL);

    windowHelpers.getIngredientsPane()
        .compareIngredientsInTable(ingredients);
  }

  @Test
  public void removingIngredientReplacesIfIngredientInUse() throws Exception {
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(getIngredient1());
    ingredients.add(getIngredient3());

    windowHelpers.getIngredientsPane()
        .removeIngredients(1)
        .saveAndReplaceIngredientsWithDefaults()
        .compareIngredientsInTable(ingredients);

    clickButtonAndWaitForDisabledThenAssert(mainFrame, ComponentNames.BUTTON_INGREDIENTSEDIT_SAVE);
  }

  @Test
  public void editingNameReplacesNameInRecipe() throws Exception {
    Ingredient newIngredient = IngredientBuilder.from(getIngredient1()).withName("New name").create();
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(newIngredient);
    ingredients.add(getIngredient2());
    ingredients.add(getIngredient3());

    windowHelpers.getIngredientsPane()
        .changeName(0, newIngredient.getName())
        .changeType(0, newIngredient.getType())
        .changeMeasure(0, newIngredient.getPrimaryMeasure())
        .compareIngredientsInTable(ingredients);

    clickButtonAndWaitForDisabledThenAssert(mainFrame, ComponentNames.BUTTON_INGREDIENTSEDIT_SAVE);
  }

  @Test
  public void removingIngredientDoesNotRemoveOnRequestIfIngredientInUse() throws Exception {
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(getIngredient1());
    ingredients.add(getIngredient2());
    ingredients.add(getIngredient3());

    windowHelpers.getIngredientsPane()
        .removeIngredients(1)
        .saveButDoNotRemoveUsedIngredients();

    assertButtonIsDisabled(mainFrame, ComponentNames.BUTTON_INGREDIENTSEDIT_SAVE);

    windowHelpers.getIngredientsPane().compareIngredientsInTable(ingredients);

    clickButtonAndWaitForDisabledThenAssert(mainFrame, ComponentNames.BUTTON_INGREDIENTSEDIT_SAVE);
  }
}
