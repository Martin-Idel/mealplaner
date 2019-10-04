// SPDX-License-Identifier: MIT

package mealplaner.proposal;

import static java.util.stream.Collectors.toList;
import static mealplaner.model.meal.enums.CourseType.DESERT;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import mealplaner.commons.Pair;
import mealplaner.model.meal.Meal;
import mealplaner.model.proposal.ProposedMenu;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.api.ProposalBuilderStep;

class DesertProposal {
  private final Map<UUID, Meal> mealData;
  private final Collection<ProposalBuilderStep> proposalBuilderSteps;

  public DesertProposal(List<Meal> meals, Collection<ProposalBuilderStep> proposalBuilderSteps) {
    this.mealData = new HashMap<>();
    meals.forEach(meal -> mealData.put(meal.getId(), meal));
    this.proposalBuilderSteps = proposalBuilderSteps;
  }

  public Optional<UUID> proposeNextDesert(
      Settings settings, Meal main, List<ProposedMenu> proposalList) {
    var meals = mealData.values().stream()
        .filter(meal -> meal.getCourseType().equals(DESERT))
        .map(desert -> Pair.of(desert, desert.getDaysPassedAsInteger()))
        .map(desert -> takeProposalIntoAccount(desert, proposalList));
    for (var step : proposalBuilderSteps) {
      meals = step.applyPluginSuggestionsToDeserts(meals, settings, main);
    }
    return meals
        .sorted((pair1, pair2) -> -(pair1.right.compareTo(pair2.right)))
        .map(pair -> pair.left.getId())
        .findFirst();
  }

  private Pair<Meal, Integer> takeProposalIntoAccount(
      Pair<Meal, Integer> pair, List<ProposedMenu> proposalList) {
    List<Meal> proposedMeals = proposalList.stream()
        .filter(menu -> menu.desert.isPresent())
        .filter(menu -> mealData.containsKey(menu.desert.get()))
        .map(menu -> mealData.get(menu.desert.get()))
        .collect(toList());
    return proposedMeals.contains(pair.left)
        ? Pair.of(pair.left, proposalList.size() - proposedMeals.indexOf(pair.left) - 1)
        : pair;
  }
}
