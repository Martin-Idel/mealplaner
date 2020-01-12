// SPDX-License-Identifier: MIT

package mealplaner.model.meal;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class MealMetaDataTest {
  @Test
  public void equalsContract() {
    EqualsVerifier.forClass(MealMetaData.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }
}
