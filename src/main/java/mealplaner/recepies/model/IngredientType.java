package mealplaner.recepies.model;

import java.util.EnumMap;

import mealplaner.BundleStore;

public enum IngredientType {
	WHITE_MEAT, RED_MEAT, MEAT_PRODUCTS, MILK_EGG_PRODUCTS, FRESH_FRUIT, CANNED_FRUIT, FRESH_VEGETABLES, CANNED_VEGETABLES, SPICE, FLUID, BAKING_GOODS, DRY_GOODS, OTHER;

	public static EnumMap<IngredientType, String> getIngredientTypeStrings(BundleStore bundles) {
		EnumMap<IngredientType, String> ingredientTypes = new EnumMap<>(IngredientType.class);
		ingredientTypes.put(WHITE_MEAT, bundles.message("WHITE_MEAT"));
		ingredientTypes.put(RED_MEAT, bundles.message("RED_MEAT"));
		ingredientTypes.put(MEAT_PRODUCTS, bundles.message("MEAT_PRODUCTS"));
		ingredientTypes.put(MILK_EGG_PRODUCTS, bundles.message("MILK_EGG_PRODUCTS"));
		ingredientTypes.put(FRESH_FRUIT, bundles.message("FRESH_FRUIT"));
		ingredientTypes.put(CANNED_FRUIT, bundles.message("CANNED_FRUIT"));
		ingredientTypes.put(FRESH_VEGETABLES, bundles.message("FRESH_VEGETABLES"));
		ingredientTypes.put(CANNED_VEGETABLES, bundles.message("CANNED_VEGETABLES"));
		ingredientTypes.put(SPICE, bundles.message("SPICE"));
		ingredientTypes.put(FLUID, bundles.message("FLUID"));
		ingredientTypes.put(BAKING_GOODS, bundles.message("BAKING_GOODS"));
		ingredientTypes.put(DRY_GOODS, bundles.message("DRY_GOODS"));
		ingredientTypes.put(OTHER, bundles.message("OTHER"));
		return ingredientTypes;
	}
}
