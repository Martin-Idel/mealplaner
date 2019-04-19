// SPDX-License-Identifier: MIT

package mealplaner.proposal;

import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;
import static mealplaner.model.meal.enums.CourseType.MAIN;
import static mealplaner.model.proposal.Proposal.from;
import static mealplaner.model.proposal.ProposedMenu.proposed;
import static mealplaner.proposal.ProposalFunctions.allows;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import mealplaner.commons.Pair;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.configuration.PreferenceMap;
import mealplaner.model.meal.Meal;
import mealplaner.model.meal.enums.CookingPreference;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.proposal.ProposedMenu;
import mealplaner.model.proposal.SideDish;
import mealplaner.model.settings.Settings;
import mealplaner.model.settings.enums.PreferenceSettings;

public class ProposalBuilder {
  private SideDish sideDish;
  private boolean firstDayIsToday;
  private boolean random;

  private final PreferenceMultiplier preferenceMultiplier;
  private final Random randomIntGenerator = new Random();
  private final Map<UUID, Meal> mealData;
  private final EntryProposal entryProposal;
  private final DesertProposal desertProposal;

  private final List<ProposedMenu> proposalList;

  public ProposalBuilder(List<Meal> meals) {
    this(meals, PreferenceMap.getPreferenceMap(), new SideDish());
  }

  public ProposalBuilder(
      List<Meal> meals,
      Map<Pair<CookingPreference, PreferenceSettings>, Integer> preferenceMap,
      SideDish sideDish) {
    this.mealData = new HashMap<>();
    meals.forEach(meal -> mealData.put(meal.getId(), meal));
    this.sideDish = sideDish;
    this.preferenceMultiplier = new PreferenceMultiplier(preferenceMap);
    proposalList = new ArrayList<>();
    this.desertProposal = new DesertProposal(meals, preferenceMultiplier);
    this.entryProposal = new EntryProposal(meals, preferenceMultiplier);
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
    setCurrentSideDishFromHistory();
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
    switch (settings[today].getCourseSettings()) {
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
    updateCurrentSidedish(mealData.get(main));
  }

  private void setCurrentSideDishFromHistory() {
    sideDish.reset();
    Iterator<Meal> mealList = getLastCookedDishes().iterator();
    if (mealList.hasNext()) {
      sideDish.current = mealList.next().getSidedish();
      while (sideDish.inARow < 3 && mealList.hasNext()
          && mealList.next().getSidedish() == sideDish.current) {
        sideDish.incrementInARow();
      }
    }
  }

  private List<Meal> getLastCookedDishes() {
    return mealData.values().stream()
        .sorted(Comparator.comparing(Meal::getDaysPassed))
        .limit(3)
        .collect(Collectors.toList());
  }

  private void updateCurrentSidedish(Meal nextProposal) {
    sideDish = (sideDish.current == nextProposal.getSidedish())
        ? sideDish.incrementInARow()
        : sideDish.resetToSideDish(nextProposal.getSidedish());
  }

  private Optional<Meal> proposeNextMain(final List<ProposedMenu> proposalList,
                                         final Settings settings) {

    return mealData.values().stream()
        .filter(meal -> meal.getCourseType().equals(MAIN))
        .filter(meal -> allows(meal, settings))
        .map(meal -> Pair.of(meal, meal.getDaysPassedAsInteger()))
        .map(pair -> takeProposalIntoAccount(pair, proposalList))
        .map(pair -> takeSidedishIntoAccount(pair, sideDish))
        .map(pair -> preferenceMultiplier.multiplyPrefs(pair, settings.getPreference()))
        .map(this::randomize)
        .sorted((pair1, pair2) -> -(pair1.right.compareTo(pair2.right)))
        .map(pair -> pair.left)
        .findFirst();
  }

  private Pair<Meal, Integer> takeProposalIntoAccount(Pair<Meal, Integer> pair,
      List<ProposedMenu> proposalList) {
    List<Meal> proposedMeals = proposalList.stream()
        .filter(menu -> mealData.containsKey(menu.main))
        .map(menu -> mealData.get(menu.main))
        .collect(toList());
    return proposedMeals.contains(pair.left)
        ? Pair.of(pair.left, proposalList.size() - proposedMeals.indexOf(pair.left) - 1)
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
}