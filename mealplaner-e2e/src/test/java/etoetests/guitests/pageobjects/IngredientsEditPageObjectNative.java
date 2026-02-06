// SPDX-License-Identifier: MIT

package etoetests.guitests.pageobjects;

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

import mealplaner.commons.NonnegativeFraction;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.IngredientType;
import mealplaner.model.recipes.Measure;

public class IngredientsEditPageObjectNative {
  private static final int NUMBER_OF_INGREDIENT_DATA_COLUMNS = 4;

  private static final int INGREDIENTS_NAME_COLUMN = 0;
  private static final int INGREDIENTS_TYPE_COLUMN = 1;
  private static final int INGREDIENTS_MEASURE_COLUMN = 2;

  private final JFrame frame;

  IngredientsEditPageObjectNative(JFrame frame) {
    this.frame = frame;
  }

  public IngredientsEditPageObjectNative compareIngredientsInTable(List<Ingredient> ingredients) {
    JTable ingredientsTable = findTable();
    assertThat(ingredientsTable.getColumnCount()).isEqualTo(NUMBER_OF_INGREDIENT_DATA_COLUMNS);
    assertThat(ingredientsTable.getRowCount()).isEqualTo(ingredients.size());
    assertTableContents(ingredientsTable, ingredientsToTableContent(ingredients));
    return this;
  }

  public IngredientsEditPageObjectNative addIngredient(Ingredient ingredient) throws Exception {
    clickAddIngredientButton();
    enterIngredientInDialog(ingredient);
    clickSaveButton();
    Thread.sleep(500);
    return this;
  }

  public IngredientsEditPageObjectNative removeIngredients(int... rows) throws Exception {
    JTable ingredientsTable = findTable();
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      ingredientsTable.setRowSelectionInterval(rows[0], rows[rows.length - 1]);
    });
    clickRemoveButton();
    Thread.sleep(500);
    return this;
  }

  public IngredientsEditPageObjectNative changeName(int row, String name) throws Exception {
    JTable ingredientsTable = findTable();
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      ingredientsTable.setValueAt(name, row, INGREDIENTS_NAME_COLUMN);
    });
    Thread.sleep(200);
    return this;
  }

  public IngredientsEditPageObjectNative changeType(int row, IngredientType type) throws Exception {
    JTable ingredientsTable = findTable();
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      ingredientsTable.setValueAt(type, row, INGREDIENTS_TYPE_COLUMN);
    });
    Thread.sleep(200);
    return this;
  }

  public IngredientsEditPageObjectNative changeMeasure(int row, Measure measure) throws Exception {
    JTable ingredientsTable = findTable();
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      ingredientsTable.setValueAt(measure, row, INGREDIENTS_MEASURE_COLUMN);
    });
    Thread.sleep(200);
    return this;
  }

  private void clickRemoveButton() throws Exception {
    javax.swing.SwingUtilities.invokeLater(() -> {
      JButton button = (JButton) findComponentByName(frame, "ButtonPanelIngredientsEdit1");
      button.doClick();
    });
    Thread.sleep(1000);
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

  private JTable findTable() {
    return (JTable) findComponentByName(frame, "IngredientsEditTable");
  }

  private void clickAddIngredientButton() throws Exception {
    javax.swing.SwingUtilities.invokeLater(() -> {
      JButton button = (JButton) findComponentByName(frame, "ButtonPanelIngredientsEdit0");
      button.doClick();
    });
    Thread.sleep(1000);
  }

private void clickSaveButton() throws Exception {
    javax.swing.SwingUtilities.invokeLater(() -> {
      JButton button = (JButton) findComponentByName(frame, "ButtonPanelIngredientsEdit2");
      button.doClick();
    });
    Thread.sleep(1000);
  }

  private void enterIngredientInDialog(Ingredient ingredient) throws Exception {
    final JDialog[] dialog = new JDialog[1];
    try {
      javax.swing.SwingUtilities.invokeLater(() -> {
        dialog[0] = findDialog();
      });
    } catch (Exception e) {
      throw new RuntimeException("Failed to find dialog", e);
    }
    Thread.sleep(500);
    if (dialog[0] == null) {
      throw new RuntimeException("Dialog not found");
    }
    enterIngredient(ingredient, dialog[0]);
  }

  private JDialog findDialog() {
    for (java.awt.Window window : java.awt.Window.getWindows()) {
      if (window instanceof JDialog && window.isVisible()) {
        return (JDialog) window;
      }
    }
    return null;
  }

  private void enterIngredient(Ingredient ingredient, JDialog dialog) throws Exception {
    javax.swing.SwingUtilities.invokeLater(() -> {
      JTextField nameField = findTextField(dialog, "InputFieldNonemptyTextIngredientName");
      nameField.setText(ingredient.getName());

      JComboBox<?> typeCombo = findComboBox(dialog, "InputFieldComboBoxIngredientType");
      @SuppressWarnings("unchecked")
      JComboBox<IngredientType> typeTypedCombo = (JComboBox<IngredientType>) typeCombo;
      typeTypedCombo.setSelectedItem(ingredient.getType());

      JComboBox<?> measureCombo = findComboBox(dialog, "InputFieldComboBoxIngredientMeasure");
      @SuppressWarnings("unchecked")
      JComboBox<Measure> measureTypedCombo = (JComboBox<Measure>) measureCombo;
      measureTypedCombo.setSelectedItem(ingredient.getPrimaryMeasure());
    });
    Thread.sleep(500);

    if (!ingredient.getMeasures().getSecondaries().isEmpty()) {
      javax.swing.SwingUtilities.invokeLater(() -> {
        JButton measuresButton = findButton(dialog, "InputFieldButtonIngredientMeasures");
        measuresButton.doClick();
      });
      Thread.sleep(1000);

      final JDialog[] secondaryDialog = new JDialog[1];
      javax.swing.SwingUtilities.invokeLater(() -> {
        secondaryDialog[0] = findDialog();
      });
      Thread.sleep(500);
      enterIngredientSecondaryMeasures(ingredient.getMeasures().getSecondaries(), secondaryDialog[0]);
    }

    javax.swing.SwingUtilities.invokeLater(() -> {
      JButton addButton = findButton(dialog, "ButtonPanelIngredientsInput1");
      addButton.doClick();
    });
    Thread.sleep(1000);
  }

  private void enterIngredientSecondaryMeasures(Map<Measure, NonnegativeFraction> secondaries,
                                                JDialog dialog) throws Exception {
    JTable table = findTableInDialog(dialog);
    var secondaryList = new ArrayList<>(secondaries.entrySet());
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      for (int i = 0; i < secondaries.size(); i++) {
        table.setValueAt(secondaryList.get(i).getKey().toString(), i, 0);
        table.setValueAt(secondaryList.get(i).getValue().toString(), i, 1);
      }
    });
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      JButton saveButton = findButton(dialog, "ButtonPanelMeasuresInput2");
      saveButton.doClick();
    });
    Thread.sleep(200);
  }

  private JTable findTableInDialog(JDialog dialog) {
    for (java.awt.Component component : dialog.getContentPane().getComponents()) {
      if (component instanceof JTable) {
        return (JTable) component;
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

  private java.awt.Component findComponentByName(java.awt.Container parent, String name) {
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

  public IngredientsEditPageObjectNative ingredientsTabbedPane() {
    javax.swing.JTabbedPane tabbedPane = findTabbedPane();
    tabbedPane.setSelectedIndex(INGREDIENTS_EDIT.number());
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
    return (JButton) findComponentByName(frame, "ButtonPanelIngredientsEdit2");
  }

  public JButton cancelButton() {
    return (JButton) findComponentByName(frame, "ButtonPanelIngredientsEdit3");
  }

  public IngredientsEditPageObjectNative saveAndReplaceIngredientsWithDefaults() throws Exception {
    javax.swing.SwingUtilities.invokeLater(() -> {
      saveButton().doClick();
    });
    Thread.sleep(500);
    
    int maxAttempts = 10;
    for (int attempt = 0; attempt < maxAttempts; attempt++) {
      JOptionPane optionPane = findOptionPane();
      if (optionPane != null) {
        javax.swing.SwingUtilities.invokeLater(() -> {
          optionPane.setValue(JOptionPane.NO_OPTION);
        });
        Thread.sleep(1500);
        break;
      }
      Thread.sleep(200);
    }
    
    return this;
  }

  public IngredientsEditPageObjectNative saveButDoNotRemoveUsedIngredients() throws Exception {
    javax.swing.SwingUtilities.invokeLater(() -> {
      saveButton().doClick();
    });
    Thread.sleep(500);

    for (int attempt = 0; attempt < 10; attempt++) {
      JDialog dialog = findDialogByOptionPane();
      if (dialog != null) {
        javax.swing.SwingUtilities.invokeAndWait(() -> {
          List<JButton> buttons = findAllButtons(dialog.getRootPane());
          if (buttons.size() >= 2) {
            buttons.get(1).doClick();
          }
        });
        Thread.sleep(2000);
        return this;
      }
      Thread.sleep(200);
    }

    return this;
  }

  private JDialog findDialogByOptionPane() {
    for (java.awt.Window window : java.awt.Window.getWindows()) {
      if (window.isVisible() && window instanceof JDialog) {
        JOptionPane optionPane = findOptionPaneInContainer((JDialog) window);
        if (optionPane != null) {
          return (JDialog) window;
        }
      }
    }
    return null;
  }

  private List<JButton> findAllButtons(java.awt.Component component) {
    List<JButton> buttons = new java.util.ArrayList<>();
    if (component instanceof JButton) {
      buttons.add((JButton) component);
    }
    if (component instanceof java.awt.Container) {
      for (java.awt.Component child : ((java.awt.Container) component).getComponents()) {
        buttons.addAll(findAllButtons(child));
      }
    }
    return buttons;
  }

  private JDialog findDialogByButton(String buttonName) {
    for (java.awt.Window window : java.awt.Window.getWindows()) {
      if (window instanceof JDialog && window.isVisible()) {
        JDialog dialog = (JDialog) window;
        JButton button = findButton(dialog, buttonName);
        if (button != null) {
          return dialog;
        }
      }
    }
    return null;
  }

  private JOptionPane findOptionPane() {
    System.out.println("findOptionPane: checking " + java.awt.Window.getWindows().length + " windows");
    for (java.awt.Window window : java.awt.Window.getWindows()) {
      System.out.println("  Window: " + window.getClass().getSimpleName() + " visible=" + window.isVisible());
      if (window.isVisible() && window instanceof JDialog) {
        JOptionPane optionPane = findOptionPaneInContainer((JDialog) window);
        if (optionPane != null) {
          System.out.println("  Found JOptionPane in window");
          return optionPane;
        }
      }
    }
    System.out.println("  No JOptionPane found");
    return null;
  }

  private JOptionPane findOptionPaneInContainer(JDialog dialog) {
    for (java.awt.Component component : dialog.getContentPane().getComponents()) {
      if (component instanceof JOptionPane) {
        return (JOptionPane) component;
      }
      if (component instanceof java.awt.Container) {
        JOptionPane found = findOptionPaneRecursive((java.awt.Container) component);
        if (found != null) {
          return found;
        }
      }
    }
    return null;
  }

  private JOptionPane findOptionPaneRecursive(java.awt.Container container) {
    if (container instanceof JOptionPane) {
      return (JOptionPane) container;
    }
    for (java.awt.Component component : container.getComponents()) {
      if (component instanceof JOptionPane) {
        return (JOptionPane) component;
      }
      if (component instanceof java.awt.Container) {
        JOptionPane found = findOptionPaneRecursive((java.awt.Container) component);
        if (found != null) {
          return found;
        }
      }
    }
    return null;
  }
}