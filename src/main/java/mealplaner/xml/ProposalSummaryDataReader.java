package mealplaner.xml;

import static mealplaner.xml.adapters.ProposalAdapter.convertProposalFromXml;
import static mealplaner.xml.adapters.SettingsAdapter.convertDefaultSettingsFromXml;
import static mealplaner.xml.util.JaxHelper.read;
import static mealplaner.xml.util.VersionControl.getVersion;

import java.util.ArrayList;

import mealplaner.MealplanerData;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.xml.model.ProposalSummaryDataXml;

public final class ProposalSummaryDataReader {
  private ProposalSummaryDataReader() {
  }

  public static MealplanerData loadXml(String filePath) {
    int versionNumber = getVersion(filePath);
    if (versionNumber == 1) {
      ProposalSummaryDataXml database = read(filePath, ProposalSummaryDataXml.class);
      return convertToMealplanerData(database);
    } else {
      return new MealplanerData();
    }
  }

  static MealplanerData convertToMealplanerData(ProposalSummaryDataXml data) {
    DefaultSettings defaultSettings = convertDefaultSettingsFromXml(data.defaultSettings);

    return new MealplanerData(new ArrayList<>(),
        new ArrayList<>(),
        data.date,
        defaultSettings,
        convertProposalFromXml(data.proposal));
  }
}
