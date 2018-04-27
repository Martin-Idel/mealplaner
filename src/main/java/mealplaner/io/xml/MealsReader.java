// SPDX-License-Identifier: MIT

package mealplaner.io.xml;

import static mealplaner.io.xml.adapters.MealAdapter.convertMealFromXml;
import static mealplaner.io.xml.util.JaxHelper.read;

import java.util.ArrayList;
import java.util.List;

import mealplaner.io.xml.model.v2.MealdatabaseXml;
import mealplaner.io.xml.util.VersionControl;
import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;

public final class MealsReader {
  private MealsReader() {
  }

  public static List<Meal> loadXml(MealplanerData data, String filePath) {
    int versionNumber = VersionControl.getVersion(filePath);
    if (versionNumber == 2) {
      MealdatabaseXml database = read(filePath, MealdatabaseXml.class);
      return convertToMeals(data, database);
    } else {
      return new ArrayList<>();
    }
  }

  private static List<Meal> convertToMeals(MealplanerData database, MealdatabaseXml data) {
    List<Meal> modelMeals = new ArrayList<>();
    data.meals.stream()
        .map(meal -> convertMealFromXml(database, meal))
        .forEach(meal -> modelMeals.add(meal));
    return modelMeals;
  }
}
