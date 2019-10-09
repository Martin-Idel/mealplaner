// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static mealplaner.io.xml.adapters.SettingsAdapter.convertSettingsV3FromXml;
import static mealplaner.io.xml.adapters.SettingsAdapter.convertSettingsV3ToXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getSettings1;
import static testcommons.CommonFunctions.getSettings2;

import org.junit.Test;

import mealplaner.Kochplaner;
import mealplaner.model.settings.Settings;

public class SettingsAdapterTest {

  @Test
  public void adapterTest() {
    Settings settings1 = getSettings1();
    Settings settings2 = getSettings2();

    Settings convertedSettings1 = convertSettingsV3FromXml(
        convertSettingsV3ToXml(settings1), Kochplaner.registerPlugins().getRegisteredSettingExtensions());
    Settings convertedSettings2 = convertSettingsV3FromXml(
        convertSettingsV3ToXml(settings2), Kochplaner.registerPlugins().getRegisteredSettingExtensions());

    assertThat(convertedSettings1).isEqualTo(settings1);
    assertThat(convertedSettings2).isEqualTo(settings2);
  }
}
