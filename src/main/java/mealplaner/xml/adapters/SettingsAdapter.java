package mealplaner.xml.adapters;

import static java.util.stream.Collectors.toMap;
import static mealplaner.commons.NonnegativeInteger.nonNegative;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import mealplaner.model.enums.CookingTime;
import mealplaner.model.settings.CookingTimeSetting;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;
import mealplaner.xml.model.v2.SettingsXml;

public final class SettingsAdapter {
  private SettingsAdapter() {
  }

  public static SettingsXml convertSettingsToXml(Settings setting) {
    CookingTimeSetting cookingTimes = setting.getCookingTime();
    List<CookingTime> prohibitedTimes = new ArrayList<>();
    Arrays.asList(CookingTime.values())
        .stream()
        .filter(cookingTime -> cookingTimes.contains(cookingTime))
        .forEach(prohibitedTime -> prohibitedTimes.add(prohibitedTime));

    return new SettingsXml(prohibitedTimes,
        setting.getNumberOfPeople().value,
        setting.getCasserole(),
        setting.getPreference());
  }

  public static Settings convertSettingsFromXml(SettingsXml setting) {
    CookingTimeSetting times = CookingTimeSetting.cookingTimeWithProhibited(
        setting.cookingTime.toArray(new CookingTime[setting.cookingTime.size()]));
    return Settings.from(times,
        nonNegative(setting.numberOfPeople),
        setting.casseroleSettings,
        setting.preference);
  }

  public static Settings convertSettingsFromXml(mealplaner.xml.model.v1.SettingsXml setting) {
    CookingTimeSetting times = CookingTimeSetting.cookingTimeWithProhibited(
        setting.cookingTime.toArray(new CookingTime[setting.cookingTime.size()]));
    return Settings.from(times,
        nonNegative(setting.numberOfPeople),
        setting.casseroleSettings,
        setting.preference);
  }

  public static Map<DayOfWeek, SettingsXml> convertDefaultSettingsToXml(
      DefaultSettings defaultSettings) {
    return defaultSettings.getDefaultSettings()
        .entrySet()
        .stream()
        .collect(toMap(entry -> entry.getKey(),
            entry -> convertSettingsToXml(entry.getValue())));
  }

  public static DefaultSettings convertDefaultSettingsFromXml(
      Map<DayOfWeek, SettingsXml> settings) {
    return DefaultSettings.from(
        settings.entrySet()
            .stream()
            .collect(toMap(
                entry -> entry.getKey(),
                entry -> convertSettingsFromXml(entry.getValue()))));
  }

  public static DefaultSettings convertDefaultSettingsFromXmlV1(
      Map<DayOfWeek, mealplaner.xml.model.v1.SettingsXml> settings) {
    return DefaultSettings.from(
        settings.entrySet()
            .stream()
            .collect(toMap(
                entry -> entry.getKey(),
                entry -> convertSettingsFromXml(entry.getValue()))));
  }
}
