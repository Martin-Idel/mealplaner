package mealplaner.model.settings;

import java.io.Serializable;

import mealplaner.model.enums.CasseroleSettings;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.PreferenceSettings;

public class Settings implements Serializable {

	private static final long serialVersionUID = 1L;

	private final CasseroleSettings casseroleSettings;
	private final PreferenceSettings preference;
	private final CookingPreferenceSetting cookingPreferences;
	private final CookingTimeSetting cookingTime;
	private final CookingUtensilSetting cookingUtensil;

	public Settings() {
		this(new CookingTimeSetting(), false, CasseroleSettings.POSSIBLE,
				PreferenceSettings.NORMAL);
	}

	public Settings(CookingTimeSetting cookingTime, boolean manyPeople,
			CasseroleSettings casseroleSettings,
			PreferenceSettings preferenceSettings) {

		this.cookingPreferences = new CookingPreferenceSetting();
		this.cookingTime = cookingTime;
		this.cookingUtensil = new CookingUtensilSetting();
		if (manyPeople) {
			this.cookingUtensil.setManyPeople();
		}
		this.casseroleSettings = casseroleSettings;
		this.cookingUtensil.setCasseroleSettings(casseroleSettings);

		this.preference = preferenceSettings;
		this.cookingPreferences.setCookingPreferences(preference);
	}

	public Settings(Settings setting) {
		this.casseroleSettings = setting.getCasserole();
		this.preference = setting.getPreference();
		this.cookingPreferences = new CookingPreferenceSetting();
		this.cookingPreferences.setCookingPreferences(this.preference);
		this.cookingTime = new CookingTimeSetting(setting.getCookingTime());
		this.cookingUtensil = new CookingUtensilSetting(setting.getCookingUtensil());
	}

	public CookingTimeSetting getCookingTime() {
		return cookingTime;
	}

	public boolean isTimeProhibited(CookingTime cookingTime) {
		return this.cookingTime.isTimeProhibited(cookingTime);
	}

	public boolean isTimePossible(CookingTime cookingTime) {
		return !isTimeProhibited(cookingTime);
	}

	public boolean shallManyPeopleEat() {
		return cookingUtensil.isUtensilProhibited(ObligatoryUtensil.PAN);
	}

	public CookingUtensilSetting getCookingUtensil() {
		return cookingUtensil;
	}

	public CookingPreferenceSetting getCookingPreference() {
		return cookingPreferences;
	}

	public CasseroleSettings getCasserole() {
		return casseroleSettings;
	}

	public PreferenceSettings getPreference() {
		return preference;
	}
}