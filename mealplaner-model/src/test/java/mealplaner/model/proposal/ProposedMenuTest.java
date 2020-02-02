// SPDX-License-Identifier: MIT

package mealplaner.model.proposal;

import static java.util.UUID.nameUUIDFromBytes;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import mealplaner.commons.NonnegativeInteger;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class ProposedMenuTest {
  @Test
  public void testToString() {
    assertThat(ProposedMenu.class.getDeclaredFields().length).isEqualTo(4);
    assertThat(ProposedMenu.proposed(
        Optional.of(nameUUIDFromBytes("Entry".getBytes(StandardCharsets.UTF_8))),
        nameUUIDFromBytes("Main".getBytes(StandardCharsets.UTF_8)),
        Optional.of(nameUUIDFromBytes("Desert".getBytes(StandardCharsets.UTF_8))),
        NonnegativeInteger.TWO).toString())
        .isEqualTo("ProposedMenu{entry=Optional[b948e8a0-2a7f-3dc9-b098-c89e8df9892c], "
            + "main=a02c83a7-dbd9-3295-beae-fb72c2bee2de, "
            + "desert=Optional[000c016d-34ff-31e2-85b6-9c67f22c83ff] for=2}");
  }

  @Test
  public void equalsContract() {
    EqualsVerifier.forClass(ProposedMenu.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }
}
