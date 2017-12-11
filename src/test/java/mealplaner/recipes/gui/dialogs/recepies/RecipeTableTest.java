package mealplaner.recipes.gui.dialogs.recepies;

import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static testcommons.CommonFunctions.getIngredient1;
import static testcommons.CommonFunctions.getIngredient2;
import static testcommons.CommonFunctions.getIngredient3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JTable;

import org.junit.Before;
import org.junit.Test;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.Measure;
import mealplaner.recipes.model.Recipe;
import mealplaner.recipes.provider.IngredientProvider;

public class RecipeTableTest {
  private IngredientProvider provider;

  private RecipeTable recipeTable;

  @Before
  public void setUp() {
    provider = mock(IngredientProvider.class);
    List<Ingredient> ingredients = Arrays.asList(getIngredient1(), getIngredient2(),
        getIngredient3());
    when(provider.getIngredients()).thenReturn(ingredients);
    when(provider.size()).thenReturn(3);
  }

  @Test
  public void recipeGetsDisplayedCorrectly() {
    Recipe recipe = createStandardRecipe();
    recipeTable = new RecipeTable(recipe, provider);

    JTable table = recipeTable.setupTable().getTable();

    assertThat(table.getRowCount()).isEqualTo(3);
    assertThat(table.getValueAt(0, 0)).isEqualTo(getIngredient1().getName());
    assertThat(table.getValueAt(0, 1)).isEqualTo(nonNegative(100));
    assertThat(table.getValueAt(1, 0)).isEqualTo(getIngredient2().getName());
    assertThat(table.getValueAt(1, 1)).isEqualTo(nonNegative(50));
  }

  @Test
  public void recipeGetsReturnedCorrectly() {
    Recipe recipe = createStandardRecipe();
    recipeTable = new RecipeTable(recipe, provider);
    recipeTable.setupTable();

    Optional<Recipe> returned = recipeTable.getRecipe(nonNegative(1));

    assertThat(returned.get()).isEqualTo(recipe);
  }

  @Test
  public void addingAnIngredientWorksCorrectly() {
    Recipe recipe = createStandardRecipe();
    recipeTable = new RecipeTable(recipe, provider);
    JTable table = recipeTable.setupTable().getTable();

    table.setValueAt("Test3", 2, 0);

    Map<Ingredient, NonnegativeInteger> recipeMap = new HashMap<>();
    recipeMap.put(getIngredient1(), nonNegative(100));
    recipeMap.put(getIngredient2(), nonNegative(50));
    recipeMap.put(getIngredient3(), nonNegative(0));
    Recipe expectedRecipe = Recipe.from(nonNegative(1), recipeMap);
    Optional<Recipe> returned = recipeTable.getRecipe(nonNegative(1));
    assertThat(table.getRowCount()).isEqualTo(4);
    assertThat(returned.get()).isEqualTo(expectedRecipe);
  }

  @Test
  public void changingAnIngredientWorksCorrectly() {
    Recipe recipe = createStandardRecipe();
    recipeTable = new RecipeTable(recipe, provider);
    JTable table = recipeTable.setupTable().getTable();

    table.setValueAt("Test3", 1, 0);

    Map<Ingredient, NonnegativeInteger> recipeMap = new HashMap<>();
    recipeMap.put(getIngredient1(), nonNegative(100));
    recipeMap.put(getIngredient3(), nonNegative(50));
    Recipe expectedRecipe = Recipe.from(nonNegative(1), recipeMap);
    Optional<Recipe> returned = recipeTable.getRecipe(nonNegative(1));
    assertThat(table.getRowCount()).isEqualTo(3);
    assertThat(table.getValueAt(1, 2)).isEqualTo(Measure.GRAM);
    assertThat(returned.get()).isEqualTo(expectedRecipe);
  }

  @Test
  public void addingAnIngredientTwiceAddsAmounts() {
    Recipe recipe = createStandardRecipe();
    recipeTable = new RecipeTable(recipe, provider);
    JTable table = recipeTable.setupTable().getTable();

    table.setValueAt("Test2", 2, 0);
    table.setValueAt(nonNegative(50), 2, 1);

    Map<Ingredient, NonnegativeInteger> recipeMap = new HashMap<>();
    recipeMap.put(getIngredient1(), nonNegative(100));
    recipeMap.put(getIngredient2(), nonNegative(50 + 50));
    Recipe expectedRecipe = Recipe.from(nonNegative(1), recipeMap);
    Optional<Recipe> returned = recipeTable.getRecipe(nonNegative(1));
    assertThat(table.getRowCount()).isEqualTo(4);
    assertThat(returned.get()).isEqualTo(expectedRecipe);
  }

  private Recipe createStandardRecipe() {
    Map<Ingredient, NonnegativeInteger> recipeMap = new HashMap<>();
    recipeMap.put(getIngredient1(), nonNegative(100));
    recipeMap.put(getIngredient2(), nonNegative(50));
    Recipe expectedRecipe = Recipe.from(nonNegative(1), recipeMap);
    return expectedRecipe;
  }
}
