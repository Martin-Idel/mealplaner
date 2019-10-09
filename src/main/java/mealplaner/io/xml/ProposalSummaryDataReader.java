// SPDX-License-Identifier: MIT

package mealplaner.io.xml;

import static mealplaner.io.xml.adapters.ProposalAdapter.convertProposalV3FromXml;
import static mealplaner.io.xml.adapters.SettingsAdapter.convertDefaultSettingsV3FromXml;
import static mealplaner.io.xml.util.JaxHelper.read;
import static mealplaner.io.xml.util.VersionControl.getVersion;

import mealplaner.io.xml.model.v3.ProposalSummaryDataXml;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.plugins.PluginStore;

public final class ProposalSummaryDataReader {
  private ProposalSummaryDataReader() {
  }

  public static ProposalSummaryModel loadXml(String filePath, PluginStore knownPlugins) {
    int versionNumber = getVersion(filePath);
    if (versionNumber == 3) {
      ProposalSummaryDataXml database = read(filePath, ProposalSummaryDataXml.class, knownPlugins);
      return convertToMealplanerDataV3(database, knownPlugins);
    } else {
      return new ProposalSummaryModel(knownPlugins);
    }
  }

  private static ProposalSummaryModel convertToMealplanerDataV3(ProposalSummaryDataXml data, PluginStore pluginStore) {
    DefaultSettings defaultSettings = convertDefaultSettingsV3FromXml(data.defaultSettings, pluginStore);
    return new ProposalSummaryModel(convertProposalV3FromXml(data.proposal),
        defaultSettings, data.date);
  }
}
