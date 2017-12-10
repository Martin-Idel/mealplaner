package mealplaner.io;

import static mealplaner.commons.BundleStore.BUNDLES;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.commons.gui.MessageDialog;
import mealplaner.recipes.provider.IngredientProvider;

public class IngredientProviderIoGui {

  public static IngredientProvider loadIngredientProvider() {
    IngredientProvider ingredientProvider;
    try {
      ingredientProvider = IngredientIo.readXml();
    } catch (MealException exc) {
      MessageDialog.errorMessages(null, exc,
          BUNDLES.errorMessage("INGREDIENT_PROVIDER_NOT_FOUND"));
      ingredientProvider = new IngredientProvider();
    }
    return ingredientProvider;
  }
}
