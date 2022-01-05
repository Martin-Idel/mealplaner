// SPDX-License-Identifier: MIT

package mealplaner.model.recipes;

import static mealplaner.commons.NonnegativeFraction.ONE;
import static mealplaner.commons.NonnegativeFraction.wholeNumber;
import static mealplaner.commons.NonnegativeInteger.FOUR;
import static mealplaner.model.recipes.IngredientBuilder.ingredient;
import static mealplaner.model.recipes.IngredientBuilder.ingredientWithValidation;
import static mealplaner.model.recipes.IngredientType.BAKING_GOODS;
import static mealplaner.model.recipes.Measure.AMOUNT;
import static mealplaner.model.recipes.Measure.GRAM;
import static mealplaner.model.recipes.Measure.MILLILITRE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.EnumMap;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.plugins.api.Fact;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import testcommonsmodel.HiddenIngredientFact;
import testcommonsmodel.TestIngredientFact;

class IngredientTest {

  @Test
  void validationWorksForSimpleFact() {
    Ingredient testIngredient = getTestIngredient();

    assertThat(testIngredient.getName()).isEqualTo("Test");
    assertThat(testIngredient.getType()).isEqualTo(BAKING_GOODS);
    assertThat(testIngredient.getMeasures().getPrimaryMeasure()).isEqualTo(AMOUNT);
    assertThat(testIngredient.getMeasures().getSecondaries()).isEmpty();
    assertThat(testIngredient.getTypedIngredientFact(TestIngredientFact.class).getTestEnum())
        .isEqualTo(TestIngredientFact.TestIngredientEnum.TEST_INGREDIENT_2);
    assertThat(((TestIngredientFact) testIngredient.getIngredientFact(TestIngredientFact.class))
        .getTestEnum()).isEqualTo(TestIngredientFact.TestIngredientEnum.TEST_INGREDIENT_2);
  }

  @Test
  void validationFailsIfFactIsMissing() {
    var set = new HashSet<Class<? extends Fact>>();
    set.add(TestIngredientFact.class);

    assertThrows(MealException.class, () -> ingredientWithValidation(set)
        .withName("Test")
        .withType(BAKING_GOODS)
        .withPrimaryMeasure(AMOUNT)
        .create());
  }

  @Test
  void nameMustNotBeEmpty() {
    assertThrows(MealException.class, () -> ingredient()
        .withName("")
        .withType(BAKING_GOODS)
        .withPrimaryMeasure(AMOUNT)
        .create());
  }

  @Test
  void validationPassesIfAdditionalFactsArePresent() {
    var set = new HashSet<Class<? extends Fact>>();
    set.add(TestIngredientFact.class);

    var ingredients = ingredientWithValidation(set)
        .withName("Test")
        .withType(BAKING_GOODS)
        .addFact(new HiddenIngredientFact(HiddenIngredientFact.TestEnum.TEST1))
        .addFact(new TestIngredientFact(TestIngredientFact.TestIngredientEnum.TEST_INGREDIENT_2))
        .withPrimaryMeasure(AMOUNT)
        .withPrimaryMeasure(AMOUNT)
        .create();

    assertThat(ingredients.getName()).isEqualTo("Test");
  }

  @Test
  void copyingAndChangingMealFactsWorksAsExpected() {
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
  void addingSecondaryIngredientsOneByOneAddsThemAll() {
    var testIngredient = ingredient()
        .withName("Test")
        .withType(BAKING_GOODS)
        .withPrimaryMeasure(AMOUNT)
        .withSecondaryMeasure(Measure.GRAM)
        .measureInPrimaryMeasure(wholeNumber(FOUR))
        .withSecondaryMeasure(MILLILITRE)
        .measureInPrimaryMeasure(ONE)
        .withSecondaryMeasure(AMOUNT)
        .measureInPrimaryMeasure(ONE)
        .create();

    assertThat(testIngredient.getMeasures().getPrimaryMeasure()).isEqualTo(AMOUNT);
    assertThat(testIngredient.getMeasures().getSecondaries()).hasSize(2);
    var secondaries = testIngredient.getMeasures().getSecondaries();
    var expected = new EnumMap<Measure, NonnegativeFraction>(Measure.class);
    expected.put(GRAM, wholeNumber(FOUR));
    expected.put(MILLILITRE, ONE);
    assertThat(secondaries).containsAllEntriesOf(expected);
  }

  @Test
  void addingSecondariesOnBulkOverrides() {
    var expected = new EnumMap<Measure, NonnegativeFraction>(Measure.class);
    expected.put(GRAM, wholeNumber(FOUR));
    expected.put(MILLILITRE, ONE);

    var testIngredient = ingredient()
        .withName("Test")
        .withType(BAKING_GOODS)
        .withPrimaryMeasure(AMOUNT)
        .withSecondaryMeasures(expected)
        .create();

    assertThat(testIngredient.getMeasures().getPrimaryMeasure()).isEqualTo(AMOUNT);
    assertThat(testIngredient.getMeasures().getSecondaries()).hasSize(2);
    var secondaries = testIngredient.getMeasures().getSecondaries();
    assertThat(secondaries).containsAllEntriesOf(expected);
  }

  @Test
  void equalsContract() {
    EqualsVerifier.forClass(Ingredient.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }

  @Test
  void toStringTest() {
    var ingredient = getTestIngredient();

    assertThat(ingredient.toString()).isEqualTo("Ingredient{uuid=" + ingredient.getId() + ", "
        + "name='Test', type=" + BAKING_GOODS.toString() + ", measures=Measures{primary=" + AMOUNT
        + ", secondaries={}}, ingredientFacts={class testcommonsmodel.TestIngredientFact=TEST_INGREDIENT_2}, "
        + "hiddenIngredientFacts=[]}");
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
