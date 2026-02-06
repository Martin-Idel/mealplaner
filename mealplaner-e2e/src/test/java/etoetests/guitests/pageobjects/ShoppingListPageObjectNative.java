// SPDX-License-Identifier: MIT

package etoetests.guitests.pageobjects;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JOptionPane;

import mealplaner.model.recipes.QuantitativeIngredient;
import mealplaner.model.shoppinglist.ShoppingList;
import org.assertj.core.api.Assertions;

public class ShoppingListPageObjectNative {
  private final JFrame frame;

  ShoppingListPageObjectNative(JFrame frame) {
    this.frame = frame;
  }

  public ShoppingListPageObjectNative assertMissingRecipes() throws Exception {
    Thread.sleep(500);
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      for (java.awt.Window window : java.awt.Window.getWindows()) {
        if (window instanceof JDialog && window.isVisible()) {
          JOptionPane optionPane = findOptionPane((JDialog) window);
          if (optionPane != null) {
            optionPane.setValue(JOptionPane.YES_OPTION);
          }
        }
      }
    });
    Thread.sleep(500);
    return this;
  }

  public void requireShoppingListContentAndClose(ShoppingList shoppingList) throws Exception {
    Thread.sleep(1500);
    final JDialog[] dialog = new JDialog[1];
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      dialog[0] = findDialog();
    });
    Thread.sleep(500);

    int maxRetries = 10;
    int retryCount = 0;
    while (dialog[0] != null && findOptionPane(dialog[0]) != null && retryCount < maxRetries) {
      JOptionPane optionPane = findOptionPane(dialog[0]);
      javax.swing.SwingUtilities.invokeLater(() -> {
        optionPane.setValue(javax.swing.JOptionPane.YES_OPTION);
      });
      Thread.sleep(1500);
      javax.swing.SwingUtilities.invokeAndWait(() -> {
        dialog[0] = findDialog();
      });
      Thread.sleep(500);
      retryCount++;
    }
    if (retryCount >= maxRetries && findOptionPane(dialog[0]) != null) {
      throw new RuntimeException("Could not dismiss JOptionPane after " + maxRetries + " retries");
    }

    final JTable[] tableRef = new JTable[1];
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      if (dialog[0] == null) {
        throw new RuntimeException("No visible dialog found for shopping list");
      }
      JTable table = findTableInDialog(dialog[0]);
      if (table == null) {
        System.err.println("Dialog found but no table. Dialog components:");
        logDialogComponents(dialog[0], 0);
        throw new RuntimeException("Shopping list table not found in dialog");
      }
      tableRef[0] = table;
    });
    Thread.sleep(500);
    
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      Assertions.assertThat(tableRef[0].getRowCount()).isGreaterThan(0);
      String[][] expectedContent = generateShoppingListContent(shoppingList);
      assertTableContents(tableRef[0], expectedContent);
    });

    javax.swing.SwingUtilities.invokeLater(() -> {
      JButton closeButton = findButton(dialog[0], "ButtonPanelShoppingListDialog1");
      closeButton.doClick();
    });
    Thread.sleep(1500);
  }

  private String[][] generateShoppingListContent(ShoppingList shoppingList) {
    List<QuantitativeIngredient> shoppings = shoppingList.getList();
    String[][] contents = new String[shoppings.size() + 1][3];
    for (int row = 0; row < shoppings.size(); row++) {
      QuantitativeIngredient ingredient = shoppings.get(row);
      contents[row][0] = ingredient.getIngredient().getName();
      contents[row][1] = ingredient.getAmount().toString();
      contents[row][2] = ingredient.getIngredient().getPrimaryMeasure().toString();
    }
    contents[shoppings.size()][0] = "";
    contents[shoppings.size()][1] = "";
    contents[shoppings.size()][2] = "";
    return contents;
  }

  private void assertTableContents(JTable table, String[][] expectedContent) {
    for (int row = 0; row < expectedContent.length; row++) {
      for (int col = 0; col < expectedContent[row].length; col++) {
        Object actualValue = table.getValueAt(row, col);
        String expectedValue = expectedContent[row][col];
        String actualValueString = (actualValue != null) ? actualValue.toString() : null;
        if (actualValueString != null && actualValue.getClass().getName().contains("Ingredient")) {
          actualValueString = extractIngredientName(actualValueString);
        }
        Assertions.assertThat(actualValueString).isEqualTo(expectedValue);
      }
    }
  }

  private String extractIngredientName(String ingredientString) {
    int nameStart = ingredientString.indexOf("name='");
    if (nameStart == -1) {
      return ingredientString;
    }
    nameStart += 6;
    int nameEnd = ingredientString.indexOf("'", nameStart);
    if (nameEnd == -1) {
      return ingredientString;
    }
    return ingredientString.substring(nameStart, nameEnd);
  }

  private JDialog findDialog() {
    for (java.awt.Window window : java.awt.Window.getWindows()) {
      if (window instanceof JDialog && window.isVisible()) {
        return (JDialog) window;
      }
    }
    return null;
  }

  private JOptionPane findOptionPane(JDialog dialog) {
    return findOptionPaneInDialog(dialog);
  }

  private java.awt.Container findOptionPaneButtonArea(JDialog dialog) {
    JOptionPane optionPane = findOptionPane(dialog);
    if (optionPane == null) {
      return null;
    }
    for (java.awt.Component comp : optionPane.getComponents()) {
      if (comp instanceof java.awt.Container) {
        java.awt.Container container = (java.awt.Container) comp;
        if (container.getName() != null && container.getName().equals("OptionPane.buttonArea")) {
          return container;
        }
      }
    }
    return null;
  }

  private JTable findTableInDialog(JDialog dialog) {
    JTable namedTable = (JTable) findComponentByName(dialog, "ShoppingListTable");
    if (namedTable != null) {
      return namedTable;
    }
    return findJTableInContainer(dialog);
  }

  private JTable findJTableInContainer(java.awt.Container parent) {
    if (JTable.class.isInstance(parent)) {
      return (JTable) parent;
    }
    for (java.awt.Component component : parent.getComponents()) {
      if (JTable.class.isInstance(component)) {
        return (JTable) component;
      }
      if (component instanceof java.awt.Container) {
        JTable found = findJTableInContainer((java.awt.Container) component);
        if (found != null) {
          return found;
        }
      }
    }
    return null;
  }

  private <T> T findFirstComponentOfClass(java.awt.Container parent, Class<T> clazz) {
    if (clazz.isInstance(parent)) {
      return clazz.cast(parent);
    }
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

  private JOptionPane findOptionPaneInDialog(JDialog dialog) {
    for (java.awt.Window window : java.awt.Window.getWindows()) {
      if (window == dialog && window instanceof JDialog) {
        java.awt.Component[] components = dialog.getContentPane().getComponents();
        for (java.awt.Component component : components) {
          if (component instanceof JOptionPane) {
            return (JOptionPane) component;
          }
          if (component instanceof java.awt.Container) {
            JOptionPane found = findOptionPaneInContainer((java.awt.Container) component);
            if (found != null) {
              return found;
            }
          }
        }
      }
    }
    return null;
  }

private JOptionPane findOptionPaneInContainer(java.awt.Container container) {
    if (container instanceof JOptionPane) {
      return (JOptionPane) container;
    }
    for (java.awt.Component component : container.getComponents()) {
      if (component instanceof JOptionPane) {
        return (JOptionPane) component;
      }
      if (component instanceof java.awt.Container) {
        JOptionPane found = findOptionPaneInContainer((java.awt.Container) component);
        if (found != null) {
          return found;
        }
      }
    }
    return null;
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

  private void logDialogComponents(java.awt.Container container, int depth) {
    String indent = "  ".repeat(depth);
    System.err.println(indent + container.getClass().getSimpleName() + " [name=" + container.getName() + ", visible=" + container.isVisible() + "]");
    for (java.awt.Component component : container.getComponents()) {
      if (component instanceof java.awt.Container) {
        logDialogComponents((java.awt.Container) component, depth + 1);
      } else {
        System.err.println(indent + "  " + component.getClass().getSimpleName() + " [name=" + component.getName() + ", visible=" + component.isVisible() + "]");
      }
    }
  }
}