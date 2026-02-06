// SPDX-License-Identifier: MIT

package etoetests.guitests.pageobjects;

import static etoetests.guitests.pageobjects.TabbedPanes.DATABASE_EDIT;
import static mealplaner.commons.BundleStore.BUNDLES;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.model.meal.Meal;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.QuantitativeIngredient;
import mealplaner.model.recipes.Recipe;
import mealplaner.plugins.builtins.courses.CourseType;
import mealplaner.plugins.builtins.courses.CourseTypeFact;
import mealplaner.plugins.comment.mealextension.CommentFact;
import mealplaner.plugins.cookingtime.mealextension.CookingTime;
import mealplaner.plugins.cookingtime.mealextension.CookingTimeFact;
import mealplaner.plugins.preference.mealextension.CookingPreference;
import mealplaner.plugins.preference.mealextension.CookingPreferenceFact;
import mealplaner.plugins.sidedish.mealextension.Sidedish;
import mealplaner.plugins.sidedish.mealextension.SidedishFact;
import mealplaner.plugins.utensil.mealextension.ObligatoryUtensil;
import mealplaner.plugins.utensil.mealextension.ObligatoryUtensilFact;

public class MealsEditPageObjectNative {
  private static final int NUMBER_OF_DATA_COLUMNS = 9;
  private static final int NUMBER_OF_INGREDIENT_COLUMNS = 3;

  private static final int DATABASE_NAME_COLUMN = 0;
  private static final int DATABASE_TIME_COLUMN = 1;
  private static final int DATABASE_SIDEDISH_COLUMN = 2;
  private static final int DATABASE_OBLIGATORY_UTENSIL_COLUMN = 3;
  private static final int DATABASE_DAYS_LAST_COOKED_COLUMN = 4;
  private static final int DATABASE_MEAL_PREFERENCE_COLUMN = 5;
  private static final int DATABASE_COURSE_TYPE_COLUMN = 6;
  private static final int DATABASE_COMMENT_COLUMN = 7;
  private static final int DATABASE_RECIPE_COLUMN = 8;

  private final JFrame frame;

  MealsEditPageObjectNative(JFrame frame) {
    this.frame = frame;
  }

  public MealsEditPageObjectNative compareDatabaseInTable(List<Meal> meals) throws Exception {
    JTable databaseTable = findTable();
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      assertThat(databaseTable.getColumnCount()).isEqualTo(NUMBER_OF_DATA_COLUMNS);
      assertThat(databaseTable.getRowCount()).isEqualTo(meals.size());
      assertTableContents(databaseTable, mealsToTableContent(meals));
    });
    return this;
  }

  public MealsEditPageObjectNative addMeal(Meal meal, Ingredient... missingIngredients) throws Exception {
    clickAddMealButton();
    addMealInDialog(meal, missingIngredients);
    Thread.sleep(500);
    return this;
  }

  public MealsEditPageObjectNative removeSelectedMeals(int... rows) throws Exception {
    JTable databaseTable = findTable();
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      databaseTable.setRowSelectionInterval(rows[0], rows[rows.length - 1]);
    });
    clickRemoveSelectedButton();
    Thread.sleep(500);
    return this;
  }

  public MealsEditPageObjectNative changeCookingTime(int row, CookingTime cookingTime) throws Exception {
    JTable databaseTable = findTable();
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      databaseTable.setValueAt(cookingTime, row, DATABASE_TIME_COLUMN);
    });
    Thread.sleep(200);
    return this;
  }

  public MealsEditPageObjectNative changeSideDish(int row, Sidedish sidedish) throws Exception {
    JTable databaseTable = findTable();
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      databaseTable.setValueAt(sidedish, row, DATABASE_SIDEDISH_COLUMN);
    });
    Thread.sleep(200);
    return this;
  }

  public MealsEditPageObjectNative changeComment(int row, String comment) throws Exception {
    JTable databaseTable = findTable();
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      databaseTable.setValueAt(comment, row, DATABASE_COMMENT_COLUMN);
    });
    Thread.sleep(200);
    return this;
  }

  public MealsEditPageObjectNative changeCourseType(int row, CourseType courseType) throws Exception {
    JTable databaseTable = findTable();
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      databaseTable.setValueAt(courseType, row, DATABASE_COURSE_TYPE_COLUMN);
    });
    Thread.sleep(200);
    return this;
  }

  public MealsEditPageObjectNative enterRecipe(int row, Recipe recipe) throws Exception {
    clickRecipeButtonInTable(row);
    Thread.sleep(1000);
    enterRecipeInDialog(recipe);
    return this;
  }

  public MealsEditPageObjectNative save() throws Exception {
    javax.swing.SwingUtilities.invokeLater(() -> {
      saveButton().doClick();
    });
    Thread.sleep(500);
    return this;
  }

  public JButton saveButton() {
    return (JButton) findComponentByName(frame, "ButtonPanelDatabaseEdit2");
  }

  public JButton cancelButton() {
    return (JButton) findComponentByName(frame, "ButtonPanelDatabaseEdit3");
  }

  private void clickAddMealButton() throws Exception {
    javax.swing.SwingUtilities.invokeLater(() -> {
      JButton button = (JButton) findComponentByName(frame, "ButtonPanelDatabaseEdit0");
      button.doClick();
    });
    Thread.sleep(1000);
  }

  private void clickRemoveSelectedButton() throws Exception {
    javax.swing.SwingUtilities.invokeLater(() -> {
      JButton button = (JButton) findComponentByName(frame, "ButtonPanelDatabaseEdit1");
      button.doClick();
    });
    Thread.sleep(1000);
  }

  private void clickRecipeButtonInTable(int row) throws Exception {
    javax.swing.SwingUtilities.invokeLater(() -> {
      JTable table = findTable();
      table.setRowSelectionInterval(row, row);
      table.setColumnSelectionInterval(DATABASE_RECIPE_COLUMN, DATABASE_RECIPE_COLUMN);
      table.changeSelection(row, DATABASE_RECIPE_COLUMN, false, false);
      javax.swing.SwingUtilities.invokeLater(() -> {
        int rowHeight = table.getRowHeight(row);
        java.awt.Rectangle cellRect = table.getCellRect(row, DATABASE_RECIPE_COLUMN, false);
        java.awt.Point point = new java.awt.Point(cellRect.x + cellRect.width / 2, cellRect.y + rowHeight / 2);
        java.awt.event.MouseEvent mouseEvent = new java.awt.event.MouseEvent(
            table,
            java.awt.event.MouseEvent.MOUSE_CLICKED,
            System.currentTimeMillis(),
            0,
            point.x,
            point.y,
            1,
            false,
            java.awt.event.MouseEvent.BUTTON1
        );
        table.dispatchEvent(mouseEvent);
      });
    });
    Thread.sleep(1500);
  }

  private JTable findTable() {
    return (JTable) findComponentByName(frame, "DatabaseEditTable");
  }

  private void assertTableContents(JTable table, String[][] expectedContent) {
    for (int row = 0; row < expectedContent.length; row++) {
      for (int col = 0; col < expectedContent[row].length; col++) {
        Object actualValue = table.getValueAt(row, col);
        String expectedString = expectedContent[row][col];
        String actualString = actualValue != null ? actualValue.toString() : null;
        assertThat(actualString).isEqualTo(expectedString);
      }
    }
  }

  private void assertCorrectRecipes(List<Meal> meals) throws Exception {
    for (int i = 0; i < meals.size(); i++) {
      if (meals.get(i).getRecipe().isPresent()) {
        Recipe recipe = meals.get(i).getRecipe().get();
        clickRecipeButtonInTable(i);
        compareRecipeInTable(recipe);
        javax.swing.SwingUtilities.invokeLater(() -> {
          JDialog dialog = findDialog("InputFieldNonnegativeIntegerRecipePortions");
          if (dialog != null) {
            dialog.dispose();
          }
        });
        Thread.sleep(200);
      }
    }
  }

  private void compareRecipeInTable(Recipe recipe) throws Exception {
    Thread.sleep(500);
    final JDialog[] dialog = new JDialog[1];
    int maxAttempts = 10;
    for (int attempt = 0; attempt < maxAttempts && dialog[0] == null; attempt++) {
      javax.swing.SwingUtilities.invokeAndWait(() -> {
        dialog[0] = findDialog("InputFieldNonnegativeIntegerRecipePortions");
      });
      if (dialog[0] == null) {
        Thread.sleep(200);
      }
    }
    Thread.sleep(200);
    JTextField portionsField = findTextField(dialog[0], "InputFieldNonnegativeIntegerRecipePortions");
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      assertThat(portionsField.getText()).isEqualTo(recipe.getNumberOfPortions().toString());
    });
    JTable recipeTable = findTableInDialog(dialog[0]);
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      assertThat(recipeTable.getColumnCount()).isEqualTo(NUMBER_OF_INGREDIENT_COLUMNS);
      assertThat(recipeTable.getRowCount()).isEqualTo(recipe.getIngredientListAsIs().size() + 1);
      assertTableContents(recipeTable, recipeToTableContent(recipe));
    });
    javax.swing.SwingUtilities.invokeLater(() -> {
      JButton closeButton = findButton(dialog[0], "ButtonPanelRecipeInput1");
      closeButton.doClick();
    });
    Thread.sleep(500);
  }

  private void addMealInDialog(Meal meal, Ingredient... missingIngredients) throws Exception {
    Thread.sleep(500);
    final JDialog[] dialog = new JDialog[1];
    int maxAttempts = 10;
    for (int attempt = 0; attempt < maxAttempts && dialog[0] == null; attempt++) {
      javax.swing.SwingUtilities.invokeAndWait(() -> {
        dialog[0] = findDialog("ButtonPanelMealInput0");
      });
      if (dialog[0] == null) {
        Thread.sleep(200);
      }
    }
    if (dialog[0] == null) {
      throw new RuntimeException("Meal input dialog not found after " + maxAttempts + " attempts");
    }
    Thread.sleep(200);
    enterMealInDialog(meal, dialog[0], missingIngredients);
    javax.swing.SwingUtilities.invokeLater(() -> {
      JButton addButton = findButton(dialog[0], "ButtonPanelMealInput0");
      addButton.doClick();
    });
    Thread.sleep(500);
  }

  private void enterMealInDialog(Meal meal, JDialog dialog, Ingredient... missingIngredients) throws Exception {
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      JTextField nameField = findTextField(dialog, "InputFieldNonemptyTextName");
      nameField.setText(meal.getName());

      JComboBox<?> cookingTimeCombo = findComboBox(dialog, "InputFieldComboBoxCookingTime");
      @SuppressWarnings("unchecked")
      JComboBox<CookingTime> cookingTimeTypedCombo = (JComboBox<CookingTime>) cookingTimeCombo;
      cookingTimeTypedCombo.setSelectedItem(meal.getTypedMealFact(CookingTimeFact.class).getCookingTime());

      JComboBox<?> sidedishCombo = findComboBox(dialog, "InputFieldComboBoxSidedish");
      @SuppressWarnings("unchecked")
      JComboBox<Sidedish> sidedishTypedCombo = (JComboBox<Sidedish>) sidedishCombo;
      sidedishTypedCombo.setSelectedItem(meal.getTypedMealFact(SidedishFact.class).getSidedish());

      JComboBox<?> utensilCombo = findComboBox(dialog, "InputFieldComboBoxObligatoryUtensil");
      @SuppressWarnings("unchecked")
      JComboBox<ObligatoryUtensil> utensilTypedCombo = (JComboBox<ObligatoryUtensil>) utensilCombo;
      utensilTypedCombo.setSelectedItem(meal.getTypedMealFact(ObligatoryUtensilFact.class).getObligatoryUtensil());

      JTextField daysField = findTextField(dialog, "InputFieldNonnegativeIntegerDaysPassed");
      daysField.setText(meal.getDaysPassed().toString());

      JComboBox<?> preferenceCombo = findComboBox(dialog, "InputFieldComboBoxCookingPreference");
      @SuppressWarnings("unchecked")
      JComboBox<CookingPreference> preferenceTypedCombo = (JComboBox<CookingPreference>) preferenceCombo;
      preferenceTypedCombo.setSelectedItem(meal.getTypedMealFact(CookingPreferenceFact.class).getCookingPreference());

      JComboBox<?> courseCombo = findComboBox(dialog, "InputFieldComboBoxCourseType");
      @SuppressWarnings("unchecked")
      JComboBox<CourseType> courseTypedCombo = (JComboBox<CourseType>) courseCombo;
      courseTypedCombo.setSelectedItem(meal.getTypedMealFact(CourseTypeFact.class).getCourseType());

      JTextField commentField = findTextField(dialog, "InputFieldTextComment");
      commentField.setText(meal.getTypedMealFact(CommentFact.class).getComment());
    });

    if (meal.getRecipe().isPresent()) {
      javax.swing.SwingUtilities.invokeLater(() -> {
        JButton recipeButton = findButton(dialog, "InputFieldButtonRecipe");
        recipeButton.doClick();
      });
      Thread.sleep(1000);
      enterRecipeInDialog(meal.getRecipe().get(), missingIngredients);
    }
  }

  private void enterRecipeInDialog(Recipe recipe, Ingredient... missingIngredients) throws Exception {
    Thread.sleep(1000);
    final JDialog[] dialog = new JDialog[1];
    final int[] attemptCounter = {0};
    int maxAttempts = 30;
    for (int attempt = 0; attempt < maxAttempts && dialog[0] == null; attempt++) {
      attemptCounter[0] = attempt;
      javax.swing.SwingUtilities.invokeAndWait(() -> {
        dialog[0] = findDialog("InputFieldNonnegativeIntegerRecipePortions");
      });
      if (dialog[0] == null) {
        Thread.sleep(300);
      }
    }
    if (dialog[0] == null) {
      throw new RuntimeException("Recipe dialog not found after " + attemptCounter[0] + " attempts");
    }
    Thread.sleep(200);

    if (missingIngredients.length != 0) {
      javax.swing.SwingUtilities.invokeLater(() -> {
        JButton addIngredientButton = findButton(dialog[0], "ButtonPanelRecipeInput0");
        addIngredientButton.doClick();
      });
      Thread.sleep(500);
      for (Ingredient ingredient : missingIngredients) {
        enterIngredientInDialog(ingredient);
        Thread.sleep(200);
      }
    }

    javax.swing.SwingUtilities.invokeAndWait(() -> {
      JTextField portionsField = findTextField(dialog[0], "InputFieldNonnegativeIntegerRecipePortions");
      portionsField.setText(recipe.getNumberOfPortions().toString());
    });

    JTable ingredientsTable = findTableInDialog(dialog[0]);
    List<QuantitativeIngredient> ingredientsAsIs = recipe.getIngredientListAsIs();
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      for (int i = 0; i < ingredientsAsIs.size(); i++) {
        QuantitativeIngredient ingredient = ingredientsAsIs.get(i);
        ingredientsTable.setValueAt(ingredient.getIngredient(), i, 0);
        ingredientsTable.setValueAt(ingredient.getAmount(), i, 1);
      }
    });

    javax.swing.SwingUtilities.invokeLater(() -> {
      JButton saveButton = findButton(dialog[0], "ButtonPanelRecipeInput2");
      saveButton.doClick();
    });
    Thread.sleep(500);
  }

  private void enterIngredientInDialog(Ingredient ingredient) throws Exception {
    Thread.sleep(500);
    final JDialog[] dialog = new JDialog[1];
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      dialog[0] = findDialog("InputFieldNonemptyTextIngredientName");
    });
    Thread.sleep(200);

    javax.swing.SwingUtilities.invokeAndWait(() -> {
      JTextField nameField = findTextField(dialog[0], "InputFieldNonemptyTextIngredientName");
      nameField.setText(ingredient.getName());

      JComboBox<?> typeCombo = findComboBox(dialog[0], "InputFieldComboBoxIngredientType");
      @SuppressWarnings("unchecked")
      JComboBox<mealplaner.model.recipes.IngredientType> typeTypedCombo = 
          (JComboBox<mealplaner.model.recipes.IngredientType>) typeCombo;
      typeTypedCombo.setSelectedItem(ingredient.getType());

      JComboBox<?> measureCombo = findComboBox(dialog[0], "InputFieldComboBoxIngredientMeasure");
      @SuppressWarnings("unchecked")
      JComboBox<mealplaner.model.recipes.Measure> measureTypedCombo = (JComboBox<mealplaner.model.recipes.Measure>) measureCombo;
      measureTypedCombo.setSelectedItem(ingredient.getPrimaryMeasure());
    });

    javax.swing.SwingUtilities.invokeLater(() -> {
      JButton addButton = findButton(dialog[0], "ButtonPanelIngredientsInput1");
      addButton.doClick();
    });
    Thread.sleep(500);
  }

  private JDialog findDialog(String... expectedComponentNames) {
    for (java.awt.Window window : java.awt.Window.getWindows()) {
      if (window instanceof JDialog && window.isVisible()) {
        JDialog dialog = (JDialog) window;
        if (expectedComponentNames.length == 0) {
          return dialog;
        }
        for (String componentName : expectedComponentNames) {
          if (findComponentByName(dialog, componentName) != null) {
            return dialog;
          }
        }
      }
    }
    return null;
  }

  private JTextField findTextField(JDialog dialog, String name) {
    return (JTextField) findComponentByName(dialog, name);
  }

  private JComboBox<?> findComboBox(JDialog dialog, String name) {
    return (JComboBox<?>) findComponentByName(dialog, name);
  }

  private JButton findButton(JDialog dialog, String name) {
    return (JButton) findComponentByName(dialog, name);
  }

  private JButton findButtonInTable(JTable table, int row, int column) {
    Object value = table.getValueAt(row, column);
    if (value instanceof JButton) {
      return (JButton) value;
    }
    return null;
  }

  private JTable findTableInDialog(JDialog dialog) {
    JTable table = (JTable) findComponentByName(dialog, "RecipeInputTable");
    if (table == null) {
      table = findFirstComponentOfClass(dialog, JTable.class);
    }
    return table;
  }

  private java.awt.Component findComponentByName(java.awt.Container parent, String name) {
    if (parent == null) {
      return null;
    }
    if (name.equals(parent.getName())) {
      return parent;
    }
    for (java.awt.Component component : parent.getComponents()) {
      if (name.equals(component.getName())) {
        return component;
      }
      if (component instanceof java.awt.Container) {
        java.awt.Component found = findComponentByName((java.awt.Container) component, name);
        if (found != null) {
          return found;
        }
      }
    }
    return null;
  }

  public MealsEditPageObjectNative mealsTabbedPane() {
    javax.swing.JTabbedPane tabbedPane = findTabbedPane();
    tabbedPane.setSelectedIndex(DATABASE_EDIT.number());
    return this;
  }

  private javax.swing.JTabbedPane findTabbedPane() {
    return findFirstComponentOfClass(frame, javax.swing.JTabbedPane.class);
  }

  private <T> T findFirstComponentOfClass(java.awt.Container parent, Class<T> clazz) {
    for (java.awt.Component component : parent.getComponents()) {
      if (clazz.isInstance(component)) {
        return clazz.cast(component);
      }
      if (component instanceof java.awt.Container) {
        T found = findFirstComponentOfClass((java.awt.Container) component, clazz);
        if (found != null) {
          return found;
        }
      }
    }
    return null;
  }

  private String[][] recipeToTableContent(Recipe recipe) {
    List<QuantitativeIngredient> ingredients = recipe.getIngredientListAsIs();
    ingredients.sort(Comparator.comparing(ingredient2 -> ingredient2.getIngredient().getName()));
    String[][] content = new String[ingredients.size() + 1][NUMBER_OF_INGREDIENT_COLUMNS];
    for (int i = 0; i < ingredients.size(); i++) {
      QuantitativeIngredient ingredient = ingredients.get(i);
      content[i][0] = ingredient.getIngredient().getName();
      content[i][1] = ingredient.getAmount().toString();
      content[i][2] = ingredient.getIngredient().getPrimaryMeasure().toString();
    }
    content[ingredients.size()][0] = "";
    content[ingredients.size()][1] = "";
    content[ingredients.size()][2] = "";
    return content;
  }

  private String[][] mealsToTableContent(List<Meal> meals) {
    String[][] content = new String[meals.size()][NUMBER_OF_DATA_COLUMNS];
    for (int i = 0; i < meals.size(); i++) {
      Meal meal = meals.get(i);
      content[i][0] = meal.getName();
      content[i][1] = meal.getTypedMealFact(CookingTimeFact.class).getCookingTime().toString();
      content[i][2] = meal.getTypedMealFact(SidedishFact.class).getSidedish().toString();
      content[i][3] = meal.getTypedMealFact(ObligatoryUtensilFact.class).getObligatoryUtensil().toString();
      content[i][4] = meal.getDaysPassed().toString();
      content[i][5] = meal.getTypedMealFact(CookingPreferenceFact.class).getCookingPreference().toString();
      content[i][6] = meal.getTypedMealFact(CourseTypeFact.class).getCourseType().toString();
      content[i][7] = meal.getTypedMealFact(CommentFact.class).getComment();
      content[i][8] = meal.getRecipe().isPresent()
          ? BUNDLES.message("editRecipeButtonLabel")
          : BUNDLES.message("createRecipeButtonLabel");
    }
    return content;
  }
}