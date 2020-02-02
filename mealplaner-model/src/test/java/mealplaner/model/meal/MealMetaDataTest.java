// SPDX-License-Identifier: MIT

package mealplaner.model.meal;

import static org.assertj.core.api.Assertions.assertThat;
import static testcommonsmodel.CommonBaseFunctions.getMeal1;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class MealMetaDataTest {
  @Test
  public void testToString() {
    assertThat(getMeal1().getMetaData().toString())
        .isEqualTo("MealMetaData{name=Test1, mealFacts={}, hiddenMealFacts=[]}");
    assertThat(MealMetaData.class.getDeclaredFields().length).isEqualTo(3 + 1);  // one static field
  }

  @Test
  public void equalsContract() {
    EqualsVerifier.forClass(MealMetaData.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }
}
