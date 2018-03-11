package mealplaner.model;

import static java.util.Optional.empty;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.Meal.createMeal;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getMeal2;

import org.junit.Before;
import org.junit.Test;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;

public class MealTest {

  private Meal sut;

  @Before
  public void setup() throws MealException {
    sut = createMeal("Test", CookingTime.SHORT, Sidedish.PASTA, ObligatoryUtensil.POT,
        CookingPreference.NO_PREFERENCE, nonNegative(5), "", empty());
  }

  @Test
  public void valuesWithLimitationsWorkCorrectly() throws MealException {
    assertThat(sut.getName()).isEqualTo("Test");
    assertThat(sut.getDaysPassed()).isEqualTo(nonNegative(5));
  }

  @Test(expected = MealException.class)
  public void setNameWithOnlyWhitespace() throws MealException {
    sut = createMeal("", CookingTime.SHORT, Sidedish.PASTA, ObligatoryUtensil.POT,
        CookingPreference.NO_PREFERENCE, nonNegative(5), "", empty());

    assertThat(sut.getName()).isEqualTo("Test");
  }

  @Test
  public void compareToWithName() throws MealException {
    Meal compareMeal = getMeal2();

    int compareResult = sut.compareTo(compareMeal);

    assertThat(compareResult).isEqualTo(-1);
  }
}