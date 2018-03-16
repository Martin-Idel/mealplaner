package mealplaner.xml;

import static mealplaner.xml.adapters.MealAdapter.convertMealFromXml;
import static mealplaner.xml.util.JaxHelper.read;

import java.util.ArrayList;
import java.util.List;

import mealplaner.model.Meal;
import mealplaner.xml.model.MealdatabaseXml;
import mealplaner.xml.util.VersionControl;

public final class MealsReader {
  private MealsReader() {
  }

  public static List<Meal> loadXml(String filePath) {
    int versionNumber = VersionControl.getVersion(filePath);
    if (versionNumber == 1) {
      MealdatabaseXml database = read(filePath, MealdatabaseXml.class);
      return convertToMeals(database);
    } else {
      return new ArrayList<>();
    }
  }

  private static List<Meal> convertToMeals(MealdatabaseXml data) {
    List<Meal> modelMeals = new ArrayList<>();
    data.meals.stream()
        .map(meal -> convertMealFromXml(meal))
        .forEach(meal -> modelMeals.add(meal));
    return modelMeals;
  }
}
