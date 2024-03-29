// SPDX-License-Identifier: MIT

package mealplaner.io.xml;

import static mealplaner.commons.NonnegativeInteger.TWO;
import static mealplaner.io.xml.ProposalSummaryDataReader.loadXml;
import static mealplaner.io.xml.ProposalSummaryDataWriter.saveXml;
import static mealplaner.model.settings.SettingsBuilder.setting;
import static org.assertj.core.api.Assertions.assertThat;
import static xmlcommons.TestSubSetting.TestSetting.TEST1;
import static xmlcommons.TestSubSetting.TestSetting.TEST2;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import mealplaner.model.MealplanerData;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.PluginStore;
import testcommons.XmlInteraction;
import xmlcommons.HiddenSubSetting;
import xmlcommons.TestSubSetting;

class SubSettingsXmlInteractionTest extends XmlInteraction {
  @Test
  void roundTripWorksCorrectlyWithSettingFacts() {
    Settings settings = setting()
        .numberOfPeople(TWO)
        .addSetting(new TestSubSetting(TEST2))
        .addSetting(new HiddenSubSetting(HiddenSubSetting.HiddenEnum.TEST2))
        .create();
    Map<DayOfWeek, Settings> defaultSettings = new HashMap<>();
    defaultSettings.put(DayOfWeek.MONDAY, settings);

    var pluginStore = new PluginStore();
    pluginStore.registerSettingExtension(
        TestSubSetting.class, TestSubSetting.class, TestSubSetting::new);
    pluginStore.registerSettingExtension(
        HiddenSubSetting.class, HiddenSubSetting.class, HiddenSubSetting::new);

    LocalDate time = LocalDate.of(2017, 5, 3);
    Proposal proposal = Proposal.from(false, new ArrayList<>(), time);
    MealplanerData mealPlan = MealplanerData.getInstance(pluginStore);
    mealPlan.setTime(time);
    mealPlan.setDefaultSettings(DefaultSettings.from(defaultSettings, pluginStore));
    mealPlan.setLastProposal(proposal);

    saveXml(mealPlan, DESTINATION_FILE_PATH, pluginStore);

    var smallerPluginStore = new PluginStore();
    smallerPluginStore.registerSettingExtension(
        TestSubSetting.class, TestSubSetting.class, TestSubSetting::new);

    var savedProposalSummary = loadXml(DESTINATION_FILE_PATH, smallerPluginStore);
    MealplanerData newMealPlan = MealplanerData.getInstance(smallerPluginStore);
    newMealPlan.setProposalSummary(
        savedProposalSummary.defaultSettings,
        savedProposalSummary.lastProposal,
        savedProposalSummary.time);

    Settings loadedSetting = savedProposalSummary.defaultSettings.getDefaultSettingsMap().get(DayOfWeek.MONDAY);
    assertThat(loadedSetting.getHiddenSubSettings()).hasSize(1);
    assertThat(loadedSetting.getTypedSubSetting(TestSubSetting.class).getSetting()).isEqualByComparingTo(TEST2);

    saveXml(newMealPlan, DESTINATION_FILE_PATH, smallerPluginStore);
    var reloadedProposalSummary = loadXml(DESTINATION_FILE_PATH, pluginStore);

    Settings reloadedSetting = reloadedProposalSummary.defaultSettings.getDefaultSettingsMap().get(DayOfWeek.MONDAY);
    assertThat(reloadedSetting).isEqualTo(settings);
  }

  @Test
  void roundTripSetsCorrectDefaultsIfNecessary() {
    Settings settings = setting()
        .numberOfPeople(TWO)
        .create();

    Map<DayOfWeek, Settings> defaultSettings = new HashMap<>();
    defaultSettings.put(DayOfWeek.MONDAY, settings);

    LocalDate time = LocalDate.of(2017, 5, 3);
    Proposal proposal = Proposal.from(false, new ArrayList<>(), time);
    MealplanerData mealPlan = MealplanerData.getInstance(new PluginStore());
    mealPlan.setTime(time);
    mealPlan.setDefaultSettings(DefaultSettings.from(defaultSettings, new PluginStore()));
    mealPlan.setLastProposal(proposal);

    saveXml(mealPlan, DESTINATION_FILE_PATH, new PluginStore());

    var pluginStore = new PluginStore();
    pluginStore.registerSettingExtension(
        TestSubSetting.class, TestSubSetting.class, TestSubSetting::new);

    var savedProposalSummary = loadXml(DESTINATION_FILE_PATH, pluginStore);

    Settings reloadedSetting = savedProposalSummary.defaultSettings.getDefaultSettingsMap().get(DayOfWeek.MONDAY);
    Settings expected = setting()
        .numberOfPeople(TWO)
        .addSetting(new TestSubSetting(TEST1))
        .create();

    assertThat(reloadedSetting).isEqualTo(expected);
  }
}
