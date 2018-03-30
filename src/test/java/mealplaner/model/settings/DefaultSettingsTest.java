package mealplaner.model.settings;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.TUESDAY;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.enums.CasseroleSettings.ONLY;
import static mealplaner.model.enums.CookingTime.LONG;
import static mealplaner.model.enums.PreferenceSettings.RARE_PREFERED;
import static mealplaner.model.settings.CookingTimeSetting.cookingTimeWithProhibited;
import static mealplaner.model.settings.DefaultSettings.from;
import static mealplaner.model.settings.Settings.from;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getSettings1;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import mealplaner.model.enums.CourseSettings;

public class DefaultSettingsTest {
  @Test
  public void constructingFromMapAndGettingAMapCopyWorks() {
    Map<DayOfWeek, Settings> defaultSettings = new HashMap<>();
    defaultSettings.put(TUESDAY, getSettings1());
    defaultSettings.put(SATURDAY,
        from(cookingTimeWithProhibited(LONG),
            nonNegative(3),
            ONLY,
            RARE_PREFERED,
            CourseSettings.ONLY_MAIN));

    Map<DayOfWeek, Settings> defaultSettingsActual = from(defaultSettings)
        .getDefaultSettings();

    assertThat(defaultSettingsActual).isEqualTo(defaultSettings);
  }
}
