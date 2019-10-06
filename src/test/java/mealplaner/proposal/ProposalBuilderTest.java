// SPDX-License-Identifier: MIT

package mealplaner.proposal;

import static mealplaner.Kochplaner.registerPlugins;
import static mealplaner.commons.NonnegativeInteger.FOUR;
import static mealplaner.commons.NonnegativeInteger.ONE;
import static mealplaner.commons.NonnegativeInteger.TWO;
import static mealplaner.commons.NonnegativeInteger.ZERO;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.model.settings.SettingsBuilder.setting;
import static mealplaner.plugins.builtins.courses.CourseSettings.ONLY_MAIN;
import static mealplaner.plugins.builtins.courses.CourseType.MAIN;
import static mealplaner.plugins.plugins.cookingtime.mealextension.CookingTime.LONG;
import static mealplaner.plugins.plugins.cookingtime.mealextension.CookingTime.MEDIUM;
import static mealplaner.plugins.plugins.cookingtime.mealextension.CookingTime.SHORT;
import static mealplaner.plugins.plugins.cookingtime.settingextension.CookingTimeSetting.cookingTimeWithProhibited;
import static mealplaner.plugins.plugins.cookingtime.settingextension.CookingTimeSetting.defaultCookingTime;
import static mealplaner.plugins.plugins.preference.mealextension.CookingPreference.NO_PREFERENCE;
import static mealplaner.plugins.plugins.preference.mealextension.CookingPreference.RARE;
import static mealplaner.plugins.plugins.preference.mealextension.CookingPreference.VERY_POPULAR;
import static mealplaner.plugins.plugins.preference.settingextension.PreferenceSettings.NORMAL;
import static mealplaner.plugins.plugins.preference.settingextension.PreferenceSettings.RARE_NONE;
import static mealplaner.plugins.plugins.preference.settingextension.PreferenceSettings.RARE_PREFERED;
import static mealplaner.plugins.plugins.preference.settingextension.PreferenceSettings.VERY_POPULAR_ONLY;
import static mealplaner.plugins.plugins.sidedish.mealextension.Sidedish.PASTA;
import static mealplaner.plugins.plugins.sidedish.mealextension.Sidedish.POTATOES;
import static mealplaner.plugins.plugins.sidedish.mealextension.Sidedish.RICE;
import static mealplaner.plugins.plugins.utensil.mealextension.ObligatoryUtensil.CASSEROLE;
import static mealplaner.plugins.plugins.utensil.mealextension.ObligatoryUtensil.PAN;
import static mealplaner.plugins.plugins.utensil.mealextension.ObligatoryUtensil.POT;
import static mealplaner.plugins.plugins.utensil.settingextension.CasseroleSettings.NONE;
import static mealplaner.plugins.plugins.utensil.settingextension.CasseroleSettings.ONLY;
import static mealplaner.plugins.plugins.utensil.settingextension.CasseroleSettings.POSSIBLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import mealplaner.commons.Pair;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.meal.Meal;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.plugins.cookingtime.CookingTimePlugin;
import mealplaner.plugins.plugins.cookingtime.settingextension.CookingTimeSetting;
import mealplaner.plugins.plugins.preference.CookingPreferencePlugin;
import mealplaner.plugins.plugins.preference.mealextension.CookingPreference;
import mealplaner.plugins.plugins.preference.settingextension.CookingPreferenceSetting;
import mealplaner.plugins.plugins.preference.settingextension.PreferenceSettings;
import mealplaner.plugins.plugins.utensil.ObligatoryUtensilPlugin;

public class ProposalBuilderTest {
  private List<Meal> meals;
  private ProposalBuilder proposalBuilder;
  private Settings[] settings;

  @Before
  public void setup() {
    meals = new ArrayList<>();
    settings = new Settings[1];
  }

  @Test
  public void proposeNoShortManyPeopleRestInactive() throws MealException {
    addMeals();
    CookingTimeSetting cookingTimeSetting = cookingTimeWithProhibited(SHORT);
    settings[0] = setting()
        .time(cookingTimeSetting)
        .course(ONLY_MAIN)
        .preference(new CookingPreferenceSetting(NORMAL))
        .casserole(POSSIBLE)
        .numberOfPeople(FOUR)
        .create();

    var steps = registerPlugins().getRegisteredProposalBuilderSteps();
    proposalBuilder = new ProposalBuilder(meals, steps);
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
        .preference(new CookingPreferenceSetting(VERY_POPULAR_ONLY))
        .casserole(NONE)
        .numberOfPeople(TWO)
        .create();

    var steps = registerPlugins().getRegisteredProposalBuilderSteps();
    proposalBuilder = new ProposalBuilder(meals, steps);
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
        .preference(new CookingPreferenceSetting(RARE_NONE))
        .casserole(ONLY)
        .numberOfPeople(TWO)
        .create();

    var steps = registerPlugins().getRegisteredProposalBuilderSteps();
    proposalBuilder = new ProposalBuilder(meals, steps);
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
        .preference(new CookingPreferenceSetting(NORMAL))
        .casserole(POSSIBLE)
        .numberOfPeople(ONE)
        .create();

    var steps = registerPlugins().getRegisteredProposalBuilderSteps();
    proposalBuilder = new ProposalBuilder(meals, steps);
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
        .preference(new CookingPreferenceSetting(RARE_PREFERED))
        .casserole(POSSIBLE)
        .numberOfPeople(TWO)
        .create();

    var steps = registerPlugins().getRegisteredProposalBuilderSteps();
    proposalBuilder = new ProposalBuilder(meals, steps);
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
        .preference(new CookingPreferenceSetting(NORMAL))
        .casserole(POSSIBLE)
        .numberOfPeople(TWO)
        .create();

    var steps = registerPlugins().getRegisteredProposalBuilderSteps();
    proposalBuilder = new ProposalBuilder(meals, steps);
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
        .preference(new CookingPreferenceSetting(RARE_PREFERED))
        .casserole(POSSIBLE)
        .numberOfPeople(TWO)
        .create();

    var steps = registerPlugins().getRegisteredProposalBuilderSteps();
    proposalBuilder = new ProposalBuilder(meals, steps);
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
        .preference(new CookingPreferenceSetting(NORMAL))
        .casserole(POSSIBLE)
        .numberOfPeople(TWO)
        .create();

    PluginStore pluginStore = new PluginStore();
    var cookingTimePlugin = new CookingTimePlugin();
    cookingTimePlugin.registerPlugins(pluginStore);
    var cookingPreferencePlugin = new CookingPreferencePlugin(preferenceMap);
    cookingPreferencePlugin.registerPlugins(pluginStore);
    var utensilPlugin = new ObligatoryUtensilPlugin();
    utensilPlugin.registerPlugins(pluginStore);
    proposalBuilder = new ProposalBuilder(meals, pluginStore.getRegisteredProposalBuilderSteps());
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
