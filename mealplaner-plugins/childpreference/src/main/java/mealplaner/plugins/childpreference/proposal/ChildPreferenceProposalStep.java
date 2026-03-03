// SPDX-License-Identifier: MIT

package mealplaner.plugins.childpreference.proposal;

import java.util.stream.Stream;

import mealplaner.commons.Pair;
import mealplaner.model.meal.Meal;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.api.ProposalBuilderStep;
import mealplaner.plugins.childpreference.mealextension.ChildPreferenceFact;
import mealplaner.plugins.childpreference.settingextension.ChildPreferenceSubSetting;

public class ChildPreferenceProposalStep implements ProposalBuilderStep {
  @Override
  public Stream<Pair<Meal, Integer>> applyPluginSuggestions(Stream<Pair<Meal, Integer>> meals, Settings settings) {
    boolean onlyChildFriendly = settings.getTypedSubSetting(ChildPreferenceSubSetting.class).isOnlyChildFriendly();
    if (!onlyChildFriendly) {
      return meals;
    }
    return meals.filter(meal -> meal.left.getTypedMealFact(ChildPreferenceFact.class).isChildFriendly());
  }
}