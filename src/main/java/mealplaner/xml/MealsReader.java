package mealplaner.xml;

import static mealplaner.xml.adapters.MealAdapter.convertMealFromXml;
import static mealplaner.xml.util.JaxHelper.read;

import java.util.ArrayList;
import java.util.List;

import mealplaner.MealplanerData;
import mealplaner.model.Meal;
import mealplaner.xml.model.v2.MealdatabaseXml;
import mealplaner.xml.util.VersionControl;

public final class MealsReader {
  private MealsReader() {
  }

  public static List<Meal> loadXml(MealplanerData data, String filePath) {
    int versionNumber = VersionControl.getVersion(filePath);
    if (versionNumber == 1) {
      mealplaner.xml.model.v1.MealdatabaseXml database = read(filePath,
          mealplaner.xml.model.v1.MealdatabaseXml.class);
      return convertToMeals(data, database);
    } else if (versionNumber == 2) {
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

  private static List<Meal> convertToMeals(MealplanerData database,
      mealplaner.xml.model.v1.MealdatabaseXml data) {
    List<Meal> modelMeals = new ArrayList<>();
    data.meals.stream()
        .map(meal -> convertMealFromXml(database, meal))
        .forEach(meal -> modelMeals.add(meal));
    return modelMeals;
  }
}
