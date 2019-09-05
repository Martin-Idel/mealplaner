// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static mealplaner.io.xml.adapters.SettingsAdapter.convertSettingsV2FromXml;
import static mealplaner.io.xml.adapters.SettingsAdapter.convertSettingsV2ToXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getSettings1;
import static testcommons.CommonFunctions.getSettings2;

import org.junit.Test;

import mealplaner.model.settings.Settings;

public class SettingsAdapterTest {

  @Test
  public void adapterTest() {
    Settings settings1 = getSettings1();
    Settings settings2 = getSettings2();

    Settings convertedSettings1 = convertSettingsV2FromXml(convertSettingsV2ToXml(settings1));
    Settings convertedSettings2 = convertSettingsV2FromXml(convertSettingsV2ToXml(settings2));

    assertThat(convertedSettings1).isEqualTo(settings1);
    assertThat(convertedSettings2).isEqualTo(settings2);
  }
}
