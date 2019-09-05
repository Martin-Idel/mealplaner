// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static mealplaner.io.xml.adapters.MealAdapter.convertMealV2FromXml;
import static mealplaner.io.xml.adapters.MealAdapter.convertMealV2ToXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;
import static testcommons.CommonFunctions.setupMealplanerDataWithAllIngredients;

import org.junit.Test;

import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;

public class MealAdapterTest {

  @Test
  public void adapterTest() {
    MealplanerData mealPlan = setupMealplanerDataWithAllIngredients();

    Meal meal1 = getMeal1();
    Meal meal2 = getMeal2();

    Meal convertedMeals1 = convertMealV2FromXml(mealPlan,
        convertMealV2ToXml(meal1));
    Meal convertedMeals2 = convertMealV2FromXml(mealPlan,
        convertMealV2ToXml(meal2));

    assertThat(convertedMeals1).isEqualTo(meal1);
    assertThat(convertedMeals2).isEqualTo(meal2);
  }
}
