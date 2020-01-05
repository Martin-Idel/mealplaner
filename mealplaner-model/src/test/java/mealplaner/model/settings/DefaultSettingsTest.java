// SPDX-License-Identifier: MIT

package mealplaner.model.settings;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.TUESDAY;
import static mealplaner.commons.NonnegativeInteger.THREE;
import static mealplaner.commons.NonnegativeInteger.TWO;
import static mealplaner.model.settings.DefaultSettings.from;
import static mealplaner.model.settings.SettingsBuilder.setting;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import mealplaner.plugins.PluginStore;

public class DefaultSettingsTest {
  @Test
  public void constructingFromMapAndGettingAMapCopyWorks() {
    Map<DayOfWeek, Settings> defaultSettings = new HashMap<>();
    defaultSettings.put(TUESDAY, setting().numberOfPeople(TWO).create());
    defaultSettings.put(SATURDAY, setting().numberOfPeople(THREE).create());

    Map<DayOfWeek, Settings> defaultSettingsActual = from(defaultSettings, new PluginStore())
        .getDefaultSettings();

    assertThat(defaultSettingsActual).isEqualTo(defaultSettings);
  }
}
