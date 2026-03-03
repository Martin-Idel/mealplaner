// SPDX-License-Identifier: MIT

package mealplaner.plugins.childpreference;

import static mealplaner.commons.NonnegativeInteger.ONE;
import static mealplaner.model.meal.MealBuilder.meal;
import static testcommons.PluginsXmlUtils.assertSaveAndReloadMealWorksCorrectly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mealplaner.model.meal.Meal;
import mealplaner.plugins.childpreference.mealextension.ChildPreferenceFact;
import testcommons.PluginsUtils;
import testcommons.XmlInteraction;

class ChildPreferenceXmlTest extends XmlInteraction {
  @BeforeEach
  void setUp() {
    PluginsUtils.setupMessageBundles(new ChildPreferencePlugin());
  }

  @Test
  void roundTripWithChildPreferenceCanBeSavedCorrectly() {
    Meal meal = meal()
        .name("Test1")
        .daysPassed(ONE)
        .addFact(new ChildPreferenceFact(true))
        .create();

    assertSaveAndReloadMealWorksCorrectly(meal, DESTINATION_FILE_PATH, new ChildPreferencePlugin());
  }
}