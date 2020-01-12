// SPDX-License-Identifier: MIT

package mealplaner.model.proposal;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class ProposalOutlineTest {
  @Test
  public void equalsContract() {
    EqualsVerifier.forClass(ProposalOutline.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }
}
