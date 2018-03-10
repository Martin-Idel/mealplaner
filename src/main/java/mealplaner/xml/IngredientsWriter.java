package mealplaner.xml;

import static mealplaner.xml.JaxHelper.save;

import java.util.ArrayList;
import java.util.List;

import mealplaner.recipes.model.Ingredient;
import mealplaner.xml.model.IngredientXml;
import mealplaner.xml.model.IngredientdatabaseXml;

public final class IngredientsWriter {
  private IngredientsWriter() {
  }

  public static void saveXml(List<Ingredient> data, String filePath) {
    IngredientdatabaseXml ingredientsDatabase = convertDataBaseToXml(data);
    save(IngredientdatabaseXml.class, ingredientsDatabase, filePath);
  }

  private static IngredientdatabaseXml convertDataBaseToXml(List<Ingredient> data) {
    List<IngredientXml> ingredientsXml = new ArrayList<>();
    data.stream()
        .map(ingredient -> new IngredientXml(
            ingredient.getName(),
            ingredient.getType(),
            ingredient.getMeasure()))
        .forEach(ingredientXml -> ingredientsXml.add(ingredientXml));
    return new IngredientdatabaseXml(ingredientsXml, 1);
  }
}
