package mealplaner.xml;

import static java.util.stream.Collectors.toMap;
import static mealplaner.xml.adapters.ProposalAdapter.convertProposalFromXml;
import static mealplaner.xml.adapters.SettingsAdapter.convertSettingsFromXml;
import static mealplaner.xml.util.JaxHelper.read;
import static mealplaner.xml.util.VersionControl.getVersion;

import java.io.IOException;
import java.util.ArrayList;

import mealplaner.MealplanerData;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.io.MealplanerFileLoader;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.xml.model.MealplanerdataXml;

public final class MealplanerDataReader {
  private MealplanerDataReader() {
  }

  public static MealplanerData loadXml(String filePath) {
    int versionNumber = getVersion(filePath);
    if (versionNumber == 1) {
      MealplanerdataXml database = read(filePath, MealplanerdataXml.class);
      return convertToMealplanerData(database);
    } else {
      // TODO: Delete once saves have been ported
      try {
        return MealplanerFileLoader.load(filePath);
      } catch (MealException | IOException e) {
        return new MealplanerData();
      }
    }
  }

  private static MealplanerData convertToMealplanerData(MealplanerdataXml data) {
    DefaultSettings defaultSettings = DefaultSettings.from(
        data.defaultSettings.entrySet()
            .stream()
            .collect(toMap(
                entry -> entry.getKey(),
                entry -> convertSettingsFromXml(entry.getValue()))));

    return new MealplanerData(new ArrayList<>(),
        data.date,
        defaultSettings,
        convertProposalFromXml(data.proposal));
  }
}
