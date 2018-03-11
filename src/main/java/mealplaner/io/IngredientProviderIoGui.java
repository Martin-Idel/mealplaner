package mealplaner.io;

import static mealplaner.commons.BundleStore.BUNDLES;

import java.util.List;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.commons.gui.MessageDialog;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.provider.IngredientProvider;
import mealplaner.xml.IngredientsReader;

public final class IngredientProviderIoGui {
  private IngredientProviderIoGui() {
  }

  public static IngredientProvider loadIngredientProvider(String filepath) {
    IngredientProvider ingredientProvider;
    try {
      List<Ingredient> ingredients = IngredientsReader.loadXml(filepath);
      ingredientProvider = new IngredientProvider(ingredients, filepath);
    } catch (MealException exc) {
      MessageDialog.errorMessages(null, exc,
          BUNDLES.errorMessage("INGREDIENT_PROVIDER_NOT_FOUND"));
      ingredientProvider = new IngredientProvider();
    }
    return ingredientProvider;
  }
}
