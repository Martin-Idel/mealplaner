// SPDX-License-Identifier: MIT

package etoetests.guitests.ingredients;

import static etoetests.CommonFunctions.getIngredient1;
import static etoetests.CommonFunctions.getIngredient2;
import static etoetests.CommonFunctions.getIngredient3;
import static mealplaner.model.recipes.IngredientBuilder.ingredient;
import static mealplaner.model.recipes.IngredientType.SPICE;
import static mealplaner.model.recipes.Measure.NONE;
import static mealplaner.model.recipes.Measures.createMeasures;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import etoetests.guitests.helpers.AssertJMealplanerTestCase;
import mealplaner.io.xml.IngredientsReader;
import mealplaner.model.recipes.Ingredient;
import mealplaner.plugins.PluginStore;

public class IngredientsInputTest extends AssertJMealplanerTestCase {

  @Test
  public void createIngredient() {
    Ingredient ingredient = ingredient()
        .withName("Test4")
        .withType(SPICE)
        .withMeasures(createMeasures(NONE))
        .create();

    windowHelpers.getIngredientsPane().addIngredient(ingredient);

    List<Ingredient> ingredientsAfterSaving = IngredientsReader
        .loadXml(DESTINATION_INGREDIENT_FILE_PATH, new PluginStore());

    assertThat(ingredientsAfterSaving)
        .hasSize(4)
        .contains(getIngredient1(), getIngredient2(), getIngredient3());
    // UUID is random upon creation, so we can't compare ingredients directly
    assertThat(ingredientsAfterSaving.get(3).getName()).isEqualTo(ingredient.getName());
    assertThat(ingredientsAfterSaving.get(3).getType()).isEqualTo(ingredient.getType());
    assertThat(ingredientsAfterSaving.get(3).getPrimaryMeasure()).isEqualTo(ingredient.getPrimaryMeasure());
  }
}
