// SPDX-License-Identifier: MIT

package mealplaner.io.xml;

import static mealplaner.io.xml.adapters.ProposalAdapter.convertProposalV3ToXml;
import static mealplaner.io.xml.adapters.SettingsAdapter.convertDefaultSettingsV3ToXml;
import static mealplaner.io.xml.util.JaxHelper.save;

import mealplaner.io.xml.model.v3.ProposalSummaryDataXml;
import mealplaner.model.MealplanerData;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.plugins.PluginStore;

public final class ProposalSummaryDataWriter {
  private ProposalSummaryDataWriter() {
  }

  public static void saveXml(MealplanerData data, String filePath, PluginStore knownPlugins) {
    ProposalSummaryDataXml mealDataBase = convertDataBaseToXml(data);
    save(filePath, ProposalSummaryDataXml.class, mealDataBase, knownPlugins);
  }

  private static ProposalSummaryDataXml convertDataBaseToXml(MealplanerData data) {
    DefaultSettings defaultSettings = data.getDefaultSettings();
    Proposal lastProposal = data.getLastProposal();
    return new ProposalSummaryDataXml(convertDefaultSettingsV3ToXml(defaultSettings),
        data.getTime(),
        convertProposalV3ToXml(lastProposal));
  }
}
