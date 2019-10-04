package mealplaner.plugins.plugins.preference.proposal;

import static mealplaner.plugins.plugins.preference.proposal.CookingPreferenceSettings.createCookingPreferenceSettings;

import java.util.Map;
import java.util.stream.Stream;

import mealplaner.commons.Pair;
import mealplaner.model.meal.Meal;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.api.ProposalBuilderStep;
import mealplaner.plugins.plugins.preference.mealextension.CookingPreference;
import mealplaner.plugins.plugins.preference.setting.CookingPreferenceSetting;
import mealplaner.plugins.plugins.preference.setting.PreferenceSettings;

public class CookingPreferenceProposalStep implements ProposalBuilderStep {
  private final PreferenceMultiplier preferenceMultiplier;

  public CookingPreferenceProposalStep(
      Map<Pair<CookingPreference, PreferenceSettings>, Integer> preferenceMap) {
    this.preferenceMultiplier = new PreferenceMultiplier(preferenceMap);
  }

  @Override
  public Stream<Pair<Meal, Integer>> applyPluginSuggestions(Stream<Pair<Meal, Integer>> meals, Settings settings) {
    var cookingPreferenceSettings = createCookingPreferenceSettings()
        .setCookingPreferences(settings.getTypedSubSetting(CookingPreferenceSetting.class).getPreferences());
    return meals.filter(meal -> !cookingPreferenceSettings.prohibits(meal.left))
        .map(pair -> preferenceMultiplier.multiplyPrefs(
            pair, settings.getTypedSubSetting(CookingPreferenceSetting.class).getPreferences()));
  }

  @Override
  public Stream<Pair<Meal, Integer>> applyPluginSuggestionsToEntries(
      Stream<Pair<Meal, Integer>> meals, Settings settings, Meal main) {
    return meals.map(pair -> preferenceMultiplier.multiplyPrefs(
        pair, settings.getTypedSubSetting(CookingPreferenceSetting.class).getPreferences()));
  }

  @Override
  public Stream<Pair<Meal, Integer>> applyPluginSuggestionsToDeserts(
      Stream<Pair<Meal, Integer>> meals, Settings settings, Meal main) {
    return meals.map(pair -> preferenceMultiplier.multiplyPrefs(
        pair, settings.getTypedSubSetting(CookingPreferenceSetting.class).getPreferences()));
  }
}
