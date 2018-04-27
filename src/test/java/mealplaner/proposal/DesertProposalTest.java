// SPDX-License-Identifier: MIT

package mealplaner.proposal;

import static java.nio.charset.Charset.forName;
import static java.util.Optional.empty;
import static java.util.UUID.nameUUIDFromBytes;
import static java.util.UUID.randomUUID;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.configuration.PreferenceMap.getPreferenceMap;
import static mealplaner.model.meal.Meal.createMeal;
import static mealplaner.model.settings.Settings.from;
import static mealplaner.model.settings.subsettings.CookingTimeSetting.cookingTimeWithProhibited;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getSettings1;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;

import mealplaner.model.meal.Meal;
import mealplaner.model.meal.enums.CookingPreference;
import mealplaner.model.meal.enums.CookingTime;
import mealplaner.model.meal.enums.CourseType;
import mealplaner.model.meal.enums.ObligatoryUtensil;
import mealplaner.model.meal.enums.Sidedish;
import mealplaner.model.settings.Settings;
import mealplaner.model.settings.enums.CasseroleSettings;
import mealplaner.model.settings.enums.CourseSettings;
import mealplaner.model.settings.enums.PreferenceSettings;

public class DesertProposalTest {
  private final List<Meal> meals = new ArrayList<>();
  private DesertProposal sut;

  @Test
  public void proposalReturnsNothingIfNoEntryIsPresent() {
    sut = new DesertProposal(meals, new PreferenceMultiplier(getPreferenceMap()));

    Optional<UUID> proposeNextDesert = sut.proposeNextDesert(getSettings1(), getMeal1(),
        new ArrayList<>());

    assertThat(proposeNextDesert).isEmpty();
  }

  @Test
  public void proposalReturnsNothingIfNoDesertFulfilsRequirements() {
    Meal meal1 = createMeal(randomUUID(), "Meal1", CookingTime.VERY_SHORT, Sidedish.PASTA,
        ObligatoryUtensil.PAN, CookingPreference.NO_PREFERENCE, CourseType.DESERT, nonNegative(50),
        "", empty());
    meals.add(meal1);
    sut = new DesertProposal(meals, new PreferenceMultiplier(getPreferenceMap()));

    Optional<UUID> proposeNextDesert = sut.proposeNextDesert(getSettings1(), getMeal1(),
        new ArrayList<>()); // Settings1 has very short as prohibited setting

    assertThat(proposeNextDesert).isEmpty();
  }

  @Test
  public void proposalReturnsDesertCookedLeastRecentlyIfManyEntriesWork() {
    Meal meal1 = createMeal(nameUUIDFromBytes("Test1Meal".getBytes(forName("UTF-8"))), "Meal5",
        CookingTime.SHORT, Sidedish.PASTA, ObligatoryUtensil.POT, CookingPreference.NO_PREFERENCE,
        CourseType.DESERT, nonNegative(100), "", empty());
    meals.add(meal1);
    Meal meal2 = createMeal(nameUUIDFromBytes("Test2Meal".getBytes(forName("UTF-8"))), "Meal1",
        CookingTime.VERY_SHORT, Sidedish.PASTA, ObligatoryUtensil.PAN,
        CookingPreference.NO_PREFERENCE, CourseType.DESERT, nonNegative(50), "", empty());
    meals.add(meal2);

    Settings settings = from(cookingTimeWithProhibited(), nonNegative(3),
        CasseroleSettings.NONE, PreferenceSettings.RARE_PREFERED, CourseSettings.ONLY_MAIN);

    Meal main = createMeal(nameUUIDFromBytes("TestMain".getBytes(forName("UTF-8"))), "Meal1",
        CookingTime.SHORT, Sidedish.NONE, ObligatoryUtensil.CASSEROLE,
        CookingPreference.NO_PREFERENCE, CourseType.MAIN, nonNegative(0), "", empty());

    sut = new DesertProposal(meals, new PreferenceMultiplier(getPreferenceMap()));

    Optional<UUID> proposeNextDesert = sut.proposeNextDesert(settings, main,
        new ArrayList<>());

    assertThat(proposeNextDesert)
        .contains(nameUUIDFromBytes("Test1Meal".getBytes(forName("UTF-8"))));
  }

  @Test
  public void proposalDoesNotCareAboutSiddish() {
    Meal meal1 = createMeal(nameUUIDFromBytes("Test1Meal".getBytes(forName("UTF-8"))), "Meal5",
        CookingTime.SHORT, Sidedish.PASTA, ObligatoryUtensil.POT, CookingPreference.NO_PREFERENCE,
        CourseType.DESERT, nonNegative(100), "", empty());
    meals.add(meal1);
    Meal meal2 = createMeal(nameUUIDFromBytes("Test2Meal".getBytes(forName("UTF-8"))), "Meal1",
        CookingTime.VERY_SHORT, Sidedish.POTATOES, ObligatoryUtensil.PAN,
        CookingPreference.NO_PREFERENCE, CourseType.DESERT, nonNegative(50), "", empty());
    meals.add(meal2);

    Settings settings = from(cookingTimeWithProhibited(), nonNegative(3),
        CasseroleSettings.NONE, PreferenceSettings.RARE_PREFERED, CourseSettings.ONLY_MAIN);

    Meal main = createMeal(nameUUIDFromBytes("TestMain".getBytes(forName("UTF-8"))), "Meal1",
        CookingTime.SHORT, Sidedish.PASTA, ObligatoryUtensil.CASSEROLE,
        CookingPreference.NO_PREFERENCE, CourseType.MAIN, nonNegative(0), "", empty());

    sut = new DesertProposal(meals, new PreferenceMultiplier(getPreferenceMap()));

    Optional<UUID> proposeNextDesert = sut.proposeNextDesert(settings, main,
        new ArrayList<>());

    assertThat(proposeNextDesert)
        .contains(nameUUIDFromBytes("Test1Meal".getBytes(forName("UTF-8"))));
  }

  @Test
  public void proposalProhibitsSameUtensilIfCasserole() {
    Meal meal1 = createMeal(nameUUIDFromBytes("Test1Meal".getBytes(forName("UTF-8"))), "Meal5",
        CookingTime.SHORT, Sidedish.PASTA, ObligatoryUtensil.CASSEROLE,
        CookingPreference.NO_PREFERENCE,
        CourseType.DESERT, nonNegative(100), "", empty());
    meals.add(meal1);
    Meal meal2 = createMeal(nameUUIDFromBytes("Test2Meal".getBytes(forName("UTF-8"))), "Meal1",
        CookingTime.VERY_SHORT, Sidedish.POTATOES, ObligatoryUtensil.PAN,
        CookingPreference.NO_PREFERENCE, CourseType.DESERT, nonNegative(50), "", empty());
    meals.add(meal2);

    Settings settings = from(cookingTimeWithProhibited(), nonNegative(3),
        CasseroleSettings.NONE, PreferenceSettings.RARE_PREFERED, CourseSettings.ONLY_MAIN);

    Meal main = createMeal(nameUUIDFromBytes("TestMain".getBytes(forName("UTF-8"))), "Meal1",
        CookingTime.SHORT, Sidedish.NONE, ObligatoryUtensil.CASSEROLE,
        CookingPreference.NO_PREFERENCE, CourseType.MAIN, nonNegative(0), "", empty());

    sut = new DesertProposal(meals, new PreferenceMultiplier(getPreferenceMap()));

    Optional<UUID> proposeNextDesert = sut.proposeNextDesert(settings, main,
        new ArrayList<>());

    assertThat(proposeNextDesert)
        .contains(nameUUIDFromBytes("Test2Meal".getBytes(forName("UTF-8"))));
  }

  @Test
  public void proposalTakesPreferenceIntoAccount() {
    Meal meal1 = createMeal(nameUUIDFromBytes("Test1Meal".getBytes(forName("UTF-8"))), "Meal5",
        CookingTime.SHORT, Sidedish.PASTA, ObligatoryUtensil.CASSEROLE,
        CookingPreference.RARE,
        CourseType.DESERT, nonNegative(100), "", empty());
    meals.add(meal1);
    Meal meal2 = createMeal(nameUUIDFromBytes("Test2Meal".getBytes(forName("UTF-8"))), "Meal1",
        CookingTime.VERY_SHORT, Sidedish.POTATOES, ObligatoryUtensil.PAN,
        CookingPreference.VERY_POPULAR, CourseType.DESERT, nonNegative(50), "", empty());
    meals.add(meal2);

    Settings settings = from(cookingTimeWithProhibited(), nonNegative(3),
        CasseroleSettings.NONE, PreferenceSettings.RARE_PREFERED, CourseSettings.ONLY_MAIN);

    Meal main = createMeal(nameUUIDFromBytes("TestMain".getBytes(forName("UTF-8"))), "Meal1",
        CookingTime.SHORT, Sidedish.NONE, ObligatoryUtensil.POT,
        CookingPreference.NO_PREFERENCE, CourseType.MAIN, nonNegative(0), "", empty());

    sut = new DesertProposal(meals, new PreferenceMultiplier(getPreferenceMap()));

    Optional<UUID> proposeNextDesert = sut.proposeNextDesert(settings, main,
        new ArrayList<>());

    assertThat(proposeNextDesert)
        .contains(nameUUIDFromBytes("Test2Meal".getBytes(forName("UTF-8"))));
  }
}
