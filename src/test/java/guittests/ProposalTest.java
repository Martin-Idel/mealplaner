package guittests;

import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static testcommons.CommonFunctions.getSettings1;
import static testcommons.CommonFunctions.getSettings2;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import guittests.helpers.AssertJMealplanerTestCase;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;

public class ProposalTest extends AssertJMealplanerTestCase {

  @Test
  public void saveDefaultSettings() {
    Settings defaultSettingTuesday = getSettings1();
    DayOfWeek dayTuesday = TUESDAY;
    Settings defaultSettingWednesday = getSettings2();
    DayOfWeek dayWednesday = WEDNESDAY;
    Map<DayOfWeek, Settings> defaultSettingsMap = new HashMap<>();
    defaultSettingsMap.put(dayTuesday, defaultSettingTuesday);
    defaultSettingsMap.put(dayWednesday, defaultSettingWednesday);
    DefaultSettings defaultSettings = DefaultSettings.from(defaultSettingsMap);

    windowHelpers.enterDefaultSettings(defaultSettings);
    windowHelpers.compareDefaultSettings(defaultSettings);
  }

  @Test
  public void makeProposalShowsCorrectOutput() {
    // implement me
  }
}
