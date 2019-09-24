// SPDX-License-Identifier: MIT

package mealplaner.model.meal;

import static mealplaner.commons.NonnegativeInteger.FIVE;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.meal.enums.CookingPreference.NO_PREFERENCE;
import static mealplaner.model.meal.enums.CookingTime.SHORT;
import static mealplaner.model.meal.enums.CourseType.MAIN;
import static mealplaner.model.meal.enums.ObligatoryUtensil.POT;
import static mealplaner.model.meal.enums.Sidedish.PASTA;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getMeal2;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealFactXml;

public class MealTest {
  private Meal sut;

  @Before
  public void setup() throws MealException {
    var mealFacts = new HashMap<Class, MealFact>();
    mealFacts.put(TestMealFact.class, new TestMealFact("unmodifiable"));
    sut = MealBuilder.meal()
        .name("Test")
        .cookingTime(SHORT)
        .sidedish(PASTA)
        .obligatoryUtensil(POT)
        .cookingPreference(NO_PREFERENCE)
        .daysPassed(FIVE)
        .courseType(MAIN)
        .comment("")
        .addMealMap(mealFacts)
        .create();
  }

  @Test
  public void valuesWithLimitationsWorkCorrectly() throws MealException {
    assertThat(sut.getName()).isEqualTo("Test");
    assertThat(sut.getDaysPassed()).isEqualTo(nonNegative(5));
  }

  @Test(expected = MealException.class)
  public void setNameWithOnlyWhitespace() throws MealException {
    sut = MealBuilder.meal()
        .name("")
        .cookingTime(SHORT)
        .sidedish(PASTA)
        .obligatoryUtensil(POT)
        .cookingPreference(NO_PREFERENCE)
        .daysPassed(FIVE)
        .courseType(MAIN)
        .comment("")
        .create();

    assertThat(sut.getName()).isEqualTo("Test");
  }

  @Test(expected = MealException.class)
  public void validationFailsForMealWithMissingFacts() throws MealException {
    var pluginStore = new PluginStore();
    pluginStore.registerMealExtension(SomeFact.class, SomeFact.class, SomeFact::new);
    sut = MealBuilder.mealWithValidator(pluginStore)
        .name("")
        .cookingTime(SHORT)
        .sidedish(PASTA)
        .obligatoryUtensil(POT)
        .cookingPreference(NO_PREFERENCE)
        .daysPassed(FIVE)
        .courseType(MAIN)
        .comment("")
        .create();
  }

  @Test
  public void compareToWithName() throws MealException {
    Meal compareMeal = getMeal2();

    int compareResult = sut.compareTo(compareMeal);

    assertThat(compareResult).isEqualTo(-1);
  }

  @Test
  public void getTypedMealFactReturnsCorrectlyTypedFact() {
    TestMealFact mealFact = sut.getTypedMealFact(TestMealFact.class);

    assertThat(mealFact.testString).startsWith("unmodifiable");
  }

  @Test
  public void getMealFactsReturnsListWhichCannotBeModified() {
    var mealFacts = sut.getMealFacts();

    mealFacts.computeIfPresent(TestMealFact.class, (clazz, mealFact) -> new TestMealFact("modified"));

    assertThat(sut.getTypedMealFact(TestMealFact.class).testString).startsWith("unmodifiable");
  }

  private static class TestMealFact implements MealFact {
    public final String testString;

    TestMealFact(String testString) {
      this.testString = testString;
    }
  }

  private static class SomeFact implements MealFact, MealFactXml {
  }
}