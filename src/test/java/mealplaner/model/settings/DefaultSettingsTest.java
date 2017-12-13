package mealplaner.model.settings;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.TUESDAY;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.enums.CasseroleSettings.ONLY;
import static mealplaner.model.enums.CookingTime.LONG;
import static mealplaner.model.enums.PreferenceSettings.RARE_PREFERED;
import static mealplaner.model.settings.CookingTimeSetting.cookingTimeWithProhibited;
import static mealplaner.model.settings.DefaultSettings.from;
import static mealplaner.model.settings.DefaultSettings.parseDefaultSettings;
import static mealplaner.model.settings.DefaultSettings.writeDefaultSettings;
import static mealplaner.model.settings.Settings.from;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.createDocument;
import static testcommons.CommonFunctions.getSettings1;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;

public class DefaultSettingsTest {

  @Test
  public void saveAndReadFromXmlNode() throws ParserConfigurationException {
    Map<DayOfWeek, Settings> defaultSetting = new HashMap<>();
    defaultSetting.put(TUESDAY, getSettings1());
    defaultSetting.put(SATURDAY,
        from(cookingTimeWithProhibited(LONG), nonNegative(3), ONLY, RARE_PREFERED));
    DefaultSettings settings = from(defaultSetting);
    Document saveFileContent = createDocument();

    DefaultSettings expected = parseDefaultSettings(
        writeDefaultSettings(saveFileContent, settings, "defaultSettings"));

    assertThat(expected).isEqualTo(settings);
  }

  @Test
  public void constructingFromMapAndGettingAMapCopyWorks() {
    Map<DayOfWeek, Settings> defaultSettings = new HashMap<>();
    defaultSettings.put(TUESDAY, getSettings1());
    defaultSettings.put(SATURDAY,
        from(cookingTimeWithProhibited(LONG), nonNegative(3), ONLY, RARE_PREFERED));

    Map<DayOfWeek, Settings> defaultSettingsActual = from(defaultSettings)
        .getDefaultSettings();

    assertThat(defaultSettingsActual).isEqualTo(defaultSettings);
  }
}
