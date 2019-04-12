// SPDX-License-Identifier: MIT

package guitests.ingredients;

import static mealplaner.model.recipes.Ingredient.ingredient;
import static mealplaner.model.recipes.IngredientType.SPICE;
import static mealplaner.model.recipes.Measure.NONE;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getIngredient1;
import static testcommons.CommonFunctions.getIngredient2;
import static testcommons.CommonFunctions.getIngredient3;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import guitests.helpers.AssertJMealplanerTestCase;
import mealplaner.io.xml.IngredientsReader;
import mealplaner.model.recipes.Ingredient;

public class IngredientsInputTest extends AssertJMealplanerTestCase {

  @Test
  public void createIngredient() {
    Ingredient ingredient = ingredient("Test4", SPICE, NONE);

    windowHelpers.getIngredientsPane().addIngredient(ingredient);

    List<Ingredient> ingredientsAfterSaving = IngredientsReader
        .loadXml(DESTINATION_INGREDIENT_FILE_PATH);

    assertThat(ingredientsAfterSaving).hasSize(4);
    assertThat(ingredientsAfterSaving).contains(getIngredient1(),
        getIngredient2(),
        getIngredient3());
    // UUID is random upon creation, so we can't compare ingredients directly
    assertThat(ingredientsAfterSaving.get(3).getName()).isEqualTo(ingredient.getName());
    assertThat(ingredientsAfterSaving.get(3).getType()).isEqualTo(ingredient.getType());
    assertThat(ingredientsAfterSaving.get(3).getMeasure()).isEqualTo(ingredient.getMeasure());
  }
}
