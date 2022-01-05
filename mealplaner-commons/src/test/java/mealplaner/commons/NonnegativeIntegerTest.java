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

class NonnegativeIntegerTest {
  @Test
  void throwsOnNegativeInteger() {
    assertThrows(NumberFormatException.class, () -> nonNegative(-3));
  }

  @Test
  void correctlyAllowsOperationsIgnoringMathematicalPrecedence() {
    assertThat(THREE.add(TWO).multiplyBy(TWO).divideBy(FOUR))
        .isEqualTo(TWO);
  }

  @Test
  void comparisonWorksCorrectly() {
    assertThat(THREE.compareTo(TWO)).isEqualTo(1);
    assertThat(TWO).isEqualByComparingTo(nonNegative(2));
    assertThat(TWO.compareTo(THREE)).isEqualTo(-1);
  }

  @Test
  void testToString() {
    assertThat(THREE.toString()).hasToString("3");
  }

  @Test
  void equalsContract() {
    EqualsVerifier.forClass(NonnegativeInteger.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }
}
