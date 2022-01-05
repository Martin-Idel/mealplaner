// SPDX-License-Identifier: MIT

package mealplaner.plugins.vegetarian;

import static mealplaner.commons.NonnegativeInteger.ONE;
import static mealplaner.model.meal.MealBuilder.meal;
import static testcommons.PluginsXmlUtils.assertSaveAndReloadMealWorksCorrectly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mealplaner.model.meal.Meal;
import mealplaner.plugins.vegetarian.mealextension.VegetarianFact;
import testcommons.PluginsUtils;
import testcommons.XmlInteraction;

class VegetarianXmlTest extends XmlInteraction {
  @BeforeEach
  void setUp() {
    PluginsUtils.setupMessageBundles(new VegetarianPlugin());
  }

  @Test
  void roundTripWithCookingTimeCanBeSavedCorrectly() {
    Meal meal = meal()
        .name("Test1")
        .daysPassed(ONE)
        .addFact(new VegetarianFact(true))
        .create();

    assertSaveAndReloadMealWorksCorrectly(meal, DESTINATION_FILE_PATH, new VegetarianPlugin());
  }
}
