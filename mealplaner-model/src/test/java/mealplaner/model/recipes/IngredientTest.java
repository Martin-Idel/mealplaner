// SPDX-License-Identifier: MIT

package mealplaner.model.recipes;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class IngredientTest {
  @Test
  public void equalsContract() {
    EqualsVerifier.forClass(Ingredient.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }
}
