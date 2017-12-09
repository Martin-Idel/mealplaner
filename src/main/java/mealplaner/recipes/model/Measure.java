package mealplaner.recipes.model;

import static mealplaner.commons.BundleStore.BUNDLES;

public enum Measure {
	GRAM(BUNDLES.message("GRAM")),
	MILLILITRE(BUNDLES.message("MILLILITRE")),
	TEASPOON(BUNDLES.message("TEASPOON")),
	TABLESPOON(BUNDLES.message("TABLESPOON")),
	NONE(BUNDLES.message("NONE"));

	private String message;

	Measure(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return message;
	}
}
