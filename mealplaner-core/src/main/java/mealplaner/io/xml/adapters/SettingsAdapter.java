// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static java.util.stream.Collectors.toMap;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.settings.SettingsBuilder.settingsWithValidation;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import mealplaner.io.xml.model.v3.SettingsXml;
import mealplaner.io.xml.util.FactsAdapter;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.ModelExtension;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.Setting;
import mealplaner.plugins.api.SettingXml;

public final class SettingsAdapter {
  private SettingsAdapter() {
  }

  public static SettingsXml convertSettingsV3ToXml(Settings setting) {
    var settings = new ArrayList<>();
    settings.addAll(
        setting.getSubSettings()
            .values()
            .stream()
            .map(Setting::convertToXml)
            .collect(Collectors.toList()));
    settings.addAll(setting.getHiddenSubSettings());
    return new SettingsXml(setting.getNumberOfPeople().value, settings);
  }

  public static Settings convertSettingsV3FromXml(
      SettingsXml setting, ModelExtension<Setting, SettingXml> knownExtensions) {
    return settingsWithValidation(knownExtensions.getAllRegisteredFacts())
        .numberOfPeople(nonNegative(setting.numberOfPeople))
        .addSettingsMap(FactsAdapter.extractFacts(setting.settings, knownExtensions))
        .addHiddenSubSettings(FactsAdapter.extractUnknownFacts(setting.settings, knownExtensions))
        .create();
  }

  public static Map<DayOfWeek, SettingsXml> convertDefaultSettingsV3ToXml(DefaultSettings defaultSettings) {
    return defaultSettings.getDefaultSettingsMap()
        .entrySet()
        .stream()
        .collect(toMap(Map.Entry::getKey,
            entry -> convertSettingsV3ToXml(entry.getValue())));
  }

  public static DefaultSettings convertDefaultSettingsV3FromXml(
      Map<DayOfWeek, SettingsXml> settings, PluginStore pluginStore) {
    return DefaultSettings.from(
        settings.entrySet()
            .stream()
            .collect(toMap(
                Map.Entry::getKey,
                entry ->
                    convertSettingsV3FromXml(entry.getValue(), pluginStore.getRegisteredSettingExtensions()))),
        pluginStore);
  }
}
