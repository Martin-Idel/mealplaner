// SPDX-License-Identifier: MIT

package mealplaner.model.proposal;

import static java.util.UUID.nameUUIDFromBytes;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import mealplaner.commons.NonnegativeInteger;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class ProposalTest {
  @Test
  void proposalReturnsCorrectDateOfFirstEntry() {
    var dateOfFirstProposedMenu = LocalDate.of(2020, 1, 1);
    Proposal proposalToday = getProposalWithOneDay(true, dateOfFirstProposedMenu);
    Proposal proposalTomorrow = getProposalWithOneDay(false, dateOfFirstProposedMenu);

    assertThat(proposalToday.getDateOfFirstProposedItem()).isEqualTo(dateOfFirstProposedMenu);
    assertThat(proposalTomorrow.getDateOfFirstProposedItem()).isAfter(dateOfFirstProposedMenu);
  }

  @Test
  void proposalContainsCorrectData() {
    var dateOfFirstProposedMenu = LocalDate.of(2020, 1, 1);
    Proposal proposalToday = getProposalWithOneDay(true, dateOfFirstProposedMenu);

    assertThat(proposalToday.getTime()).isEqualTo(dateOfFirstProposedMenu);
    assertThat(proposalToday.getSize()).isEqualTo(1);
    assertThat(proposalToday.getItem(0)).isNotNull();
    assertThat(proposalToday.isToday()).isTrue();
  }

  private Proposal getProposalWithOneDay(boolean includeToday, LocalDate date) {
    return Proposal.from(includeToday, Collections.singletonList(ProposedMenu.threeCourseMeal(
        nameUUIDFromBytes("Entry".getBytes(StandardCharsets.UTF_8)),
        nameUUIDFromBytes("Main".getBytes(StandardCharsets.UTF_8)),
        nameUUIDFromBytes("Desert".getBytes(StandardCharsets.UTF_8)),
        NonnegativeInteger.FIVE)),
        date
    );
  }

  @Test
  void testToString() {
    assertThat(Proposal.class.getDeclaredFields()).hasSize(3);
    assertThat(Proposal.from(false, new ArrayList<>(), LocalDate.of(2020, 2, 2)).toString())
        .hasToString("Proposal{mealList=[], calendar=2020-02-02, includeToday=false}");
  }

  @Test
  void equalsContract() {
    EqualsVerifier.forClass(Proposal.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }
}
