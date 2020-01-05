// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static mealplaner.io.xml.adapters.RecipeAdapter.convertRecipeV3FromXml;
import static mealplaner.io.xml.adapters.RecipeAdapter.convertRecipeV3ToXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonBaseFunctions.getRecipe1;
import static testcommons.CommonBaseFunctions.getRecipe2;
import static testcommons.CommonBaseFunctions.setupMealplanerDataWithAllIngredients;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import mealplaner.model.MealplanerData;
import mealplaner.model.recipes.Recipe;

public class RecipeAdapterTest {
  @Test
  public void adapterTest() {
    MealplanerData mealplan = setupMealplanerDataWithAllIngredients();
    Optional<Recipe> recipe1 = of(getRecipe1());
    Optional<Recipe> recipe2 = of(getRecipe2());

    Optional<Recipe> convertedRecipes1 = convertRecipeV3FromXml(mealplan,
        convertRecipeV3ToXml(recipe1));
    Optional<Recipe> convertedRecipes2 = convertRecipeV3FromXml(mealplan,
        convertRecipeV3ToXml(recipe2));

    assertThat(convertedRecipes1).isEqualTo(recipe1);
    assertThat(convertedRecipes2).isEqualTo(recipe2);
  }

  @Test
  public void correctlyHandlesEmptyOptionals() {
    MealplanerData mealplan = setupMealplanerDataWithAllIngredients();
    Optional<Recipe> emptyRecipe = empty();

    Optional<Recipe> empty = convertRecipeV3FromXml(mealplan,
        convertRecipeV3ToXml(emptyRecipe));

    assertThat(empty).isEqualTo(emptyRecipe);
  }
}
