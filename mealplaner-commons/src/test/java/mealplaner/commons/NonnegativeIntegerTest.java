// SPDX-License-Identifier: MIT

package mealplaner.commons;

import static mealplaner.commons.NonnegativeInteger.FOUR;
import static mealplaner.commons.NonnegativeInteger.THREE;
import static mealplaner.commons.NonnegativeInteger.TWO;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class NonnegativeIntegerTest {
  @Test
  public void throwsOnNegativeInteger() {
    assertThrows(NumberFormatException.class, () -> nonNegative(-3));
  }

  @Test
  public void correctlyAllowsOperationsIgnoringMathematicalPrecedence() {
    assertThat(THREE.add(TWO).multiplyBy(TWO).divideBy(FOUR))
        .isEqualTo(TWO);
  }

  @Test
  public void comparisonWorksCorrectly() {
    assertThat(THREE.compareTo(TWO)).isEqualTo(1);
    assertThat(TWO.compareTo(nonNegative(2))).isEqualTo(0);
    assertThat(TWO.compareTo(THREE)).isEqualTo(-1);
  }

  @Test
  public void testToString() {
    assertThat(THREE.toString()).isEqualTo("3");
  }

  @Test
  public void equalsContract() {
    EqualsVerifier.forClass(NonnegativeInteger.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }
}
