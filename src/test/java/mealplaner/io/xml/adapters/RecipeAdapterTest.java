package mealplaner.io.xml.adapters;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static mealplaner.io.xml.adapters.RecipeAdapter.convertRecipeFromXml;
import static mealplaner.io.xml.adapters.RecipeAdapter.convertRecipeToXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getRecipe1;
import static testcommons.CommonFunctions.getRecipe2;
import static testcommons.CommonFunctions.setupMealplanerDataWithAllIngredients;

import java.util.Optional;

import org.junit.Test;

import mealplaner.model.MealplanerData;
import mealplaner.model.recipes.Recipe;

public class RecipeAdapterTest {
  @Test
  public void adapterTest() {
    MealplanerData mealplan = setupMealplanerDataWithAllIngredients();
    Optional<Recipe> recipe1 = of(getRecipe1());
    Optional<Recipe> recipe2 = of(getRecipe2());

    Optional<Recipe> convertedRecipes1 = convertRecipeFromXml(mealplan,
        convertRecipeToXml(recipe1));
    Optional<Recipe> convertedRecipes2 = convertRecipeFromXml(mealplan,
        convertRecipeToXml(recipe2));

    assertThat(convertedRecipes1).isEqualTo(recipe1);
    assertThat(convertedRecipes2).isEqualTo(recipe2);
  }

  @Test
  public void correctlyHandlesEmptyOptionals() {
    MealplanerData mealplan = setupMealplanerDataWithAllIngredients();
    Optional<Recipe> emptyRecipe = empty();

    Optional<Recipe> empty = convertRecipeFromXml(mealplan,
        convertRecipeToXml(emptyRecipe));

    assertThat(empty).isEqualTo(emptyRecipe);
  }
}
