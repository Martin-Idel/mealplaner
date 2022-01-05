// SPDX-License-Identifier: MIT

package etoetests.xmlsmoketests;

import static etoetests.CommonFunctions.getMeal1;
import static etoetests.CommonFunctions.getMeal2;
import static etoetests.CommonFunctions.getMeal3;
import static etoetests.CommonFunctions.setupMealplanerDataWithAllIngredients;
import static mealplaner.io.xml.MealsReader.loadXml;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;

class MealsXmlInteractionTest extends EtoeXmlInteraction {
  private static final String RESOURCE_FILE_WITH_THREE_MEALS_V3 = "src/test/resources/mealsXmlV3.xml";

  @Test
  void loadingMealsWorksCorrectlyForVersion3() {
    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal1());
    meals.add(getMeal2());
    meals.add(getMeal3());
    meals.sort(Meal::compareTo);

    loadFileWithName(RESOURCE_FILE_WITH_THREE_MEALS_V3);

    MealplanerData mealPlan = setupMealplanerDataWithAllIngredients();
    List<Meal> database = loadXml(mealPlan, DESTINATION_FILE_PATH, pluginStore);

    database.sort(Meal::compareTo);

    assertThat(database).containsExactlyElementsOf(meals);
  }
}
