// SPDX-License-Identifier: MIT

package mealplaner.io.xml;

import static mealplaner.io.xml.adapters.MealAdapter.convertMealV3FromXml;
import static mealplaner.io.xml.util.JaxHelper.read;

import java.util.ArrayList;
import java.util.List;

import mealplaner.io.xml.model.v3.MealdatabaseXml;
import mealplaner.io.xml.util.VersionControl;
import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;
import mealplaner.plugins.PluginStore;

public final class MealsReader {
  private MealsReader() {
  }

  public static List<Meal> loadXml(MealplanerData data, String filePath, PluginStore knownPlugins) {
    int versionNumber = VersionControl.getVersion(filePath);
    if (versionNumber == 3) {
      MealdatabaseXml database = read(filePath, MealdatabaseXml.class, knownPlugins);
      return convertToMealsV3(data, database, knownPlugins);
    } else {
      return new ArrayList<>();
    }
  }

  private static List<Meal> convertToMealsV3(
      MealplanerData database, MealdatabaseXml data, PluginStore knownPlugins) {
    List<Meal> modelMeals = new ArrayList<>();
    data.meals.stream()
        .map(meal -> convertMealV3FromXml(database, meal, knownPlugins))
        .forEach(modelMeals::add);
    return modelMeals;
  }
}
