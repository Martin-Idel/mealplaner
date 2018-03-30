package mealplaner.xml;

import static java.util.stream.Collectors.toList;
import static mealplaner.xml.adapters.IngredientAdapter.convertIngredientFromXml;
import static mealplaner.xml.util.JaxHelper.read;

import java.util.ArrayList;
import java.util.List;

import mealplaner.recipes.model.Ingredient;
import mealplaner.xml.model.v2.IngredientdatabaseXml;
import mealplaner.xml.util.VersionControl;

public final class IngredientsReader {
  private IngredientsReader() {
  }

  public static List<Ingredient> loadXml(String filePath) {
    int versionNumber = VersionControl.getVersion(filePath);
    if (versionNumber == 1) {
      mealplaner.xml.model.v1.IngredientdatabaseXml database = read(filePath,
          mealplaner.xml.model.v1.IngredientdatabaseXml.class);
      return convertToIngredients(database);
    } else if (versionNumber == 2) {
      IngredientdatabaseXml database = read(filePath, IngredientdatabaseXml.class);
      return convertToIngredients(database);
    } else {
      return new ArrayList<>();
    }
  }

  private static List<Ingredient> convertToIngredients(IngredientdatabaseXml database) {
    return database.ingredients.stream()
        .map(ingredient -> convertIngredientFromXml(ingredient))
        .collect(toList());
  }

  private static List<Ingredient> convertToIngredients(
      mealplaner.xml.model.v1.IngredientdatabaseXml database) {
    return database.ingredients.stream()
        .map(ingredient -> convertIngredientFromXml(ingredient))
        .collect(toList());
  }
}
