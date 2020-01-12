// SPDX-License-Identifier: MIT

package mealplaner.commons;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class NonnegativeIntegerTest {
  @Test
  public void equalsContract() {
    EqualsVerifier.forClass(NonnegativeInteger.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }
}
