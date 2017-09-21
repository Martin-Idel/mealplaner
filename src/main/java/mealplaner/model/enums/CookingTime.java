package mealplaner.model.enums;

import java.util.EnumMap;

import mealplaner.BundleStore;

public enum CookingTime {
	SHORT, MEDIUM, LONG;

	public static EnumMap<CookingTime, String> getCookingTimeStrings(BundleStore bundles) {
		EnumMap<CookingTime, String> lengthNames = new EnumMap<>(CookingTime.class);
		lengthNames.put(CookingTime.SHORT, bundles.message("short"));
		lengthNames.put(CookingTime.MEDIUM, bundles.message("medium"));
		lengthNames.put(CookingTime.LONG, bundles.message("long"));
		return lengthNames;
	}
}
