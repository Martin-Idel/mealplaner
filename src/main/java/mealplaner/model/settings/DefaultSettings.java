// SPDX-License-Identifier: MIT

package mealplaner.model.settings;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static mealplaner.model.settings.SettingsBuilder.defaultSetting;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mealplaner.plugins.PluginStore;

public final class DefaultSettings {
  private static final List<DayOfWeek> DAYS_OF_WEEK = Arrays.asList(DayOfWeek.values());
  private final Map<DayOfWeek, Settings> defaultSettings;

  private DefaultSettings(Map<DayOfWeek, Settings> defaultSettings, PluginStore pluginStore) {
    this.defaultSettings = defaultSettings;
    DAYS_OF_WEEK.forEach(dayOfWeek -> this.defaultSettings
        .computeIfAbsent(dayOfWeek, day -> defaultSetting(pluginStore)));
  }

  public static DefaultSettings from(
      Map<DayOfWeek, Settings> defaultSettings, PluginStore pluginStore) {
    return new DefaultSettings(defaultSettings, pluginStore);
  }

  public static DefaultSettings createDefaultSettings(PluginStore pluginStore) {
    return new DefaultSettings(new HashMap<>(), pluginStore);
  }

  private static Map<DayOfWeek, Settings> copyHashMap(Map<DayOfWeek, Settings> defaultSettings) {
    return defaultSettings.entrySet().stream()
        .collect(toMap(Map.Entry::getKey, entry -> Settings.copy(entry.getValue())));
  }

  public Map<DayOfWeek, Settings> getDefaultSettings() {
    return copyHashMap(defaultSettings);
  }

  @Override
  public String toString() {
    return "[" + DAYS_OF_WEEK.stream()
        .map(day -> day.toString() + " " + defaultSettings.get(day).toString())
        .collect(joining(",")) + "]";
  }

  @Override
  public int hashCode() {
    return 31 + ((defaultSettings == null) ? 0 : defaultSettings.hashCode());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    DefaultSettings other = (DefaultSettings) obj;
    return other.defaultSettings.equals(defaultSettings);
  }
}
