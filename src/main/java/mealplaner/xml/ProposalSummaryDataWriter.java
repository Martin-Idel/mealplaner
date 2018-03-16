package mealplaner.xml;

import static mealplaner.xml.adapters.ProposalAdapter.convertProposalToXml;
import static mealplaner.xml.adapters.SettingsAdapter.convertDefaultSettingsToXml;
import static mealplaner.xml.util.JaxHelper.save;

import mealplaner.MealplanerData;
import mealplaner.model.Proposal;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.xml.model.ProposalSummaryDataXml;

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
