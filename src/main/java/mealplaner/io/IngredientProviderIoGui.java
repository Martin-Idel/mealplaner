package mealplaner.io;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.io.IngredientIo.readXml;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.commons.gui.MessageDialog;
import mealplaner.recipes.provider.IngredientProvider;

public final class IngredientProviderIoGui {
  private IngredientProviderIoGui() {
  }

  public static IngredientProvider loadIngredientProvider(String filepath) {
    IngredientProvider ingredientProvider;
    try {
      ingredientProvider = readXml(filepath);
    } catch (MealException exc) {
      MessageDialog.errorMessages(null, exc,
          BUNDLES.errorMessage("INGREDIENT_PROVIDER_NOT_FOUND"));
      ingredientProvider = new IngredientProvider();
    }
    return ingredientProvider;
  }
}
