// SPDX-License-Identifier: MIT

package mealplaner.model.meal;

import static mealplaner.commons.NonnegativeInteger.FIVE;
import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.model.meal.MealBuilder.mealWithValidator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static testcommonsmodel.CommonBaseFunctions.getMeal2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealFactXml;

public class MealTest {
  private Meal sut;

  @BeforeEach
  public void setup() throws MealException {
    sut = meal()
        .name("Test")
        .daysPassed(FIVE)
        .addFact(new TestMealFact("unmodifiable"))
        .create();
  }

  @Test
  public void setNameThrowsForEmptyNames() throws MealException {
    assertThrows(MealException.class, () -> meal()
        .name("")
        .daysPassed(FIVE)
        .create());
  }

  @Test
  public void validationFailsForMealWithMissingFacts() throws MealException {
    var pluginStore = new PluginStore();
    pluginStore.registerMealExtension(SomeFact.class, SomeFact.class, SomeFact::new);
    assertThrows(MealException.class, () -> mealWithValidator(pluginStore)
        .name("")
        .addFact(new HiddenMealFact(HiddenMealFact.HiddenEnum.TEST1))
        .create());
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
    final String testString;

    TestMealFact(String testString) {
      this.testString = testString;
    }
  }

  private static class SomeFact implements MealFact, MealFactXml {
  }
}