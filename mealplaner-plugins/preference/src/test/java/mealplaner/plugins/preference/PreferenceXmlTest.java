// SPDX-License-Identifier: MIT

package mealplaner.plugins.preference;

import static mealplaner.commons.NonnegativeInteger.ONE;
import static mealplaner.commons.NonnegativeInteger.TWO;
import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.model.settings.SettingsBuilder.setting;
import static mealplaner.plugins.preference.mealextension.CookingPreference.VERY_POPULAR;
import static mealplaner.plugins.preference.settingextension.PreferenceSettings.RARE_NONE;
import static testcommons.PluginsXmlUtils.assertSaveAndReloadMealWorksCorrectly;
import static testcommons.PluginsXmlUtils.assertSaveAndReloadSettingWorksCorrectly;

import org.junit.jupiter.api.Test;

import mealplaner.model.meal.Meal;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.preference.mealextension.CookingPreferenceFact;
import mealplaner.plugins.preference.settingextension.CookingPreferenceSubSetting;
import testcommons.XmlInteraction;

public class PreferenceXmlTest extends XmlInteraction {
  @Test
  public void roundTripWithCookingTimeCanBeSavedCorrectly() {
    Meal meal = meal()
        .name("Test1")
        .daysPassed(ONE)
        .addFact(new CookingPreferenceFact(VERY_POPULAR))
        .create();

    assertSaveAndReloadMealWorksCorrectly(meal, DESTINATION_FILE_PATH, new CookingPreferencePlugin());
  }

  @Test
  public void roundTripWithCookingTimeSubSettingWorksCorrectly() {
    Settings settings = setting()
        .numberOfPeople(TWO)
        .addSetting(new CookingPreferenceSubSetting(RARE_NONE))
        .create();

    assertSaveAndReloadSettingWorksCorrectly(settings, new CookingPreferencePlugin(), DESTINATION_FILE_PATH);
  }
}
