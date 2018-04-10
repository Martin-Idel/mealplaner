package mealplaner;

import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;
import static mealplaner.model.Proposal.from;
import static mealplaner.model.ProposedMenu.proposed;
import static mealplaner.model.enums.CourseType.DESERT;
import static mealplaner.model.enums.CourseType.ENTRY;
import static mealplaner.model.enums.CourseType.MAIN;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import mealplaner.commons.Pair;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;
import mealplaner.model.ProposedMenu;
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

  private List<ProposedMenu> proposalList;

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

  public Proposal propose(Settings[] settings, List<Meal> meals) {
    setCurrentSideDishFromHistory(meals);
    if (!meals.isEmpty()) {
      for (int today = 0; today < settings.length; today++) {
        makeNextProposal(settings, meals, today);
      }
    }
    return from(firstDayIsToday, proposalList);
  }

  private void makeNextProposal(Settings[] settings, List<Meal> meals, int today) {
    Optional<UUID> entry = empty();
    UUID main = meals.get(0).getId();
    Optional<UUID> desert = empty();
    switch (settings[today].getCourseSettings()) {
    case ONLY_MAIN:
      main = proposeNextMain(meals, proposalList, settings[today]).orElse(meals.get(0)).getId();
      break;
    case ENTRY_MAIN:
      entry = proposeNextEntry(meals);
      main = proposeNextMain(meals, proposalList, settings[today]).orElse(meals.get(0)).getId();
      break;
    case MAIN_DESERT:
      main = proposeNextMain(meals, proposalList, settings[today]).orElse(meals.get(0)).getId();
      desert = proposeNextDesert(meals);
      break;
    case THREE_COURSE:
      entry = proposeNextEntry(meals);
      main = proposeNextMain(meals, proposalList, settings[today]).orElse(meals.get(0)).getId();
      desert = proposeNextDesert(meals);
      break;
    default:
      throw new MealException("Internal Error: This should not happen");
    }
    proposalList.add(proposed(entry, main, desert, settings[today].getNumberOfPeople()));
    updateCurrentSidedish(getMeal(main, meals));
  }

  private Optional<UUID> proposeNextEntry(List<Meal> meals) {
    return meals.stream()
        .filter(meal -> meal.getCourseType().equals(ENTRY))
        .findFirst()
        .map(meal -> meal.getId());
  }

  private Optional<UUID> proposeNextDesert(List<Meal> meals) {
    return meals.stream()
        .filter(meal -> meal.getCourseType().equals(DESERT))
        .findFirst()
        .map(meal -> meal.getId());
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

  public Optional<Meal> proposeNextMain(
      List<Meal> meals,
      final List<ProposedMenu> proposalList,
      final Settings settings) {

    return meals.stream()
        .filter(meal -> meal.getCourseType().equals(MAIN))
        .filter(meal -> allows(meal, settings))
        .map(meal -> Pair.of(meal, meal.getDaysPassedAsInteger()))
        .map(pair -> takeProposalIntoAccount(pair, proposalList, meals))
        .map(pair -> takeSidedishIntoAccount(pair, sideDish))
        .map(pair -> multiplyPrefs(pair, settings.getPreference()))
        .map(pair -> randomize(pair))
        .sorted((pair1, pair2) -> -(pair1.right.compareTo(pair2.right)))
        .map(pair -> pair.left)
        .findFirst();
  }

  private Pair<Meal, Integer> takeProposalIntoAccount(Pair<Meal, Integer> pair,
      List<ProposedMenu> proposalList, List<Meal> meals) {
    List<Meal> proposedMeals = proposalList.stream()
        .map(menu -> getMeal(menu.main, meals))
        .collect(toList());
    return proposedMeals.contains(pair.left)
        ? Pair.of(pair.left, proposalList.size() - proposedMeals.indexOf(pair.left) - 1)
        : pair;
  }

  private Meal getMeal(UUID uuid, List<Meal> meals) {
    return meals.stream()
        .filter(meal -> meal.getId().equals(uuid))
        .findFirst()
        .get();
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