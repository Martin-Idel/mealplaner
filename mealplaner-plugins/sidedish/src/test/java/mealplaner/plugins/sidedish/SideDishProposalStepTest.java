package mealplaner.plugins.sidedish;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static mealplaner.commons.NonnegativeInteger.ONE;
import static mealplaner.commons.NonnegativeInteger.TWO;
import static mealplaner.commons.NonnegativeInteger.ZERO;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.model.settings.SettingsBuilder.setting;
import static mealplaner.plugins.builtins.courses.CourseType.MAIN;
import static mealplaner.plugins.sidedish.mealextension.Sidedish.NONE;
import static mealplaner.plugins.sidedish.mealextension.Sidedish.PASTA;
import static mealplaner.plugins.sidedish.mealextension.Sidedish.RICE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import mealplaner.model.proposal.Proposal;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.builtins.courses.CourseTypeFact;
import mealplaner.plugins.sidedish.mealextension.SidedishFact;
import mealplaner.plugins.sidedish.proposal.SideDishProposalStep;
import mealplaner.proposal.ProposalBuilder;

public class SideDishProposalStepTest {
  @Test
  public void applyPluginSuggestionsDoesNotAllowForTimesSameSideDishInARow() {
    var meal1 = meal()
        .name("Test1")
        .daysPassed(nonNegative(102))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new SidedishFact(PASTA))
        .create();
    var meal2 = meal()
        .name("Test2")
        .daysPassed(nonNegative(101))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new SidedishFact(PASTA))
        .create();
    var meal3 = meal()
        .name("Test3")
        .daysPassed(nonNegative(100))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new SidedishFact(PASTA))
        .create();
    var meal4 = meal()
        .name("Test4")
        .daysPassed(nonNegative(99))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new SidedishFact(PASTA))
        .create();
    var meal5 = meal()
        .name("Test5")
        .daysPassed(ONE)
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new SidedishFact(NONE))
        .create();
    var settings = new Settings[]{trivialSetting(), trivialSetting(), trivialSetting(), trivialSetting()};

    var sut = new ProposalBuilder(
        asList(meal1, meal2, meal3, meal4, meal5),
        singletonList(new SideDishProposalStep()));

    Proposal proposal = sut.propose(settings);

    assertThat(proposal.getProposalList()).hasSize(4);
    assertThat(proposal.getProposalList().get(0).main).isEqualTo(meal1.getId());
    assertThat(proposal.getProposalList().get(1).main).isEqualTo(meal2.getId());
    assertThat(proposal.getProposalList().get(2).main).isEqualTo(meal3.getId());
    assertThat(proposal.getProposalList().get(3).main).isEqualTo(meal5.getId());
  }

  @Test
  public void applyPluginSuggestionsLowersProbabilityOfSideDishIfItOccurredLast() {
    var meal1 = meal()
        .name("Test1")
        .daysPassed(nonNegative(100))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new SidedishFact(PASTA))
        .create();
    var meal2 = meal()
        .name("Test2")
        .daysPassed(nonNegative(50))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new SidedishFact(PASTA))
        .create();
    var meal3 = meal()
        .name("Test3")
        .daysPassed(nonNegative(10))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new SidedishFact(PASTA))
        .create();
    var meal4 = meal()
        .name("Test4")
        .daysPassed(nonNegative(9))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new SidedishFact(NONE))
        .create();
    var settings = new Settings[]{trivialSetting(), trivialSetting(), trivialSetting()};

    var sut = new ProposalBuilder(
        asList(meal1, meal2, meal3, meal4),
        singletonList(new SideDishProposalStep()));

    Proposal proposal = sut.propose(settings);

    assertThat(proposal.getProposalList()).hasSize(3);
    assertThat(proposal.getProposalList().get(0).main).isEqualTo(meal1.getId());
    assertThat(proposal.getProposalList().get(1).main).isEqualTo(meal2.getId());
    assertThat(proposal.getProposalList().get(2).main).isEqualTo(meal4.getId());
  }

  @Test
  public void applyPluginSuggestionsResetsCorrectlyAfterDifferentSideDish() {
    var meal1 = meal()
        .name("Test1")
        .daysPassed(nonNegative(100))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new SidedishFact(PASTA))
        .create();
    var meal2 = meal()
        .name("Test2")
        .daysPassed(nonNegative(50))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new SidedishFact(NONE))
        .create();
    var meal3 = meal()
        .name("Test3")
        .daysPassed(nonNegative(10))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new SidedishFact(PASTA))
        .create();
    var meal4 = meal()
        .name("Test4")
        .daysPassed(nonNegative(9))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new SidedishFact(PASTA))
        .create();
    var meal5 = meal()
        .name("Test5")
        .daysPassed(nonNegative(8))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new SidedishFact(RICE))
        .create();
    var settings = new Settings[]{trivialSetting(), trivialSetting(), trivialSetting(), trivialSetting()};

    var sut = new ProposalBuilder(
        asList(meal1, meal2, meal3, meal4, meal5),
        singletonList(new SideDishProposalStep()));

    Proposal proposal = sut.propose(settings);

    assertThat(proposal.getProposalList()).hasSize(4);
    assertThat(proposal.getProposalList().get(0).main).isEqualTo(meal1.getId());
    assertThat(proposal.getProposalList().get(1).main).isEqualTo(meal2.getId());
    assertThat(proposal.getProposalList().get(2).main).isEqualTo(meal3.getId());
    assertThat(proposal.getProposalList().get(3).main).isEqualTo(meal4.getId());
  }

  @Test
  public void applyPluginSuggestionsCorrectlyTakesHistoricMealsIntoAccount() {
    var meal1 = meal()
        .name("Test1")
        .daysPassed(ZERO)
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new SidedishFact(PASTA))
        .create();
    var meal2 = meal()
        .name("Test2")
        .daysPassed(ONE)
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new SidedishFact(PASTA))
        .create();
    var meal3 = meal()
        .name("Test3")
        .daysPassed(TWO)
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new SidedishFact(PASTA))
        .create();
    var meal4 = meal()
        .name("Test4")
        .daysPassed(nonNegative(10))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new SidedishFact(NONE))
        .create();
    var meal5 = meal()
        .name("Test5")
        .daysPassed(nonNegative(100))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new SidedishFact(PASTA))
        .create();
    var settings = new Settings[]{trivialSetting()};

    var sut = new ProposalBuilder(
        asList(meal1, meal2, meal3, meal4, meal5),
        singletonList(new SideDishProposalStep()));

    Proposal proposal = sut.propose(settings);

    assertThat(proposal.getProposalList()).hasSize(1);
    assertThat(proposal.getProposalList().get(0).main).isEqualTo(meal4.getId());
  }

  private Settings trivialSetting() {
    return setting()
        .numberOfPeople(TWO)
        .create();
  }
}
