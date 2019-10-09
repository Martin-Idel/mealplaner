// SPDX-License-Identifier: MIT

package mealplaner.proposal;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.nameUUIDFromBytes;
import static mealplaner.Kochplaner.registerPlugins;
import static mealplaner.commons.NonnegativeInteger.ZERO;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.plugins.builtins.courses.CourseType.DESERT;
import static mealplaner.plugins.builtins.courses.CourseType.MAIN;
import static mealplaner.plugins.plugins.cookingtime.mealextension.CookingTime.SHORT;
import static mealplaner.plugins.plugins.cookingtime.mealextension.CookingTime.VERY_SHORT;
import static mealplaner.plugins.plugins.preference.mealextension.CookingPreference.NO_PREFERENCE;
import static mealplaner.plugins.plugins.preference.mealextension.CookingPreference.RARE;
import static mealplaner.plugins.plugins.preference.mealextension.CookingPreference.VERY_POPULAR;
import static mealplaner.plugins.plugins.sidedish.mealextension.Sidedish.NONE;
import static mealplaner.plugins.plugins.sidedish.mealextension.Sidedish.PASTA;
import static mealplaner.plugins.plugins.sidedish.mealextension.Sidedish.POTATOES;
import static mealplaner.plugins.plugins.utensil.mealextension.ObligatoryUtensil.CASSEROLE;
import static mealplaner.plugins.plugins.utensil.mealextension.ObligatoryUtensil.PAN;
import static mealplaner.plugins.plugins.utensil.mealextension.ObligatoryUtensil.POT;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getSettings1;
import static testcommons.CommonFunctions.getSettings3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;

import mealplaner.model.meal.Meal;
import mealplaner.plugins.builtins.courses.CourseTypeFact;
import mealplaner.plugins.plugins.comment.mealextension.CommentFact;
import mealplaner.plugins.plugins.cookingtime.mealextension.CookingTimeFact;
import mealplaner.plugins.plugins.preference.mealextension.CookingPreferenceFact;
import mealplaner.plugins.plugins.sidedish.mealextension.SidedishFact;
import mealplaner.plugins.plugins.utensil.mealextension.ObligatoryUtensilFact;

public class DesertProposalTest {
  private final List<Meal> meals = new ArrayList<>();
  private DesertProposal sut;

  @Test
  public void proposalReturnsNothingIfNoEntryIsPresent() {
    var pluginStore = registerPlugins();
    sut = new DesertProposal(meals, pluginStore.getRegisteredProposalBuilderSteps());

    Optional<UUID> proposeNextDesert = sut.proposeNextDesert(getSettings1(), getMeal1(),
        new ArrayList<>());

    assertThat(proposeNextDesert).isEmpty();
  }

  @Test
  public void proposalReturnsNothingIfNoDesertFulfilsRequirements() {
    Meal meal1 = meal()
        .name("Meal1")
        .addFact(new CookingTimeFact(VERY_SHORT))
        .addFact(new SidedishFact(PASTA))
        .addFact(new ObligatoryUtensilFact(PAN))
        .addFact(new CookingPreferenceFact(NO_PREFERENCE))
        .addFact(new CourseTypeFact(DESERT))
        .addFact(new CommentFact(""))
        .daysPassed(nonNegative(50))
        .create();
    meals.add(meal1);
    var pluginStore = registerPlugins();
    sut = new DesertProposal(meals, pluginStore.getRegisteredProposalBuilderSteps());

    Optional<UUID> proposeNextDesert = sut.proposeNextDesert(getSettings1(), getMeal1(),
        new ArrayList<>()); // Settings1 has very short as prohibited setting

    assertThat(proposeNextDesert).isEmpty();
  }

  @Test
  public void proposalReturnsDesertCookedLeastRecentlyIfManyEntriesWork() {
    Meal meal1 = meal()
        .id(nameUUIDFromBytes("Test1Meal".getBytes(UTF_8)))
        .name("Meal5")
        .addFact(new CookingTimeFact(SHORT))
        .addFact(new SidedishFact(PASTA))
        .addFact(new ObligatoryUtensilFact(POT))
        .addFact(new CookingPreferenceFact(NO_PREFERENCE))
        .addFact(new CourseTypeFact(DESERT))
        .addFact(new CommentFact(""))
        .daysPassed(nonNegative(100))
        .create();
    meals.add(meal1);
    Meal meal2 = meal()
        .id(nameUUIDFromBytes("Test2Meal".getBytes(UTF_8)))
        .name("Meal1")
        .addFact(new CookingTimeFact(VERY_SHORT))
        .addFact(new SidedishFact(PASTA))
        .addFact(new ObligatoryUtensilFact(PAN))
        .addFact(new CookingPreferenceFact(NO_PREFERENCE))
        .addFact(new CourseTypeFact(DESERT))
        .addFact(new CommentFact(""))
        .daysPassed(nonNegative(50))
        .create();
    meals.add(meal2);

    Meal main = meal()
        .id(nameUUIDFromBytes("TestMain".getBytes(UTF_8)))
        .name("Meal1")
        .addFact(new CookingTimeFact(SHORT))
        .addFact(new SidedishFact(NONE))
        .addFact(new ObligatoryUtensilFact(CASSEROLE))
        .addFact(new CookingPreferenceFact(NO_PREFERENCE))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new CommentFact(""))
        .daysPassed(ZERO)
        .create();

    var pluginStore = registerPlugins();
    sut = new DesertProposal(meals, pluginStore.getRegisteredProposalBuilderSteps());

    Optional<UUID> proposeNextDesert = sut.proposeNextDesert(getSettings3(), main,
        new ArrayList<>());

    assertThat(proposeNextDesert)
        .contains(nameUUIDFromBytes("Test1Meal".getBytes(UTF_8)));
  }

  @Test
  public void proposalDoesNotCareAboutSiddish() {
    Meal meal1 = meal()
        .id(nameUUIDFromBytes("Test1Meal".getBytes(UTF_8)))
        .name("Meal5")
        .addFact(new CookingTimeFact(SHORT))
        .addFact(new SidedishFact(PASTA))
        .addFact(new ObligatoryUtensilFact(POT))
        .addFact(new CookingPreferenceFact(NO_PREFERENCE))
        .addFact(new CourseTypeFact(DESERT))
        .addFact(new CommentFact(""))
        .daysPassed(nonNegative(100))
        .create();

    meals.add(meal1);
    Meal meal2 = meal()
        .id(nameUUIDFromBytes("Test2Meal".getBytes(UTF_8)))
        .name("Meal1")
        .addFact(new CookingTimeFact(VERY_SHORT))
        .addFact(new SidedishFact(POTATOES))
        .addFact(new ObligatoryUtensilFact(PAN))
        .addFact(new CookingPreferenceFact(NO_PREFERENCE))
        .addFact(new CourseTypeFact(DESERT))
        .addFact(new CommentFact(""))
        .daysPassed(nonNegative(50))
        .create();
    meals.add(meal2);

    Meal main = meal()
        .id(nameUUIDFromBytes("TestMain".getBytes(UTF_8)))
        .name("Meal1")
        .addFact(new CookingTimeFact(SHORT))
        .addFact(new SidedishFact(PASTA))
        .addFact(new ObligatoryUtensilFact(CASSEROLE))
        .addFact(new CookingPreferenceFact(NO_PREFERENCE))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new CommentFact(""))
        .daysPassed(ZERO)
        .create();
    var pluginStore = registerPlugins();
    sut = new DesertProposal(meals, pluginStore.getRegisteredProposalBuilderSteps());

    Optional<UUID> proposeNextDesert = sut.proposeNextDesert(getSettings3(), main,
        new ArrayList<>());

    assertThat(proposeNextDesert)
        .contains(nameUUIDFromBytes("Test1Meal".getBytes(UTF_8)));
  }

  @Test
  public void proposalProhibitsSameUtensilIfCasserole() {
    Meal meal1 = meal()
        .id(nameUUIDFromBytes("Test1Meal".getBytes(UTF_8)))
        .name("Meal5")
        .addFact(new CookingTimeFact(SHORT))
        .addFact(new SidedishFact(PASTA))
        .addFact(new ObligatoryUtensilFact(CASSEROLE))
        .addFact(new CookingPreferenceFact(NO_PREFERENCE))
        .addFact(new CourseTypeFact(DESERT))
        .addFact(new CommentFact(""))
        .daysPassed(nonNegative(100))
        .create();
    meals.add(meal1);
    Meal meal2 = meal()
        .id(nameUUIDFromBytes("Test2Meal".getBytes(UTF_8)))
        .name("Meal1")
        .addFact(new CookingTimeFact(VERY_SHORT))
        .addFact(new SidedishFact(POTATOES))
        .addFact(new ObligatoryUtensilFact(PAN))
        .addFact(new CookingPreferenceFact(NO_PREFERENCE))
        .addFact(new CourseTypeFact(DESERT))
        .addFact(new CommentFact(""))
        .daysPassed(nonNegative(50))
        .create();
    meals.add(meal2);

    Meal main = meal()
        .id(nameUUIDFromBytes("TestMain".getBytes(UTF_8)))
        .name("Meal2")
        .addFact(new CookingTimeFact(SHORT))
        .addFact(new SidedishFact(NONE))
        .addFact(new ObligatoryUtensilFact(CASSEROLE))
        .addFact(new CookingPreferenceFact(NO_PREFERENCE))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new CommentFact(""))
        .daysPassed(ZERO)
        .create();

    var pluginStore = registerPlugins();
    sut = new DesertProposal(meals, pluginStore.getRegisteredProposalBuilderSteps());

    Optional<UUID> proposeNextDesert = sut.proposeNextDesert(getSettings3(), main,
        new ArrayList<>());

    assertThat(proposeNextDesert)
        .contains(nameUUIDFromBytes("Test2Meal".getBytes(UTF_8)));
  }

  @Test
  public void proposalTakesPreferenceIntoAccount() {
    Meal meal1 = meal()
        .id(nameUUIDFromBytes("Test1Meal".getBytes(UTF_8)))
        .name("Meal5")
        .addFact(new CookingTimeFact(SHORT))
        .addFact(new SidedishFact(PASTA))
        .addFact(new ObligatoryUtensilFact(CASSEROLE))
        .addFact(new CookingPreferenceFact(RARE))
        .addFact(new CourseTypeFact(DESERT))
        .addFact(new CommentFact(""))
        .daysPassed(nonNegative(100))
        .create();
    meals.add(meal1);
    Meal meal2 = meal()
        .id(nameUUIDFromBytes("Test2Meal".getBytes(UTF_8)))
        .name("Meal1")
        .addFact(new CookingTimeFact(VERY_SHORT))
        .addFact(new SidedishFact(POTATOES))
        .addFact(new ObligatoryUtensilFact(PAN))
        .addFact(new CookingPreferenceFact(VERY_POPULAR))
        .addFact(new CourseTypeFact(DESERT))
        .addFact(new CommentFact(""))
        .daysPassed(nonNegative(50))
        .create();
    meals.add(meal2);

    Meal main = meal()
        .id(nameUUIDFromBytes("TestMain".getBytes(UTF_8)))
        .name("Meal1")
        .addFact(new CookingTimeFact(SHORT))
        .addFact(new SidedishFact(NONE))
        .addFact(new ObligatoryUtensilFact(POT))
        .addFact(new CookingPreferenceFact(NO_PREFERENCE))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new CommentFact(""))
        .daysPassed(ZERO)
        .create();

    var pluginStore = registerPlugins();
    sut = new DesertProposal(meals, pluginStore.getRegisteredProposalBuilderSteps());

    Optional<UUID> proposeNextDesert = sut.proposeNextDesert(getSettings3(), main,
        new ArrayList<>());

    assertThat(proposeNextDesert)
        .contains(nameUUIDFromBytes("Test2Meal".getBytes(UTF_8)));
  }
}
