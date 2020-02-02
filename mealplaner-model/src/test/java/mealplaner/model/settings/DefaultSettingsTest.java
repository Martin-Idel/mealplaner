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
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class DefaultSettingsTest {
  @Test
  public void constructingFromMapAndGettingAMapCopyWorks() {
    Map<DayOfWeek, Settings> defaultSettings = new HashMap<>();
    defaultSettings.put(TUESDAY, setting().numberOfPeople(TWO).create());
    defaultSettings.put(SATURDAY, setting().numberOfPeople(THREE).create());

    Map<DayOfWeek, Settings> defaultSettingsActual = from(defaultSettings, new PluginStore())
        .getDefaultSettings();

    assertThat(defaultSettingsActual).containsAllEntriesOf(defaultSettings);
  }

  @Test
  public void testToString() {
    Map<DayOfWeek, Settings> defaultSettings = new HashMap<>();
    defaultSettings.put(TUESDAY, setting().numberOfPeople(TWO).create());
    defaultSettings.put(SATURDAY, setting().numberOfPeople(THREE).create());

    DefaultSettings defaultSettingsActual = from(defaultSettings, new PluginStore());

    assertThat(defaultSettingsActual.toString())
        .contains("day=SATURDAY, settings=Settings{numberOfPeople=3, subSettings={}, hiddenSubSettings=[]}")
        .contains("day=FRIDAY, settings=Settings{numberOfPeople=2, subSettings={}, hiddenSubSettings=[]}");
    assertThat(DefaultSettings.class.getDeclaredFields().length).isEqualTo(1 + 1); // one static field
  }

  @Test
  public void equalsContract() {
    EqualsVerifier.forClass(DefaultSettings.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }
}
