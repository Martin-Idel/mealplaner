// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.recepies;

import static mealplaner.commons.NonnegativeFraction.wholeNumber;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.recipes.QuantitativeIngredient.createQuantitativeIngredient;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonBaseFunctions.getIngredient1;
import static testcommons.CommonBaseFunctions.getIngredient2;
import static testcommons.CommonBaseFunctions.getIngredient3;
import static testcommons.CommonBaseFunctions.getRecipe3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.swing.JTable;

import org.junit.Before;
import org.junit.Test;

import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.Measure;
import mealplaner.model.recipes.QuantitativeIngredient;
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

    List<QuantitativeIngredient> recipeIngredients = new ArrayList<>();
    recipeIngredients.add(createQuantitativeIngredient(
        getIngredient1(), getIngredient1().getPrimaryMeasure(), wholeNumber(nonNegative(100))));
    recipeIngredients.add(createQuantitativeIngredient(
        getIngredient2(), getIngredient2().getPrimaryMeasure(), wholeNumber(nonNegative(50))));
    recipeIngredients.add(createQuantitativeIngredient(
        getIngredient3(), getIngredient3().getPrimaryMeasure(), wholeNumber(nonNegative(0))));
    Recipe expectedRecipe = Recipe.from(nonNegative(1), recipeIngredients);
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

    List<QuantitativeIngredient> recipeIngredients = new ArrayList<>();
    recipeIngredients.add(createQuantitativeIngredient(
        getIngredient1(), getIngredient1().getPrimaryMeasure(), wholeNumber(nonNegative(100))));
    recipeIngredients.add(createQuantitativeIngredient(
        getIngredient3(), getIngredient3().getPrimaryMeasure(), wholeNumber(nonNegative(50))));
    Recipe expectedRecipe = Recipe.from(nonNegative(1), recipeIngredients);
    Optional<Recipe> returned = recipeTable.getRecipe(nonNegative(1));
    assertThat(table.getRowCount()).isEqualTo(3);
    assertThat(table.getValueAt(1, 2)).isEqualTo(Measure.GRAM);
    assertThat(returned.get()).isEqualTo(expectedRecipe);
  }
}
