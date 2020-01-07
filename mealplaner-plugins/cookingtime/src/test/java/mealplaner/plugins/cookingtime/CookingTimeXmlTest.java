// SPDX-License-Identifier: MIT

package mealplaner.plugins.cookingtime;

import static mealplaner.commons.NonnegativeInteger.ONE;
import static mealplaner.commons.NonnegativeInteger.TWO;
import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.plugins.cookingtime.mealextension.CookingTime.LONG;
import static mealplaner.plugins.cookingtime.mealextension.CookingTime.SHORT;
import static mealplaner.plugins.cookingtime.settingextension.CookingTimeSubSetting.cookingTimeWithProhibited;
import static testcommons.PluginsXmlUtils.assertSaveAndReloadMealWorksCorrectly;
import static testcommons.PluginsXmlUtils.assertSaveAndReloadSettingWorksCorrectly;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mealplaner.model.meal.Meal;
import mealplaner.model.settings.Settings;
import mealplaner.model.settings.SettingsBuilder;
import mealplaner.plugins.cookingtime.mealextension.CookingTimeFact;
import testcommons.PluginsUtils;
import testcommons.XmlInteraction;

public class CookingTimeXmlTest extends XmlInteraction {
  @BeforeEach
  public void setUp() {
    PluginsUtils.setupMessageBundles(new CookingTimePlugin());
  }

  @Test
  public void roundTripWithCookingTimeCanBeSavedCorrectly() {
    Meal meal = meal()
        .name("Test1")
        .daysPassed(ONE)
        .addFact(new CookingTimeFact(SHORT))
        .create();

    assertSaveAndReloadMealWorksCorrectly(meal, DESTINATION_FILE_PATH, new CookingTimePlugin());
  }

  @Test
  public void roundTripWithCookingTimeSubSettingWorksCorrectly() {
    Settings settings = SettingsBuilder.setting()
        .numberOfPeople(TWO)
        .addSetting(cookingTimeWithProhibited(SHORT, LONG))
        .create();

    assertSaveAndReloadSettingWorksCorrectly(settings, new CookingTimePlugin(), DESTINATION_FILE_PATH);
  }
}
