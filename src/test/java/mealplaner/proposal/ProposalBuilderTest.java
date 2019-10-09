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
import static mealplaner.plugins.plugins.cookingtime.settingextension.CookingTimeSubSetting.cookingTimeWithProhibited;
import static mealplaner.plugins.plugins.cookingtime.settingextension.CookingTimeSubSetting.defaultCookingTime;
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
import mealplaner.plugins.builtins.courses.CourseTypeFact;
import mealplaner.plugins.builtins.courses.CourseTypeSetting;
import mealplaner.plugins.plugins.comment.mealextension.CommentFact;
import mealplaner.plugins.plugins.cookingtime.CookingTimePlugin;
import mealplaner.plugins.plugins.cookingtime.mealextension.CookingTimeFact;
import mealplaner.plugins.plugins.cookingtime.settingextension.CookingTimeSubSetting;
import mealplaner.plugins.plugins.preference.CookingPreferencePlugin;
import mealplaner.plugins.plugins.preference.mealextension.CookingPreference;
import mealplaner.plugins.plugins.preference.mealextension.CookingPreferenceFact;
import mealplaner.plugins.plugins.preference.settingextension.CookingPreferenceSubSetting;
import mealplaner.plugins.plugins.preference.settingextension.PreferenceSettings;
import mealplaner.plugins.plugins.sidedish.mealextension.SidedishFact;
import mealplaner.plugins.plugins.utensil.ObligatoryUtensilPlugin;
import mealplaner.plugins.plugins.utensil.mealextension.ObligatoryUtensilFact;
import mealplaner.plugins.plugins.utensil.settingextension.CasseroleSubSetting;

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
    CookingTimeSubSetting cookingTimeSubSetting = cookingTimeWithProhibited(SHORT);
    settings[0] = setting()
        .addSetting(cookingTimeSubSetting)
        .addSetting(new CasseroleSubSetting(POSSIBLE))
        .addSetting(new CookingPreferenceSubSetting(NORMAL))
        .addSetting(new CourseTypeSetting(ONLY_MAIN))
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
    CookingTimeSubSetting cookingTimeSubSetting = defaultCookingTime();
    settings[0] = setting()
        .addSetting(cookingTimeSubSetting)
        .addSetting(new CasseroleSubSetting(NONE))
        .addSetting(new CookingPreferenceSubSetting(VERY_POPULAR_ONLY))
        .addSetting(new CourseTypeSetting(ONLY_MAIN))
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
    CookingTimeSubSetting cookingTimeSubSetting = defaultCookingTime();
    settings[0] = setting()
        .addSetting(cookingTimeSubSetting)
        .addSetting(new CasseroleSubSetting(ONLY))
        .addSetting(new CookingPreferenceSubSetting(RARE_NONE))
        .addSetting(new CourseTypeSetting(ONLY_MAIN))
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
    CookingTimeSubSetting cookingTimeSubSetting = defaultCookingTime();
    settings[0] = setting()
        .addSetting(cookingTimeSubSetting)
        .addSetting(new CasseroleSubSetting(POSSIBLE))
        .addSetting(new CookingPreferenceSubSetting(NORMAL))
        .addSetting(new CourseTypeSetting(ONLY_MAIN))
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
    CookingTimeSubSetting cookingTimeSubSetting = defaultCookingTime();
    settings[0] = setting()
        .addSetting(cookingTimeSubSetting)
        .addSetting(new CasseroleSubSetting(POSSIBLE))
        .addSetting(new CookingPreferenceSubSetting(RARE_PREFERED))
        .addSetting(new CourseTypeSetting(ONLY_MAIN))
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
    CookingTimeSubSetting cookingTimeSubSetting = defaultCookingTime();
    settings[0] = setting()
        .addSetting(cookingTimeSubSetting)
        .addSetting(new CasseroleSubSetting(POSSIBLE))
        .addSetting(new CookingPreferenceSubSetting(NORMAL))
        .addSetting(new CourseTypeSetting(ONLY_MAIN))
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
    CookingTimeSubSetting cookingTimeSubSetting = defaultCookingTime();
    settings[0] = setting()
        .addSetting(cookingTimeSubSetting)
        .addSetting(new CasseroleSubSetting(POSSIBLE))
        .addSetting(new CookingPreferenceSubSetting(RARE_PREFERED))
        .addSetting(new CourseTypeSetting(ONLY_MAIN))
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
    CookingTimeSubSetting cookingTimeSubSetting = defaultCookingTime();
    settings[0] = setting()
        .addSetting(cookingTimeSubSetting)
        .addSetting(new CasseroleSubSetting(POSSIBLE))
        .addSetting(new CookingPreferenceSubSetting(NORMAL))
        .addSetting(new CourseTypeSetting(ONLY_MAIN))
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
        .addFact(new CookingTimeFact(SHORT))
        .addFact(new SidedishFact(PASTA))
        .addFact(new ObligatoryUtensilFact(PAN))
        .addFact(new CookingPreferenceFact(NO_PREFERENCE))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new CommentFact(""))
        .addDaysPassed(nonNegative(50))
        .create();
    meals.add(meal1);
    Meal meal2 = meal()
        .name("Meal2")
        .addFact(new CookingTimeFact(MEDIUM))
        .addFact(new SidedishFact(PASTA))
        .addFact(new ObligatoryUtensilFact(CASSEROLE))
        .addFact(new CookingPreferenceFact(RARE))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new CommentFact(""))
        .addDaysPassed(nonNegative(101))
        .create();
    meals.add(meal2);
    Meal meal3 = meal()
        .name("Meal3")
        .addFact(new CookingTimeFact(LONG))
        .addFact(new SidedishFact(RICE))
        .addFact(new ObligatoryUtensilFact(POT))
        .addFact(new CookingPreferenceFact(VERY_POPULAR))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new CommentFact(""))
        .addDaysPassed(nonNegative(20))
        .create();
    meals.add(meal3);
    Meal meal4 = meal()
        .name("Meal4")
        .addFact(new CookingTimeFact(MEDIUM))
        .addFact(new SidedishFact(POTATOES))
        .addFact(new ObligatoryUtensilFact(CASSEROLE))
        .addFact(new CookingPreferenceFact(VERY_POPULAR))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new CommentFact(""))
        .addDaysPassed(nonNegative(25))
        .create();
    meals.add(meal4);
    Meal meal5 = meal()
        .name("Meal5")
        .addFact(new CookingTimeFact(SHORT))
        .addFact(new SidedishFact(PASTA))
        .addFact(new ObligatoryUtensilFact(POT))
        .addFact(new CookingPreferenceFact(NO_PREFERENCE))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new CommentFact(""))
        .addDaysPassed(nonNegative(100))
        .create();
    meals.add(meal5);
  }

  private void addMealsToTestMultipliers() throws MealException {
    Meal meal1 = meal()
        .name("Meal1")
        .addFact(new CookingTimeFact(SHORT))
        .addFact(new SidedishFact(PASTA))
        .addFact(new ObligatoryUtensilFact(PAN))
        .addFact(new CookingPreferenceFact(NO_PREFERENCE))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new CommentFact(""))
        .addDaysPassed(ZERO)
        .create();
    meals.add(meal1);
    Meal meal2 = meal()
        .name("Meal2")
        .addFact(new CookingTimeFact(MEDIUM))
        .addFact(new SidedishFact(PASTA))
        .addFact(new ObligatoryUtensilFact(CASSEROLE))
        .addFact(new CookingPreferenceFact(RARE))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new CommentFact(""))
        .addDaysPassed(nonNegative(10))
        .create();
    meals.add(meal2);
    Meal meal3 = meal()
        .name("Meal3")
        .addFact(new CookingTimeFact(LONG))
        .addFact(new SidedishFact(RICE))
        .addFact(new ObligatoryUtensilFact(POT))
        .addFact(new CookingPreferenceFact(VERY_POPULAR))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new CommentFact(""))
        .addDaysPassed(nonNegative(20))
        .create();
    meals.add(meal3);
    Meal meal4 = meal()
        .name("Meal4")
        .addFact(new CookingTimeFact(MEDIUM))
        .addFact(new SidedishFact(POTATOES))
        .addFact(new ObligatoryUtensilFact(CASSEROLE))
        .addFact(new CookingPreferenceFact(NO_PREFERENCE))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new CommentFact(""))
        .addDaysPassed(nonNegative(30))
        .create();
    meals.add(meal4);
    Meal meal5 = meal()
        .name("Meal5")
        .addFact(new CookingTimeFact(SHORT))
        .addFact(new SidedishFact(PASTA))
        .addFact(new ObligatoryUtensilFact(POT))
        .addFact(new CookingPreferenceFact(NO_PREFERENCE))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new CommentFact(""))
        .addDaysPassed(nonNegative(70))
        .create();
    meals.add(meal5);
  }
}
