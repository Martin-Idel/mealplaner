package mealplaner.plugins.utensil;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static mealplaner.commons.NonnegativeInteger.FIVE;
import static mealplaner.commons.NonnegativeInteger.TWO;
import static mealplaner.commons.NonnegativeInteger.ZERO;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.model.settings.SettingsBuilder.setting;
import static mealplaner.plugins.builtins.courses.CourseSettings.MAIN_DESERT;
import static mealplaner.plugins.builtins.courses.CourseType.DESERT;
import static mealplaner.plugins.builtins.courses.CourseType.MAIN;
import static mealplaner.plugins.utensil.mealextension.ObligatoryUtensil.CASSEROLE;
import static mealplaner.plugins.utensil.mealextension.ObligatoryUtensil.PAN;
import static mealplaner.plugins.utensil.mealextension.ObligatoryUtensil.POT;
import static mealplaner.plugins.utensil.settingextension.CasseroleSettings.NONE;
import static mealplaner.plugins.utensil.settingextension.CasseroleSettings.POSSIBLE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import mealplaner.model.proposal.Proposal;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.builtins.courses.CourseTypeFact;
import mealplaner.plugins.builtins.courses.CourseTypeSetting;
import mealplaner.plugins.utensil.mealextension.ObligatoryUtensilFact;
import mealplaner.plugins.utensil.proposal.UtensilProposalStep;
import mealplaner.plugins.utensil.settingextension.CasseroleSubSetting;
import mealplaner.proposal.ProposalBuilder;

public class UtensilProposalStepTest {
  @Test
  public void applyPluginSuggestionsEliminatesCasseroleDishesIfNoCasserolesAllowed() {
    var meal1 = meal()
        .name("Test1")
        .daysPassed(nonNegative(100))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new ObligatoryUtensilFact(CASSEROLE))
        .create();
    var meal2 = meal()
        .name("Test2")
        .daysPassed(ZERO)
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new ObligatoryUtensilFact(PAN))
        .create();
    var settings = new Settings[] {setting()
        .numberOfPeople(TWO)
        .addSetting(new CasseroleSubSetting(NONE))
        .create()};

    var sut = new ProposalBuilder(asList(meal1, meal2), singletonList(new UtensilProposalStep()));

    Proposal proposal = sut.propose(settings);

    assertThat(proposal.getProposalList()).hasSize(1);
    assertThat(proposal.getProposalList().get(0).main).isEqualTo(meal2.getId());
  }

  @Test
  public void applyPluginSuggestionsEliminatesPanIfTooManyPeopleEat() {
    var meal1 = meal()
        .name("Test1")
        .daysPassed(nonNegative(100))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new ObligatoryUtensilFact(PAN))
        .create();
    var meal2 = meal()
        .name("Test2")
        .daysPassed(ZERO)
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new ObligatoryUtensilFact(POT))
        .create();
    var settings = new Settings[] {setting()
        .numberOfPeople(FIVE)
        .addSetting(new CasseroleSubSetting(POSSIBLE))
        .create()};

    var sut = new ProposalBuilder(asList(meal1, meal2), singletonList(new UtensilProposalStep()));

    Proposal proposal = sut.propose(settings);

    assertThat(proposal.getProposalList()).hasSize(1);
    assertThat(proposal.getProposalList().get(0).main).isEqualTo(meal2.getId());
  }

  @Test
  public void applyPluginSuggestionsUsesDifferentUtensilsForMainCourseAndDesert() {
    var meal1 = meal()
        .name("Test1")
        .daysPassed(ZERO)
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new ObligatoryUtensilFact(PAN))
        .create();
    var desert1 = meal()
        .name("Desert1")
        .daysPassed(nonNegative(100))
        .addFact(new CourseTypeFact(DESERT))
        .addFact(new ObligatoryUtensilFact(PAN))
        .create();
    var desert2 = meal()
        .name("Desert2")
        .daysPassed(ZERO)
        .addFact(new CourseTypeFact(DESERT))
        .addFact(new ObligatoryUtensilFact(CASSEROLE))
        .create();
    var settings = new Settings[] {setting()
        .numberOfPeople(FIVE)
        .addSetting(new CourseTypeSetting(MAIN_DESERT))
        .addSetting(new CasseroleSubSetting(POSSIBLE))
        .create()};

    var sut = new ProposalBuilder(asList(meal1, desert1, desert2), singletonList(new UtensilProposalStep()));

    Proposal proposal = sut.propose(settings);

    assertThat(proposal.getProposalList()).hasSize(1);
    assertThat(proposal.getProposalList().get(0).desert).contains(desert2.getId());
  }
}
