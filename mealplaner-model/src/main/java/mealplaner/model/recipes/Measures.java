// SPDX-License-Identifier: MIT

package mealplaner.model.recipes;

import static mealplaner.commons.NonnegativeFraction.ONE;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.commons.errorhandling.MealException;

public class Measures {
  public static final Measures DEFAULT_MEASURES = new Measures();
  private final Measure primary;
  private final Map<Measure, NonnegativeFraction> secondaries;

  private Measures() {
    this(Measure.NONE, new HashMap<>());
  }

  private Measures(Measure primary, Map<Measure, NonnegativeFraction> secondaries) {
    this.primary = primary;
    this.secondaries = secondaries;
  }

  public static Measures createMeasures(Measure primary) {
    return new Measures(primary, new HashMap<>());
  }

  public static Measures createMeasures(Measure primary, Map<Measure, NonnegativeFraction> secondaries) {
    return new Measures(primary, secondaries);
  }

  public Measure getPrimaryMeasure() {
    return primary;
  }

  public Map<Measure, NonnegativeFraction> getSecondaries() {
    return secondaries;
  }

  public boolean contains(Measure measure) {
    return measure.equals(primary) || secondaries.containsKey(measure);
  }

  public NonnegativeFraction getConversionFactor(Measure measureFrom, Measure measureTo) {
    var conversionMeasureFromToPrimary = measureFrom.equals(primary) ? ONE : secondaries.get(measureFrom);
    var conversionMeasureToToPrimary = measureTo.equals(primary) ? ONE : secondaries.get(measureTo);
    if (conversionMeasureFromToPrimary == null) {
      throw new MealException("Measure " + measureFrom + " is not contained in secondaries");
    } else if (conversionMeasureToToPrimary == null) {
      throw new MealException("Measure " + measureTo + " is not contained in secondaries");
    }
    return conversionMeasureFromToPrimary.multiplyBy(conversionMeasureToToPrimary.invert());
  }

  @Override
  public String toString() {
    return "Measures{" + "primary=" + primary + ", secondaries=" + secondaries + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Measures measures = (Measures) o;
    return primary == measures.primary
        && secondaries.equals(measures.secondaries);
  }

  @Override
  public int hashCode() {
    return Objects.hash(primary, secondaries);
  }
}
