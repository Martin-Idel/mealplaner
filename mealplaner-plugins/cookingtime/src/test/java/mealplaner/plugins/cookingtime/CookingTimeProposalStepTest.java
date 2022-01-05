// SPDX-License-Identifier: MIT

package mealplaner.plugins.cookingtime;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static mealplaner.commons.NonnegativeInteger.TWO;
import static mealplaner.commons.NonnegativeInteger.ZERO;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.model.settings.SettingsBuilder.setting;
import static mealplaner.plugins.builtins.courses.CourseType.MAIN;
import static mealplaner.plugins.cookingtime.mealextension.CookingTime.LONG;
import static mealplaner.plugins.cookingtime.mealextension.CookingTime.SHORT;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import mealplaner.model.proposal.Proposal;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.builtins.courses.CourseTypeFact;
import mealplaner.plugins.cookingtime.mealextension.CookingTime;
import mealplaner.plugins.cookingtime.mealextension.CookingTimeFact;
import mealplaner.plugins.cookingtime.proposal.CookingTimeProposalStep;
import mealplaner.plugins.cookingtime.settingextension.CookingTimeSubSetting;
import mealplaner.proposal.ProposalBuilder;
import testcommons.PluginsUtils;

class CookingTimeProposalStepTest {
  @Test
  void applyPluginSuggestionsEliminatesUnwantedCookingTimes() {
    PluginsUtils.setupMessageBundles(new CookingTimePlugin());
    var meal1 = meal()
        .name("Test1")
        .daysPassed(nonNegative(100))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new CookingTimeFact(SHORT))
        .create();
    var meal2 = meal()
        .name("Test2")
        .daysPassed(ZERO)
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new CookingTimeFact(LONG))
        .create();
    var cookingTimes = new HashSet<CookingTime>();
    cookingTimes.add(SHORT);
    var settings = new Settings[] {setting()
        .numberOfPeople(TWO)
        .addSetting(new CookingTimeSubSetting(cookingTimes))
        .create()};

    var sut = new ProposalBuilder(asList(meal1, meal2), singletonList(new CookingTimeProposalStep()));

    Proposal proposal = sut.propose(settings);

    assertThat(proposal.getProposalList()).hasSize(1);
    assertThat(proposal.getProposalList().get(0).main).isEqualTo(meal2.getId());
  }
}
