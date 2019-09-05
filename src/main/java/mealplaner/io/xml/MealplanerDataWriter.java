// SPDX-License-Identifier: MIT

package mealplaner.io.xml;

import static java.util.stream.Collectors.toList;
import static mealplaner.io.xml.adapters.ProposalAdapter.convertProposalV3ToXml;
import static mealplaner.io.xml.adapters.SettingsAdapter.convertDefaultSettingsV3ToXml;
import static mealplaner.io.xml.util.JaxHelper.save;

import java.util.List;

import mealplaner.io.xml.adapters.IngredientAdapter;
import mealplaner.io.xml.adapters.MealAdapter;
import mealplaner.io.xml.model.v3.IngredientXml;
import mealplaner.io.xml.model.v3.MealXml;
import mealplaner.io.xml.model.v3.MealplanerDataXml;
import mealplaner.model.MealplanerData;

public final class MealplanerDataWriter {
  private MealplanerDataWriter() {
  }

  public static void saveXml(MealplanerData data, String filePath) {
    MealplanerDataXml mealDataBase = convertDataBaseToXml(data);
    save(MealplanerDataXml.class, mealDataBase, filePath);
  }

  private static MealplanerDataXml convertDataBaseToXml(MealplanerData data) {
    List<MealXml> mealXmls = data.getMeals().stream()
        .map(MealAdapter::convertMealV3ToXml)
        .collect(toList());
    List<IngredientXml> ingredientsXml = data.getIngredients().stream()
        .map(IngredientAdapter::convertIngredientV3ToXml)
        .collect(toList());
    return new MealplanerDataXml(convertDefaultSettingsV3ToXml(data.getDefaultSettings()),
        data.getTime(),
        convertProposalV3ToXml(data.getLastProposal()), mealXmls, ingredientsXml);
  }
}
