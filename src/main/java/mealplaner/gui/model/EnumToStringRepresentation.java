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
		EnumMap<CookingTime, String> lengthNames = new EnumMap<CookingTime, String>(CookingTime.class);
		lengthNames.put(CookingTime.SHORT, messages.getString("length1"));
		lengthNames.put(CookingTime.MEDIUM, messages.getString("length2"));
		lengthNames.put(CookingTime.LONG, messages.getString("length3"));
		return lengthNames;
	}

	public static EnumMap<Sidedish, String> getSidedishStrings(ResourceBundle messages) {
		EnumMap<Sidedish, String> sideDishNames = new EnumMap<Sidedish, String>(Sidedish.class);
		sideDishNames.put(Sidedish.POTATOES, messages.getString("stark1"));
		sideDishNames.put(Sidedish.PASTA, messages.getString("stark2"));
		sideDishNames.put(Sidedish.RICE, messages.getString("stark3"));
		sideDishNames.put(Sidedish.NONE, messages.getString("stark4"));
		return sideDishNames;
	}

	public static EnumMap<ObligatoryUtensil, String> getObligatoryUtensilStrings(ResourceBundle messages) {
		EnumMap<ObligatoryUtensil, String> obligatoryUtensilNames = new EnumMap<ObligatoryUtensil, String>(
				ObligatoryUtensil.class);
		obligatoryUtensilNames.put(ObligatoryUtensil.POT, messages.getString("utensil1"));
		obligatoryUtensilNames.put(ObligatoryUtensil.PAN, messages.getString("utensil2"));
		obligatoryUtensilNames.put(ObligatoryUtensil.CASSEROLE, messages.getString("utensil3"));
		return obligatoryUtensilNames;
	}

	public static EnumMap<CookingPreference, String> getCookingPreferenceStrings(ResourceBundle messages) {
		EnumMap<CookingPreference, String> cookingPreferenceNames = new EnumMap<CookingPreference, String>(
				CookingPreference.class);
		cookingPreferenceNames.put(CookingPreference.VERY_POPULAR, messages.getString("malus1"));
		cookingPreferenceNames.put(CookingPreference.NO_PREFERENCE, messages.getString("malus2"));
		cookingPreferenceNames.put(CookingPreference.RARE, messages.getString("malus3"));
		return cookingPreferenceNames;
	}

	public static EnumMap<CasseroleSettings, String> getCasseroleSettingsStrings(ResourceBundle messages) {
		EnumMap<CasseroleSettings, String> casseroleSettings = new EnumMap<CasseroleSettings, String>(
				CasseroleSettings.class);
		casseroleSettings.put(CasseroleSettings.POSSIBLE, messages.getString("casserole1"));
		casseroleSettings.put(CasseroleSettings.ONLY, messages.getString("casserole2"));
		casseroleSettings.put(CasseroleSettings.NONE, messages.getString("casserole3"));
		return casseroleSettings;
	}

	public static EnumMap<PreferenceSettings, String> getPreferenceSettingsStrings(ResourceBundle messages) {
		EnumMap<PreferenceSettings, String> cookingPreferenceNames = new EnumMap<PreferenceSettings, String>(
				PreferenceSettings.class);
		cookingPreferenceNames.put(PreferenceSettings.NORMAL, messages.getString("preference1"));
		cookingPreferenceNames.put(PreferenceSettings.VERY_POPULAR_ONLY, messages.getString("preference2"));
		cookingPreferenceNames.put(PreferenceSettings.RARE_NONE, messages.getString("preference3"));
		cookingPreferenceNames.put(PreferenceSettings.RARE_PREFERED, messages.getString("preference4"));
		return cookingPreferenceNames;
	}
}
