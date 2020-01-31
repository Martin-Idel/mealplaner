// SPDX-License-Identifier: MIT

package mealplaner.model.recipes;

import static mealplaner.model.recipes.IngredientBuilder.ingredientWithValidation;
import static mealplaner.model.recipes.IngredientType.BAKING_GOODS;
import static mealplaner.model.recipes.Measure.AMOUNT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.plugins.api.Fact;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import testcommonsmodel.HiddenIngredientFact;
import testcommonsmodel.TestIngredientFact;

public class IngredientTest {

  @Test
  public void validationWorksForSimpleFact() {
    Ingredient testIngredient = getTestIngredient();

    assertThat(testIngredient.getName()).isEqualTo("Test");
    assertThat(testIngredient.getType()).isEqualTo(BAKING_GOODS);
    assertThat(testIngredient.getMeasures().getPrimaryMeasure()).isEqualTo(AMOUNT);
    assertThat(testIngredient.getMeasures().getSecondaries()).isEmpty();
    assertThat(testIngredient.getTypedIngredientFact(TestIngredientFact.class).getTestEnum())
        .isEqualTo(TestIngredientFact.TestIngredientEnum.TEST_INGREDIENT_2);
  }

  @Test
  public void validationFailsIfFactIsMissing() {
    var set = new HashSet<Class<? extends Fact>>();
    set.add(TestIngredientFact.class);

    assertThrows(MealException.class, () -> ingredientWithValidation(set)
        .withName("Test")
        .withType(BAKING_GOODS)
        .withPrimaryMeasure(AMOUNT)
        .create());
  }

  @Test
  public void validationPassesIfAdditionalFactsArePresent() {
    var set = new HashSet<Class<? extends Fact>>();
    set.add(TestIngredientFact.class);

    var ingredients = ingredientWithValidation(set)
        .withName("Test")
        .withType(BAKING_GOODS)
        .addFact(new HiddenIngredientFact(HiddenIngredientFact.TestEnum.TEST1))
        .addFact(new TestIngredientFact(TestIngredientFact.TestIngredientEnum.TEST_INGREDIENT_2))
        .withPrimaryMeasure(AMOUNT)
        .create();

    assertThat(ingredients.getName()).isEqualTo("Test");
  }

  @Test
  public void copyingAndChangingMealFactsWorksAsExpected() {
    var ingredient = getTestIngredient();
    var changedIngredient = IngredientBuilder.from(ingredient)
        .changeFact(new TestIngredientFact(TestIngredientFact.TestIngredientEnum.TEST_INGREDIENT_1))
        .create();

    assertThat(changedIngredient.getName()).isEqualTo("Test");
    assertThat(changedIngredient.getType()).isEqualTo(BAKING_GOODS);
    assertThat(changedIngredient.getMeasures().getPrimaryMeasure()).isEqualTo(AMOUNT);
    assertThat(changedIngredient.getMeasures().getSecondaries()).isEmpty();
    assertThat(changedIngredient.getTypedIngredientFact(TestIngredientFact.class).getTestEnum())
        .isEqualTo(TestIngredientFact.TestIngredientEnum.TEST_INGREDIENT_1);
  }

  @Test
  public void equalsContract() {
    EqualsVerifier.forClass(Ingredient.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }

  private Ingredient getTestIngredient() {
    var set = new HashSet<Class<? extends Fact>>();
    set.add(TestIngredientFact.class);
    return ingredientWithValidation(set)
        .withName("Test")
        .withType(BAKING_GOODS)
        .addFact(new TestIngredientFact(TestIngredientFact.TestIngredientEnum.TEST_INGREDIENT_2))
        .withPrimaryMeasure(AMOUNT)
        .create();
  }
}
