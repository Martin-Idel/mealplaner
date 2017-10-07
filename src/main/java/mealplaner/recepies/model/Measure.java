package mealplaner.recepies.model;

import java.util.EnumMap;

import mealplaner.BundleStore;

public enum Measure {
	GRAM, MILLILITRE, TEASPOON, TABLESPOON, NONE;

	public static EnumMap<Measure, String> getMeasureStrings(BundleStore bundles) {
		EnumMap<Measure, String> measure = new EnumMap<>(Measure.class);
		measure.put(GRAM, bundles.message("GRAM"));
		measure.put(MILLILITRE, bundles.message("MILLILITRE"));
		measure.put(TEASPOON, bundles.message("TEASPOON"));
		measure.put(TABLESPOON, bundles.message("TABLESPOON"));
		measure.put(NONE, bundles.message("NONE"));
		return measure;
	}
}
