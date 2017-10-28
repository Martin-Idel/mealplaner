package mealplaner.io;

import static mealplaner.BundleStore.BUNDLES;

import mealplaner.errorhandling.MealException;
import mealplaner.gui.commons.MessageDialog;
import mealplaner.recipes.provider.IngredientProvider;

public class IngredientProviderIoGui {

	public static IngredientProvider loadIngredientProvider() {
		IngredientProvider ingredientProvider;
		try {
			ingredientProvider = IngredientIO.readXml();
		} catch (MealException exc) {
			MessageDialog.errorMessages(null, exc,
					BUNDLES.errorMessage("INGREDIENT_PROVIDER_NOT_FOUND"));
			ingredientProvider = new IngredientProvider();
		}
		return ingredientProvider;
	}
}
