package mealplaner.model.configuration;

import static mealplaner.model.enums.CookingPreference.NO_PREFERENCE;
import static mealplaner.model.enums.CookingPreference.RARE;
import static mealplaner.model.enums.CookingPreference.VERY_POPULAR;
import static mealplaner.model.enums.PreferenceSettings.NORMAL;
import static mealplaner.model.enums.PreferenceSettings.RARE_NONE;
import static mealplaner.model.enums.PreferenceSettings.RARE_PREFERED;

import java.util.HashMap;
import java.util.Map;

import mealplaner.commons.Pair;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.PreferenceSettings;

public final class PreferenceMap {
  private PreferenceMap() {
  }

  public static Map<Pair<CookingPreference, PreferenceSettings>, Integer> getPreferenceMap() {
    HashMap<Pair<CookingPreference, PreferenceSettings>, Integer> preferenceMap = new HashMap<>();
    preferenceMap.put(Pair.of(VERY_POPULAR, NORMAL), 4);
    preferenceMap.put(Pair.of(VERY_POPULAR, RARE_NONE), 4);
    preferenceMap.put(Pair.of(NO_PREFERENCE, NORMAL), 2);
    preferenceMap.put(Pair.of(NO_PREFERENCE, RARE_NONE), 2);
    preferenceMap.put(Pair.of(RARE, RARE_PREFERED), 2);
    return preferenceMap;
  }
}
