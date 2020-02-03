// SPDX-License-Identifier: MIT

package mealplaner.model.proposal;

import static java.util.UUID.nameUUIDFromBytes;
import static mealplaner.model.proposal.ProposalOutline.ProposalOutlineBuilder.of;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import mealplaner.commons.NonnegativeInteger;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class ProposalTest {
  @Test
  public void proposalReturnsCorrectDateOfFirstEntry() {
    var dateOfFirstProposedMenu = LocalDate.of(2020, 1, 1);
    Proposal proposalToday = getProposalWithOneDay(true, dateOfFirstProposedMenu);
    Proposal proposalTomorrow = getProposalWithOneDay(false, dateOfFirstProposedMenu);

    assertThat(proposalToday.getDateOfFirstProposedItem()).isEqualTo(dateOfFirstProposedMenu);
    assertThat(proposalTomorrow.getDateOfFirstProposedItem()).isAfter(dateOfFirstProposedMenu);
  }

  @Test
  public void proposalContainsCorrectData() {
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
  public void testToString() {
    assertThat(Proposal.class.getDeclaredFields().length).isEqualTo(3);
    assertThat(Proposal.from(false, new ArrayList<>(), LocalDate.of(2020, 2, 2)).toString())
        .isEqualTo("Proposal{mealList=[], calendar=2020-02-02, includeToday=false}");
  }

  @Test
  public void equalsContract() {
    EqualsVerifier.forClass(Proposal.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }
}
