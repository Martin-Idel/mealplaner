package mealplaner.plugins.plugins.cookingtime;

import java.util.stream.Stream;

import mealplaner.commons.Pair;
import mealplaner.model.meal.Meal;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.api.ProposalBuilderStep;

public class CookingTimeProposalStep implements ProposalBuilderStep {
  @Override
  public Stream<Pair<Meal, Integer>> applyPluginSuggestions(Stream<Pair<Meal, Integer>> meals, Settings settings) {
    return meals.filter(meal -> !settings.getTypedSubSetting(CookingTimeSetting.class).prohibits(meal.left));
  }
}
