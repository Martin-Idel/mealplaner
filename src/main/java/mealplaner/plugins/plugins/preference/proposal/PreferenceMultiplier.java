// SPDX-License-Identifier: MIT

package mealplaner.plugins.plugins.preference.proposal;

import java.util.Map;

import mealplaner.commons.Pair;
import mealplaner.model.meal.Meal;
import mealplaner.plugins.plugins.preference.mealextension.CookingPreference;
import mealplaner.plugins.plugins.preference.mealextension.CookingPreferenceFact;
import mealplaner.plugins.plugins.preference.setting.PreferenceSettings;

class PreferenceMultiplier {
  private final Map<Pair<CookingPreference, PreferenceSettings>, Integer> preferenceMap;

  public PreferenceMultiplier(
      Map<Pair<CookingPreference, PreferenceSettings>, Integer> preferenceMap) {
    this.preferenceMap = preferenceMap;
  }

  public Pair<Meal, Integer> multiplyPrefs(
      Pair<Meal, Integer> pair, PreferenceSettings preferenceSetting) {
    Pair<CookingPreference, PreferenceSettings> currentSettings = Pair
        .of(pair.left.getTypedMealFact(CookingPreferenceFact.class).getCookingPreference(),
            preferenceSetting);
    return preferenceMap.containsKey(currentSettings)
        ? Pair.of(pair.left, pair.right * preferenceMap.get(currentSettings))
        : pair;
  }
}
