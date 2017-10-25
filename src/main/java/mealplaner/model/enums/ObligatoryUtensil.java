package mealplaner.model.enums;

import static mealplaner.BundleStore.BUNDLES;

public enum ObligatoryUtensil {
	POT(BUNDLES.message("pot")),
	PAN(BUNDLES.message("pan")),
	CASSEROLE(BUNDLES.message("casserole"));

	private String message;

	ObligatoryUtensil(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return message;
	}
}
