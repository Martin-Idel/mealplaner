// SPDX-License-Identifier: MIT

package mealplaner.commons;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class PairTest {
  @Test
  public void equalsContract() {
    EqualsVerifier.forClass(Pair.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }
}
