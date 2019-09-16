// SPDX-License-Identifier: MIT

package mealplaner.io.xml;

import static java.util.stream.Collectors.toList;
import static mealplaner.io.xml.util.JaxHelper.save;

import java.util.List;

import mealplaner.PluginStore;
import mealplaner.io.xml.adapters.IngredientAdapter;
import mealplaner.io.xml.model.v3.IngredientXml;
import mealplaner.io.xml.model.v3.IngredientdatabaseXml;
import mealplaner.model.recipes.Ingredient;

public final class IngredientsWriter {
  private IngredientsWriter() {
  }

  public static void saveXml(List<Ingredient> data, String filePath, PluginStore knownPlugins) {
    IngredientdatabaseXml ingredientsDatabase = convertDataBaseToXml(data);
    save(filePath, IngredientdatabaseXml.class, ingredientsDatabase, knownPlugins);
  }

  private static IngredientdatabaseXml convertDataBaseToXml(List<Ingredient> data) {
    List<IngredientXml> ingredientsXml = data.stream()
        .map(IngredientAdapter::convertIngredientV3ToXml)
        .collect(toList());
    return new IngredientdatabaseXml(ingredientsXml);
  }
}
