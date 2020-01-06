// SPDX-License-Identifier: MIT

package mealplaner.model.recipes;

import static mealplaner.commons.NonnegativeFraction.fraction;
import static mealplaner.model.recipes.QuantitativeIngredient.createQuantitativeIngredient;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommonsmodel.CommonBaseFunctions.getIngredient4;

import org.junit.jupiter.api.Test;

public class QuantitativeIngredientTest {

  @Test
  public void convertToMeasureMultipliesAmounts() {
    var ingredient = getIngredient4();

    var quantitativeIngredient = createQuantitativeIngredient(
        ingredient, Measure.TEASPOON, fraction(100, 1));

    assertThat(quantitativeIngredient.convertToPrimaryMeasure())
        .isEqualTo(createQuantitativeIngredient(ingredient, Measure.GRAM, fraction(50, 1)));
  }
}
