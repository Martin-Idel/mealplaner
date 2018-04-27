// SPDX-License-Identifier: MIT

package mealplaner.proposal;

import static java.util.Optional.empty;
import static java.util.UUID.randomUUID;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.meal.Meal.createMeal;
import static mealplaner.model.meal.enums.CookingPreference.RARE;
import static mealplaner.model.settings.Settings.from;
import static mealplaner.model.settings.enums.PreferenceSettings.NORMAL;
import static mealplaner.model.settings.subsettings.CookingTimeSetting.cookingTimeWithProhibited;
import static mealplaner.model.settings.subsettings.CookingTimeSetting.defaultCookingTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import mealplaner.commons.Pair;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.configuration.PreferenceMap;
import mealplaner.model.meal.Meal;
import mealplaner.model.meal.enums.CookingPreference;
import mealplaner.model.meal.enums.CookingTime;
import mealplaner.model.meal.enums.CourseType;
import mealplaner.model.meal.enums.ObligatoryUtensil;
import mealplaner.model.meal.enums.Sidedish;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.proposal.SideDish;
import mealplaner.model.settings.Settings;
import mealplaner.model.settings.enums.CasseroleSettings;
import mealplaner.model.settings.enums.CourseSettings;
import mealplaner.model.settings.enums.PreferenceSettings;
import mealplaner.model.settings.subsettings.CookingTimeSetting;

public class ProposalBuilderTest {
  private List<Meal> meals;
  private ProposalBuilder proposalBuilder;
  private SideDish sideDish;
  private Settings[] settings;

  @Before
  public void setup() {
    meals = new ArrayList<>();
    sideDish = new SideDish();
    settings = new Settings[1];
  }

  @Test
  public void proposeNoShortManyPeopleRestInactive() throws MealException {
    addMeals();
    CookingTimeSetting cookingTimeSetting = cookingTimeWithProhibited(CookingTime.SHORT);
    settings[0] = from(cookingTimeSetting, nonNegative(4),
        CasseroleSettings.POSSIBLE, PreferenceSettings.NORMAL, CourseSettings.ONLY_MAIN);

    proposalBuilder = new ProposalBuilder(meals, PreferenceMap.getPreferenceMap(), sideDish);
    Proposal proposal = proposalBuilder.propose(settings);

    assertEquals(1, proposal.getProposalList().size());
    assertThat(meals.get(1).getId()).isEqualTo(proposal.getItem(0).main);
  }

  @Test
  public void proposeOnlyVeryPopoularNoCasserole() throws MealException {
    addMeals();
    CookingTimeSetting cookingTimeSetting = defaultCookingTime();
    settings[0] = from(cookingTimeSetting, nonNegative(2),
        CasseroleSettings.NONE, PreferenceSettings.VERY_POPULAR_ONLY, CourseSettings.ONLY_MAIN);

    proposalBuilder = new ProposalBuilder(meals, PreferenceMap.getPreferenceMap(), sideDish);
    Proposal proposal = proposalBuilder.propose(settings);

    assertThat(meals.get(2).getId()).isEqualTo(proposal.getItem(0).main);
  }

  @Test
  public void proposeOnlyCasseroleRareNone() throws MealException {
    addMeals();
    CookingTimeSetting cookingTimeSetting = defaultCookingTime();
    settings[0] = from(cookingTimeSetting, nonNegative(2),
        CasseroleSettings.ONLY, PreferenceSettings.RARE_NONE, CourseSettings.ONLY_MAIN);

    proposalBuilder = new ProposalBuilder(meals, PreferenceMap.getPreferenceMap(), sideDish);
    Proposal proposal = proposalBuilder.propose(settings);

    assertThat(meals.get(3).getId()).isEqualTo(proposal.getItem(0).main);
  }

  @Test
  public void proposeSideDishMultiplierTwo() throws MealException {
    addMealsToTestMultipliers();
    CookingTimeSetting cookingTimeSetting = defaultCookingTime();
    settings[0] = from(cookingTimeSetting, nonNegative(1),
        CasseroleSettings.POSSIBLE, PreferenceSettings.NORMAL, CourseSettings.ONLY_MAIN);

    proposalBuilder = new ProposalBuilder(meals, PreferenceMap.getPreferenceMap(), sideDish);
    Proposal proposal = proposalBuilder.propose(settings);

    assertThat(meals.get(2).getId()).isEqualTo(proposal.getItem(0).main);
  }

  @Test
  public void proposePreferenceMultiplierRare() throws MealException {
    addMeals();
    CookingTimeSetting cookingTimeSetting = defaultCookingTime();
    settings[0] = from(cookingTimeSetting, nonNegative(2), CasseroleSettings.POSSIBLE,
        PreferenceSettings.RARE_PREFERED, CourseSettings.ONLY_MAIN);

    proposalBuilder = new ProposalBuilder(meals, PreferenceMap.getPreferenceMap(), sideDish);
    Proposal proposal = proposalBuilder.propose(settings);

    assertThat(meals.get(1).getId()).isEqualTo(proposal.getItem(0).main);
  }

  @Test
  public void proposePreferenceMultiplierNormal() throws MealException {
    addMeals();
    CookingTimeSetting cookingTimeSetting = defaultCookingTime();
    settings[0] = from(cookingTimeSetting, nonNegative(2), CasseroleSettings.POSSIBLE,
        PreferenceSettings.NORMAL, CourseSettings.ONLY_MAIN);

    proposalBuilder = new ProposalBuilder(meals, PreferenceMap.getPreferenceMap(), sideDish);
    Proposal proposal = proposalBuilder.propose(settings);

    assertThat(meals.get(4).getId()).isEqualTo(proposal.getItem(0).main);
  }

  @Test
  public void proposePreferenceMultiplierRarePreferredButCookedRecently()
      throws MealException {
    addMealsToTestMultipliers();
    CookingTimeSetting cookingTimeSetting = defaultCookingTime();
    settings[0] = from(cookingTimeSetting, nonNegative(2), CasseroleSettings.POSSIBLE,
        PreferenceSettings.RARE_PREFERED, CourseSettings.ONLY_MAIN);

    proposalBuilder = new ProposalBuilder(meals, PreferenceMap.getPreferenceMap(), sideDish);
    Proposal proposal = proposalBuilder.propose(settings);

    assertThat(meals.get(4).getId()).isEqualTo(proposal.getItem(0).main);
  }

  @Test
  public void multipliersCanBeConfiguredByDifferentHashMap() {
    addMealsToTestMultipliers();
    HashMap<Pair<CookingPreference, PreferenceSettings>, Integer> preferenceMap = new HashMap<>();
    preferenceMap.put(Pair.of(RARE, NORMAL), 10);
    CookingTimeSetting cookingTimeSetting = defaultCookingTime();
    settings[0] = from(cookingTimeSetting, nonNegative(2), CasseroleSettings.POSSIBLE,
        PreferenceSettings.NORMAL, CourseSettings.ONLY_MAIN);

    proposalBuilder = new ProposalBuilder(meals, preferenceMap, sideDish);
    Proposal proposal = proposalBuilder.propose(settings);

    assertThat(meals.get(1).getId()).isEqualTo(proposal.getItem(0).main);
  }

  private void addMeals() throws MealException {
    Meal meal1 = createMeal(randomUUID(), "Meal1", CookingTime.SHORT, Sidedish.PASTA,
        ObligatoryUtensil.PAN, CookingPreference.NO_PREFERENCE, CourseType.MAIN, nonNegative(50),
        "", empty());
    meals.add(meal1);
    Meal meal2 = createMeal(randomUUID(), "Meal2", CookingTime.MEDIUM, Sidedish.PASTA,
        ObligatoryUtensil.CASSEROLE, CookingPreference.RARE, CourseType.MAIN, nonNegative(101), "",
        empty());
    meals.add(meal2);
    Meal meal3 = createMeal(randomUUID(), "Meal3", CookingTime.LONG, Sidedish.RICE,
        ObligatoryUtensil.POT, CookingPreference.VERY_POPULAR, CourseType.MAIN, nonNegative(20), "",
        empty());
    meals.add(meal3);
    Meal meal4 = createMeal(randomUUID(), "Meal4", CookingTime.MEDIUM, Sidedish.POTATOES,
        ObligatoryUtensil.CASSEROLE, CookingPreference.VERY_POPULAR, CourseType.MAIN,
        nonNegative(25), "", empty());
    meals.add(meal4);
    Meal meal5 = createMeal(randomUUID(), "Meal5", CookingTime.SHORT, Sidedish.PASTA,
        ObligatoryUtensil.POT,
        CookingPreference.NO_PREFERENCE, CourseType.MAIN, nonNegative(100), "", empty());
    meals.add(meal5);
  }

  private void addMealsToTestMultipliers() throws MealException {
    Meal meal1 = createMeal(randomUUID(), "Meal1", CookingTime.SHORT, Sidedish.PASTA,
        ObligatoryUtensil.PAN,
        CookingPreference.NO_PREFERENCE, CourseType.MAIN, nonNegative(0), "", empty());
    meals.add(meal1);
    Meal meal2 = createMeal(randomUUID(), "Meal2", CookingTime.MEDIUM, Sidedish.PASTA,
        ObligatoryUtensil.CASSEROLE,
        CookingPreference.RARE, CourseType.MAIN, nonNegative(10), "", empty());
    meals.add(meal2);
    Meal meal3 = createMeal(randomUUID(), "Meal3", CookingTime.LONG, Sidedish.RICE,
        ObligatoryUtensil.POT,
        CookingPreference.VERY_POPULAR, CourseType.MAIN, nonNegative(20), "", empty());
    meals.add(meal3);
    Meal meal4 = createMeal(randomUUID(), "Meal4", CookingTime.MEDIUM, Sidedish.POTATOES,
        ObligatoryUtensil.CASSEROLE,
        CookingPreference.NO_PREFERENCE, CourseType.MAIN, nonNegative(30), "", empty());
    meals.add(meal4);
    Meal meal5 = createMeal(randomUUID(), "Meal5", CookingTime.SHORT, Sidedish.PASTA,
        ObligatoryUtensil.POT,
        CookingPreference.NO_PREFERENCE, CourseType.MAIN, nonNegative(70), "", empty());
    meals.add(meal5);
  }
}
