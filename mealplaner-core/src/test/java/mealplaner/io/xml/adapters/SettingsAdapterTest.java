// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static mealplaner.commons.NonnegativeInteger.TWO;
import static mealplaner.io.xml.adapters.SettingsAdapter.convertSettingsV3FromXml;
import static mealplaner.io.xml.adapters.SettingsAdapter.convertSettingsV3ToXml;
import static mealplaner.model.settings.SettingsBuilder.setting;
import static org.assertj.core.api.Assertions.assertThat;
import static xmlcommons.TestSubSetting.TestSetting.TEST1;
import static xmlcommons.TestSubSetting.TestSetting.TEST2;

import org.junit.jupiter.api.Test;

import mealplaner.model.settings.Settings;
import mealplaner.plugins.ModelExtension;
import mealplaner.plugins.api.Setting;
import mealplaner.plugins.api.SettingXml;
import xmlcommons.TestSubSetting;

class SettingsAdapterTest {

  @Test
  void adapterTestRoundtripOfSettings() {
    Settings settings1 = setting()
        .numberOfPeople(TWO)
        .addSetting(new TestSubSetting(TEST2))
        .create();

    var modelExtension = new ModelExtension<Setting, SettingXml>();
    modelExtension.registerClass(TestSubSetting.class, TestSubSetting.class, TestSubSetting::new);

    Settings convertedSettings1 = convertSettingsV3FromXml(
        convertSettingsV3ToXml(settings1), modelExtension);

    assertThat(convertedSettings1).isEqualTo(settings1);
  }

  @Test
  void adapterTestAddsDefaultsForMissingSettings() {
    Settings settings1 = setting()
        .numberOfPeople(TWO)
        .create();

    Settings expected = setting()
        .numberOfPeople(TWO)
        .addSetting(new TestSubSetting(TEST1))
        .create();

    var modelExtension = new ModelExtension<Setting, SettingXml>();
    modelExtension.registerClass(TestSubSetting.class, TestSubSetting.class, TestSubSetting::new);

    Settings convertedSettings1 = convertSettingsV3FromXml(
        convertSettingsV3ToXml(settings1), modelExtension);

    assertThat(convertedSettings1).isEqualTo(expected);
  }
}
