package mealplaner.model.settings;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import mealplaner.model.Meal;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.PreferenceSettings;

public class CookingPreferenceSetting implements CookingSetting {
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

	@Override
	public int hashCode() {
		return 31 + ((prohibitedCookingPreference == null) ? 0
				: prohibitedCookingPreference.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		CookingPreferenceSetting other = (CookingPreferenceSetting) obj;
		if (!prohibitedCookingPreference.equals(other.prohibitedCookingPreference)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "CookingPreferenceSetting [prohibitedCookingPreference="
				+ prohibitedCookingPreference + "]";
	}
}
