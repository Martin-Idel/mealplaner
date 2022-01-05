// SPDX-License-Identifier: MIT

package mealplaner.plugins.sidedish;

import static mealplaner.commons.NonnegativeInteger.ONE;
import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.plugins.sidedish.mealextension.Sidedish.PASTA;
import static testcommons.PluginsXmlUtils.assertSaveAndReloadMealWorksCorrectly;

import org.junit.jupiter.api.Test;

import mealplaner.model.meal.Meal;
import mealplaner.plugins.sidedish.mealextension.SidedishFact;
import testcommons.PluginsUtils;
import testcommons.XmlInteraction;

class SideDishXmlTest extends XmlInteraction {
  @Test
  void roundTripWithCookingTimeCanBeSavedCorrectly() {
    PluginsUtils.setupMessageBundles(new SideDishPlugin());
    Meal meal = meal()
        .name("Test1")
        .daysPassed(ONE)
        .addFact(new SidedishFact(PASTA))
        .create();

    assertSaveAndReloadMealWorksCorrectly(meal, DESTINATION_FILE_PATH, new SideDishPlugin());
  }
}
