// SPDX-License-Identifier: MIT

package mealplaner.model.settings;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static mealplaner.model.settings.SettingsBuilder.defaultSetting;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import mealplaner.plugins.PluginStore;

public final class DefaultSettings {
  private static final List<DayOfWeek> DAYS_OF_WEEK = Arrays.asList(DayOfWeek.values());
  private final Map<DayOfWeek, Settings> defaultSettingsMap = new EnumMap<>(DayOfWeek.class);

  private DefaultSettings(Map<DayOfWeek, Settings> defaultSettingsMap, PluginStore pluginStore) {
    this.defaultSettingsMap.putAll(defaultSettingsMap);
    DAYS_OF_WEEK.forEach(dayOfWeek -> this.defaultSettingsMap
        .computeIfAbsent(dayOfWeek, day -> defaultSetting(pluginStore)));
  }

  public static DefaultSettings from(
      Map<DayOfWeek, Settings> defaultSettings, PluginStore pluginStore) {
    return new DefaultSettings(defaultSettings, pluginStore);
  }

  public static DefaultSettings createDefaultSettings(PluginStore pluginStore) {
    return new DefaultSettings(new EnumMap<>(DayOfWeek.class), pluginStore);
  }

  private static Map<DayOfWeek, Settings> copyMap(Map<DayOfWeek, Settings> defaultSettings) {
    return defaultSettings.entrySet().stream()
        .collect(toMap(Map.Entry::getKey,
            entry -> SettingsBuilder.from(entry.getValue()).create(),
            (l, r) -> l,
            () -> new EnumMap<>(DayOfWeek.class)));
  }

  public Map<DayOfWeek, Settings> getDefaultSettingsMap() {
    return copyMap(defaultSettingsMap);
  }

  @Override
  public String toString() {
    return "DefaultSettings{" + DAYS_OF_WEEK.stream()
        .map(day -> "day=" + day.toString() + ", " + "settings=" + defaultSettingsMap.get(day).toString())
        .collect(joining(",")) + "}";
  }

  @Override
  public int hashCode() {
    return 31 + defaultSettingsMap.hashCode();
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
    return other.defaultSettingsMap.equals(defaultSettingsMap);
  }
}
