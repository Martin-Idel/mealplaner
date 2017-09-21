package mealplaner.model.enums;

import java.util.EnumMap;

import mealplaner.BundleStore;

public enum Sidedish {
	POTATOES, PASTA, RICE, NONE;

	public static EnumMap<Sidedish, String> getSidedishStrings(BundleStore bundles) {
		EnumMap<Sidedish, String> sideDishNames = new EnumMap<Sidedish, String>(Sidedish.class);
		sideDishNames.put(Sidedish.POTATOES, bundles.message("potatoes"));
		sideDishNames.put(Sidedish.PASTA, bundles.message("pasta"));
		sideDishNames.put(Sidedish.RICE, bundles.message("rice"));
		sideDishNames.put(Sidedish.NONE, bundles.message("none"));
		return sideDishNames;
	}

}
