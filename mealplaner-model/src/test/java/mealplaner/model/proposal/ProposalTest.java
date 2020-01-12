// SPDX-License-Identifier: MIT

package mealplaner.model.proposal;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class ProposalTest {
  @Test
  public void equalsContract() {
    EqualsVerifier.forClass(Proposal.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }
}
