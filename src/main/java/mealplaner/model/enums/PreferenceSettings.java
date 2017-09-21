package mealplaner.model.enums;

import java.util.EnumMap;

import mealplaner.BundleStore;

public enum PreferenceSettings {
	NORMAL, VERY_POPULAR_ONLY, RARE_NONE, RARE_PREFERED;

	public static EnumMap<PreferenceSettings, String> getPreferenceSettingsStrings(
			BundleStore bundles) {
		EnumMap<PreferenceSettings, String> cookingPreferenceNames = new EnumMap<PreferenceSettings, String>(
				PreferenceSettings.class);
		cookingPreferenceNames.put(PreferenceSettings.NORMAL, bundles.message("normalPref"));
		cookingPreferenceNames.put(PreferenceSettings.VERY_POPULAR_ONLY,
				bundles.message("veryPopularOnlyPref"));
		cookingPreferenceNames.put(PreferenceSettings.RARE_NONE, bundles.message("noSeldomPref"));
		cookingPreferenceNames.put(PreferenceSettings.RARE_PREFERED, bundles.message("seldomPref"));
		return cookingPreferenceNames;
	}
}
