// SPDX-License-Identifier: MIT

package mealplaner.io.xml;

import static java.util.stream.Collectors.toList;
import static mealplaner.io.xml.adapters.IngredientAdapter.convertIngredientToXml;
import static mealplaner.io.xml.adapters.MealAdapter.convertMealToXml;
import static mealplaner.io.xml.adapters.ProposalAdapter.convertProposalToXml;
import static mealplaner.io.xml.adapters.SettingsAdapter.convertDefaultSettingsToXml;
import static mealplaner.io.xml.util.JaxHelper.save;

import java.util.List;

import mealplaner.io.xml.adapters.IngredientAdapter;
import mealplaner.io.xml.adapters.MealAdapter;
import mealplaner.io.xml.model.v2.IngredientXml;
import mealplaner.io.xml.model.v2.MealXml;
import mealplaner.io.xml.model.v2.MealplanerDataXml;
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
        .map(MealAdapter::convertMealToXml)
        .collect(toList());
    List<IngredientXml> ingredientsXml = data.getIngredients().stream()
        .map(IngredientAdapter::convertIngredientToXml)
        .collect(toList());
    return new MealplanerDataXml(convertDefaultSettingsToXml(data.getDefaultSettings()),
        data.getTime(),
        convertProposalToXml(data.getLastProposal()), mealXmls, ingredientsXml);
  }
}
