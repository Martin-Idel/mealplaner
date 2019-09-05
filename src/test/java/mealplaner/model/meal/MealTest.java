// SPDX-License-Identifier: MIT

package mealplaner.model.meal;

import static java.util.Optional.empty;
import static java.util.UUID.randomUUID;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.meal.Meal.createMeal;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getMeal2;

import org.junit.Before;
import org.junit.Test;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.meal.enums.CookingPreference;
import mealplaner.model.meal.enums.CookingTime;
import mealplaner.model.meal.enums.CourseType;
import mealplaner.model.meal.enums.ObligatoryUtensil;
import mealplaner.model.meal.enums.Sidedish;

public class MealTest {
  private Meal sut;

  @Before
  public void setup() throws MealException {
    sut = createMeal(randomUUID(),
        "Test",
        CookingTime.SHORT,
        Sidedish.PASTA,
        ObligatoryUtensil.POT,
        CookingPreference.NO_PREFERENCE,
        CourseType.MAIN,
        nonNegative(5),
        "",
        empty());
  }

  @Test
  public void valuesWithLimitationsWorkCorrectly() throws MealException {
    assertThat(sut.getName()).isEqualTo("Test");
    assertThat(sut.getDaysPassed()).isEqualTo(nonNegative(5));
  }

  @Test(expected = MealException.class)
  public void setNameWithOnlyWhitespace() throws MealException {
    sut = createMeal(randomUUID(),
        "",
        CookingTime.SHORT,
        Sidedish.PASTA,
        ObligatoryUtensil.POT,
        CookingPreference.NO_PREFERENCE,
        CourseType.MAIN,
        nonNegative(5),
        "",
        empty());

    assertThat(sut.getName()).isEqualTo("Test");
  }

  @Test
  public void compareToWithName() throws MealException {
    Meal compareMeal = getMeal2();

    int compareResult = sut.compareTo(compareMeal);

    assertThat(compareResult).isEqualTo(-1);
  }
}