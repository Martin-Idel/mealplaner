// SPDX-License-Identifier: MIT

package mealplaner.io.xml;

import static java.util.stream.Collectors.toList;
import static mealplaner.io.xml.adapters.MealAdapter.convertMealV3FromXml;
import static mealplaner.io.xml.adapters.ProposalAdapter.convertProposalV3FromXml;
import static mealplaner.io.xml.adapters.SettingsAdapter.convertDefaultSettingsV3FromXml;
import static mealplaner.io.xml.util.JaxHelper.read;
import static mealplaner.io.xml.util.VersionControl.getVersion;

import java.util.List;

import mealplaner.io.xml.adapters.IngredientAdapter;
import mealplaner.io.xml.model.v3.MealplanerDataXml;
import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.plugins.PluginStore;

public final class MealplanerDataReader {
  private MealplanerDataReader() {
  }

  public static MealplanerData loadXml(String filePath, PluginStore knownPlugins) {
    int versionNumber = getVersion(filePath);
    if (versionNumber == 3) {
      MealplanerDataXml database = read(filePath, MealplanerDataXml.class, knownPlugins);
      return convertToData(database, knownPlugins);
    } else {
      MealplanerData data = MealplanerData.getInstance(knownPlugins);
      data.clear();
      return data;
    }
  }

  private static MealplanerData convertToData(MealplanerDataXml database, PluginStore plugins) {
    DefaultSettings defaultSettings = convertDefaultSettingsV3FromXml(
        database.defaultSettings, plugins);
    MealplanerData data = MealplanerData.getInstance(plugins);

    List<Ingredient> ingredients = database.ingredients.stream()
        .map(ingredient -> IngredientAdapter.convertIngredientV3FromXml(ingredient, plugins))
        .collect(toList());
    data.setIngredients(ingredients);

    List<Meal> modelMeals = database.meals.stream()
        .map(meal -> convertMealV3FromXml(data, meal, plugins))
        .collect(toList());
    data.setMeals(modelMeals);

    data.setTime(database.date);
    data.setDefaultSettings(defaultSettings);
    data.setLastProposal(convertProposalV3FromXml(database.proposal));
    return data;
  }
}