// SPDX-License-Identifier: MIT

package testcommonsmodel;

import mealplaner.plugins.api.Fact;
import mealplaner.plugins.api.FactXml;
import mealplaner.plugins.api.Setting;
import mealplaner.plugins.api.SettingXml;

public class TestSubSetting implements Setting, SettingXml {
  private final SettingEnum settingEnum;

  public TestSubSetting() {
    settingEnum = SettingEnum.TEST_SETTING_1;
  }

  public TestSubSetting(SettingEnum settingEnum) {
    this.settingEnum = settingEnum;
  }

  @Override
  public FactXml convertToXml() {
    return this;
  }

  @Override
  public Fact convertToFact() {
    return this;
  }

  public SettingEnum getSettingEnum() {
    return settingEnum;
  }

  public enum SettingEnum {
    TEST_SETTING_1, TEST_SETTING_2
  }
}
