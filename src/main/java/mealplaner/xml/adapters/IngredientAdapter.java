package mealplaner.xml.adapters;

import mealplaner.recipes.model.Ingredient;
import mealplaner.xml.model.IngredientXml;

public final class IngredientAdapter {
  private IngredientAdapter() {
  }

  public static IngredientXml convertIngredientToXml(Ingredient ingredient) {
    return new IngredientXml(ingredient.getName(), ingredient.getType(), ingredient.getMeasure());
  }

  public static Ingredient convertIngredientFromXml(IngredientXml ingredient) {
    return Ingredient.ingredient(ingredient.name, ingredient.type, ingredient.measure);
  }

}
