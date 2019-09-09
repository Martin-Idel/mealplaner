package mealplaner.plugins.api;

import java.util.stream.Stream;

import mealplaner.model.meal.Meal;

public interface ProposalBuilderStep {
  Stream<Meal> applyPluginSuggestions(Stream<Meal> meal);
}
