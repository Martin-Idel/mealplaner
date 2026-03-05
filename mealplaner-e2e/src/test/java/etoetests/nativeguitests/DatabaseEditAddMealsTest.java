// SPDX-License-Identifier: MIT

package etoetests.nativeguitests;

import static etoetests.CommonFunctions.getIngredient1;
import static etoetests.CommonFunctions.getIngredient2;
import static etoetests.CommonFunctions.getIngredient3;
import static etoetests.CommonFunctions.getMeal1;
import static etoetests.CommonFunctions.getMeal2;
import static java.util.UUID.nameUUIDFromBytes;
import static mealplaner.commons.NonnegativeFraction.fraction;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.meal.MealBuilder.from;
import static mealplaner.model.recipes.IngredientBuilder.ingredient;
import static mealplaner.model.recipes.IngredientType.CANNED_FRUIT;
import static mealplaner.model.recipes.Measure.GRAM;
import static mealplaner.model.recipes.Measures.createMeasures;
import static mealplaner.model.recipes.QuantitativeIngredient.createQuantitativeIngredient;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import etoetests.guitests.constants.ComponentNames;
import etoetests.guitests.helpers.MealplanerTestCase;
import mealplaner.model.meal.Meal;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.QuantitativeIngredient;
import mealplaner.model.recipes.Recipe;

@Tag("guitest")
public class DatabaseEditAddMealsTest extends MealplanerTestCase {
  @Test
  public void addMealAddsMealsInCorrectOrder() throws Exception {
    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal1());
    meals.add(getMeal2());
    windowHelpers.getMealsPane()
        .addMeal(getMeal2())
        .addMeal(getMeal1())
        .compareDatabaseInTable(meals);
    clickButtonAndWaitForDisabledThenAssert(mainFrame, ComponentNames.BUTTON_DATABASEEDIT_CANCEL);
    assertButtonIsDisabled(mainFrame, ComponentNames.BUTTON_DATABASEEDIT_SAVE);
  }

  @Test
  public void addMealWithNewIngredientsAddsIngredientsToIngredientsList() throws Exception {
    List<Meal> meals = new ArrayList<>();
    meals.add(getMealWithNewIngredient());
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(getIngredient1());
    ingredients.add(getIngredient2());
    ingredients.add(getIngredient3());
    ingredients.add(getIngredient4());
    windowHelpers.getMealsPane()
        .addMeal(getMealWithNewIngredient(), getIngredient4())
        .save()
        .compareDatabaseInTable(meals);
    windowHelpers.getIngredientsPane()
        .compareIngredientsInTable(ingredients);
  }

  private static Meal getMealWithNewIngredient() {
    List<QuantitativeIngredient> recipeMap = new ArrayList<>();
    recipeMap.add(createQuantitativeIngredient(getIngredient1(), fraction(3, 1)));
    recipeMap.add(createQuantitativeIngredient(getIngredient4(), fraction(2, 1)));
    Recipe recipe = Recipe.from(nonNegative(1), recipeMap);
    return from(getMeal2()).recipe(recipe).create();
  }

  private static Ingredient getIngredient4() {
    return ingredient()
        .withUuid(nameUUIDFromBytes("Test4".getBytes(StandardCharsets.UTF_8)))
        .withName("Test4")
        .withType(CANNED_FRUIT)
        .withMeasures(createMeasures(GRAM))
        .create();
  }
}
