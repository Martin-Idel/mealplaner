// SPDX-License-Identifier: MIT

package mealplaner.proposal;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.nameUUIDFromBytes;
import static mealplaner.Kochplaner.registerPlugins;
import static mealplaner.commons.NonnegativeInteger.ZERO;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.model.meal.enums.CourseType.ENTRY;
import static mealplaner.model.meal.enums.CourseType.MAIN;
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
import static testcommons.CommonFunctions.getSettings4;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;

import mealplaner.model.meal.Meal;

public class EntryProposalTest {
  private final List<Meal> meals = new ArrayList<>();
  private EntryProposal sut;

  @Test
  public void proposalReturnsNothingIfNoEntryIsPresent() {
    var pluginStore = registerPlugins();
    sut = new EntryProposal(meals, pluginStore.getRegisteredProposalBuilderSteps());

    Optional<UUID> proposeNextEntry = sut.proposeNextEntry(getSettings1(), getMeal1(),
        new ArrayList<>());

    assertThat(proposeNextEntry).isEmpty();
  }

  @Test
  public void proposalReturnsNothingIfNoEntryFulfilsRequirements() {
    Meal meal1 = meal()
        .name("Meal1")
        .cookingTime(VERY_SHORT)
        .sidedish(PASTA)
        .obligatoryUtensil(PAN)
        .cookingPreference(NO_PREFERENCE)
        .daysPassed(nonNegative(50))
        .courseType(ENTRY)
        .comment("")
        .create();
    meals.add(meal1);
    var pluginStore = registerPlugins();
    sut = new EntryProposal(meals, pluginStore.getRegisteredProposalBuilderSteps());

    Optional<UUID> proposeNextEntry = sut.proposeNextEntry(getSettings1(), getMeal1(),
        new ArrayList<>()); // Settings1 has very short as prohibited setting

    assertThat(proposeNextEntry).isEmpty();
  }

  @Test
  public void proposalReturnsEntryCookedLeastRecentlyIfManyEntriesWork() {
    Meal meal1 = meal()
        .id(nameUUIDFromBytes("Test1Meal".getBytes(UTF_8)))
        .name("Meal5")
        .cookingTime(SHORT)
        .sidedish(PASTA)
        .obligatoryUtensil(POT)
        .cookingPreference(NO_PREFERENCE)
        .daysPassed(nonNegative(100))
        .courseType(ENTRY)
        .comment("")
        .create();
    meals.add(meal1);
    Meal meal2 = meal()
        .id(nameUUIDFromBytes("Test2Meal".getBytes(UTF_8)))
        .name("Meal1")
        .cookingTime(VERY_SHORT)
        .sidedish(PASTA)
        .obligatoryUtensil(PAN)
        .cookingPreference(NO_PREFERENCE)
        .daysPassed(nonNegative(50))
        .courseType(ENTRY)
        .comment("")
        .create();
    meals.add(meal2);

    Meal main = meal()
        .id(nameUUIDFromBytes("TestMain".getBytes(UTF_8)))
        .name("Meal1")
        .cookingTime(SHORT)
        .sidedish(NONE)
        .obligatoryUtensil(CASSEROLE)
        .cookingPreference(NO_PREFERENCE)
        .daysPassed(ZERO)
        .courseType(MAIN)
        .comment("")
        .create();

    var pluginStore = registerPlugins();
    sut = new EntryProposal(meals, pluginStore.getRegisteredProposalBuilderSteps());

    Optional<UUID> proposeNextEntry = sut.proposeNextEntry(getSettings4(), main,
        new ArrayList<>());

    assertThat(proposeNextEntry)
        .contains(nameUUIDFromBytes("Test1Meal".getBytes(UTF_8)));
  }

  @Test
  public void proposalProhibitsSameSidedish() {
    Meal meal1 = meal()
        .name("Meal5")
        .cookingTime(SHORT)
        .sidedish(PASTA)
        .obligatoryUtensil(POT)
        .cookingPreference(NO_PREFERENCE)
        .daysPassed(nonNegative(100))
        .courseType(ENTRY)
        .comment("")
        .create();
    meals.add(meal1);
    Meal meal2 = meal()
        .id(nameUUIDFromBytes("Test2Meal".getBytes(UTF_8)))
        .name("Meal1")
        .cookingTime(VERY_SHORT)
        .sidedish(POTATOES)
        .obligatoryUtensil(PAN)
        .cookingPreference(NO_PREFERENCE)
        .daysPassed(nonNegative(50))
        .courseType(ENTRY)
        .comment("")
        .create();
    meals.add(meal2);

    Meal main = meal()
        .id(nameUUIDFromBytes("TestMain".getBytes(UTF_8)))
        .name("Meal1")
        .cookingTime(SHORT)
        .sidedish(PASTA)
        .obligatoryUtensil(CASSEROLE)
        .cookingPreference(NO_PREFERENCE)
        .daysPassed(ZERO)
        .courseType(MAIN)
        .comment("")
        .create();

    var pluginStore = registerPlugins();
    sut = new EntryProposal(meals, pluginStore.getRegisteredProposalBuilderSteps());

    Optional<UUID> proposeNextEntry = sut.proposeNextEntry(getSettings4(), main,
        new ArrayList<>());

    assertThat(proposeNextEntry)
        .contains(nameUUIDFromBytes("Test2Meal".getBytes(UTF_8)));
  }

  @Test
  public void proposalProhibitsSameUtensilIfCasserole() {
    Meal meal1 = meal()
        .name("Meal5")
        .cookingTime(SHORT)
        .sidedish(PASTA)
        .obligatoryUtensil(CASSEROLE)
        .cookingPreference(NO_PREFERENCE)
        .daysPassed(nonNegative(100))
        .courseType(ENTRY)
        .comment("")
        .create();
    meals.add(meal1);
    Meal meal2 = meal()
        .id(nameUUIDFromBytes("Test2Meal".getBytes(UTF_8)))
        .name("Meal1")
        .cookingTime(VERY_SHORT)
        .sidedish(POTATOES)
        .obligatoryUtensil(PAN)
        .cookingPreference(NO_PREFERENCE)
        .daysPassed(nonNegative(50))
        .courseType(ENTRY)
        .comment("")
        .create();
    meals.add(meal2);

    Meal main = meal()
        .id(nameUUIDFromBytes("TestMain".getBytes(UTF_8)))
        .name("Meal1")
        .cookingTime(SHORT)
        .sidedish(NONE)
        .obligatoryUtensil(CASSEROLE)
        .cookingPreference(NO_PREFERENCE)
        .daysPassed(ZERO)
        .courseType(MAIN)
        .comment("")
        .create();
    var pluginStore = registerPlugins();
    sut = new EntryProposal(meals, pluginStore.getRegisteredProposalBuilderSteps());

    Optional<UUID> proposeNextEntry = sut.proposeNextEntry(getSettings4(), main,
        new ArrayList<>());

    assertThat(proposeNextEntry)
        .contains(nameUUIDFromBytes("Test2Meal".getBytes(UTF_8)));
  }

  @Test
  public void proposalTakesPreferenceIntoAccount() {
    Meal meal1 = meal()
        .name("Meal5")
        .cookingTime(SHORT)
        .sidedish(PASTA)
        .obligatoryUtensil(CASSEROLE)
        .cookingPreference(RARE)
        .daysPassed(nonNegative(100))
        .courseType(ENTRY)
        .comment("")
        .create();
    meals.add(meal1);
    Meal meal2 = meal()
        .id(nameUUIDFromBytes("Test2Meal".getBytes(UTF_8)))
        .name("Meal1")
        .cookingTime(VERY_SHORT)
        .sidedish(POTATOES)
        .obligatoryUtensil(PAN)
        .cookingPreference(VERY_POPULAR)
        .daysPassed(nonNegative(50))
        .courseType(ENTRY)
        .comment("")
        .create();
    meals.add(meal2);

    Meal main = meal()
        .id(nameUUIDFromBytes("TestMain".getBytes(UTF_8)))
        .name("Meal1")
        .cookingTime(SHORT)
        .sidedish(NONE)
        .obligatoryUtensil(POT)
        .cookingPreference(NO_PREFERENCE)
        .daysPassed(ZERO)
        .courseType(MAIN)
        .comment("")
        .create();
    var pluginStore = registerPlugins();
    sut = new EntryProposal(meals, pluginStore.getRegisteredProposalBuilderSteps());

    Optional<UUID> proposeNextEntry = sut.proposeNextEntry(getSettings4(), main,
        new ArrayList<>());

    assertThat(proposeNextEntry)
        .contains(nameUUIDFromBytes("Test2Meal".getBytes(UTF_8)));
  }
}
