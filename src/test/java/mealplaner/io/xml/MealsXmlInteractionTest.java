// SPDX-License-Identifier: MIT

package mealplaner.io.xml;

import static mealplaner.io.xml.MealsReader.loadXml;
import static mealplaner.io.xml.MealsWriter.saveXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;
import static testcommons.CommonFunctions.getMeal3;
import static testcommons.CommonFunctions.setupMealplanerDataWithAllIngredients;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;
import testcommons.XmlInteraction;

public class MealsXmlInteractionTest extends XmlInteraction {
  private static final String RESOURCE_FILE_WITH_THREE_MEALS_V2 = "src/test/resources/mealsXmlV2.xml";

  @Test
  public void loadingMealsWorksCorrectlyForVersion2() {
    MealplanerData mealPlan = setupMealplanerDataWithAllIngredients();
    List<Meal> meals = fillListWithThreeMeals();

    loadFileWithName(RESOURCE_FILE_WITH_THREE_MEALS_V2);

    List<Meal> database = loadXml(mealPlan, DESTINATION_FILE_PATH);

    database.sort((meal1, meal2) -> meal1.compareTo(meal2));

    assertThat(database).containsExactlyElementsOf(meals);
  }

  @Test
  public void roundTripResultsInSameOutputAsInput() {
    MealplanerData mealPlan = setupMealplanerDataWithAllIngredients();
    List<Meal> meals = fillListWithThreeMeals();

    saveXml(meals, DESTINATION_FILE_PATH);
    List<Meal> roundTripMeals = loadXml(mealPlan, DESTINATION_FILE_PATH);

    roundTripMeals.sort((meal1, meal2) -> meal1.compareTo(meal2));
    meals.sort((meal1, meal2) -> meal1.compareTo(meal2));
    assertThat(roundTripMeals).containsExactlyElementsOf(meals);
  }

  private List<Meal> fillListWithThreeMeals() {
    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal1());
    meals.add(getMeal2());
    meals.add(getMeal3());
    meals.sort((meal1, meal2) -> meal1.compareTo(meal2));
    return meals;
  }
}
