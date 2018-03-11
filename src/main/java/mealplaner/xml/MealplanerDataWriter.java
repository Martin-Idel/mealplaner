package mealplaner.xml;

import static java.util.stream.Collectors.toMap;
import static mealplaner.xml.adapters.ProposalAdapter.convertProposalToXml;
import static mealplaner.xml.adapters.SettingsAdapter.convertSettingsToXml;
import static mealplaner.xml.util.JaxHelper.save;

import java.time.DayOfWeek;
import java.util.Map;

import mealplaner.MealplanerData;
import mealplaner.model.Proposal;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.xml.model.MealplanerdataXml;
import mealplaner.xml.model.SettingsXml;

public final class MealplanerDataWriter {
  private MealplanerDataWriter() {
  }

  public static void saveXml(MealplanerData data, String filePath) {
    MealplanerdataXml mealDataBase = convertDataBaseToXml(data);
    save(MealplanerdataXml.class, mealDataBase, filePath);
  }

  private static MealplanerdataXml convertDataBaseToXml(MealplanerData data) {
    DefaultSettings defaultSettings = data.getDefaultSettings();
    Proposal lastProposal = data.getLastProposal();
    return new MealplanerdataXml(convertDefaultSettingsToXml(defaultSettings),
        data.getTime(),
        convertProposalToXml(lastProposal));
  }

  private static Map<DayOfWeek, SettingsXml> convertDefaultSettingsToXml(
      DefaultSettings defaultSettings) {
    return defaultSettings.getDefaultSettings()
        .entrySet()
        .stream()
        .collect(toMap(entry -> entry.getKey(),
            entry -> convertSettingsToXml(entry.getValue())));
  }
}
