package mealplaner.io.xml;

import static java.util.stream.Collectors.toList;
import static mealplaner.io.xml.adapters.IngredientAdapter.convertIngredientFromXml;
import static mealplaner.io.xml.util.JaxHelper.read;

import java.util.ArrayList;
import java.util.List;

import mealplaner.io.xml.model.v2.IngredientdatabaseXml;
import mealplaner.io.xml.util.VersionControl;
import mealplaner.model.recipes.Ingredient;

public final class IngredientsReader {
  private IngredientsReader() {
  }

  public static List<Ingredient> loadXml(String filePath) {
    int versionNumber = VersionControl.getVersion(filePath);
    if (versionNumber == 2) {
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
}
