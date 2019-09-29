// SPDX-License-Identifier: MIT

package mealplaner.proposal;

import static mealplaner.Kochplaner.registerPlugins;
import static mealplaner.commons.NonnegativeInteger.FOUR;
import static mealplaner.commons.NonnegativeInteger.ONE;
import static mealplaner.commons.NonnegativeInteger.TWO;
import static mealplaner.commons.NonnegativeInteger.ZERO;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.model.meal.enums.CookingPreference.NO_PREFERENCE;
import static mealplaner.model.meal.enums.CookingPreference.RARE;
import static mealplaner.model.meal.enums.CookingPreference.VERY_POPULAR;
import static mealplaner.model.meal.enums.CourseType.MAIN;
import static mealplaner.model.meal.enums.ObligatoryUtensil.CASSEROLE;
import static mealplaner.model.meal.enums.ObligatoryUtensil.PAN;
import static mealplaner.model.meal.enums.ObligatoryUtensil.POT;
import static mealplaner.model.meal.enums.Sidedish.PASTA;
import static mealplaner.model.meal.enums.Sidedish.POTATOES;
import static mealplaner.model.meal.enums.Sidedish.RICE;
import static mealplaner.model.settings.SettingsBuilder.setting;
import static mealplaner.model.settings.enums.CasseroleSettings.NONE;
import static mealplaner.model.settings.enums.CasseroleSettings.ONLY;
import static mealplaner.model.settings.enums.CasseroleSettings.POSSIBLE;
import static mealplaner.model.settings.enums.CourseSettings.ONLY_MAIN;
import static mealplaner.model.settings.enums.PreferenceSettings.NORMAL;
import static mealplaner.model.settings.enums.PreferenceSettings.RARE_NONE;
import static mealplaner.model.settings.enums.PreferenceSettings.RARE_PREFERED;
import static mealplaner.model.settings.enums.PreferenceSettings.VERY_POPULAR_ONLY;
import static mealplaner.plugins.plugins.cookingtime.CookingTime.LONG;
import static mealplaner.plugins.plugins.cookingtime.CookingTime.MEDIUM;
import static mealplaner.plugins.plugins.cookingtime.CookingTime.SHORT;
import static mealplaner.plugins.plugins.cookingtime.CookingTimeSetting.cookingTimeWithProhibited;
import static mealplaner.plugins.plugins.cookingtime.CookingTimeSetting.defaultCookingTime;
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
import mealplaner.model.proposal.Proposal;
import mealplaner.model.proposal.SideDish;
import mealplaner.model.settings.Settings;
import mealplaner.model.settings.enums.PreferenceSettings;
import mealplaner.plugins.plugins.cookingtime.CookingTimeSetting;

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
    CookingTimeSetting cookingTimeSetting = cookingTimeWithProhibited(SHORT);
    settings[0] = setting()
        .time(cookingTimeSetting)
        .course(ONLY_MAIN)
        .preference(NORMAL)
        .casserole(POSSIBLE)
        .numberOfPeople(FOUR)
        .create();

    var steps = registerPlugins().getRegisteredProposalBuilderSteps();
    proposalBuilder = new ProposalBuilder(meals, PreferenceMap.getPreferenceMap(), sideDish, steps);
    Proposal proposal = proposalBuilder.propose(settings);

    assertEquals(1, proposal.getProposalList().size());
    assertThat(meals.get(1).getId()).isEqualTo(proposal.getItem(0).main);
  }

  @Test
  public void proposeOnlyVeryPopularNoCasserole() throws MealException {
    addMeals();
    CookingTimeSetting cookingTimeSetting = defaultCookingTime();
    settings[0] = setting()
        .time(cookingTimeSetting)
        .course(ONLY_MAIN)
        .preference(VERY_POPULAR_ONLY)
        .casserole(NONE)
        .numberOfPeople(TWO)
        .create();

    var steps = registerPlugins().getRegisteredProposalBuilderSteps();
    proposalBuilder = new ProposalBuilder(meals, PreferenceMap.getPreferenceMap(), sideDish, steps);
    Proposal proposal = proposalBuilder.propose(settings);

    assertThat(meals.get(2).getId()).isEqualTo(proposal.getItem(0).main);
  }

  @Test
  public void proposeOnlyCasseroleRareNone() throws MealException {
    addMeals();
    CookingTimeSetting cookingTimeSetting = defaultCookingTime();
    settings[0] = setting()
        .time(cookingTimeSetting)
        .course(ONLY_MAIN)
        .preference(RARE_NONE)
        .casserole(ONLY)
        .numberOfPeople(TWO)
        .create();

    var steps = registerPlugins().getRegisteredProposalBuilderSteps();
    proposalBuilder = new ProposalBuilder(meals, PreferenceMap.getPreferenceMap(), sideDish, steps);
    Proposal proposal = proposalBuilder.propose(settings);

    assertThat(meals.get(3).getId()).isEqualTo(proposal.getItem(0).main);
  }

  @Test
  public void proposeSideDishMultiplierTwo() throws MealException {
    addMealsToTestMultipliers();
    CookingTimeSetting cookingTimeSetting = defaultCookingTime();
    settings[0] = setting()
        .time(cookingTimeSetting)
        .course(ONLY_MAIN)
        .preference(NORMAL)
        .casserole(POSSIBLE)
        .numberOfPeople(ONE)
        .create();

    proposalBuilder = new ProposalBuilder(meals, PreferenceMap.getPreferenceMap(), sideDish, new ArrayList<>());
    Proposal proposal = proposalBuilder.propose(settings);

    assertThat(meals.get(2).getId()).isEqualTo(proposal.getItem(0).main);
  }

  @Test
  public void proposePreferenceMultiplierRare() throws MealException {
    addMeals();
    CookingTimeSetting cookingTimeSetting = defaultCookingTime();
    settings[0] = setting()
        .time(cookingTimeSetting)
        .course(ONLY_MAIN)
        .preference(RARE_PREFERED)
        .casserole(POSSIBLE)
        .numberOfPeople(TWO)
        .create();

    var steps = registerPlugins().getRegisteredProposalBuilderSteps();
    proposalBuilder = new ProposalBuilder(meals, PreferenceMap.getPreferenceMap(), sideDish, steps);
    Proposal proposal = proposalBuilder.propose(settings);

    assertThat(meals.get(1).getId()).isEqualTo(proposal.getItem(0).main);
  }

  @Test
  public void proposePreferenceMultiplierNormal() throws MealException {
    addMeals();
    CookingTimeSetting cookingTimeSetting = defaultCookingTime();
    settings[0] = setting()
        .time(cookingTimeSetting)
        .course(ONLY_MAIN)
        .preference(NORMAL)
        .casserole(POSSIBLE)
        .numberOfPeople(TWO)
        .create();

    var steps = registerPlugins().getRegisteredProposalBuilderSteps();
    proposalBuilder = new ProposalBuilder(meals, PreferenceMap.getPreferenceMap(), sideDish, steps);
    Proposal proposal = proposalBuilder.propose(settings);

    assertThat(meals.get(4).getId()).isEqualTo(proposal.getItem(0).main);
  }

  @Test
  public void proposePreferenceMultiplierRarePreferredButCookedRecently()
      throws MealException {
    addMealsToTestMultipliers();
    CookingTimeSetting cookingTimeSetting = defaultCookingTime();
    settings[0] = setting()
        .time(cookingTimeSetting)
        .course(ONLY_MAIN)
        .preference(RARE_PREFERED)
        .casserole(POSSIBLE)
        .numberOfPeople(TWO)
        .create();

    var steps = registerPlugins().getRegisteredProposalBuilderSteps();
    proposalBuilder = new ProposalBuilder(meals, PreferenceMap.getPreferenceMap(), sideDish, steps);
    Proposal proposal = proposalBuilder.propose(settings);

    assertThat(meals.get(4).getId()).isEqualTo(proposal.getItem(0).main);
  }

  @Test
  public void multipliersCanBeConfiguredByDifferentHashMap() {
    addMealsToTestMultipliers();
    HashMap<Pair<CookingPreference, PreferenceSettings>, Integer> preferenceMap = new HashMap<>();
    preferenceMap.put(Pair.of(RARE, NORMAL), 10);
    CookingTimeSetting cookingTimeSetting = defaultCookingTime();
    settings[0] = setting()
        .time(cookingTimeSetting)
        .course(ONLY_MAIN)
        .preference(NORMAL)
        .casserole(POSSIBLE)
        .numberOfPeople(TWO)
        .create();

    var steps = registerPlugins().getRegisteredProposalBuilderSteps();
    proposalBuilder = new ProposalBuilder(meals, preferenceMap, sideDish, steps);
    Proposal proposal = proposalBuilder.propose(settings);

    assertThat(meals.get(1).getId()).isEqualTo(proposal.getItem(0).main);
  }

  private void addMeals() throws MealException {
    Meal meal1 = meal()
        .name("Meal1")
        .cookingTime(SHORT)
        .sidedish(PASTA)
        .obligatoryUtensil(PAN)
        .cookingPreference(NO_PREFERENCE)
        .courseType(MAIN)
        .addDaysPassed(nonNegative(50))
        .comment("")
        .create();
    meals.add(meal1);
    Meal meal2 = meal()
        .name("Meal2")
        .cookingTime(MEDIUM)
        .sidedish(PASTA)
        .obligatoryUtensil(CASSEROLE)
        .cookingPreference(RARE)
        .courseType(MAIN)
        .addDaysPassed(nonNegative(101))
        .comment("")
        .create();
    meals.add(meal2);
    Meal meal3 = meal()
        .name("Meal3")
        .cookingTime(LONG)
        .sidedish(RICE)
        .obligatoryUtensil(POT)
        .cookingPreference(VERY_POPULAR)
        .courseType(MAIN)
        .addDaysPassed(nonNegative(20))
        .comment("")
        .create();
    meals.add(meal3);
    Meal meal4 = meal()
        .name("Meal4")
        .cookingTime(MEDIUM)
        .sidedish(POTATOES)
        .obligatoryUtensil(CASSEROLE)
        .cookingPreference(VERY_POPULAR)
        .courseType(MAIN)
        .addDaysPassed(nonNegative(25))
        .comment("")
        .create();
    meals.add(meal4);
    Meal meal5 = meal()
        .name("Meal5")
        .cookingTime(SHORT)
        .sidedish(PASTA)
        .obligatoryUtensil(POT)
        .cookingPreference(NO_PREFERENCE)
        .courseType(MAIN)
        .addDaysPassed(nonNegative(100))
        .comment("")
        .create();
    meals.add(meal5);
  }

  private void addMealsToTestMultipliers() throws MealException {
    Meal meal1 = meal()
        .name("Meal1")
        .cookingTime(SHORT)
        .sidedish(PASTA)
        .obligatoryUtensil(PAN)
        .cookingPreference(NO_PREFERENCE)
        .courseType(MAIN)
        .addDaysPassed(ZERO)
        .create();
    meals.add(meal1);
    Meal meal2 = meal()
        .name("Meal2")
        .cookingTime(MEDIUM)
        .sidedish(PASTA)
        .obligatoryUtensil(CASSEROLE)
        .cookingPreference(RARE)
        .courseType(MAIN)
        .addDaysPassed(nonNegative(10))
        .create();
    meals.add(meal2);
    Meal meal3 = meal()
        .name("Meal3")
        .cookingTime(LONG)
        .sidedish(RICE)
        .obligatoryUtensil(POT)
        .cookingPreference(VERY_POPULAR)
        .courseType(MAIN)
        .addDaysPassed(nonNegative(20))
        .create();
    meals.add(meal3);
    Meal meal4 = meal()
        .name("Meal4")
        .cookingTime(MEDIUM)
        .sidedish(POTATOES)
        .obligatoryUtensil(CASSEROLE)
        .cookingPreference(NO_PREFERENCE)
        .courseType(MAIN)
        .addDaysPassed(nonNegative(30))
        .create();
    meals.add(meal4);
    Meal meal5 = meal()
        .name("Meal5")
        .cookingTime(SHORT)
        .sidedish(PASTA)
        .obligatoryUtensil(POT)
        .cookingPreference(NO_PREFERENCE)
        .courseType(MAIN)
        .addDaysPassed(nonNegative(70))
        .create();
    meals.add(meal5);
  }
}
