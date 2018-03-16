package mealplaner.xml;

import static java.util.stream.Collectors.toList;
import static mealplaner.xml.adapters.IngredientAdapter.convertIngredientToXml;
import static mealplaner.xml.adapters.MealAdapter.convertMealToXml;
import static mealplaner.xml.adapters.ProposalAdapter.convertProposalToXml;
import static mealplaner.xml.adapters.SettingsAdapter.convertDefaultSettingsToXml;
import static mealplaner.xml.util.JaxHelper.save;

import java.util.List;

import mealplaner.MealplanerData;
import mealplaner.xml.model.IngredientXml;
import mealplaner.xml.model.MealXml;
import mealplaner.xml.model.MealplanerDataXml;

public final class MealplanerDataWriter {
  private MealplanerDataWriter() {
  }

  public static void saveXml(MealplanerData data, String filePath) {
    MealplanerDataXml mealDataBase = convertDataBaseToXml(data);
    save(MealplanerDataXml.class, mealDataBase, filePath);
  }

  private static MealplanerDataXml convertDataBaseToXml(MealplanerData data) {
    List<MealXml> mealXmls = data.getMeals().stream()
        .map(meal -> convertMealToXml(meal))
        .collect(toList());
    List<IngredientXml> ingredientsXml = data.getIngredients().stream()
        .map(ingredient -> convertIngredientToXml(ingredient))
        .collect(toList());
    return new MealplanerDataXml(convertDefaultSettingsToXml(data.getDefaultSettings()),
        data.getTime(),
        convertProposalToXml(data.getLastProposal()), mealXmls, ingredientsXml);
  }
}
