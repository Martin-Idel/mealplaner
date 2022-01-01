// SPDX-License-Identifier: MIT

package mealplaner.proposal;

import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;
import static mealplaner.model.proposal.Proposal.from;
import static mealplaner.model.proposal.ProposedMenu.proposed;
import static mealplaner.plugins.builtins.courses.CourseSettings.ONLY_MAIN;
import static mealplaner.plugins.builtins.courses.CourseType.DESERT;
import static mealplaner.plugins.builtins.courses.CourseType.ENTRY;
import static mealplaner.plugins.builtins.courses.CourseType.MAIN;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import mealplaner.api.ProposalBuilderInterface;
import mealplaner.commons.Pair;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.meal.Meal;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.proposal.ProposedMenu;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.api.ProposalBuilderStep;
import mealplaner.plugins.builtins.courses.CourseTypeFact;
import mealplaner.plugins.builtins.courses.CourseTypeSetting;

public class ProposalBuilder implements ProposalBuilderInterface {
  private boolean firstDayIsToday = false;
  private boolean random = false;

  private final Random randomIntGenerator = new Random();
  private final Map<UUID, Meal> mealDataMain = new HashMap<>();
  private final Map<UUID, Meal> mealDataEntry = new HashMap<>();
  private final Map<UUID, Meal> mealDataDesert = new HashMap<>();

  private final List<ProposedMenu> proposalList;
  private final Collection<ProposalBuilderStep> proposalBuilderSteps;

  public ProposalBuilder(
      List<Meal> meals,
      Collection<ProposalBuilderStep> proposalBuilderSteps) {
    this.proposalBuilderSteps = new ArrayList<>();
    this.proposalBuilderSteps.addAll(proposalBuilderSteps);
    meals.stream()
        .filter(meal -> meal.getTypedMealFact(CourseTypeFact.class).getCourseType().equals(ENTRY))
        .forEach(meal -> mealDataEntry.put(meal.getId(), meal));
    meals.stream()
        .filter(meal -> meal.getTypedMealFact(CourseTypeFact.class).getCourseType().equals(MAIN))
        .forEach(meal -> mealDataMain.put(meal.getId(), meal));
    meals.stream()
        .filter(meal -> meal.getTypedMealFact(CourseTypeFact.class).getCourseType().equals(DESERT))
        .forEach(meal -> mealDataDesert.put(meal.getId(), meal));
    proposalList = new ArrayList<>();
  }

  @Override
  public ProposalBuilder randomise(boolean randomise) {
    random = randomise;
    return this;
  }

  @Override
  public ProposalBuilder firstProposal(boolean today) {
    firstDayIsToday = today;
    return this;
  }

  @Override
  public Proposal propose(Settings... settings) { // NOPMD
    for (var step : proposalBuilderSteps) {
      step.setupProposalStep(mealDataMain);
    }
    if (!mealDataMain.isEmpty()) {
      for (int today = 0; today < settings.length; today++) {
        makeNextProposal(settings, today);
      }
    }
    return from(firstDayIsToday, proposalList);
  }

  private void makeNextProposal(Settings[] settings, int today) {
    Optional<UUID> entry = empty();
    UUID main = mealDataMain.entrySet()
        .stream()
        .findFirst()
        .map(Map.Entry::getKey)
        .orElseThrow();
    Optional<UUID> desert = empty();
    switch (settings[today]
        .getTypedSubSettingOrDefault(CourseTypeSetting.class, new CourseTypeSetting(ONLY_MAIN))
        .getCourseSetting()) {
      case ONLY_MAIN:
        main = proposeNextMain(proposalList, settings[today]).orElse(main);
        break;
      case ENTRY_MAIN:
        main = proposeNextMain(proposalList, settings[today]).orElse(main);
        entry = proposeNextEntry(settings[today], mealDataMain.get(main), proposalList, mealDataEntry);
        break;
      case MAIN_DESERT:
        main = proposeNextMain(proposalList, settings[today]).orElse(main);
        desert = proposeNextDesert(settings[today], mealDataMain.get(main), proposalList, mealDataDesert);
        break;
      case THREE_COURSE:
        main = proposeNextMain(proposalList, settings[today]).orElse(main);
        entry = proposeNextEntry(settings[today], mealDataMain.get(main), proposalList, mealDataEntry);
        desert = proposeNextDesert(settings[today], mealDataMain.get(main), proposalList, mealDataDesert);
        break;
      default:
        throw new MealException("Internal Error: This should not happen");
    }
    proposalList.add(proposed(entry, main, desert, settings[today].getNumberOfPeople()));
    for (var step : proposalBuilderSteps) {
      step.incrementProposalStepBetweenMeals(mealDataMain.get(main));
    }
  }

  private Optional<UUID> proposeNextMain(
      final List<ProposedMenu> proposalList, final Settings settings) {
    var proposalStream = mealDataMain.values().stream()
        .map(meal -> Pair.of(meal, meal.getDaysPassedAsInteger()))
        .map(pair -> takeProposalIntoAccount(
            pair, proposalList, menu -> true, menu -> menu.main, mealDataMain));
    for (var proposalStep : proposalBuilderSteps) {
      proposalStream = proposalStep.applyPluginSuggestions(proposalStream, settings);
    }
    return pickMeal(proposalStream);
  }

  private Optional<UUID> proposeNextDesert(
      Settings settings, Meal main, List<ProposedMenu> proposalList, Map<UUID, Meal> mealData) {
    var meals = mealData.values().stream()
        .map(desert -> Pair.of(desert, desert.getDaysPassedAsInteger()))
        .map(desert -> takeProposalIntoAccount(
            desert, proposalList, menu -> menu.desert.isPresent(), menu -> menu.desert.get(), mealDataDesert));
    for (var step : proposalBuilderSteps) {
      meals = step.applyPluginSuggestionsToDeserts(meals, settings, main);
    }
    return pickMeal(meals);
  }

  private Optional<UUID> proposeNextEntry(
      Settings settings, Meal main, List<ProposedMenu> proposalList, Map<UUID, Meal> mealData) {
    var meals = mealData.values().stream()
        .map(entry -> Pair.of(entry, entry.getDaysPassedAsInteger()))
        .map(entry -> takeProposalIntoAccount(
            entry, proposalList, menu -> menu.entry.isPresent(), menu -> menu.entry.get(), mealDataEntry));
    for (var step : proposalBuilderSteps) {
      meals = step.applyPluginSuggestionsToEntries(meals, settings, main);
    }
    return pickMeal(meals);
  }

  private Optional<UUID> pickMeal(Stream<Pair<Meal, Integer>> meals) {
    return meals
        .map(this::randomize)
        .sorted((pair1, pair2) -> pair2.right.compareTo(pair1.right))
        .map(pair -> pair.left.getId())
        .findFirst();
  }

  private Pair<Meal, Integer> randomize(Pair<Meal, Integer> pair) {
    return random ? Pair.of(pair.left, pair.right + randomIntGenerator.nextInt(100)) : pair;
  }

  private Pair<Meal, Integer> takeProposalIntoAccount(
      Pair<Meal, Integer> pair,
      List<ProposedMenu> proposalList,
      Predicate<ProposedMenu> presentCheck,
      Function<ProposedMenu, UUID> menuGetter,
      Map<UUID, Meal> mealData) {
    List<Meal> proposedMeals = proposalList.stream()
        .filter(presentCheck)
        .filter(menu -> mealData.containsKey(menuGetter.apply(menu)))
        .map(menu -> mealData.get(menuGetter.apply(menu)))
        .collect(toList());
    return proposedMeals.contains(pair.left)
        ? Pair.of(pair.left, proposalList.size() - proposedMeals.indexOf(pair.left) - 1)
        : Pair.of(pair.left, pair.right + proposalList.size());
  }
}
