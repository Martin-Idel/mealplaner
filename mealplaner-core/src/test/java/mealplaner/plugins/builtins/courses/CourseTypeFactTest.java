// SPDX-License-Identifier: MIT

package mealplaner.plugins.builtins.courses;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class CourseTypeFactTest {
  @Test
  public void equalsContract() {
    EqualsVerifier.forClass(CourseTypeFact.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }
}
