package mealplaner.io.xml;

import static mealplaner.io.xml.adapters.ProposalAdapter.convertProposalToXml;
import static mealplaner.io.xml.adapters.SettingsAdapter.convertDefaultSettingsToXml;
import static mealplaner.io.xml.util.JaxHelper.save;

import mealplaner.io.xml.model.v2.ProposalSummaryDataXml;
import mealplaner.model.MealplanerData;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.settings.DefaultSettings;

public final class ProposalSummaryDataWriter {
  private ProposalSummaryDataWriter() {
  }

  public static void saveXml(MealplanerData data, String filePath) {
    ProposalSummaryDataXml mealDataBase = convertDataBaseToXml(data);
    save(ProposalSummaryDataXml.class, mealDataBase, filePath);
  }

  private static ProposalSummaryDataXml convertDataBaseToXml(MealplanerData data) {
    DefaultSettings defaultSettings = data.getDefaultSettings();
    Proposal lastProposal = data.getLastProposal();
    return new ProposalSummaryDataXml(convertDefaultSettingsToXml(defaultSettings),
        data.getTime(),
        convertProposalToXml(lastProposal));
  }
}
