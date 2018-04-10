package mealplaner.io.xml;

import static mealplaner.io.xml.adapters.ProposalAdapter.convertProposalFromXml;
import static mealplaner.io.xml.adapters.SettingsAdapter.convertDefaultSettingsFromXml;
import static mealplaner.io.xml.adapters.SettingsAdapter.convertDefaultSettingsFromXmlV1;
import static mealplaner.io.xml.util.JaxHelper.read;
import static mealplaner.io.xml.util.VersionControl.getVersion;

import mealplaner.io.xml.model.v2.ProposalSummaryDataXml;
import mealplaner.model.MealplanerData;
import mealplaner.model.settings.DefaultSettings;

public final class ProposalSummaryDataReader {
  private ProposalSummaryDataReader() {
  }

  public static ProposalSummaryModel loadXml(MealplanerData data, String filePath) {
    int versionNumber = getVersion(filePath);
    if (versionNumber == 1) {
      mealplaner.io.xml.model.v1.ProposalSummaryDataXml database = read(filePath,
          mealplaner.io.xml.model.v1.ProposalSummaryDataXml.class);
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
      mealplaner.io.xml.model.v1.ProposalSummaryDataXml data) {
    DefaultSettings defaultSettings = convertDefaultSettingsFromXmlV1(data.defaultSettings);
    return new ProposalSummaryModel(convertProposalFromXml(database, data.proposal),
        defaultSettings, data.date);
  }
}