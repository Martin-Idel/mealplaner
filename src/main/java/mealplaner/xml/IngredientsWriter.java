package mealplaner.xml;

import static java.util.stream.Collectors.toList;
import static mealplaner.xml.adapters.IngredientAdapter.convertIngredientToXml;
import static mealplaner.xml.util.JaxHelper.save;

import java.util.List;

import mealplaner.recipes.model.Ingredient;
import mealplaner.xml.model.v2.IngredientXml;
import mealplaner.xml.model.v2.IngredientdatabaseXml;

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
