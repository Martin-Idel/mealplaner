// SPDX-License-Identifier: MIT

package mealplaner.plugins.courses.proposal;

import static mealplaner.plugins.builtins.courses.CourseType.DESERT;
import static mealplaner.plugins.builtins.courses.CourseType.ENTRY;
import static mealplaner.plugins.builtins.courses.CourseType.MAIN;

import java.util.stream.Stream;

import mealplaner.commons.Pair;
import mealplaner.model.meal.Meal;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.api.ProposalBuilderStep;
import mealplaner.plugins.builtins.courses.CourseTypeFact;

public class CourseTypeProposalStep implements ProposalBuilderStep {
  @Override
  public Stream<Pair<Meal, Integer>> applyPluginSuggestions(
      Stream<Pair<Meal, Integer>> meals, Settings settings) {
    return meals.filter(meal -> meal.left.getTypedMealFact(CourseTypeFact.class).getCourseType() == MAIN);
  }

  @Override
  public Stream<Pair<Meal, Integer>> applyPluginSuggestionsToDeserts(
      Stream<Pair<Meal, Integer>> meals, Settings settings, Meal main) {
    return meals.filter(meal -> meal.left.getTypedMealFact(CourseTypeFact.class).getCourseType() == DESERT);
  }

  @Override
  public Stream<Pair<Meal, Integer>> applyPluginSuggestionsToEntries(
      Stream<Pair<Meal, Integer>> meals, Settings settings, Meal main) {
    return meals.filter(meal -> meal.left.getTypedMealFact(CourseTypeFact.class).getCourseType() == ENTRY);
  }
}
