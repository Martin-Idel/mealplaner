package mealplaner.gui.model;

import java.util.EnumMap;

import mealplaner.BundleStore;
import mealplaner.model.enums.CasseroleSettings;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.PreferenceSettings;
import mealplaner.model.enums.Sidedish;

public class EnumToStringRepresentation {
	public static EnumMap<CookingTime, String> getCookingTimeStrings(BundleStore bundles) {
		EnumMap<CookingTime, String> lengthNames = new EnumMap<CookingTime, String>(
				CookingTime.class);
		lengthNames.put(CookingTime.SHORT, bundles.message("short"));
		lengthNames.put(CookingTime.MEDIUM, bundles.message("medium"));
		lengthNames.put(CookingTime.LONG, bundles.message("long"));
		return lengthNames;
	}

	public static EnumMap<Sidedish, String> getSidedishStrings(BundleStore bundles) {
		EnumMap<Sidedish, String> sideDishNames = new EnumMap<Sidedish, String>(Sidedish.class);
		sideDishNames.put(Sidedish.POTATOES, bundles.message("potatoes"));
		sideDishNames.put(Sidedish.PASTA, bundles.message("pasta"));
		sideDishNames.put(Sidedish.RICE, bundles.message("rice"));
		sideDishNames.put(Sidedish.NONE, bundles.message("none"));
		return sideDishNames;
	}

	public static EnumMap<ObligatoryUtensil, String> getObligatoryUtensilStrings(
			BundleStore bundles) {
		EnumMap<ObligatoryUtensil, String> obligatoryUtensilNames = new EnumMap<ObligatoryUtensil, String>(
				ObligatoryUtensil.class);
		obligatoryUtensilNames.put(ObligatoryUtensil.POT, bundles.message("pot"));
		obligatoryUtensilNames.put(ObligatoryUtensil.PAN, bundles.message("pan"));
		obligatoryUtensilNames.put(ObligatoryUtensil.CASSEROLE, bundles.message("casserole"));
		return obligatoryUtensilNames;
	}

	public static EnumMap<CookingPreference, String> getCookingPreferenceStrings(
			BundleStore bundles) {
		EnumMap<CookingPreference, String> cookingPreferenceNames = new EnumMap<CookingPreference, String>(
				CookingPreference.class);
		cookingPreferenceNames.put(CookingPreference.VERY_POPULAR,
				bundles.message("veryPopular"));
		cookingPreferenceNames.put(CookingPreference.NO_PREFERENCE,
				bundles.message("noPreference"));
		cookingPreferenceNames.put(CookingPreference.RARE, bundles.message("seldom"));
		return cookingPreferenceNames;
	}

	public static EnumMap<CasseroleSettings, String> getCasseroleSettingsStrings(
			BundleStore bundles) {
		EnumMap<CasseroleSettings, String> casseroleSettings = new EnumMap<CasseroleSettings, String>(
				CasseroleSettings.class);
		casseroleSettings.put(CasseroleSettings.POSSIBLE, bundles.message("allowedCasseroles"));
		casseroleSettings.put(CasseroleSettings.ONLY, bundles.message("onlyCasseroles"));
		casseroleSettings.put(CasseroleSettings.NONE, bundles.message("noCasseroles"));
		return casseroleSettings;
	}

	public static EnumMap<PreferenceSettings, String> getPreferenceSettingsStrings(
			BundleStore bundles) {
		EnumMap<PreferenceSettings, String> cookingPreferenceNames = new EnumMap<PreferenceSettings, String>(
				PreferenceSettings.class);
		cookingPreferenceNames.put(PreferenceSettings.NORMAL, bundles.message("normalPref"));
		cookingPreferenceNames.put(PreferenceSettings.VERY_POPULAR_ONLY,
				bundles.message("veryPopularOnlyPref"));
		cookingPreferenceNames.put(PreferenceSettings.RARE_NONE,
				bundles.message("noSeldomPref"));
		cookingPreferenceNames.put(PreferenceSettings.RARE_PREFERED,
				bundles.message("seldomPref"));
		return cookingPreferenceNames;
	}
}
