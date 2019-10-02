// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static java.util.stream.Collectors.toMap;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.settings.SettingsBuilder.settingsWithValidation;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import mealplaner.io.xml.model.v2.SettingsXml;
import mealplaner.io.xml.util.FactsAdapter;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;
import mealplaner.model.settings.SettingsBuilder;
import mealplaner.plugins.ModelExtension;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.Setting;
import mealplaner.plugins.api.SettingXml;
import mealplaner.plugins.plugins.cookingtime.CookingTime;
import mealplaner.plugins.plugins.cookingtime.CookingTimeSetting;
import mealplaner.plugins.plugins.preference.setting.CookingPreferenceSetting;

public final class SettingsAdapter {
  private SettingsAdapter() {
  }

  public static SettingsXml convertSettingsV2ToXml(Settings setting) {
    CookingTimeSetting cookingTimes = setting.getTypedSubSetting(CookingTimeSetting.class);
    List<CookingTime> prohibitedTimes = new ArrayList<>();
    Arrays.stream(CookingTime.values())
        .filter(cookingTimes::contains)
        .forEach(prohibitedTimes::add);

    return new SettingsXml(prohibitedTimes,
        setting.getNumberOfPeople().value,
        setting.getCasserole(),
        setting.getTypedSubSetting(CookingPreferenceSetting.class).getPreferences(),
        setting.getCourseSettings());
  }

  public static mealplaner.io.xml.model.v3.SettingsXml convertSettingsV3ToXml(Settings setting) {
    var settings = new ArrayList<Object>();
    settings.addAll(
        setting.getSubSettings()
            .values()
            .stream()
            .map(Setting::convertToXml)
            .collect(Collectors.toList()));
    settings.addAll(setting.getHiddenSubSettings());
    return new mealplaner.io.xml.model.v3.SettingsXml(
        setting.getNumberOfPeople().value,
        setting.getCasserole(),
        setting.getCourseSettings(),
        settings);
  }

  public static Settings convertSettingsV2FromXml(SettingsXml setting) {
    CookingTimeSetting times = CookingTimeSetting.cookingTimeWithProhibited(
        setting.cookingTime.toArray(new CookingTime[0]));  // NOPMD
    return SettingsBuilder.setting()
        .time(times)
        .numberOfPeople(nonNegative(setting.numberOfPeople))
        .casserole(setting.casseroleSettings)
        .preference(new CookingPreferenceSetting(setting.preferenceSettings))
        .course(setting.courseSettings)
        .create();
  }

  public static Settings convertSettingsV3FromXml(
      mealplaner.io.xml.model.v3.SettingsXml setting, ModelExtension<Setting, SettingXml> knownExtensions) {
    return settingsWithValidation(knownExtensions.getAllRegisteredFacts())
        .numberOfPeople(nonNegative(setting.numberOfPeople))
        .casserole(setting.casseroleSettings)
        .course(setting.courseSettings)
        .addSettingsMap(FactsAdapter.extractFacts(setting.settings, knownExtensions))
        .addHiddenSubSettings(FactsAdapter.extractUnknownFacts(setting.settings, knownExtensions))
        .create();
  }

  public static Map<DayOfWeek, SettingsXml> convertDefaultSettingsV2ToXml(
      DefaultSettings defaultSettings) {
    return defaultSettings.getDefaultSettings()
        .entrySet()
        .stream()
        .collect(toMap(Map.Entry::getKey,
            entry -> convertSettingsV2ToXml(entry.getValue())));
  }

  public static Map<DayOfWeek, mealplaner.io.xml.model.v3.SettingsXml> convertDefaultSettingsV3ToXml(
      DefaultSettings defaultSettings) {
    return defaultSettings.getDefaultSettings()
        .entrySet()
        .stream()
        .collect(toMap(Map.Entry::getKey,
            entry -> convertSettingsV3ToXml(entry.getValue())));
  }

  public static DefaultSettings convertDefaultSettingsV2FromXml(
      Map<DayOfWeek, SettingsXml> settings, PluginStore pluginStore) {
    return DefaultSettings.from(
        settings.entrySet()
            .stream()
            .collect(toMap(
                Map.Entry::getKey,
                entry -> convertSettingsV2FromXml(entry.getValue()))), pluginStore);
  }

  public static DefaultSettings convertDefaultSettingsV3FromXml(
      Map<DayOfWeek, mealplaner.io.xml.model.v3.SettingsXml> settings,
      PluginStore pluginStore) {
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
