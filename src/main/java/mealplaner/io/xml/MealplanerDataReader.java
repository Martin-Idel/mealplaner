// SPDX-License-Identifier: MIT

package mealplaner.io.xml;

import static java.util.stream.Collectors.toList;
import static mealplaner.io.xml.adapters.MealAdapter.convertMealFromXml;
import static mealplaner.io.xml.adapters.ProposalAdapter.convertProposalFromXml;
import static mealplaner.io.xml.adapters.SettingsAdapter.convertDefaultSettingsFromXml;
import static mealplaner.io.xml.util.JaxHelper.read;
import static mealplaner.io.xml.util.VersionControl.getVersion;

import java.util.List;

import mealplaner.io.xml.adapters.IngredientAdapter;
import mealplaner.io.xml.model.v2.MealplanerDataXml;
import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.settings.DefaultSettings;

public final class MealplanerDataReader {
  private MealplanerDataReader() {
  }

  public static MealplanerData loadXml(String filePath) {
    int versionNumber = getVersion(filePath);
    if (versionNumber == 2) {
      MealplanerDataXml database = read(filePath, MealplanerDataXml.class);
      return convertToData(database);
    } else {
      MealplanerData data = MealplanerData.getInstance();
      data.clear();
      return data;
    }
  }

  private static MealplanerData convertToData(MealplanerDataXml database) {
    DefaultSettings defaultSettings = convertDefaultSettingsFromXml(database.defaultSettings);
    MealplanerData data = MealplanerData.getInstance();

    List<Ingredient> ingredients = database.ingredients.stream()
        .map(IngredientAdapter::convertIngredientFromXml)
        .collect(toList());
    data.setIngredients(ingredients);

    List<Meal> modelMeals = database.meals.stream()
        .map(meal -> convertMealFromXml(data, meal))
        .collect(toList());
    data.setMeals(modelMeals);

    data.setTime(database.date);
    data.setDefaultSettings(defaultSettings);
    data.setLastProposal(convertProposalFromXml(database.proposal));
    return data;
  }
}