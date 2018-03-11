package mealplaner.xml.adapters;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static mealplaner.xml.adapters.RecipeAdapter.convertRecipeFromXml;
import static mealplaner.xml.adapters.RecipeAdapter.convertRecipeToXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getRecipe1;
import static testcommons.CommonFunctions.getRecipe2;

import java.util.Optional;

import org.junit.Test;

import mealplaner.recipes.model.Recipe;

public class RecipeAdapterTest {
  @Test
  public void adapterTest() {
    Optional<Recipe> recipe1 = of(getRecipe1());
    Optional<Recipe> recipe2 = of(getRecipe2());

    Optional<Recipe> convertedRecipes1 = convertRecipeFromXml(
        convertRecipeToXml(recipe1));
    Optional<Recipe> convertedRecipes2 = convertRecipeFromXml(
        convertRecipeToXml(recipe2));

    assertThat(convertedRecipes1).isEqualTo(recipe1);
    assertThat(convertedRecipes2).isEqualTo(recipe2);
  }

  @Test
  public void correctlyHandlesEmptyOptionals() {
    Optional<Recipe> emptyRecipe = empty();

    Optional<Recipe> empty = convertRecipeFromXml(
        convertRecipeToXml(emptyRecipe));

    assertThat(empty).isEqualTo(emptyRecipe);
  }
}
