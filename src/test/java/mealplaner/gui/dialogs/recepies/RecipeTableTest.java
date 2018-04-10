package mealplaner.gui.dialogs.recepies;

import static mealplaner.commons.NonnegativeFraction.fraction;
import static mealplaner.commons.NonnegativeFraction.wholeNumber;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getIngredient1;
import static testcommons.CommonFunctions.getIngredient2;
import static testcommons.CommonFunctions.getIngredient3;
import static testcommons.CommonFunctions.getRecipe3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JTable;

import org.junit.Before;
import org.junit.Test;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.gui.dialogs.recepies.RecipeTable;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.Measure;
import mealplaner.model.recipes.Recipe;

public class RecipeTableTest {
  private RecipeTable recipeTable;
  private List<Ingredient> ingredients;

  @Before
  public void setUp() {
    ingredients = Arrays.asList(getIngredient1(), getIngredient2(),
        getIngredient3());
  }

  @Test
  public void recipeGetsDisplayedCorrectly() {
    Recipe recipe = getRecipe3();
    recipeTable = new RecipeTable(recipe, ingredients);

    JTable table = recipeTable.setupTable().getTable();

    assertThat(table.getRowCount()).isEqualTo(3);
    assertThat(table.getValueAt(0, 0)).isEqualTo(getIngredient1().getName());
    assertThat(table.getValueAt(0, 1)).isEqualTo(wholeNumber(nonNegative(100)));
    assertThat(table.getValueAt(1, 0)).isEqualTo(getIngredient2().getName());
    assertThat(table.getValueAt(1, 1)).isEqualTo(wholeNumber(nonNegative(50)));
  }

  @Test
  public void recipeGetsReturnedCorrectly() {
    Recipe recipe = getRecipe3();
    recipeTable = new RecipeTable(recipe, ingredients);
    recipeTable.setupTable();

    Optional<Recipe> returned = recipeTable.getRecipe(nonNegative(1));

    assertThat(returned.get()).isEqualTo(recipe);
  }

  @Test
  public void addingAnIngredientWorksCorrectly() {
    Recipe recipe = getRecipe3();
    recipeTable = new RecipeTable(recipe, ingredients);
    JTable table = recipeTable.setupTable().getTable();

    table.setValueAt("Test3", 2, 0);

    Map<Ingredient, NonnegativeFraction> recipeMap = new HashMap<>();
    recipeMap.put(getIngredient1(), wholeNumber(nonNegative(100)));
    recipeMap.put(getIngredient2(), wholeNumber(nonNegative(50)));
    recipeMap.put(getIngredient3(), wholeNumber(nonNegative(0)));
    Recipe expectedRecipe = Recipe.from(nonNegative(1), recipeMap);
    Optional<Recipe> returned = recipeTable.getRecipe(nonNegative(1));
    assertThat(table.getRowCount()).isEqualTo(4);
    assertThat(returned.get()).isEqualTo(expectedRecipe);
  }

  @Test
  public void changingAnIngredientWorksCorrectly() {
    Recipe recipe = getRecipe3();
    recipeTable = new RecipeTable(recipe, ingredients);
    JTable table = recipeTable.setupTable().getTable();

    table.setValueAt("Test3", 1, 0);

    Map<Ingredient, NonnegativeFraction> recipeMap = new HashMap<>();
    recipeMap.put(getIngredient1(), wholeNumber(nonNegative(100)));
    recipeMap.put(getIngredient3(), wholeNumber(nonNegative(50)));
    Recipe expectedRecipe = Recipe.from(nonNegative(1), recipeMap);
    Optional<Recipe> returned = recipeTable.getRecipe(nonNegative(1));
    assertThat(table.getRowCount()).isEqualTo(3);
    assertThat(table.getValueAt(1, 2)).isEqualTo(Measure.GRAM);
    assertThat(returned.get()).isEqualTo(expectedRecipe);
  }

  @Test
  public void addingAnIngredientTwiceAddsAmounts() {
    Recipe recipe = getRecipe3();
    recipeTable = new RecipeTable(recipe, ingredients);
    JTable table = recipeTable.setupTable().getTable();

    table.setValueAt("Test2", 2, 0);
    table.setValueAt(wholeNumber(nonNegative(50)), 2, 1);

    Map<Ingredient, NonnegativeFraction> recipeMap = new HashMap<>();
    recipeMap.put(getIngredient1(), wholeNumber(nonNegative(100)));
    recipeMap.put(getIngredient2(), wholeNumber(nonNegative(50 + 50)));
    Recipe expectedRecipe = Recipe.from(nonNegative(1), recipeMap);
    Optional<Recipe> returned = recipeTable.getRecipe(nonNegative(1));
    assertThat(table.getRowCount()).isEqualTo(4);
    assertThat(returned.get()).isEqualTo(expectedRecipe);
  }

  @Test
  public void addingAnIngredientTwiceWithFractionsAddsFractions() {
    Recipe recipe = getRecipe3();
    recipeTable = new RecipeTable(recipe, ingredients);
    JTable table = recipeTable.setupTable().getTable();

    table.setValueAt("Test2", 2, 0);
    table.setValueAt(fraction(40, 11), 2, 1);

    Map<Ingredient, NonnegativeFraction> recipeMap = new HashMap<>();
    recipeMap.put(getIngredient1(), wholeNumber(nonNegative(100)));
    recipeMap.put(getIngredient2(), fraction(550 + 40, 11));
    Recipe expectedRecipe = Recipe.from(nonNegative(1), recipeMap);
    Optional<Recipe> returned = recipeTable.getRecipe(nonNegative(1));
    assertThat(table.getRowCount()).isEqualTo(4);
    assertThat(returned.get()).isEqualTo(expectedRecipe);
  }
}
