package mealplaner.gui.model;

import java.util.EnumMap;
import java.util.ResourceBundle;

import mealplaner.model.enums.CasseroleSettings;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.PreferenceSettings;
import mealplaner.model.enums.Sidedish;

public class EnumToStringRepresentation {
	public static EnumMap<CookingTime, String> getCookingTimeStrings(ResourceBundle messages) {
		EnumMap<CookingTime, String> lengthNames = new EnumMap<CookingTime, String>(
				CookingTime.class);
		lengthNames.put(CookingTime.SHORT, messages.getString("short"));
		lengthNames.put(CookingTime.MEDIUM, messages.getString("medium"));
		lengthNames.put(CookingTime.LONG, messages.getString("long"));
		return lengthNames;
	}

	public static EnumMap<Sidedish, String> getSidedishStrings(ResourceBundle messages) {
		EnumMap<Sidedish, String> sideDishNames = new EnumMap<Sidedish, String>(Sidedish.class);
		sideDishNames.put(Sidedish.POTATOES, messages.getString("potatoes"));
		sideDishNames.put(Sidedish.PASTA, messages.getString("pasta"));
		sideDishNames.put(Sidedish.RICE, messages.getString("rice"));
		sideDishNames.put(Sidedish.NONE, messages.getString("none"));
		return sideDishNames;
	}

	public static EnumMap<ObligatoryUtensil, String> getObligatoryUtensilStrings(
			ResourceBundle messages) {
		EnumMap<ObligatoryUtensil, String> obligatoryUtensilNames = new EnumMap<ObligatoryUtensil, String>(
				ObligatoryUtensil.class);
		obligatoryUtensilNames.put(ObligatoryUtensil.POT, messages.getString("pot"));
		obligatoryUtensilNames.put(ObligatoryUtensil.PAN, messages.getString("pan"));
		obligatoryUtensilNames.put(ObligatoryUtensil.CASSEROLE, messages.getString("casserole"));
		return obligatoryUtensilNames;
	}

	public static EnumMap<CookingPreference, String> getCookingPreferenceStrings(
			ResourceBundle messages) {
		EnumMap<CookingPreference, String> cookingPreferenceNames = new EnumMap<CookingPreference, String>(
				CookingPreference.class);
		cookingPreferenceNames.put(CookingPreference.VERY_POPULAR,
				messages.getString("veryPopular"));
		cookingPreferenceNames.put(CookingPreference.NO_PREFERENCE,
				messages.getString("noPreference"));
		cookingPreferenceNames.put(CookingPreference.RARE, messages.getString("seldom"));
		return cookingPreferenceNames;
	}

	public static EnumMap<CasseroleSettings, String> getCasseroleSettingsStrings(
			ResourceBundle messages) {
		EnumMap<CasseroleSettings, String> casseroleSettings = new EnumMap<CasseroleSettings, String>(
				CasseroleSettings.class);
		casseroleSettings.put(CasseroleSettings.POSSIBLE, messages.getString("allowedCasseroles"));
		casseroleSettings.put(CasseroleSettings.ONLY, messages.getString("onlyCasseroles"));
		casseroleSettings.put(CasseroleSettings.NONE, messages.getString("noCasseroles"));
		return casseroleSettings;
	}

	public static EnumMap<PreferenceSettings, String> getPreferenceSettingsStrings(
			ResourceBundle messages) {
		EnumMap<PreferenceSettings, String> cookingPreferenceNames = new EnumMap<PreferenceSettings, String>(
				PreferenceSettings.class);
		cookingPreferenceNames.put(PreferenceSettings.NORMAL, messages.getString("normalPref"));
		cookingPreferenceNames.put(PreferenceSettings.VERY_POPULAR_ONLY,
				messages.getString("veryPopularOnlyPref"));
		cookingPreferenceNames.put(PreferenceSettings.RARE_NONE,
				messages.getString("noSeldomPref"));
		cookingPreferenceNames.put(PreferenceSettings.RARE_PREFERED,
				messages.getString("seldomPref"));
		return cookingPreferenceNames;
	}
}
