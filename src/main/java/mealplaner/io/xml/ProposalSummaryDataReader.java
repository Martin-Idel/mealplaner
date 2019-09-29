// SPDX-License-Identifier: MIT

package mealplaner.io.xml;

import static mealplaner.io.xml.adapters.ProposalAdapter.convertProposalV2FromXml;
import static mealplaner.io.xml.adapters.ProposalAdapter.convertProposalV3FromXml;
import static mealplaner.io.xml.adapters.SettingsAdapter.convertDefaultSettingsV2FromXml;
import static mealplaner.io.xml.adapters.SettingsAdapter.convertDefaultSettingsV3FromXml;
import static mealplaner.io.xml.util.JaxHelper.read;
import static mealplaner.io.xml.util.VersionControl.getVersion;

import mealplaner.io.xml.model.v2.ProposalSummaryDataXml;
import mealplaner.model.MealplanerData;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.plugins.ModelExtension;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.Setting;
import mealplaner.plugins.api.SettingXml;

public final class ProposalSummaryDataReader {
  private ProposalSummaryDataReader() {
  }

  public static ProposalSummaryModel loadXml(
      MealplanerData data, String filePath, PluginStore knownPlugins) {
    int versionNumber = getVersion(filePath);
    if (versionNumber == 2) {
      ProposalSummaryDataXml database = read(filePath, ProposalSummaryDataXml.class, knownPlugins);
      return convertToMealplanerDataV2(database, knownPlugins);
    } else if (versionNumber == 3) {
      mealplaner.io.xml.model.v3.ProposalSummaryDataXml database =
          read(filePath, mealplaner.io.xml.model.v3.ProposalSummaryDataXml.class, knownPlugins);
      return convertToMealplanerDataV3(database, knownPlugins);
    } else {
      return new ProposalSummaryModel(knownPlugins);
    }
  }

  private static ProposalSummaryModel convertToMealplanerDataV2(
      ProposalSummaryDataXml data, PluginStore pluginStore) {
    DefaultSettings defaultSettings = convertDefaultSettingsV2FromXml(data.defaultSettings, pluginStore);
    return new ProposalSummaryModel(convertProposalV2FromXml(data.proposal),
        defaultSettings, data.date);
  }

  private static ProposalSummaryModel convertToMealplanerDataV3(
      mealplaner.io.xml.model.v3.ProposalSummaryDataXml data,
      PluginStore pluginStore) {
    DefaultSettings defaultSettings = convertDefaultSettingsV3FromXml(data.defaultSettings, pluginStore);
    return new ProposalSummaryModel(convertProposalV3FromXml(data.proposal),
        defaultSettings, data.date);
  }
}
