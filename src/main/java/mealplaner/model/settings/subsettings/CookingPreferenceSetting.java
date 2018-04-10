package mealplaner.model.settings.subsettings;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import mealplaner.model.meal.Meal;
import mealplaner.model.meal.enums.CookingPreference;
import mealplaner.model.settings.enums.PreferenceSettings;

public final class CookingPreferenceSetting implements CookingSetting {
  private final Set<CookingPreference> prohibitedCookingPreference;

  private CookingPreferenceSetting(Set<CookingPreference> prohibitedCookingPreference) {
    this.prohibitedCookingPreference = prohibitedCookingPreference;
  }

  public static CookingPreferenceSetting from(
      Set<CookingPreference> prohibitedCookingPreference) {
    return new CookingPreferenceSetting(prohibitedCookingPreference);
  }

  public static CookingPreferenceSetting createCookingPreferenceSettings() {
    return new CookingPreferenceSetting(new HashSet<>());
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
    return prohibitedCookingPreference.equals(other.prohibitedCookingPreference);
  }

  @Override
  public String toString() {
    return "CookingPreferenceSetting [prohibitedCookingPreference="
        + prohibitedCookingPreference + "]";
  }
}
