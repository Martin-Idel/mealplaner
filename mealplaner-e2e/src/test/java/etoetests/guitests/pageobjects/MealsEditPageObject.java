// SPDX-License-Identifier: MIT

package etoetests.guitests.pageobjects;

import static etoetests.guitests.constants.ComponentNames.BUTTON_DATABASEEDIT_ADD;
import static etoetests.guitests.constants.ComponentNames.BUTTON_DATABASEEDIT_REMOVE;
import static etoetests.guitests.constants.ComponentNames.BUTTON_DATABASEEDIT_SAVE;
import static etoetests.guitests.constants.ComponentNames.BUTTON_DATABASEEDIT_CANCEL;
import static etoetests.guitests.constants.ComponentNames.BUTTON_MEALINPUT_ADD;
import static etoetests.guitests.constants.ComponentNames.BUTTON_RECIPEINPUT_ADD_INGREDIENT;
import static etoetests.guitests.constants.ComponentNames.BUTTON_RECIPEINPUT_CLOSE;
import static etoetests.guitests.constants.ComponentNames.BUTTON_RECIPEINPUT_SAVE;
import static etoetests.guitests.constants.ComponentNames.BUTTON_INGREDIENTSINPUT_ADD;
import static etoetests.guitests.pageobjects.TabbedPanes.DATABASE_EDIT;
import static mealplaner.commons.BundleStore.BUNDLES;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import etoetests.guitests.helpers.SwingTestHelper;
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

public class MealsEditPageObject extends BasePageObject {
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

  MealsEditPageObject(JFrame frame) {
    super(frame);
  }

  public MealsEditPageObject compareDatabaseInTable(List<Meal> meals) throws Exception {
    JTable databaseTable = findTableByName(frame, "DatabaseEditTable");
    helper.invokeAndWaitVoid(() -> {
      assertThat(databaseTable.getColumnCount()).isEqualTo(NUMBER_OF_DATA_COLUMNS);
      assertThat(databaseTable.getRowCount()).isEqualTo(meals.size());
      assertTableContents(databaseTable, mealsToTableContent(meals));
    });
    return this;
  }

  public MealsEditPageObject addMeal(Meal meal, Ingredient... missingIngredients) throws Exception {
    clickAddMealButton();
    addMealInDialog(meal, missingIngredients);
    return this;
  }

  public MealsEditPageObject removeSelectedMeals(int... rows) throws Exception {
    JTable databaseTable = findTableByName(frame, "DatabaseEditTable");
    helper.invokeAndWaitVoid(() -> {
      databaseTable.setRowSelectionInterval(rows[0], rows[rows.length - 1]);
    });
    clickRemoveSelectedButton();
    return this;
  }

  public MealsEditPageObject changeCookingTime(int row, CookingTime cookingTime) throws Exception {
    JTable databaseTable = findTableByName(frame, "DatabaseEditTable");
    helper.invokeAndWaitVoid(() -> {
      databaseTable.setValueAt(cookingTime, row, DATABASE_TIME_COLUMN);
    });
    return this;
  }

  public MealsEditPageObject changeSideDish(int row, Sidedish sidedish) throws Exception {
    JTable databaseTable = findTableByName(frame, "DatabaseEditTable");
    helper.invokeAndWaitVoid(() -> {
      databaseTable.setValueAt(sidedish, row, DATABASE_SIDEDISH_COLUMN);
    });
    return this;
  }

  public MealsEditPageObject changeComment(int row, String comment) throws Exception {
    JTable databaseTable = findTableByName(frame, "DatabaseEditTable");
    helper.invokeAndWaitVoid(() -> {
      databaseTable.setValueAt(comment, row, DATABASE_COMMENT_COLUMN);
    });
    return this;
  }

  public MealsEditPageObject changeCourseType(int row, CourseType courseType) throws Exception {
    JTable databaseTable = findTableByName(frame, "DatabaseEditTable");
    helper.invokeAndWaitVoid(() -> {
      databaseTable.setValueAt(courseType, row, DATABASE_COURSE_TYPE_COLUMN);
    });
    return this;
  }

  public MealsEditPageObject enterRecipe(int row, Recipe recipe) throws Exception {
    clickRecipeButtonInTable(row);
    enterRecipeInDialog(recipe);
    return this;
  }

  public MealsEditPageObject save() throws Exception {
    helper.clickButtonOnEdt(saveButton());
    return this;
  }

  public JButton saveButton() {
    try {
      return helper.findComponentByName(frame, BUTTON_DATABASEEDIT_SAVE);
    } catch (Exception e) {
      throw new RuntimeException("Failed to find save button", e);
    }
  }

  public JButton cancelButton() {
    try {
      return helper.findComponentByName(frame, BUTTON_DATABASEEDIT_CANCEL);
    } catch (Exception e) {
      throw new RuntimeException("Failed to find cancel button", e);
    }
  }

  private void clickAddMealButton() throws Exception {
    JButton button = helper.findComponentByName(frame, BUTTON_DATABASEEDIT_ADD);
    helper.clickButtonOnEdt(button);
    helper.waitForCondition(() -> {
      return helper.findDialog() != null;
    }, 1000, "Dialog did not appear within timeout");
  }

  private void clickRemoveSelectedButton() throws Exception {
    JButton button = helper.findComponentByName(frame, BUTTON_DATABASEEDIT_REMOVE);
    helper.clickButtonOnEdt(button);
    helper.handleOptionPaneWithOptionIfExists(JOptionPane.YES_OPTION, 2000);
  }

  private void clickRecipeButtonInTable(int row) throws Exception {
    helper.invokeLaterVoid(() -> {
      JTable table = findTableByName(frame, "DatabaseEditTable");
      table.setRowSelectionInterval(row, row);
      table.setColumnSelectionInterval(DATABASE_RECIPE_COLUMN, DATABASE_RECIPE_COLUMN);
      table.changeSelection(row, DATABASE_RECIPE_COLUMN, false, false);
    });
    clickTableCell(row, DATABASE_RECIPE_COLUMN);
    helper.waitForCondition(() -> {
      return helper.findDialog() != null;
    }, 1500, "Dialog did not appear within timeout");
  }

  private void clickTableCell(int row, int column) {
    helper.invokeLaterVoid(() -> {
      JTable table = findTableByName(frame, "DatabaseEditTable");
      int rowHeight = table.getRowHeight(row);
      java.awt.Rectangle cellRect = table.getCellRect(row, column, false);
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
  }

  private void addMealInDialog(Meal meal, Ingredient... missingIngredients) throws Exception {
    JDialog dialog = helper.findDialogContaining(BUTTON_MEALINPUT_ADD, 2000);
    enterMealInDialog(meal, dialog, missingIngredients);
    JButton addButton = helper.findComponentByName(dialog, BUTTON_MEALINPUT_ADD);
    helper.clickButtonOnEdt(addButton);
    helper.waitForDialogToClose(dialog, 3000);
  }

  private void enterMealInDialog(Meal meal, JDialog dialog, Ingredient... missingIngredients) throws Exception {
    setMealFactFieldsInDialog(meal, dialog);
    setMealTextFieldsInDialog(meal, dialog);

    if (meal.getRecipe().isPresent()) {
      JButton recipeButton = helper.findComponentByName(dialog, "InputFieldButtonRecipe");
      helper.clickButtonOnEdt(recipeButton);
      enterRecipeInDialog(meal.getRecipe().get(), missingIngredients);
    }
  }

  private void setMealFactFieldsInDialog(Meal meal, JDialog dialog) throws Exception {
    setCookingTimeField(meal, dialog);
    setSidedishField(meal, dialog);
    setUtensilField(meal, dialog);
    setPreferenceField(meal, dialog);
    setCourseTypeField(meal, dialog);
  }

  private <T> void setComboBoxField(JDialog dialog, String componentName, T value) throws Exception {
    JComboBox<?> combo = helper.findComponentByName(dialog, componentName);
    @SuppressWarnings("unchecked")
    JComboBox<T> typedCombo = (JComboBox<T>) combo;
    typedCombo.setSelectedItem(value);
  }

  private void setCookingTimeField(Meal meal, JDialog dialog) throws Exception {
    setComboBoxField(dialog, "InputFieldComboBoxCookingTime",
        meal.getTypedMealFact(CookingTimeFact.class).getCookingTime());
  }

  private void setSidedishField(Meal meal, JDialog dialog) throws Exception {
    setComboBoxField(dialog, "InputFieldComboBoxSidedish",
        meal.getTypedMealFact(SidedishFact.class).getSidedish());
  }

  private void setUtensilField(Meal meal, JDialog dialog) throws Exception {
    setComboBoxField(dialog, "InputFieldComboBoxObligatoryUtensil",
        meal.getTypedMealFact(ObligatoryUtensilFact.class).getObligatoryUtensil());
  }

  private void setPreferenceField(Meal meal, JDialog dialog) throws Exception {
    setComboBoxField(dialog, "InputFieldComboBoxCookingPreference",
        meal.getTypedMealFact(CookingPreferenceFact.class).getCookingPreference());
  }

  private void setCourseTypeField(Meal meal, JDialog dialog) throws Exception {
    setComboBoxField(dialog, "InputFieldComboBoxCourseType",
        meal.getTypedMealFact(CourseTypeFact.class).getCourseType());
  }

  private void setMealTextFieldsInDialog(Meal meal, JDialog dialog) throws Exception {
    JTextField nameField = helper.findComponentByName(dialog, "InputFieldNonemptyTextName");
    JTextField daysField = helper.findComponentByName(dialog, "InputFieldNonnegativeIntegerDaysPassed");
    JTextField commentField = helper.findComponentByName(dialog, "InputFieldTextComment");

    helper.invokeAndWaitVoid(() -> {
      nameField.setText(meal.getName());
      daysField.setText(meal.getDaysPassed().toString());
      commentField.setText(meal.getTypedMealFact(CommentFact.class).getComment());
    });
  }

  private void enterRecipeInDialog(Recipe recipe, Ingredient... missingIngredients) throws Exception {
    JDialog dialog = helper.findDialogContaining("InputFieldNonnegativeIntegerRecipePortions", 3000);

    addMissingIngredientsInRecipeDialog(dialog, missingIngredients);
    setRecipePortionsField(dialog, recipe);
    setIngredientsTable(dialog, recipe);
    saveRecipeDialog(dialog);
  }

  private void addMissingIngredientsInRecipeDialog(JDialog dialog, Ingredient... missingIngredients) throws Exception {
    if (missingIngredients.length == 0) {
      return;
    }

    JButton addIngredientButton = helper.findComponentByName(dialog, BUTTON_RECIPEINPUT_ADD_INGREDIENT);
    helper.clickButtonOnEdt(addIngredientButton);
    for (Ingredient ingredient : missingIngredients) {
      JDialog ingredientDialog = helper.findDialogContaining("InputFieldNonemptyTextIngredientName", 500);
      enterIngredientInDialog(ingredient, ingredientDialog);
    }
  }

  private void setRecipePortionsField(JDialog dialog, Recipe recipe) throws Exception {
    JTextField portionsField = helper.findComponentByName(dialog, "InputFieldNonnegativeIntegerRecipePortions");
    helper.invokeAndWaitVoid(() -> {
      portionsField.setText(recipe.getNumberOfPortions().toString());
    });
  }

  private void setIngredientsTable(JDialog dialog, Recipe recipe) throws Exception {
    JTable ingredientsTable = findNamedTableInDialogOrFallback(dialog, "RecipeInputTable");
    List<QuantitativeIngredient> ingredientsAsIs = recipe.getIngredientListAsIs();
    helper.invokeAndWaitVoid(() -> {
      for (int i = 0; i < ingredientsAsIs.size(); i++) {
        QuantitativeIngredient ingredient = ingredientsAsIs.get(i);
        ingredientsTable.setValueAt(ingredient.getIngredient(), i, 0);
        ingredientsTable.setValueAt(ingredient.getAmount(), i, 1);
      }
    });
  }

  private void saveRecipeDialog(JDialog dialog) throws Exception {
    JButton saveButton = helper.findComponentByName(dialog, BUTTON_RECIPEINPUT_SAVE);
    helper.clickButtonOnEdt(saveButton);
    helper.waitForDialogToClose(dialog, 3000);
  }

  private void enterIngredientInDialog(Ingredient ingredient, JDialog dialog) {
    try {
      setIngredientNameField(dialog, ingredient);
      setIngredientTypeField(dialog, ingredient);
      setIngredientMeasureField(dialog, ingredient);
      saveIngredientDialog(dialog);
    } catch (Exception e) {
      throw new RuntimeException("Failed to enter ingredient in dialog", e);
    }
  }

  private void setIngredientNameField(JDialog dialog, Ingredient ingredient) throws Exception {
    JTextField nameField = helper.findComponentByName(dialog, "InputFieldNonemptyTextIngredientName");
    helper.invokeAndWaitVoid(() -> {
      nameField.setText(ingredient.getName());
    });
  }

  private void setIngredientTypeField(JDialog dialog, Ingredient ingredient) throws Exception {
    JComboBox<?> typeCombo = helper.findComponentByName(dialog, "InputFieldComboBoxIngredientType");
    @SuppressWarnings("unchecked")
    JComboBox<mealplaner.model.recipes.IngredientType> typeTypedCombo = 
        (JComboBox<mealplaner.model.recipes.IngredientType>) typeCombo;
    typeTypedCombo.setSelectedItem(ingredient.getType());
  }

  private void setIngredientMeasureField(JDialog dialog, Ingredient ingredient) throws Exception {
    JComboBox<?> measureCombo = helper.findComponentByName(dialog, "InputFieldComboBoxIngredientMeasure");
    @SuppressWarnings("unchecked")
    JComboBox<mealplaner.model.recipes.Measure> measureTypedCombo = (JComboBox<mealplaner.model.recipes.Measure>) measureCombo;
    measureTypedCombo.setSelectedItem(ingredient.getPrimaryMeasure());
  }

  private void saveIngredientDialog(JDialog dialog) throws Exception {
    JButton saveButton = helper.findComponentByName(dialog, BUTTON_INGREDIENTSINPUT_ADD);
    helper.clickButtonOnEdt(saveButton);
    helper.waitForDialogToClose(dialog, 3000);
  }

  

  public MealsEditPageObject mealsTabbedPane() {
    try {
      javax.swing.JTabbedPane tabbedPane = helper.findFirstComponentOfClass(frame, javax.swing.JTabbedPane.class);
      tabbedPane.setSelectedIndex(DATABASE_EDIT.number());
    } catch (Exception e) {
      throw new RuntimeException("Failed to set meals tabbed pane", e);
    }
    return this;
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