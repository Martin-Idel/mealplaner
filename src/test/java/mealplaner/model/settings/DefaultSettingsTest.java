// SPDX-License-Identifier: MIT

package mealplaner.model.settings;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.TUESDAY;
import static mealplaner.commons.NonnegativeInteger.THREE;
import static mealplaner.model.settings.DefaultSettings.from;
import static mealplaner.model.settings.enums.CasseroleSettings.ONLY;
import static mealplaner.plugins.plugins.cookingtime.CookingTime.LONG;
import static mealplaner.plugins.plugins.cookingtime.CookingTimeSetting.cookingTimeWithProhibited;
import static mealplaner.plugins.plugins.preference.setting.PreferenceSettings.RARE_PREFERED;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getSettings1;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import mealplaner.Kochplaner;
import mealplaner.model.settings.enums.CourseSettings;
import mealplaner.plugins.plugins.preference.setting.CookingPreferenceSetting;

public class DefaultSettingsTest {
  @Test
  public void constructingFromMapAndGettingAMapCopyWorks() {
    Map<DayOfWeek, Settings> defaultSettings = new HashMap<>();
    defaultSettings.put(TUESDAY, getSettings1());
    defaultSettings.put(SATURDAY, SettingsBuilder.setting()
        .time(cookingTimeWithProhibited(LONG))
        .numberOfPeople(THREE)
        .casserole(ONLY)
        .preference(new CookingPreferenceSetting(RARE_PREFERED))
        .course(CourseSettings.ONLY_MAIN)
        .create());

    Map<DayOfWeek, Settings> defaultSettingsActual = from(defaultSettings, Kochplaner.registerPlugins())
        .getDefaultSettings();

    assertThat(defaultSettingsActual).isEqualTo(defaultSettings);
  }
}
