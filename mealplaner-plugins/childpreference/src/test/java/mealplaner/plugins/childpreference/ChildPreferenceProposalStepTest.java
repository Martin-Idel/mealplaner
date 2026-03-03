// SPDX-License-Identifier: MIT

package mealplaner.plugins.childpreference;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static mealplaner.commons.NonnegativeInteger.TWO;
import static mealplaner.commons.NonnegativeInteger.ZERO;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.model.settings.SettingsBuilder.setting;
import static mealplaner.plugins.builtins.courses.CourseType.MAIN;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import mealplaner.model.proposal.Proposal;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.builtins.courses.CourseTypeFact;
import mealplaner.plugins.childpreference.mealextension.ChildPreferenceFact;
import mealplaner.plugins.childpreference.proposal.ChildPreferenceProposalStep;
import mealplaner.plugins.childpreference.settingextension.ChildPreferenceSubSetting;
import mealplaner.proposal.ProposalBuilder;
import testcommons.PluginsUtils;

class ChildPreferenceProposalStepTest {
  @Test
  void applyPluginSuggestionsFiltersOutNonChildFriendlyMealsWhenFilterIsEnabled() {
    PluginsUtils.setupMessageBundles(new ChildPreferencePlugin());
    var meal1 = meal()
        .name("Test1")
        .daysPassed(nonNegative(100))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new ChildPreferenceFact(true))
        .create();
    var meal2 = meal()
        .name("Test2")
        .daysPassed(ZERO)
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new ChildPreferenceFact(false))
        .create();
    var settings = new Settings[] {setting()
        .numberOfPeople(TWO)
        .addSetting(new ChildPreferenceSubSetting(true))
        .create()};

    var sut = new ProposalBuilder(asList(meal1, meal2), singletonList(new ChildPreferenceProposalStep()));

    Proposal proposal = sut.propose(settings);

    assertThat(proposal.getProposalList()).hasSize(1);
  }

  @Test
  void applyPluginSuggestionsFiltersCorrectMealWhenFilterIsEnabled() {
    PluginsUtils.setupMessageBundles(new ChildPreferencePlugin());
    var meal1 = meal()
        .name("Test1")
        .daysPassed(nonNegative(100))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new ChildPreferenceFact(true))
        .create();
    var meal2 = meal()
        .name("Test2")
        .daysPassed(ZERO)
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new ChildPreferenceFact(false))
        .create();
    var settings = new Settings[] {setting()
        .numberOfPeople(TWO)
        .addSetting(new ChildPreferenceSubSetting(true))
        .create()};

    var sut = new ProposalBuilder(asList(meal1, meal2), singletonList(new ChildPreferenceProposalStep()));

    Proposal proposal = sut.propose(settings);

    assertThat(proposal.getProposalList().get(0).main).isEqualTo(meal1.getId());
  }

  @Test
  void applyPluginSuggestionsAllowsAllMealsWhenFilterIsDisabled() {
    PluginsUtils.setupMessageBundles(new ChildPreferencePlugin());
    var meal1 = meal()
        .name("Test1")
        .daysPassed(nonNegative(100))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new ChildPreferenceFact(true))
        .create();
    var meal2 = meal()
        .name("Test2")
        .daysPassed(ZERO)
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new ChildPreferenceFact(false))
        .create();
    var settings = new Settings[] {setting()
        .numberOfPeople(TWO)
        .addSetting(new ChildPreferenceSubSetting(false))
        .create()};

    var sut = new ProposalBuilder(asList(meal1, meal2), singletonList(new ChildPreferenceProposalStep()));

    Proposal proposal = sut.propose(settings);

    assertThat(proposal.getProposalList()).hasSize(1);
  }
}