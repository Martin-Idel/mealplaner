// SPDX-License-Identifier: MIT

package mealplaner.plugins.builtins.courses;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class CourseTypeSettingTest {
  @Test
  void equalsContract() {
    EqualsVerifier.forClass(CourseTypeSetting.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }
}
