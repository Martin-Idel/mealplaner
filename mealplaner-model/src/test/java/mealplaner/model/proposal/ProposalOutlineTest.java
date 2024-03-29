// SPDX-License-Identifier: MIT

package mealplaner.model.proposal;

import static mealplaner.model.proposal.ProposalOutline.ProposalOutlineBuilder.of;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class ProposalOutlineTest {
  @Test
  void proposalOutlineBuildingSetsValuesCorrectly() {
    ProposalOutline proposalOutline = of(5, LocalDate.of(2020, 1, 1))
        .takeDefaultSettings()
        .randomise()
        .includeToday()
        .build();

    assertThat(proposalOutline.isIncludedToday()).isTrue();
    assertThat(proposalOutline.isShallBeRandomised()).isTrue();
    assertThat(proposalOutline.takeDefaultSettings()).isTrue();
    assertThat(proposalOutline.getNumberOfDays()).isEqualTo(5);
    assertThat(proposalOutline.getDateToday()).isEqualTo(LocalDate.of(2020, 1, 1));
  }

  @Test
  void testToString() {
    assertThat(ProposalOutline.class.getDeclaredFields()).hasSize(5);
    assertThat(of(5, LocalDate.of(2020, 1, 1)).build().toString())
        .hasToString("ProposalOutline{numberOfDays=5, includedToday=false, shallBeRandomised=false, "
            + "dateToday=2020-01-01, takeDefaultSettings=false}");
  }

  @Test
  void equalsContract() {
    EqualsVerifier.forClass(ProposalOutline.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }
}
