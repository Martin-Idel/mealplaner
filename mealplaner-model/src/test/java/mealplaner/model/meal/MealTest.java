// SPDX-License-Identifier: MIT

package mealplaner.model.meal;

import static mealplaner.commons.NonnegativeInteger.FIVE;
import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.model.meal.MealBuilder.mealWithValidator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static testcommonsmodel.CommonBaseFunctions.getMeal1;
import static testcommonsmodel.CommonBaseFunctions.getMeal2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealFactXml;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import testcommonsmodel.HiddenMealFact;

public class MealTest {
  private Meal sut;

  @BeforeEach
  public void setUp() throws MealException {
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
  public void copyingMealCopiesAllAspects() throws MealException {
    var meal = getMeal1();
    var copiedMeal = MealBuilder.from(meal).changeFact(new SomeFact()).create();

    assertThat(copiedMeal.getId()).isEqualTo(meal.getId());
    assertThat(copiedMeal.getDaysPassed()).isEqualTo(meal.getDaysPassed());
    assertThat(copiedMeal.getDaysPassedAsInteger()).isEqualTo(meal.getDaysPassed().value);
    assertThat(copiedMeal.getMetaData().getName()).isEqualTo(meal.getMetaData().getName());
    assertThat(copiedMeal.getMealFact(SomeFact.class)).isNotNull();
  }

  @Test
  public void validationFailsForMealWithMissingFacts() throws MealException {
    var pluginStore = new PluginStore();
    pluginStore.registerMealExtension(SomeFact.class, SomeFact.class, SomeFact::new);
    pluginStore.registerMealExtension(HiddenMealFact.class, HiddenMealFact.class, HiddenMealFact::new);
    assertThrows(MealException.class, () -> mealWithValidator(pluginStore)
        .name("Test")
        .addFact(new HiddenMealFact(HiddenMealFact.HiddenEnum.TEST1))
        .create());
  }

  @Test
  public void validationSucceedsIfAllFactsArePresent() throws MealException {
    var pluginStore = new PluginStore();
    pluginStore.registerMealExtension(SomeFact.class, SomeFact.class, SomeFact::new);

    var meal = mealWithValidator(pluginStore)
        .name("Test")
        .addFact(new SomeFact())
        .create();

    assertThat(meal.getMetaData().getName()).isEqualTo("Test");
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

  @Test
  public void testToString() {
    assertThat(getMeal1().toString())
        .isEqualTo("Meal{uuid=0e6db2d7-8818-31ff-80d4-c21e0f2f4a7b, "
            + "metadata=MealMetaData{name=Test1, mealFacts={}, hiddenMealFacts=[]}, "
            + "daysPassed=5, recipe=Optional.empty}");
    assertThat(Meal.class.getDeclaredFields().length).isEqualTo(4 + 1);  // one static field
  }

  @Test
  public void equalsContract() {
    EqualsVerifier.forClass(Meal.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
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