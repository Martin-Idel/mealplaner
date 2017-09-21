package mealplaner.model.enums;

import java.util.EnumMap;

import mealplaner.BundleStore;

public enum CookingPreference {
	VERY_POPULAR, NO_PREFERENCE, RARE;

	public static EnumMap<CookingPreference, String> getCookingPreferenceStrings(
			BundleStore bundles) {
		EnumMap<CookingPreference, String> cookingPreferenceNames = new EnumMap<CookingPreference, String>(
				CookingPreference.class);
		cookingPreferenceNames.put(CookingPreference.VERY_POPULAR, bundles.message("veryPopular"));
		cookingPreferenceNames.put(CookingPreference.NO_PREFERENCE,
				bundles.message("noPreference"));
		cookingPreferenceNames.put(CookingPreference.RARE, bundles.message("seldom"));
		return cookingPreferenceNames;
	}

}
