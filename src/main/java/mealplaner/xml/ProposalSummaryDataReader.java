package mealplaner.xml;

import static mealplaner.xml.adapters.ProposalAdapter.convertProposalFromXml;
import static mealplaner.xml.adapters.SettingsAdapter.convertDefaultSettingsFromXml;
import static mealplaner.xml.adapters.SettingsAdapter.convertDefaultSettingsFromXmlV1;
import static mealplaner.xml.util.JaxHelper.read;
import static mealplaner.xml.util.VersionControl.getVersion;

import mealplaner.MealplanerData;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.xml.model.v2.ProposalSummaryDataXml;

public final class ProposalSummaryDataReader {
  private ProposalSummaryDataReader() {
  }

  public static ProposalSummaryModel loadXml(MealplanerData data, String filePath) {
    int versionNumber = getVersion(filePath);
    if (versionNumber == 1) {
      mealplaner.xml.model.v1.ProposalSummaryDataXml database = read(filePath,
          mealplaner.xml.model.v1.ProposalSummaryDataXml.class);
      return convertToMealplanerData(data, database);
    } else if (versionNumber == 2) {
      ProposalSummaryDataXml database = read(filePath, ProposalSummaryDataXml.class);
      return convertToMealplanerData(data, database);
    } else {
      return new ProposalSummaryModel();
    }
  }

  static ProposalSummaryModel convertToMealplanerData(MealplanerData database,
      ProposalSummaryDataXml data) {
    DefaultSettings defaultSettings = convertDefaultSettingsFromXml(data.defaultSettings);
    return new ProposalSummaryModel(convertProposalFromXml(database, data.proposal),
        defaultSettings, data.date);
  }

  static ProposalSummaryModel convertToMealplanerData(MealplanerData database,
      mealplaner.xml.model.v1.ProposalSummaryDataXml data) {
    DefaultSettings defaultSettings = convertDefaultSettingsFromXmlV1(data.defaultSettings);
    return new ProposalSummaryModel(convertProposalFromXml(database, data.proposal),
        defaultSettings, data.date);
  }
}
