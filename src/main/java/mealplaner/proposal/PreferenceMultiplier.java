// SPDX-License-Identifier: MIT

package mealplaner.proposal;

import java.util.Map;

import mealplaner.commons.Pair;
import mealplaner.model.meal.Meal;
import mealplaner.model.meal.enums.CookingPreference;
import mealplaner.model.settings.enums.PreferenceSettings;

public class PreferenceMultiplier {
  private final Map<Pair<CookingPreference, PreferenceSettings>, Integer> preferenceMap;

  public PreferenceMultiplier(
      Map<Pair<CookingPreference, PreferenceSettings>, Integer> preferenceMap) {
    this.preferenceMap = preferenceMap;
  }

  public Pair<Meal, Integer> multiplyPrefs(Pair<Meal, Integer> pair,
      PreferenceSettings preferenceSetting) {
    Pair<CookingPreference, PreferenceSettings> currentSettings = Pair
        .of(pair.left.getCookingPreference(), preferenceSetting);
    return preferenceMap.containsKey(currentSettings)
        ? Pair.of(pair.left, pair.right * preferenceMap.get(currentSettings))
        : pair;
  }
}
