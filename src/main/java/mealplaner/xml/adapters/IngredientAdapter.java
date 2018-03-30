package mealplaner.xml.adapters;

import mealplaner.recipes.model.Ingredient;
import mealplaner.xml.model.v2.IngredientXml;

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

  public static Ingredient convertIngredientFromXml(
      mealplaner.xml.model.v1.IngredientXml ingredient) {
    return Ingredient.ingredient(ingredient.name,
        ingredient.type,
        ingredient.measure);
  }
}
