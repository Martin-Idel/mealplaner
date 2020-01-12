// SPDX-License-Identifier: MIT

package mealplaner.model.recipes;

import static mealplaner.commons.NonnegativeFraction.fraction;
import static mealplaner.model.recipes.Measure.GRAM;
import static mealplaner.model.recipes.Measure.MILLILITRE;
import static mealplaner.model.recipes.Measure.TABLESPOON;
import static mealplaner.model.recipes.Measures.createMeasures;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.meal.Meal;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class MeasuresTest {

  @Test
  public void getConversionFactorConvertsCorrectlyToPrimaryMeasure() {
    var secondaries = new HashMap<Measure, NonnegativeFraction>();
    secondaries.put(TABLESPOON, fraction(1, 2));
    Measures measures = createMeasures(MILLILITRE, secondaries);

    assertThat(measures.getConversionFactor(TABLESPOON, MILLILITRE))
        .isEqualTo(fraction(1, 2));
  }

  @Test
  public void getConversionFactorConvertsCorrectlyFromPrimaryMeasure() {
    var secondaries = new HashMap<Measure, NonnegativeFraction>();
    secondaries.put(TABLESPOON, fraction(1, 2));
    Measures measures = createMeasures(MILLILITRE, secondaries);

    assertThat(measures.getConversionFactor(MILLILITRE, TABLESPOON))
        .isEqualTo(fraction(2, 1));
  }

  @Test
  public void getConversionFactorConvertsCorrectlyBetweenMeasures() {
    var secondaries = new HashMap<Measure, NonnegativeFraction>();
    secondaries.put(TABLESPOON, fraction(1, 2));
    secondaries.put(GRAM, fraction(1, 4));
    Measures measures = createMeasures(MILLILITRE, secondaries);

    assertThat(measures.getConversionFactor(GRAM, TABLESPOON))
        .isEqualTo(fraction(1, 2));
  }

  @Test
  public void getConversionFactorThrowsExceptionWhenFromMeasureIsNotContainedInMeasures() {
    var secondaries = new HashMap<Measure, NonnegativeFraction>();
    secondaries.put(TABLESPOON, fraction(1, 2));
    Measures measures = createMeasures(MILLILITRE, secondaries);

    assertThrows(MealException.class, () -> measures.getConversionFactor(GRAM, TABLESPOON));
  }

  @Test
  public void equalsContract() {
    EqualsVerifier.forClass(Measures.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }
}
