// SPDX-License-Identifier: MIT

package mealplaner.model.recipes;

import static mealplaner.commons.NonnegativeFraction.fraction;
import static mealplaner.model.recipes.QuantitativeIngredient.createQuantitativeIngredient;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommonsmodel.CommonBaseFunctions.getIngredient4;

import java.security.InvalidParameterException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class QuantitativeIngredientTest {

  @Test
  public void convertToMeasureMultipliesAmounts() {
    var ingredient = getIngredient4();

    var quantitativeIngredient = createQuantitativeIngredient(
        ingredient, Measure.TEASPOON, fraction(100, 1));

    assertThat(quantitativeIngredient.convertToPrimaryMeasure())
        .isEqualTo(createQuantitativeIngredient(ingredient, Measure.GRAM, fraction(50, 1)));
  }

  @Test
  public void constructionThrowsIfMeasureIsNotKnownToIngredient() {
    var ingredient = getIngredient4();

    Assertions.assertThrows(InvalidParameterException.class, () -> createQuantitativeIngredient(
        ingredient, Measure.AMOUNT, fraction(100, 1)));
  }

  @Test
  public void equalsContract() {
    EqualsVerifier.forClass(QuantitativeIngredient.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }

  @Test
  public void toStringTest() {
    var ingredient = getIngredient4();

    var quantitativeIngredient = createQuantitativeIngredient(
        ingredient, Measure.TEASPOON, fraction(100, 1));

    assertThat(quantitativeIngredient.toString()).isEqualTo(
        "QuantitativeIngredient{ingredient=Ingredient{uuid=41d5e808-720c-3ee7-9257-214e952a6721, "
            + "name='Test4', type=Meat products, measures=Measures{primary=g, secondaries={tsp=1/2}}, "
            + "ingredientFacts={}, hiddenIngredientFacts=[]}, measure=tsp, amount=100}");
    assertThat(QuantitativeIngredient.class.getDeclaredFields().length).isEqualTo(3 + 1); // one static field
  }
}
