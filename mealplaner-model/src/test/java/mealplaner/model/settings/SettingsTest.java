// SPDX-License-Identifier: MIT

package mealplaner.model.settings;

import static mealplaner.commons.NonnegativeInteger.TWO;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommonsmodel.CommonBaseFunctions.getSettings1;

import java.util.HashSet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.Fact;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import testcommonsmodel.TestSubSetting;

class SettingsTest {
  @Test
  void validationThrowsExceptionIfNotAllFactsArePresent() {
    var settingMap = new HashSet<Class<? extends Fact>>();
    settingMap.add(TestSubSetting.class);

    Assertions.assertThrows(MealException.class, () -> SettingsBuilder
        .settingsWithValidation(settingMap)
        .numberOfPeople(TWO)
        .create());
  }

  @Test
  void getTypedSettingsOrDefaultDefaultsIfSettingIsMissing() {
    Settings settings = SettingsBuilder.setting()
        .numberOfPeople(TWO)
        .create();

    assertThat(settings
        .getTypedSubSettingOrDefault(
            TestSubSetting.class,
            new TestSubSetting(TestSubSetting.SettingEnum.TEST_SETTING_2))
        .getSettingEnum())
        .isEqualTo(TestSubSetting.SettingEnum.TEST_SETTING_2);
  }

  @Test
  void defaultSettingsProvidesValidSettings() {
    var pluginStore = new PluginStore();
    pluginStore.registerSettingExtension(TestSubSetting.class, TestSubSetting.class, TestSubSetting::new);

    Settings settings = SettingsBuilder.defaultSetting(pluginStore);

    assertThat(settings.getNumberOfPeople()).isEqualTo(TWO);
    assertThat(settings.getSubSettings()).hasSize(1);
    assertThat(settings.getTypedSubSetting(TestSubSetting.class).getSettingEnum())
        .isEqualTo(TestSubSetting.SettingEnum.TEST_SETTING_1);
    assertThat(settings
        .getTypedSubSettingOrDefault(
            TestSubSetting.class,
            new TestSubSetting(TestSubSetting.SettingEnum.TEST_SETTING_2))
        .getSettingEnum())
        .isEqualTo(TestSubSetting.SettingEnum.TEST_SETTING_1);
    assertThat(((TestSubSetting) settings.getSubSetting(TestSubSetting.class)).getSettingEnum())
        .isEqualTo(TestSubSetting.SettingEnum.TEST_SETTING_1);
  }

  @Test
  void copyingAndChangingSubSettingsDoesNotModifyOriginalSetting() {
    var pluginStore = new PluginStore();
    pluginStore.registerSettingExtension(TestSubSetting.class, TestSubSetting.class, TestSubSetting::new);

    Settings originalSettings = SettingsBuilder.defaultSetting(pluginStore);
    Settings changedSettings = SettingsBuilder.from(originalSettings)
        .changeSetting(new TestSubSetting(TestSubSetting.SettingEnum.TEST_SETTING_2))
        .create();

    assertThat(changedSettings.getNumberOfPeople()).isEqualTo(TWO);
    assertThat(changedSettings.getSubSettings()).hasSize(1);
    assertThat(changedSettings.getTypedSubSetting(TestSubSetting.class).getSettingEnum())
        .isEqualTo(TestSubSetting.SettingEnum.TEST_SETTING_2);
    assertThat(originalSettings.getTypedSubSetting(TestSubSetting.class).getSettingEnum())
        .isEqualTo(TestSubSetting.SettingEnum.TEST_SETTING_1);
  }

  @Test
  void equalsContract() {
    EqualsVerifier.forClass(Settings.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }

  @Test
  void testToString() {
    assertThat(getSettings1().toString())
        .isEqualTo("Settings{numberOfPeople=3, subSettings={}, hiddenSubSettings=[]}");
    assertThat(Settings.class.getDeclaredFields().length).isEqualTo(3);
  }
}
