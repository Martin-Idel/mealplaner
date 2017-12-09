package mealplaner.model.enums;

import static mealplaner.commons.BundleStore.BUNDLES;

import java.util.Arrays;
import java.util.EnumMap;

public enum Sidedish {
	POTATOES(BUNDLES.message("potatoes")), PASTA(BUNDLES.message("pasta")), RICE(
			BUNDLES.message("rice")), NONE(BUNDLES.message("none"));

	private String message;

	Sidedish(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return message;
	}

	public static EnumMap<Sidedish, String> getSidedishStrings() {
		EnumMap<Sidedish, String> sideDishNames = new EnumMap<Sidedish, String>(Sidedish.class);
		Arrays.asList(Sidedish.values()).forEach(dish -> sideDishNames.put(dish, dish.toString()));
		return sideDishNames;
	}
}
