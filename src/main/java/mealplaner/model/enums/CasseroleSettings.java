package mealplaner.model.enums;

import java.util.EnumMap;

import mealplaner.BundleStore;

public enum CasseroleSettings {
	POSSIBLE, ONLY, NONE;

	public static EnumMap<CasseroleSettings, String> getCasseroleSettingsStrings(BundleStore bundles) {
		EnumMap<CasseroleSettings, String> casseroleSettings = new EnumMap<CasseroleSettings, String>(
				CasseroleSettings.class);
		casseroleSettings.put(CasseroleSettings.POSSIBLE, bundles.message("allowedCasseroles"));
		casseroleSettings.put(CasseroleSettings.ONLY, bundles.message("onlyCasseroles"));
		casseroleSettings.put(CasseroleSettings.NONE, bundles.message("noCasseroles"));
		return casseroleSettings;
	}
}
