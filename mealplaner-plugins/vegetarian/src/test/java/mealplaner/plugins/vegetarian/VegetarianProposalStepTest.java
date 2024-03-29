// SPDX-License-Identifier: MIT

package mealplaner.plugins.vegetarian;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static mealplaner.commons.NonnegativeInteger.TWO;
import static mealplaner.commons.NonnegativeInteger.ZERO;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.model.settings.SettingsBuilder.setting;
import static mealplaner.plugins.builtins.courses.CourseType.MAIN;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mealplaner.model.proposal.Proposal;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.builtins.courses.CourseTypeFact;
import mealplaner.plugins.vegetarian.mealextension.VegetarianFact;
import mealplaner.proposal.ProposalBuilder;
import testcommons.PluginsUtils;

class VegetarianProposalStepTest {
  @BeforeEach
  void setUp() {
    PluginsUtils.setupMessageBundles(new VegetarianPlugin());
  }

  @Test
  void applyPluginSuggestionsDoesNotEasilyProposeTwoMeatMealsInARow() {
    var meal1 = meal()
        .name("Test1")
        .daysPassed(TWO)
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new VegetarianFact(true))
        .create();
    var meal2 = meal()
        .name("Test2")
        .daysPassed(ZERO)
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new VegetarianFact(true))
        .create();
    var meal3 = meal()
        .name("Test3")
        .daysPassed(nonNegative(10))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new VegetarianFact(false))
        .create();
    var meal4 = meal()
        .name("Test4")
        .daysPassed(nonNegative(9))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new VegetarianFact(false))
        .create();
    var settings = new Settings[]{
        setting().numberOfPeople(TWO).create(),
        setting().numberOfPeople(TWO).create()};

    var sut = new ProposalBuilder(asList(meal1, meal2, meal3, meal4),
        singletonList(new mealplaner.plugins.vegetarian.proposal.VegetarianProposalStep()));

    Proposal proposal = sut.propose(settings);

    assertThat(proposal.getProposalList()).hasSize(2);
    assertThat(proposal.getProposalList().get(0).main).isEqualTo(meal3.getId());
    assertThat(proposal.getProposalList().get(1).main).isEqualTo(meal1.getId());
  }

  @Test
  void applyPluginSuggestionsTakesHistoryIntoAccount() {
    var meal1 = meal()
        .name("Test1")
        .daysPassed(ZERO)
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new VegetarianFact(false))
        .create();
    var meal2 = meal()
        .name("Test2")
        .daysPassed(TWO)
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new VegetarianFact(true))
        .create();
    var meal3 = meal()
        .name("Test3")
        .daysPassed(nonNegative(100))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new VegetarianFact(false))
        .create();
    var settings = new Settings[]{setting().numberOfPeople(TWO).create()};

    var sut = new ProposalBuilder(asList(meal1, meal2, meal3),
        singletonList(new mealplaner.plugins.vegetarian.proposal.VegetarianProposalStep()));

    Proposal proposal = sut.propose(settings);

    assertThat(proposal.getProposalList()).hasSize(1);
    assertThat(proposal.getProposalList().get(0).main).isEqualTo(meal2.getId());
  }
}
