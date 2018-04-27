// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import mealplaner.io.xml.model.v2.IngredientXml;
import mealplaner.model.recipes.Ingredient;

public final class IngredientAdapter {
  private IngredientAdapter() {
  }

  public static IngredientXml convertIngredientToXml(Ingredient ingredient) {
    return new IngredientXml(ingredient.getId(),
        ingredient.getName(),
        ingredient.getType(),
        ingredient.getMeasure());
  }

  public static Ingredient convertIngredientFromXml(IngredientXml ingredient) {
    return Ingredient.ingredientWithUuid(ingredient.uuid,
        ingredient.name,
        ingredient.type,
        ingredient.measure);
  }
}
