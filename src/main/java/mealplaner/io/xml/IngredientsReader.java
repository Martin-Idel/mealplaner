// SPDX-License-Identifier: MIT

package mealplaner.io.xml;

import static java.util.stream.Collectors.toList;
import static mealplaner.io.xml.util.JaxHelper.read;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.core.config.plugins.Plugin;

import mealplaner.PluginStore;
import mealplaner.io.xml.adapters.IngredientAdapter;
import mealplaner.io.xml.model.v2.IngredientdatabaseXml;
import mealplaner.io.xml.util.VersionControl;
import mealplaner.model.recipes.Ingredient;

public final class IngredientsReader {
  private IngredientsReader() {
  }

  public static List<Ingredient> loadXml(String filePath, PluginStore knownPlugins) {
    int versionNumber = VersionControl.getVersion(filePath);
    if (versionNumber == 2) {
      IngredientdatabaseXml database = read(filePath, IngredientdatabaseXml.class, knownPlugins);
      return convertToIngredients(database);
    } else if (versionNumber == 3) {
      mealplaner.io.xml.model.v3.IngredientdatabaseXml database = read(
          filePath, mealplaner.io.xml.model.v3.IngredientdatabaseXml.class, knownPlugins);
      return convertToIngredientsV3(database, knownPlugins);
    } else {
      return new ArrayList<>();
    }
  }

  private static List<Ingredient> convertToIngredientsV3(
      mealplaner.io.xml.model.v3.IngredientdatabaseXml database,
      PluginStore plugins) {
    return database.ingredients.stream()
        .map(ingredient -> IngredientAdapter.convertIngredientV3FromXml(ingredient, plugins))
        .collect(toList());
  }

  private static List<Ingredient> convertToIngredients(IngredientdatabaseXml database) {
    return database.ingredients.stream()
        .map(IngredientAdapter::convertIngredientV2FromXml)
        .collect(toList());
  }
}
