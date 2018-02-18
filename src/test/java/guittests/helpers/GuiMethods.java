package guittests.helpers;

import static guittests.helpers.TabbedPanes.DATABASE_EDIT;
import static mealplaner.commons.BundleStore.BUNDLES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.data.TableCell.row;

import java.util.List;

import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTableFixture;

import mealplaner.model.Meal;
import mealplaner.recipes.model.QuantitativeIngredient;
import mealplaner.recipes.model.Recipe;

public final class GuiMethods {
  private static final int NUMBER_OF_DATA_COLUMNS = 8;
  private static final int RECIPE_COLUMN_IN_DATA_BASE = NUMBER_OF_DATA_COLUMNS - 1;
  private static final int NUMBER_OF_INGREDIENT_COLUMNS = 3;

  private final FrameFixture window;

  private GuiMethods(FrameFixture window) {
    this.window = window;
  }

  public static GuiMethods create(FrameFixture window) {
    return new GuiMethods(window);
  }

  public void enterMealFromMenu(Meal meal) {
    window.menuItem("MenuFile").click();
    window.menuItem("MenuItemCreateMeal").click();
    DialogFixture mealInputDialog = window.dialog();
    mealInputDialog.textBox("InputFieldNonemptyTextName")
        .enterText(meal.getName());
    mealInputDialog.comboBox("InputFieldComboBoxCookingTime")
        .selectItem(meal.getCookingTime().toString());
    mealInputDialog.comboBox("InputFieldComboBoxSidedish")
        .selectItem(meal.getSidedish().toString());
    mealInputDialog.comboBox("InputFieldComboBoxObligatoryUtensil")
        .selectItem(meal.getObligatoryUtensil().toString());
    mealInputDialog.comboBox("InputFieldComboBoxCookingPreference")
        .selectItem(meal.getCookingPreference().toString());
    mealInputDialog.textBox("InputFieldNonnegativeIntegerDaysPassed")
        .enterText(meal.getDaysPassed().toString());
    mealInputDialog.textBox("InputFieldTextComment")
        .enterText(meal.getComment());
    if (meal.getRecipe().isPresent()) {
      mealInputDialog.button("InputFieldButtonRecipe").click();
      enterRecipe(meal.getRecipe().get(), mealInputDialog);
    }
    mealInputDialog.button("ButtonPanelMealInput0").click();
    mealInputDialog.button("ButtonPanelMealInput1").click();
  }

  private void enterRecipe(Recipe recipe, DialogFixture dialog) {
    dialog.textBox("InputFieldNonnegativeIntegerRecipePortions")
        .enterText(recipe.getNumberOfPortions().toString());
    JTableFixture ingredientsTable = dialog.table();
    List<QuantitativeIngredient> ingredientsAsIs = recipe.getIngredientListAsIs();
    for (int i = 0; i < ingredientsAsIs.size(); i++) {
      QuantitativeIngredient ingredient = ingredientsAsIs.get(i);
      ingredientsTable.enterValue(row(i).column(0), ingredient.getIngredient().getName());
      ingredientsTable.enterValue(row(i).column(1), ingredient.getAmount().toString());
    }
    dialog.button("ButtonPanelRecipeInput1").click();
  }

  public void compareDatabaseInTable(List<Meal> meals) {
    window.tabbedPane().selectTab(DATABASE_EDIT.number()).click();
    JTableFixture databaseTable = window.table().requireColumnCount(NUMBER_OF_DATA_COLUMNS)
        .requireRowCount(meals.size());
    databaseTable.requireContents(mealsToTableContent(meals));
    assertCorrectRecipes(meals, databaseTable);
  }

  private void assertCorrectRecipes(List<Meal> meals, JTableFixture databaseTable) {
    for (int i = 0; i < meals.size(); i++) {
      if (meals.get(i).getRecipe().isPresent()) {
        Recipe recipe = meals.get(i).getRecipe().get();
        databaseTable.cell(row(i).column(RECIPE_COLUMN_IN_DATA_BASE)).click();
        DialogFixture recipeDialog = window.dialog();
        assertThat(recipeDialog.textBox("InputFieldNonnegativeIntegerRecipePortions").text())
            .isEqualTo(recipe.getNumberOfPortions().toString());
        JTableFixture recipeTable = recipeDialog.table();
        recipeTable.requireColumnCount(NUMBER_OF_INGREDIENT_COLUMNS)
            .requireRowCount(recipe.getIngredientListAsIs().size() + 1);
        recipeTable.requireContents(recipeToTableContent(recipe));
        recipeDialog.button("ButtonPanelRecipeInput1").click();
      }
    }
  }

  private String[][] recipeToTableContent(Recipe recipe) {
    List<QuantitativeIngredient> ingredients = recipe.getIngredientListAsIs();
    String[][] content = new String[ingredients.size() + 1][NUMBER_OF_INGREDIENT_COLUMNS];
    for (int i = 0; i < ingredients.size(); i++) {
      QuantitativeIngredient ingredient = ingredients.get(i);
      content[i][0] = ingredient.getIngredient().getName();
      content[i][1] = ingredient.getAmount().toString();
      content[i][2] = ingredient.getIngredient().getMeasure().toString();
    }
    content[ingredients.size()][0] = "";
    content[ingredients.size()][1] = "0";
    content[ingredients.size()][2] = "-";
    return content;
  }

  private String[][] mealsToTableContent(List<Meal> meals) {
    String[][] content = new String[meals.size()][NUMBER_OF_DATA_COLUMNS];
    for (int i = 0; i < meals.size(); i++) {
      Meal meal = meals.get(i);
      content[i][0] = meal.getName();
      content[i][1] = meal.getCookingTime().toString();
      content[i][2] = meal.getSidedish().toString();
      content[i][3] = meal.getObligatoryUtensil().toString();
      content[i][4] = meal.getDaysPassed().toString();
      content[i][5] = meal.getCookingPreference().toString();
      content[i][6] = meal.getComment();
      content[i][7] = meal.getRecipe().isPresent()
          ? BUNDLES.message("editRecipeButtonLabel")
          : BUNDLES.message("createRecipeButtonLabel");
    }
    return content;
  }
}
