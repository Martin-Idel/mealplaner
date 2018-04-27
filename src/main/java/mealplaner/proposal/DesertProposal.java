// SPDX-License-Identifier: MIT

package mealplaner.proposal;

import static java.util.stream.Collectors.toList;
import static mealplaner.model.meal.enums.CourseType.DESERT;
import static mealplaner.proposal.ProposalFunctions.allows;
import static mealplaner.proposal.ProposalFunctions.useDifferentUtensil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import mealplaner.commons.Pair;
import mealplaner.model.meal.Meal;
import mealplaner.model.proposal.ProposedMenu;
import mealplaner.model.settings.Settings;

public class DesertProposal {
  private final Map<UUID, Meal> mealData;
  private final PreferenceMultiplier preferenceMultiplier;

  public DesertProposal(List<Meal> meals, PreferenceMultiplier preferenceMultiplier) {
    this.mealData = new HashMap<>();
    meals.forEach(meal -> mealData.put(meal.getId(), meal));
    this.preferenceMultiplier = preferenceMultiplier;
  }

  public Optional<UUID> proposeNextDesert(Settings settings, Meal main,
      List<ProposedMenu> proposalList) {
    return mealData.values().stream()
        .filter(meal -> meal.getCourseType().equals(DESERT))
        .filter(desert -> allows(desert, settings))
        .filter(desert -> useDifferentUtensil(desert, main))
        .map(desert -> Pair.of(desert, desert.getDaysPassedAsInteger()))
        .map(desert -> takeProposalIntoAccount(desert, proposalList))
        .map(desert -> preferenceMultiplier.multiplyPrefs(desert, settings.getPreference()))
        .sorted((pair1, pair2) -> -(pair1.right.compareTo(pair2.right)))
        .map(pair -> pair.left.getId())
        .findFirst();
  }

  private Pair<Meal, Integer> takeProposalIntoAccount(Pair<Meal, Integer> pair,
      List<ProposedMenu> proposalList) {
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
