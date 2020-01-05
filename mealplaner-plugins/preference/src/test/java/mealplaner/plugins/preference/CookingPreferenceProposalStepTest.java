package mealplaner.plugins.preference;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static mealplaner.commons.NonnegativeInteger.ONE;
import static mealplaner.commons.NonnegativeInteger.TWO;
import static mealplaner.commons.NonnegativeInteger.ZERO;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.model.settings.SettingsBuilder.setting;
import static mealplaner.plugins.builtins.courses.CourseType.MAIN;
import static mealplaner.plugins.preference.mealextension.CookingPreference.NO_PREFERENCE;
import static mealplaner.plugins.preference.mealextension.CookingPreference.RARE;
import static mealplaner.plugins.preference.mealextension.CookingPreference.VERY_POPULAR;
import static mealplaner.plugins.preference.proposal.PreferenceMap.getPreferenceMap;
import static mealplaner.plugins.preference.settingextension.PreferenceSettings.NORMAL;
import static mealplaner.plugins.preference.settingextension.PreferenceSettings.RARE_NONE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import mealplaner.model.proposal.Proposal;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.builtins.courses.CourseTypeFact;
import mealplaner.plugins.preference.mealextension.CookingPreferenceFact;
import mealplaner.plugins.preference.proposal.CookingPreferenceProposalStep;
import mealplaner.plugins.preference.settingextension.CookingPreferenceSubSetting;
import mealplaner.proposal.ProposalBuilder;

public class CookingPreferenceProposalStepTest {
  @Test
  public void applyPluginSuggestionsEliminatesUnwantedPreferences() {
    var meal1 = meal()
        .name("Test1")
        .daysPassed(nonNegative(100))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new CookingPreferenceFact(RARE))
        .create();
    var meal2 = meal()
        .name("Test2")
        .daysPassed(ZERO)
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new CookingPreferenceFact(NO_PREFERENCE))
        .create();
    var settings = new Settings[] {setting()
        .numberOfPeople(TWO)
        .addSetting(new CookingPreferenceSubSetting(RARE_NONE))
        .create()};

    var sut = new ProposalBuilder(
        asList(meal1, meal2),
        singletonList(new CookingPreferenceProposalStep(getPreferenceMap())));

    Proposal proposal = sut.propose(settings);

    assertThat(proposal.getProposalList()).hasSize(1);
    assertThat(proposal.getProposalList().get(0).main).isEqualTo(meal2.getId());
  }

  @Test
  public void applyPluginSuggestionsChangesPreferencesAccordingToPreferenceMap() {
    var meal1 = meal()
        .name("Test1")
        .daysPassed(TWO)
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new CookingPreferenceFact(RARE))
        .create();
    var meal2 = meal()
        .name("Test2")
        .daysPassed(ONE)
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new CookingPreferenceFact(VERY_POPULAR))
        .create();
    var settings = new Settings[] {setting()
        .numberOfPeople(TWO)
        .addSetting(new CookingPreferenceSubSetting(NORMAL))
        .create()};

    var sut = new ProposalBuilder(
        asList(meal1, meal2),
        singletonList(new CookingPreferenceProposalStep(getPreferenceMap())));

    Proposal proposal = sut.propose(settings);

    assertThat(proposal.getProposalList()).hasSize(1);
    assertThat(proposal.getProposalList().get(0).main).isEqualTo(meal2.getId());
  }
}
