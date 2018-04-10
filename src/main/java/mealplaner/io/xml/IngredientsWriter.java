package mealplaner.io.xml;

import static java.util.stream.Collectors.toList;
import static mealplaner.io.xml.adapters.IngredientAdapter.convertIngredientToXml;
import static mealplaner.io.xml.util.JaxHelper.save;

import java.util.List;

import mealplaner.io.xml.model.v2.IngredientXml;
import mealplaner.io.xml.model.v2.IngredientdatabaseXml;
import mealplaner.model.recipes.Ingredient;

public final class IngredientsWriter {
  private IngredientsWriter() {
  }

  public static void saveXml(List<Ingredient> data, String filePath) {
    IngredientdatabaseXml ingredientsDatabase = convertDataBaseToXml(data);
    save(IngredientdatabaseXml.class, ingredientsDatabase, filePath);
  }

  private static IngredientdatabaseXml convertDataBaseToXml(List<Ingredient> data) {
    List<IngredientXml> ingredientsXml = data.stream()
        .map(ingredient -> convertIngredientToXml(ingredient))
        .collect(toList());
    return new IngredientdatabaseXml(ingredientsXml);
  }
}
