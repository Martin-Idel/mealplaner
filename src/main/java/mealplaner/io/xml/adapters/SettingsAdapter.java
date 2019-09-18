// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static java.util.stream.Collectors.toMap;
import static mealplaner.commons.NonnegativeInteger.nonNegative;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import mealplaner.io.xml.model.v2.SettingsXml;
import mealplaner.io.xml.util.FactsAdapter;
import mealplaner.model.meal.enums.CookingTime;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;
import mealplaner.model.settings.subsettings.CookingTimeSetting;
import mealplaner.plugins.ModelExtension;
import mealplaner.plugins.api.Setting;
import mealplaner.plugins.api.SettingXml;

public final class SettingsAdapter {
  private SettingsAdapter() {
  }

  public static SettingsXml convertSettingsV2ToXml(Settings setting) {
    CookingTimeSetting cookingTimes = setting.getCookingTime();
    List<CookingTime> prohibitedTimes = new ArrayList<>();
    Arrays.stream(CookingTime.values())
        .filter(cookingTimes::contains)
        .forEach(prohibitedTimes::add);

    return new SettingsXml(prohibitedTimes,
        setting.getNumberOfPeople().value,
        setting.getCasserole(),
        setting.getPreference(),
        setting.getCourseSettings());
  }

  public static mealplaner.io.xml.model.v3.SettingsXml convertSettingsV3ToXml(Settings setting) {
    CookingTimeSetting cookingTimes = setting.getCookingTime();
    List<CookingTime> prohibitedTimes = new ArrayList<>();
    Arrays.stream(CookingTime.values())
        .filter(cookingTimes::contains)
        .forEach(prohibitedTimes::add);

    var settings = new ArrayList<Object>();
    settings.addAll(
        setting.getSubSettings()
            .values()
            .stream()
            .map(Setting::convertToXml)
            .collect(Collectors.toUnmodifiableList()));
    settings.addAll(setting.getHiddenSubSettings());
    return new mealplaner.io.xml.model.v3.SettingsXml(prohibitedTimes,
        setting.getNumberOfPeople().value,
        setting.getCasserole(),
        setting.getPreference(),
        setting.getCourseSettings(),
        settings);
  }

  public static Settings convertSettingsV2FromXml(SettingsXml setting) {
    CookingTimeSetting times = CookingTimeSetting.cookingTimeWithProhibited(
        setting.cookingTime.toArray(new CookingTime[0]));  // NOPMD
    return Settings.from(times,
        nonNegative(setting.numberOfPeople),
        setting.casseroleSettings,
        setting.preferenceSettings,
        setting.courseSettings);
  }

  public static Settings convertSettingsV3FromXml(
      mealplaner.io.xml.model.v3.SettingsXml setting, ModelExtension<Setting, SettingXml> knownExtensions) {
    CookingTimeSetting times = CookingTimeSetting.cookingTimeWithProhibited(
        setting.cookingTime.toArray(new CookingTime[0]));  // NOPMD
    return Settings.from(times,
        nonNegative(setting.numberOfPeople),
        setting.casseroleSettings,
        setting.preferenceSettings,
        setting.courseSettings,
        FactsAdapter.extractFacts(setting.settings, knownExtensions),
        FactsAdapter.extractUnknownFacts(setting.settings, knownExtensions));
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
      Map<DayOfWeek, SettingsXml> settings) {
    return DefaultSettings.from(
        settings.entrySet()
            .stream()
            .collect(toMap(
                    Map.Entry::getKey,
                entry -> convertSettingsV2FromXml(entry.getValue()))));
  }

  public static DefaultSettings convertDefaultSettingsV3FromXml(
      Map<DayOfWeek, mealplaner.io.xml.model.v3.SettingsXml> settings,
      ModelExtension<Setting, SettingXml> knownExtensions) {
    return DefaultSettings.from(
        settings.entrySet()
            .stream()
            .collect(toMap(
                Map.Entry::getKey,
                entry -> convertSettingsV3FromXml(entry.getValue(), knownExtensions))));
  }
}
