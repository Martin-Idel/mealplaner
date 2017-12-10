package mealplaner;

import static java.util.Arrays.asList;
import static mealplaner.model.Proposal.from;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import mealplaner.commons.Pair;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;
import mealplaner.model.SideDish;
import mealplaner.model.configuration.PreferenceMap;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.PreferenceSettings;
import mealplaner.model.settings.Settings;

public class ProposalBuilder {

  private SideDish sideDish;
  private boolean firstDayIsToday;
  private boolean random;

  private final Map<Pair<CookingPreference, PreferenceSettings>, Integer> preferenceMap;
  private final Random randomIntGenerator = new Random();

  private List<Meal> proposalList;

  public ProposalBuilder() {
    this(PreferenceMap.getPreferenceMap(), new SideDish());
  }

  public ProposalBuilder(
      Map<Pair<CookingPreference, PreferenceSettings>, Integer> preferenceMap,
      SideDish sideDish) {
    this.sideDish = sideDish;
    this.preferenceMap = preferenceMap;
    proposalList = new ArrayList<>();
  }

  public ProposalBuilder randomise(boolean randomise) {
    random = randomise;
    return this;
  }

  public ProposalBuilder firstProposal(boolean today) {
    firstDayIsToday = today;
    return this;
  }

  public Proposal propose(List<Meal> meals, Settings[] settings) {
    setCurrentSideDishFromHistory(meals);
    if (!meals.isEmpty()) {
      for (int today = 0; today < settings.length; today++) {
        Optional<Meal> nextMeal = proposeNextMeal(meals, proposalList, settings[today]);
        proposalList.add(nextMeal.orElse(meals.get(0)));
        updateCurrentSidedish(nextMeal.orElse(meals.get(0)));
      }
    }
    return from(firstDayIsToday, proposalList, asList(settings));
  }

  private void setCurrentSideDishFromHistory(List<Meal> meals) {
    sideDish.reset();
    Iterator<Meal> mealList = getLastCookedDishes(meals).iterator();
    if (mealList.hasNext()) {
      sideDish.current = mealList.next().getSidedish();
      while (sideDish.inARow < 3 && mealList.hasNext()
          && mealList.next().getSidedish() == sideDish.current) {
        sideDish.incrementInARow();
      }
    }
  }

  private List<Meal> getLastCookedDishes(List<Meal> meals) {
    return meals.stream()
        .sorted((meal1, meal2) -> meal1.getDaysPassed().compareTo(meal2.getDaysPassed()))
        .limit(3)
        .collect(Collectors.toList());
  }

  private void updateCurrentSidedish(Meal nextProposal) {
    sideDish = (sideDish.current == nextProposal.getSidedish())
        ? sideDish.incrementInARow()
        : sideDish.resetToSideDish(nextProposal.getSidedish());
  }

  public Optional<Meal> proposeNextMeal(
      List<Meal> meals,
      final List<Meal> proposalList,
      final Settings settings) {

    return meals.stream()
        .filter(meal -> allows(meal, settings))
        .map(meal -> Pair.of(meal, meal.getDaysPassedAsInteger()))
        .map(pair -> takeProposalIntoAccount(pair, proposalList))
        .map(pair -> takeSidedishIntoAccount(pair, sideDish))
        .map(pair -> multiplyPrefs(pair, settings.getPreference()))
        .map(pair -> randomize(pair))
        .sorted((pair1, pair2) -> -(pair1.right.compareTo(pair2.right)))
        .map(pair -> pair.left)
        .findFirst();
  }

  private Pair<Meal, Integer> takeProposalIntoAccount(Pair<Meal, Integer> pair,
      List<Meal> proposalList) {
    return proposalList.contains(pair.left)
        ? Pair.of(pair.left, proposalList.size() - proposalList.indexOf(pair.left) - 1)
        : pair;
  }

  private Pair<Meal, Integer> takeSidedishIntoAccount(Pair<Meal, Integer> pair,
      SideDish sideDish) {
    return (pair.left.getSidedish() == sideDish.current)
        ? Pair.of(pair.left, (int) (pair.right * (3f - sideDish.inARow) / 2))
        : pair;
  }

  private Pair<Meal, Integer> randomize(Pair<Meal, Integer> pair) {
    return random ? Pair.of(pair.left, pair.right + randomIntGenerator.nextInt(7)) : pair;
  }

  private Pair<Meal, Integer> multiplyPrefs(Pair<Meal, Integer> pair,
      PreferenceSettings preferenceSetting) {

    Pair<CookingPreference, PreferenceSettings> currentSettings = Pair
        .of(pair.left.getCookingPreference(), preferenceSetting);
    return preferenceMap.containsKey(currentSettings)
        ? Pair.of(pair.left, pair.right * preferenceMap.get(currentSettings))
        : pair;
  }

  private boolean allows(Meal meal, Settings settings) {
    return !(settings.getCookingTime().prohibits(meal)
        || settings.getCookingUtensil().prohibits(meal)
        || settings.getCookingPreference().prohibits(meal));
  }
}