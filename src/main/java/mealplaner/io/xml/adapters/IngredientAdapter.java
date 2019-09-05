// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import mealplaner.io.xml.model.v2.IngredientXml;
import mealplaner.io.xml.model.v3.MeasuresXml;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.Measures;

public final class IngredientAdapter {
  private IngredientAdapter() {
  }

  public static IngredientXml convertIngredientV2ToXml(Ingredient ingredient) {
    return new IngredientXml(ingredient.getId(),
        ingredient.getName(),
        ingredient.getType(),
        ingredient.getPrimaryMeasure());
  }

  public static mealplaner.io.xml.model.v3.IngredientXml convertIngredientV3ToXml(Ingredient ingredient) {
    return new mealplaner.io.xml.model.v3.IngredientXml(ingredient.getId(),
        ingredient.getName(),
        ingredient.getType(),
        new MeasuresXml(ingredient.getMeasures()));
  }

  public static Ingredient convertIngredientV2FromXml(IngredientXml ingredient) {
    return Ingredient.ingredientWithUuid(ingredient.uuid,
        ingredient.name,
        ingredient.type,
        Measures.createMeasures(ingredient.measure));
  }

  public static Ingredient convertIngredientV3FromXml(mealplaner.io.xml.model.v3.IngredientXml ingredient) {
    return Ingredient.ingredientWithUuid(ingredient.uuid,
        ingredient.name,
        ingredient.type,
        Measures.createMeasures(ingredient.measures.primaryMeasure, ingredient.measures.secondaryMeasures));
  }
}
