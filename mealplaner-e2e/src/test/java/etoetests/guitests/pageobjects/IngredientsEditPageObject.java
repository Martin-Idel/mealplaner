// SPDX-License-Identifier: MIT

package etoetests.guitests.pageobjects;

import static etoetests.guitests.constants.ComponentNames.BUTTON_INGREDIENTSEDIT_ADD;
import static etoetests.guitests.constants.ComponentNames.BUTTON_INGREDIENTSEDIT_REMOVE;
import static etoetests.guitests.constants.ComponentNames.BUTTON_INGREDIENTSEDIT_SAVE;
import static etoetests.guitests.constants.ComponentNames.BUTTON_INGREDIENTSEDIT_CANCEL;
import static etoetests.guitests.constants.ComponentNames.BUTTON_INGREDIENTSINPUT_ADD;
import static etoetests.guitests.constants.ComponentNames.BUTTON_MEASURESINPUT_SAVE;
import static etoetests.guitests.pageobjects.TabbedPanes.INGREDIENTS_EDIT;
import static mealplaner.commons.BundleStore.BUNDLES;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import etoetests.guitests.helpers.SwingTestHelper;
import mealplaner.commons.NonnegativeFraction;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.IngredientType;
import mealplaner.model.recipes.Measure;

public class IngredientsEditPageObject extends BasePageObject {
  private static final int NUMBER_OF_INGREDIENT_DATA_COLUMNS = 4;

  private static final int INGREDIENTS_NAME_COLUMN = 0;
  private static final int INGREDIENTS_TYPE_COLUMN = 1;
  private static final int INGREDIENTS_MEASURE_COLUMN = 2;

  IngredientsEditPageObject(JFrame frame) {
    super(frame);
  }

  public IngredientsEditPageObject compareIngredientsInTable(List<Ingredient> ingredients) {
    JTable ingredientsTable = findTableByName(frame, "IngredientsEditTable");
    assertThat(ingredientsTable.getColumnCount()).isEqualTo(NUMBER_OF_INGREDIENT_DATA_COLUMNS);
    assertThat(ingredientsTable.getRowCount()).isEqualTo(ingredients.size());
    assertTableContents(ingredientsTable, ingredientsToTableContent(ingredients));
    return this;
  }

  public IngredientsEditPageObject addIngredient(Ingredient ingredient) throws Exception {
    clickAddIngredientButton();
    enterIngredientInDialog(ingredient);
    clickSaveButton();
    waitForTableUpdate();
    return this;
  }

  public IngredientsEditPageObject removeIngredients(int... rows) throws Exception {
    JTable ingredientsTable = findTableByName(frame, "IngredientsEditTable");
    helper.invokeAndWaitVoid(() -> {
      ingredientsTable.setRowSelectionInterval(rows[0], rows[rows.length - 1]);
    });
    clickRemoveButton();
    waitForTableUpdate();
    return this;
  }

  public IngredientsEditPageObject changeName(int row, String name) throws Exception {
    JTable ingredientsTable = findTableByName(frame, "IngredientsEditTable");
    helper.invokeAndWaitVoid(() -> {
      ingredientsTable.setValueAt(name, row, INGREDIENTS_NAME_COLUMN);
    });
    waitForTableUpdate();
    return this;
  }

  public IngredientsEditPageObject changeType(int row, IngredientType type) throws Exception {
    JTable ingredientsTable = findTableByName(frame, "IngredientsEditTable");
    helper.invokeAndWaitVoid(() -> {
      ingredientsTable.setValueAt(type, row, INGREDIENTS_TYPE_COLUMN);
    });
    waitForTableUpdate();
    return this;
  }

  public IngredientsEditPageObject changeMeasure(int row, Measure measure) throws Exception {
    JTable ingredientsTable = findTableByName(frame, "IngredientsEditTable");
    helper.invokeAndWaitVoid(() -> {
      ingredientsTable.setValueAt(measure, row, INGREDIENTS_MEASURE_COLUMN);
    });
    waitForTableUpdate();
    return this;
  }

  private void clickRemoveButton() throws Exception {
    JButton button = helper.findComponentByName(frame, BUTTON_INGREDIENTSEDIT_REMOVE);
    helper.clickButtonOnEdt(button);
    helper.handleOptionPaneWithOptionIfExists(javax.swing.JOptionPane.YES_OPTION, 2000);
  }

  private void waitForTableUpdate() throws Exception {
    helper.waitForCondition(() -> {
      try {
        helper.invokeAndWaitVoid(() -> {
          findTableByName(frame, "IngredientsEditTable");
        });
        return true;
      } catch (Exception e) {
        return false;
      }
    }, 1000, "Table did not update within timeout");
  }

  private void waitForOptionPaneToBeHandled() throws Exception {
    helper.waitForCondition(() -> {
      return helper.findOptionPane() == null;
    }, 2000, "OptionPane did not close within timeout");
  }

  

  

private void clickAddIngredientButton() throws Exception {
    JButton button = helper.findComponentByName(frame, BUTTON_INGREDIENTSEDIT_ADD);
    helper.clickButtonOnEdt(button);
  }

 private void clickSaveButton() throws Exception {
    JButton button = helper.findComponentByName(frame, BUTTON_INGREDIENTSEDIT_SAVE);
    helper.clickButtonOnEdt(button);
    helper.waitForOptionPaneToBeClosed(1000);
  }

  private void enterIngredientInDialog(Ingredient ingredient) throws Exception {
    try {
      JDialog dialog = helper.findDialogContaining("InputFieldNonemptyTextIngredientName", 1000);
      if (dialog == null) {
        throw new RuntimeException("Dialog not found");
      }
      enterIngredient(ingredient, dialog);
    } catch (Exception e) {
      throw new RuntimeException("Failed to find dialog", e);
    }
  }

  private void enterIngredient(Ingredient ingredient, JDialog dialog) throws Exception {
    JTextField nameField = helper.findComponentByName(dialog, "InputFieldNonemptyTextIngredientName");
    JComboBox<?> typeCombo = helper.findComponentByName(dialog, "InputFieldComboBoxIngredientType");
    JComboBox<?> measureCombo = helper.findComponentByName(dialog, "InputFieldComboBoxIngredientMeasure");
    
    helper.invokeLaterVoid(() -> {
      nameField.setText(ingredient.getName());
      
      @SuppressWarnings("unchecked")
      JComboBox<IngredientType> typeTypedCombo = (JComboBox<IngredientType>) typeCombo;
      typeTypedCombo.setSelectedItem(ingredient.getType());
      
      @SuppressWarnings("unchecked")
      JComboBox<Measure> measureTypedCombo = (JComboBox<Measure>) measureCombo;
      measureTypedCombo.setSelectedItem(ingredient.getPrimaryMeasure());
    });

    if (!ingredient.getMeasures().getSecondaries().isEmpty()) {
      JButton measuresButton = helper.findComponentByName(dialog, "InputFieldButtonIngredientMeasures");
      helper.clickButtonOnEdt(measuresButton);
      JDialog secondaryDialog = helper.findDialog(1000);
      if (secondaryDialog == null) {
        throw new RuntimeException("Secondary dialog not found");
      }
      enterIngredientSecondaryMeasures(ingredient.getMeasures().getSecondaries(), secondaryDialog);
    }

    JButton addButton = helper.findComponentByName(dialog, BUTTON_INGREDIENTSINPUT_ADD);
    helper.clickButtonOnEdt(addButton);
    helper.waitForDialogToClose(dialog, 3000);
  }

  private void enterIngredientSecondaryMeasures(Map<Measure, NonnegativeFraction> secondaries,
                                                JDialog dialog) throws Exception {
JTable table = helper.findFirstComponentOfClass(dialog, JTable.class);
    var secondaryList = new ArrayList<>(secondaries.entrySet());
    helper.invokeAndWaitVoid(() -> {
      for (int i = 0; i < secondaries.size(); i++) {
        table.setValueAt(secondaryList.get(i).getKey(), i, 0);
        table.setValueAt(secondaryList.get(i).getValue().toString(), i, 1);
      }
    });
    JButton saveButton = helper.findComponentByName(dialog, BUTTON_MEASURESINPUT_SAVE);
    helper.clickButtonOnEdt(saveButton);
    helper.waitForDialogToClose(dialog, 2000);
  }

  public IngredientsEditPageObject ingredientsTabbedPane() {
    javax.swing.JTabbedPane tabbedPane = helper.findFirstComponentOfClass(frame, javax.swing.JTabbedPane.class);
    tabbedPane.setSelectedIndex(INGREDIENTS_EDIT.number());
    return this;
  }

  private String[][] ingredientsToTableContent(List<Ingredient> ingredients) {
    String[][] content = new String[ingredients.size()][NUMBER_OF_INGREDIENT_DATA_COLUMNS];
    for (int i = 0; i < ingredients.size(); i++) {
      Ingredient ingredient = ingredients.get(i);
      content[i][0] = ingredient.getName();
      content[i][1] = ingredient.getType().toString();
      content[i][2] = ingredient.getPrimaryMeasure().toString();
      content[i][3] = ingredient.getMeasures().getSecondaries().isEmpty()
          ? BUNDLES.message("insertSecondaryMeasuresButtonDefaultLabel")
          : BUNDLES.message("insertSecondaryMeasuresButtonLabel");
    }
    return content;
  }

  public JButton saveButton() {
    try {
      return helper.findComponentByName(frame, BUTTON_INGREDIENTSEDIT_SAVE);
    } catch (Exception e) {
      throw new RuntimeException("Failed to find save button", e);
    }
  }

  public JButton cancelButton() {
    try {
      return helper.findComponentByName(frame, BUTTON_INGREDIENTSEDIT_CANCEL);
    } catch (Exception e) {
      throw new RuntimeException("Failed to find cancel button", e);
    }
  }

  public IngredientsEditPageObject saveAndReplaceIngredientsWithDefaults() throws Exception {
    helper.clickButtonOnEdt(saveButton());
    
    JOptionPane optionPane = helper.findOptionPane(2000);
    if (optionPane != null) {
      helper.invokeLaterVoid(() -> {
        optionPane.setValue(JOptionPane.NO_OPTION);
      });
      helper.waitForCondition(() -> {
        return helper.findOptionPane() == null;
      }, 1500, "OptionPane did not close within timeout");
    }
    
    return this;
  }

  public IngredientsEditPageObject saveButDoNotRemoveUsedIngredients() throws Exception {
    helper.clickButtonOnEdt(saveButton());

    JOptionPane optionPane = helper.findOptionPane(2000);
    if (optionPane != null) {
      List<JButton> buttons = helper.findAllButtons(optionPane.getParent());
      if (buttons.size() >= 2) {
        helper.invokeAndWaitVoid(() -> {
          buttons.get(1).doClick();
        });
        helper.waitForCondition(() -> {
          return helper.findOptionPane() == null;
        }, 2000, "OptionPane did not close within timeout");
      }
    }

    return this;
  }
}