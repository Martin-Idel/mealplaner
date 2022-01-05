// SPDX-License-Identifier: MIT

package mealplaner.commons;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class PairTest {
  @Test
  void mapRightLeftAllowsSimpleMap() {
    var pair = Pair.of(NonnegativeInteger.nonNegative(3), NonnegativeFraction.fraction(1, 3));
    var mappedPair = pair.mapLeft(NonnegativeInteger::toString).mapRight(NonnegativeFraction::getDenominator);

    assertThat(mappedPair.left).isEqualTo("3");
    assertThat(mappedPair.right).isEqualTo(3);
  }

  @Test
  void equalsContract() {
    EqualsVerifier.forClass(Pair.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }
}
