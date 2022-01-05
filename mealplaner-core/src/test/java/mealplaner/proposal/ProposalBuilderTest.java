// SPDX-License-Identifier: MIT

package mealplaner.proposal;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static mealplaner.commons.NonnegativeInteger.FIVE;
import static mealplaner.commons.NonnegativeInteger.ONE;
import static mealplaner.commons.NonnegativeInteger.TWO;
import static mealplaner.commons.NonnegativeInteger.ZERO;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.model.settings.SettingsBuilder.setting;
import static mealplaner.plugins.builtins.courses.CourseSettings.ENTRY_MAIN;
import static mealplaner.plugins.builtins.courses.CourseSettings.ONLY_MAIN;
import static mealplaner.plugins.builtins.courses.CourseSettings.THREE_COURSE;
import static mealplaner.plugins.builtins.courses.CourseType.DESERT;
import static mealplaner.plugins.builtins.courses.CourseType.ENTRY;
import static mealplaner.plugins.builtins.courses.CourseType.MAIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import mealplaner.commons.Pair;
import mealplaner.model.meal.Meal;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.api.ProposalBuilderStep;
import mealplaner.plugins.builtins.courses.CourseTypeFact;
import mealplaner.plugins.builtins.courses.CourseTypeSetting;

class ProposalBuilderTest {

  @Test
  void proposalBuilderProposesMealsAccordingToLastCooked() {
    var meal1 = meal()
        .name("Test1")
        .daysPassed(nonNegative(10))
        .addFact(new CourseTypeFact(MAIN))
        .create();
    var meal2 = meal()
        .name("Test2")
        .daysPassed(ONE)
        .addFact(new CourseTypeFact(MAIN))
        .create();
    var meal3 = meal()
        .name("Test3")
        .daysPassed(FIVE)
        .addFact(new CourseTypeFact(MAIN))
        .create();
    var settings = new Settings[]{trivialSetting(), trivialSetting(), trivialSetting()};

    var sut = new ProposalBuilder(
        asList(meal1, meal2, meal3),
        new ArrayList<>());

    Proposal proposal = sut.propose(settings);

    assertThat(proposal.getProposalList()).hasSize(3);
    assertThat(proposal.getProposalList().get(0).main).isEqualTo(meal1.getId());
    assertThat(proposal.getProposalList().get(1).main).isEqualTo(meal3.getId());
    assertThat(proposal.getProposalList().get(2).main).isEqualTo(meal2.getId());
  }

  @Test
  void proposalBuilderCorrectlyTakesProposalIntoAccount() {
    var meal1 = meal()
        .name("Test1")
        .daysPassed(nonNegative(10))
        .addFact(new CourseTypeFact(MAIN))
        .create();
    var meal2 = meal()
        .name("Test2")
        .daysPassed(ZERO)
        .addFact(new CourseTypeFact(MAIN))
        .create();
    var settings = new Settings[]{trivialSetting(), trivialSetting()};

    var sut = new ProposalBuilder(
        asList(meal1, meal2),
        new ArrayList<>());

    Proposal proposal = sut.propose(settings);

    assertThat(proposal.getProposalList()).hasSize(2);
    assertThat(proposal.getProposalList().get(0).main).isEqualTo(meal1.getId());
    assertThat(proposal.getProposalList().get(1).main).isEqualTo(meal2.getId());
  }

  @Test
  void proposalBuilderUsesTodayIfTodayElseTomorrow() {
    var meal1 = meal()
        .name("Test1")
        .daysPassed(nonNegative(10))
        .addFact(new CourseTypeFact(MAIN))
        .create();
    var meal2 = meal()
        .name("Test2")
        .daysPassed(ZERO)
        .addFact(new CourseTypeFact(MAIN))
        .create();
    var settings = new Settings[]{trivialSetting(), trivialSetting()};

    var sut = new ProposalBuilder(asList(meal1, meal2), new ArrayList<>())
        .firstProposal(true);

    Proposal proposal = sut.propose(settings);

    assertThat(proposal.getProposalList()).hasSize(2);
    assertThat(proposal.getDateOfFirstProposedItem()).isToday();

    sut = new ProposalBuilder(asList(meal1, meal2), new ArrayList<>())
        .firstProposal(false);

    proposal = sut.propose(settings);

    assertThat(proposal.getProposalList()).hasSize(2);
    assertThat(proposal.getDateOfFirstProposedItem()).isAfter(LocalDate.now());
  }

  @Test
  void proposalBuilderProposesMainAndEntryIfSettingIsAccordingly() {
    var meal1 = meal()
        .name("Test1")
        .daysPassed(nonNegative(10))
        .addFact(new CourseTypeFact(MAIN))
        .create();
    var meal2 = meal()
        .name("Test2")
        .daysPassed(ZERO)
        .addFact(new CourseTypeFact(MAIN))
        .create();
    var meal3 = meal()
        .name("Test3")
        .daysPassed(TWO)
        .addFact(new CourseTypeFact(ENTRY))
        .create();
    var settings = new Settings[]{
        setting()
            .numberOfPeople(TWO)
            .addSetting(new CourseTypeSetting(ENTRY_MAIN))
            .create()};

    var sut = new ProposalBuilder(asList(meal1, meal2, meal3), new ArrayList<>());

    Proposal proposal = sut.propose(settings);

    assertThat(proposal.getProposalList()).hasSize(1);
    assertThat(proposal.getProposalList().get(0).main).isEqualByComparingTo(meal1.getId());
    assertThat(proposal.getProposalList().get(0).entry).contains(meal3.getId());
    assertThat(proposal.getProposalList().get(0).desert).isEmpty();
  }


  @Test
  void proposalBuilderProposesNothingIfNoMainInList() {
    var meal1 = meal()
        .name("Test1")
        .daysPassed(nonNegative(10))
        .addFact(new CourseTypeFact(DESERT))
        .create();
    var settings = new Settings[]{
        setting()
            .numberOfPeople(TWO)
            .addSetting(new CourseTypeSetting(ONLY_MAIN))
            .create()};

    var sut = new ProposalBuilder(singletonList(meal1), new ArrayList<>());

    Proposal proposal = sut.propose(settings);

    assertThat(proposal.getProposalList()).hasSize(0);
  }

  @Test
  @SuppressWarnings("unchecked")
  void proposalBuilderCorrectlyCallsProposalSteps() {
    ProposalBuilderStep step = Mockito.mock(ProposalBuilderStep.class);
    when(step.applyPluginSuggestions(any(), any()))
        .thenAnswer((Answer<Stream<Pair<Meal, Integer>>>) invocation
            -> (Stream<Pair<Meal, Integer>>) invocation.getArguments()[0]);
    when(step.applyPluginSuggestionsToEntries(any(), any(), any()))
        .thenAnswer((Answer<Stream<Pair<Meal, Integer>>>) invocation
            -> (Stream<Pair<Meal, Integer>>) invocation.getArguments()[0]);
    when(step.applyPluginSuggestionsToDeserts(any(), any(), any()))
        .thenAnswer((Answer<Stream<Pair<Meal, Integer>>>) invocation
            -> (Stream<Pair<Meal, Integer>>) invocation.getArguments()[0]);
    InOrder inOrder = Mockito.inOrder(step);

    var meal1 = meal()
        .name("Test1")
        .daysPassed(nonNegative(10))
        .addFact(new CourseTypeFact(MAIN))
        .create();
    var meal2 = meal()
        .name("Test2")
        .daysPassed(ZERO)
        .addFact(new CourseTypeFact(DESERT))
        .create();
    var meal3 = meal()
        .name("Test3")
        .daysPassed(TWO)
        .addFact(new CourseTypeFact(ENTRY))
        .create();
    var settings = new Settings[]{
        setting()
            .numberOfPeople(TWO)
            .addSetting(new CourseTypeSetting(THREE_COURSE))
            .create()};

    var sut = new ProposalBuilder(asList(meal1, meal2, meal3), singletonList(step));

    Proposal proposal = sut.propose(settings);

    assertThat(proposal.getProposalList()).hasSize(1);
    assertThat(proposal.getProposalList().get(0).main).isEqualByComparingTo(meal1.getId());
    assertThat(proposal.getProposalList().get(0).entry).contains(meal3.getId());
    assertThat(proposal.getProposalList().get(0).desert).contains(meal2.getId());

    inOrder.verify(step).setupProposalStep(any());
    inOrder.verify(step).applyPluginSuggestions(any(), any());
    inOrder.verify(step).applyPluginSuggestionsToEntries(any(), any(), any());
    inOrder.verify(step).applyPluginSuggestionsToDeserts(any(), any(), any());
    inOrder.verify(step).incrementProposalStepBetweenMeals(any());
  }

  private Settings trivialSetting() {
    return setting()
        .numberOfPeople(TWO)
        .create();
  }
}
