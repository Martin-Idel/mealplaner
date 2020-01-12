// SPDX-License-Identifier: MIT

package mealplaner.model.proposal;

import org.junit.jupiter.api.Test;

import mealplaner.model.meal.MealMetaData;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class ProposedMenuTest {
  @Test
  public void equalsContract() {
    EqualsVerifier.forClass(ProposedMenu.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }
}
