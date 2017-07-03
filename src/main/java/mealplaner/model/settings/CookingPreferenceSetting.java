package mealplaner.model.settings;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import mealplaner.model.Meal;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.PreferenceSettings;

public class CookingPreferenceSetting implements Serializable, CookingSetting {

	private static final long serialVersionUID = 1L;
	private final Set<CookingPreference> prohibitedCookingPreference;

	public CookingPreferenceSetting() {
		this(new HashSet<CookingPreference>());
	}

	public CookingPreferenceSetting(Set<CookingPreference> prohibitedUtensil) {
		this.prohibitedCookingPreference = prohibitedUtensil;
	}

	public void setCookingPreferences(PreferenceSettings preferences) {
		if (preferences == PreferenceSettings.RARE_NONE) {
			prohibitedCookingPreference.add(CookingPreference.RARE);
		} else if (preferences == PreferenceSettings.VERY_POPULAR_ONLY) {
			prohibitedCookingPreference.addAll(Arrays.asList(CookingPreference.values()));
			prohibitedCookingPreference.remove(CookingPreference.VERY_POPULAR);
		}
	}

	public void reset() {
		prohibitedCookingPreference.removeAll(Arrays.asList(CookingPreference.values()));
	}

	@Override
	public boolean prohibits(Meal meal) {
		return prohibitedCookingPreference.contains(meal.getCookingPreference());
	}
}
