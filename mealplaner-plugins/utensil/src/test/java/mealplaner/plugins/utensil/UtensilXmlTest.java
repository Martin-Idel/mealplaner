// SPDX-License-Identifier: MIT

package mealplaner.plugins.utensil;

import static mealplaner.commons.NonnegativeInteger.ONE;
import static mealplaner.commons.NonnegativeInteger.TWO;
import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.model.settings.SettingsBuilder.setting;
import static mealplaner.plugins.utensil.mealextension.ObligatoryUtensil.CASSEROLE;
import static mealplaner.plugins.utensil.settingextension.CasseroleSettings.ONLY;
import static testcommons.PluginsXmlUtils.assertSaveAndReloadMealWorksCorrectly;
import static testcommons.PluginsXmlUtils.assertSaveAndReloadSettingWorksCorrectly;

import org.junit.jupiter.api.Test;

import mealplaner.model.meal.Meal;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.utensil.mealextension.ObligatoryUtensilFact;
import mealplaner.plugins.utensil.settingextension.CasseroleSubSetting;
import testcommons.XmlInteraction;

public class UtensilXmlTest extends XmlInteraction {
  @Test
  public void roundTripWithCookingTimeCanBeSavedCorrectly() {
    Meal meal = meal()
        .name("Test1")
        .daysPassed(ONE)
        .addFact(new ObligatoryUtensilFact(CASSEROLE))
        .create();

    assertSaveAndReloadMealWorksCorrectly(meal, DESTINATION_FILE_PATH, new ObligatoryUtensilPlugin());
  }

  @Test
  public void roundTripWithCookingTimeSubSettingWorksCorrectly() {
    Settings settings = setting()
        .numberOfPeople(TWO)
        .addSetting(new CasseroleSubSetting(ONLY))
        .create();

    assertSaveAndReloadSettingWorksCorrectly(settings, new ObligatoryUtensilPlugin(), DESTINATION_FILE_PATH);
  }
}
