package mealplaner.model.enums;

import static mealplaner.BundleStore.BUNDLES;

public enum CookingPreference {
	VERY_POPULAR(BUNDLES.message("veryPopular")),
	NO_PREFERENCE(BUNDLES.message("noPreference")),
	RARE(BUNDLES.message("seldom"));

	private String message;

	CookingPreference(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return message;
	}
}