package mealplaner.xml.adapters;

import static mealplaner.commons.NonnegativeInteger.nonNegative;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mealplaner.model.enums.CookingTime;
import mealplaner.model.settings.CookingTimeSetting;
import mealplaner.model.settings.Settings;
import mealplaner.xml.model.SettingsXml;

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
}
