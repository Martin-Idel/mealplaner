package mealplaner.xml;

import static java.util.stream.Collectors.toList;
import static mealplaner.xml.JaxHelper.read;

import java.util.ArrayList;
import java.util.List;

import mealplaner.recipes.model.Ingredient;
import mealplaner.xml.model.IngredientXml;
import mealplaner.xml.model.IngredientdatabaseXml;

public class IngredientsReader {
  private IngredientsReader() {
  }

  public static List<Ingredient> loadXml(String filePath) {
    int versionNumber = VersionControl.getVersion(filePath);
    if (versionNumber == 1) {
      IngredientdatabaseXml database = read(filePath, IngredientdatabaseXml.class);
      return database.ingredients.stream()
          .map(ingredient -> convertIngredientFromXml(ingredient))
          .collect(toList());

    } else {
      // TODO: Delete once saves have been ported
      return new ArrayList<>();
      // try {
      // return MealplanerFileLoader.load(filePath).getMeals();
      // } catch (MealException | IOException e) {
      // return new ArrayList<>();
      // }
    }
  }

  public static Ingredient convertIngredientFromXml(IngredientXml ingredient) {
    return Ingredient.ingredient(ingredient.name, ingredient.type, ingredient.measure);
  }
}
