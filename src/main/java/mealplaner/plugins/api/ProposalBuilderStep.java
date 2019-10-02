package mealplaner.plugins.api;

import java.util.stream.Stream;

import mealplaner.commons.Pair;
import mealplaner.model.meal.Meal;
import mealplaner.model.settings.Settings;

public interface ProposalBuilderStep {
  Stream<Pair<Meal, Integer>> applyPluginSuggestions(Stream<Pair<Meal, Integer>> meals, Settings settings);

  default Stream<Pair<Meal, Integer>> applyPluginSuggestionsToDeserts(
      Stream<Pair<Meal, Integer>> meals, Settings settings) {
    return applyPluginSuggestions(meals, settings);
  }

  default Stream<Pair<Meal, Integer>> applyPluginSuggestionsToEntries(
      Stream<Pair<Meal, Integer>> meals, Settings settings) {
    return applyPluginSuggestions(meals, settings);
  }
}
