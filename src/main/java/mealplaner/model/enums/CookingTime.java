package mealplaner.model.enums;

import java.util.EnumMap;

import mealplaner.BundleStore;

public enum CookingTime {
	VERY_SHORT, SHORT, MEDIUM, LONG;

	public static EnumMap<CookingTime, String> getCookingTimeStrings(BundleStore bundles) {
		EnumMap<CookingTime, String> lengthNames = new EnumMap<>(CookingTime.class);
		lengthNames.put(VERY_SHORT, bundles.message("veryshort"));
		lengthNames.put(SHORT, bundles.message("short"));
		lengthNames.put(MEDIUM, bundles.message("medium"));
		lengthNames.put(LONG, bundles.message("long"));
		return lengthNames;
	}
}
