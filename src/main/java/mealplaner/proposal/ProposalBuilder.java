// SPDX-License-Identifier: MIT

package mealplaner.proposal;

import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;
import static mealplaner.model.proposal.Proposal.from;
import static mealplaner.model.proposal.ProposedMenu.proposed;
import static mealplaner.plugins.builtins.courses.CourseSettings.ONLY_MAIN;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import mealplaner.commons.Pair;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.meal.Meal;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.proposal.ProposedMenu;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.api.ProposalBuilderStep;
import mealplaner.plugins.builtins.courses.CourseTypeSetting;

public class ProposalBuilder {
  private boolean firstDayIsToday;
  private boolean random;

  private final Random randomIntGenerator = new Random();
  private final Map<UUID, Meal> mealData;
  private final EntryProposal entryProposal;
  private final DesertProposal desertProposal;

  private final List<ProposedMenu> proposalList;
  private final Collection<ProposalBuilderStep> proposalBuilderSteps;

  public ProposalBuilder(
      List<Meal> meals,
      Collection<ProposalBuilderStep> proposalBuilderSteps) {
    this.proposalBuilderSteps = proposalBuilderSteps;
    this.mealData = new HashMap<>();
    meals.forEach(meal -> mealData.put(meal.getId(), meal));
    proposalList = new ArrayList<>();
    this.desertProposal = new DesertProposal(meals, proposalBuilderSteps);
    this.entryProposal = new EntryProposal(meals, proposalBuilderSteps);
  }

  public ProposalBuilder randomise(boolean randomise) {
    random = randomise;
    return this;
  }

  public ProposalBuilder firstProposal(boolean today) {
    firstDayIsToday = today;
    return this;
  }

  public Proposal propose(Settings[] settings) { // NOPMD
    for (var step : proposalBuilderSteps) {
      step.setupProposalStep(mealData);
    }
    if (!mealData.isEmpty()) {
      for (int today = 0; today < settings.length; today++) {
        makeNextProposal(settings, today);
      }
    }
    return from(firstDayIsToday, proposalList);
  }

  private void makeNextProposal(Settings[] settings, int today) {
    Optional<UUID> entry = empty();
    UUID main = mealData.entrySet().iterator().next().getKey();
    Meal defaultMeal = mealData.get(main);
    Optional<UUID> desert = empty();
    switch (settings[today]
        .getTypedSubSettingOrDefault(CourseTypeSetting.class, new CourseTypeSetting(ONLY_MAIN))
        .getCourseSetting()) {
      case ONLY_MAIN:
        main = proposeNextMain(proposalList, settings[today]).orElse(defaultMeal).getId();
        break;
      case ENTRY_MAIN:
        main = proposeNextMain(proposalList, settings[today]).orElse(defaultMeal).getId();
        entry = entryProposal.proposeNextEntry(settings[today], mealData.get(main), proposalList);
        break;
      case MAIN_DESERT:
        main = proposeNextMain(proposalList, settings[today]).orElse(defaultMeal).getId();
        desert = desertProposal.proposeNextDesert(settings[today], mealData.get(main),
            proposalList);
        break;
      case THREE_COURSE:
        main = proposeNextMain(proposalList, settings[today]).orElse(defaultMeal).getId();
        entry = entryProposal.proposeNextEntry(settings[today], mealData.get(main), proposalList);
        desert = desertProposal.proposeNextDesert(settings[today], mealData.get(main),
            proposalList);
        break;
      default:
        throw new MealException("Internal Error: This should not happen");
    }
    proposalList.add(proposed(entry, main, desert, settings[today].getNumberOfPeople()));
    for (var step : proposalBuilderSteps) {
      step.incrementProposalStepBetweenMeals(mealData.get(main));
    }
  }

  private Optional<Meal> proposeNextMain(
      final List<ProposedMenu> proposalList, final Settings settings) {
    var proposalStream = mealData.values().stream()
        .map(meal -> Pair.of(meal, meal.getDaysPassedAsInteger()))
        .map(pair -> takeProposalIntoAccount(pair, proposalList));
    for (var proposalStep : proposalBuilderSteps) {
      proposalStream = proposalStep.applyPluginSuggestions(proposalStream, settings);
    }
    return proposalStream
        .map(this::randomize)
        .sorted((pair1, pair2) -> -(pair1.right.compareTo(pair2.right)))
        .map(pair -> pair.left)
        .findFirst();
  }

  private Pair<Meal, Integer> takeProposalIntoAccount(
      Pair<Meal, Integer> pair, List<ProposedMenu> proposalList) {
    List<Meal> proposedMeals = proposalList.stream()
        .filter(menu -> mealData.containsKey(menu.main))
        .map(menu -> mealData.get(menu.main))
        .collect(toList());
    return proposedMeals.contains(pair.left)
        ? Pair.of(pair.left, proposalList.size() - proposedMeals.indexOf(pair.left) - 1)
        : pair;
  }

  private Pair<Meal, Integer> randomize(Pair<Meal, Integer> pair) {
    return random ? Pair.of(pair.left, pair.right + randomIntGenerator.nextInt(7)) : pair;
  }
}