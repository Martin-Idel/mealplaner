// SPDX-License-Identifier: MIT

package mealplaner.io.xml;

import static mealplaner.io.xml.adapters.MealAdapter.convertMealV2FromXml;
import static mealplaner.io.xml.adapters.MealAdapter.convertMealV3FromXml;
import static mealplaner.io.xml.util.JaxHelper.read;

import java.util.ArrayList;
import java.util.List;

import mealplaner.plugins.PluginStore;
import mealplaner.io.xml.model.v2.MealdatabaseXml;
import mealplaner.io.xml.util.VersionControl;
import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;

public final class MealsReader {
  private MealsReader() {
  }

  public static List<Meal> loadXml(MealplanerData data, String filePath, PluginStore knownPlugins) {
    int versionNumber = VersionControl.getVersion(filePath);
    if (versionNumber == 2) {
      MealdatabaseXml database = read(filePath, MealdatabaseXml.class, knownPlugins);
      return convertToMealsV2(data, database);
    } else if (versionNumber == 3) {
      mealplaner.io.xml.model.v3.MealdatabaseXml database =
          read(filePath, mealplaner.io.xml.model.v3.MealdatabaseXml.class, knownPlugins);
      return convertToMealsV3(data, database, knownPlugins);
    } else {
      return new ArrayList<>();
    }
  }

  private static List<Meal> convertToMealsV2(MealplanerData database, MealdatabaseXml data) {
    List<Meal> modelMeals = new ArrayList<>();
    data.meals.stream()
        .map(meal -> convertMealV2FromXml(database, meal))
        .forEach(modelMeals::add);
    return modelMeals;
  }

  private static List<Meal> convertToMealsV3(
      MealplanerData database, mealplaner.io.xml.model.v3.MealdatabaseXml data, PluginStore knownPlugins) {
    List<Meal> modelMeals = new ArrayList<>();
    data.meals.stream()
        .map(meal -> convertMealV3FromXml(database, meal, knownPlugins))
        .forEach(modelMeals::add);
    return modelMeals;
  }
}
