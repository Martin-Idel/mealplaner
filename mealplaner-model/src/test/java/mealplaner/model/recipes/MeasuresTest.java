// SPDX-License-Identifier: MIT

package mealplaner.model.recipes;

import static mealplaner.commons.NonnegativeFraction.fraction;
import static mealplaner.model.recipes.Measure.GRAM;
import static mealplaner.model.recipes.Measure.MILLILITRE;
import static mealplaner.model.recipes.Measure.TABLESPOON;
import static mealplaner.model.recipes.Measures.createMeasures;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.EnumMap;

import org.junit.jupiter.api.Test;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.commons.errorhandling.MealException;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class MeasuresTest {

  @Test
  void getConversionFactorConvertsCorrectlyToPrimaryMeasure() {
    var secondaries = new EnumMap<Measure, NonnegativeFraction>(Measure.class);
    secondaries.put(TABLESPOON, fraction(1, 2));
    Measures measures = createMeasures(MILLILITRE, secondaries);

    assertThat(measures.getConversionFactor(TABLESPOON, MILLILITRE))
        .isEqualTo(fraction(1, 2));
  }

  @Test
  void getConversionFactorConvertsCorrectlyFromPrimaryMeasure() {
    var secondaries = new EnumMap<Measure, NonnegativeFraction>(Measure.class);
    secondaries.put(TABLESPOON, fraction(1, 2));
    Measures measures = createMeasures(MILLILITRE, secondaries);

    assertThat(measures.getConversionFactor(MILLILITRE, TABLESPOON))
        .isEqualTo(fraction(2, 1));
  }

  @Test
  void getConversionFactorConvertsCorrectlyBetweenMeasures() {
    var secondaries = new EnumMap<Measure, NonnegativeFraction>(Measure.class);
    secondaries.put(TABLESPOON, fraction(1, 2));
    secondaries.put(GRAM, fraction(1, 4));
    Measures measures = createMeasures(MILLILITRE, secondaries);

    assertThat(measures.getConversionFactor(GRAM, TABLESPOON))
        .isEqualTo(fraction(1, 2));
  }

  @Test
  void getConversionFactorThrowsExceptionWhenFromMeasureIsNotContainedInMeasures() {
    var secondaries = new EnumMap<Measure, NonnegativeFraction>(Measure.class);
    secondaries.put(TABLESPOON, fraction(1, 2));
    Measures measures = createMeasures(MILLILITRE, secondaries);

    assertThrows(MealException.class, () -> measures.getConversionFactor(GRAM, TABLESPOON));
  }

  @Test
  void getConversionFactorThrowsExceptionWhenToMeasureIsNotContainedInMeasures() {
    var secondaries = new EnumMap<Measure, NonnegativeFraction>(Measure.class);
    secondaries.put(TABLESPOON, fraction(1, 2));
    Measures measures = createMeasures(MILLILITRE, secondaries);

    assertThrows(MealException.class, () -> measures.getConversionFactor(TABLESPOON, GRAM));
  }

  @Test
  void createMeasuresRemovesPrimaryMeasuresFromSecondaries() {
    var secondaries = new EnumMap<Measure, NonnegativeFraction>(Measure.class);
    secondaries.put(TABLESPOON, fraction(1, 2));
    secondaries.put(MILLILITRE, fraction(1, 2));
    Measures measures = createMeasures(MILLILITRE, secondaries);

    assertThat(measures.getSecondaries()).hasSize(1);
    assertThat(measures.getSecondaries()).containsKey(TABLESPOON);
  }

  @Test
  void equalsContract() {
    EqualsVerifier.forClass(Measures.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }

  @Test
  void testToString() {
    assertThat(Measures.DEFAULT_MEASURES.toString()).hasToString("Measures{primary=-, secondaries={}}");
    assertThat(Measures.class.getDeclaredFields()).hasSize(2 + 1); // one static field
  }
}
