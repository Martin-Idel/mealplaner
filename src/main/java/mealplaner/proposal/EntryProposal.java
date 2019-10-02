// SPDX-License-Identifier: MIT

package mealplaner.proposal;

import static java.util.stream.Collectors.toList;
import static mealplaner.model.meal.enums.CourseType.ENTRY;
import static mealplaner.proposal.ProposalFunctions.allows;
import static mealplaner.proposal.ProposalFunctions.useDifferentSidedish;
import static mealplaner.proposal.ProposalFunctions.useDifferentUtensil;

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

class EntryProposal {
  private final Map<UUID, Meal> mealData;
  private final Collection<ProposalBuilderStep> proposalBuilderSteps;

  public EntryProposal(List<Meal> meals, Collection<ProposalBuilderStep> proposalBuilderSteps) {
    this.mealData = new HashMap<>();
    meals.forEach(meal -> mealData.put(meal.getId(), meal));
    this.proposalBuilderSteps = proposalBuilderSteps;
  }

  public Optional<UUID> proposeNextEntry(
      Settings settings, Meal main, List<ProposedMenu> proposalList) {
    var meals = mealData.values().stream()
        .filter(meal -> meal.getCourseType().equals(ENTRY))
        .filter(entry -> allows(entry, settings))
        .filter(entry -> useDifferentSidedish(entry, main))
        .filter(entry -> useDifferentUtensil(entry, main))
        .map(entry -> Pair.of(entry, entry.getDaysPassedAsInteger()))
        .map(entry -> takeProposalIntoAccount(entry, proposalList));
    for (var step : proposalBuilderSteps) {
      meals = step.applyPluginSuggestionsToEntries(meals, settings);
    }
    return meals
        .sorted((pair1, pair2) -> -(pair1.right.compareTo(pair2.right)))
        .map(pair -> pair.left.getId())
        .findFirst();
  }

  private Pair<Meal, Integer> takeProposalIntoAccount(
      Pair<Meal, Integer> pair, List<ProposedMenu> proposalList) {
    List<Meal> proposedMeals = proposalList.stream()
        .filter(menu -> menu.entry.isPresent())
        .filter(menu -> mealData.containsKey(menu.entry.get()))
        .map(menu -> mealData.get(menu.entry.get()))
        .collect(toList());
    return proposedMeals.contains(pair.left)
        ? Pair.of(pair.left, proposalList.size() - proposedMeals.indexOf(pair.left) - 1)
        : pair;
  }
}
