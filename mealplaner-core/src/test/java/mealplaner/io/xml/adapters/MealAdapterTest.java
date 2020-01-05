// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static mealplaner.commons.NonnegativeInteger.ONE;
import static mealplaner.io.xml.adapters.MealAdapter.convertMealV3FromXml;
import static mealplaner.io.xml.adapters.MealAdapter.convertMealV3ToXml;
import static mealplaner.model.meal.MealBuilder.meal;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonBaseFunctions.setupMealplanerDataWithAllIngredients;
import static xmlcommons.TestMealFact.TestEnum.TEST1;

import org.junit.Test;

import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;
import mealplaner.plugins.PluginStore;
import xmlcommons.TestMealFact;

public class MealAdapterTest {

  @Test
  public void adapterTest() {
    MealplanerData mealPlan = setupMealplanerDataWithAllIngredients();

    Meal meal1 = meal()
        .name("Test1")
        .addDaysPassed(ONE)
        .addFact(new TestMealFact(TEST1))
        .create();

    var pluginStore = new PluginStore();
    pluginStore.registerMealExtension(TestMealFact.class, TestMealFact.class, TestMealFact::new);
    Meal convertedMeals1 = convertMealV3FromXml(mealPlan, convertMealV3ToXml(meal1), pluginStore);

    assertThat(convertedMeals1).isEqualTo(meal1);
  }
}
