// SPDX-License-Identifier: MIT

package mealplaner.plugins.preference.proposal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import mealplaner.model.meal.Meal;
import mealplaner.plugins.preference.mealextension.CookingPreference;
import mealplaner.plugins.preference.mealextension.CookingPreferenceFact;
import mealplaner.plugins.preference.settingextension.PreferenceSettings;

public final class CookingPreferenceSettings {
  private final Set<CookingPreference> prohibitedCookingPreference;

  private CookingPreferenceSettings(Set<CookingPreference> prohibitedCookingPreference) {
    this.prohibitedCookingPreference = prohibitedCookingPreference;
  }

  public static CookingPreferenceSettings from(
      Set<CookingPreference> prohibitedCookingPreference) {
    return new CookingPreferenceSettings(prohibitedCookingPreference);
  }

  public static CookingPreferenceSettings createCookingPreferenceSettings() {
    return new CookingPreferenceSettings(new HashSet<>());
  }

  public CookingPreferenceSettings setCookingPreferences(PreferenceSettings preferences) {
    if (preferences == PreferenceSettings.RARE_NONE) {
      prohibitedCookingPreference.add(CookingPreference.RARE);
    } else if (preferences == PreferenceSettings.VERY_POPULAR_ONLY) {
      prohibitedCookingPreference.addAll(Arrays.asList(CookingPreference.values()));
      prohibitedCookingPreference.remove(CookingPreference.VERY_POPULAR);
    }
    return this;
  }

  public void reset() {
    prohibitedCookingPreference.removeAll(Arrays.asList(CookingPreference.values()));
  }

  public boolean prohibits(Meal meal) {
    return prohibitedCookingPreference.contains(
        meal.getTypedMealFact(CookingPreferenceFact.class).getCookingPreference());
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
    CookingPreferenceSettings other = (CookingPreferenceSettings) obj;
    return prohibitedCookingPreference.equals(other.prohibitedCookingPreference);
  }

  @Override
  public String toString() {
    return "CookingPreferenceSetting [prohibitedCookingPreference="
        + prohibitedCookingPreference + "]";
  }
}
