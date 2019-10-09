// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static mealplaner.io.xml.adapters.MealAdapter.convertMealV3FromXml;
import static mealplaner.io.xml.adapters.MealAdapter.convertMealV3ToXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;
import static testcommons.CommonFunctions.setupMealplanerDataWithAllIngredients;

import org.junit.Test;

import mealplaner.Kochplaner;
import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;

public class MealAdapterTest {

  @Test
  public void adapterTest() {
    MealplanerData mealPlan = setupMealplanerDataWithAllIngredients();

    Meal meal1 = getMeal1();
    Meal meal2 = getMeal2();

    Meal convertedMeals1 = convertMealV3FromXml(mealPlan,
        convertMealV3ToXml(meal1), Kochplaner.registerPlugins());
    Meal convertedMeals2 = convertMealV3FromXml(mealPlan,
        convertMealV3ToXml(meal2), Kochplaner.registerPlugins());

    assertThat(convertedMeals1).isEqualTo(meal1);
    assertThat(convertedMeals2).isEqualTo(meal2);
  }
}
