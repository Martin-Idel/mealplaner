package mealplaner.plugins.api;

import java.util.stream.Stream;

import mealplaner.commons.Pair;
import mealplaner.model.meal.Meal;

public interface ProposalBuilderStep {
  Stream<Meal> applyPluginSuggestions(Stream<Pair<Meal, Integer>> meal);
}
