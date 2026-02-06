// SPDX-License-Identifier: MIT

package etoetests.guitests.pageobjects;

import static java.time.LocalDate.now;
import static java.time.format.DateTimeFormatter.ofLocalizedDate;
import static java.time.format.FormatStyle.SHORT;
import static java.time.format.TextStyle.FULL;

import java.time.LocalDate;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import org.assertj.core.api.Assertions;

import mealplaner.model.meal.Meal;

public class ProposalTablePageObjectNative {
  private final JFrame frame;

  ProposalTablePageObjectNative(JFrame frame) {
    this.frame = frame;
  }

  public ShoppingListPageObjectNative requireProposalFrom(List<Meal> mealOutput) throws Exception {
    Thread.sleep(1500);
    final JDialog[] dialog = new JDialog[1];
    SwingUtilities.invokeAndWait(() -> {
      dialog[0] = findDialog();
    });
    Thread.sleep(500);

    JTable table = findTableInDialog(dialog[0]);
    SwingUtilities.invokeAndWait(() -> {
      String[][] expectedContent = generateProposalOutputContent(mealOutput);
      Assertions.assertThat(table.getColumnCount()).isEqualTo(3);
      Assertions.assertThat(table.getRowCount()).isEqualTo(mealOutput.size());
      assertTableContents(table, expectedContent);
    });

    SwingUtilities.invokeLater(() -> {
      JButton proceedButton = findButton(dialog[0], "ButtonPanelProposalOutput1");
      proceedButton.doClick();
    });
    Thread.sleep(1500);

    return new ShoppingListPageObjectNative(frame);
  }

  public void assertMissingRecipes() throws Exception {
    Thread.sleep(500);
    final JDialog[] dialog = new JDialog[1];
    SwingUtilities.invokeAndWait(() -> {
      dialog[0] = findDialog("OptionPane");
    });
if (dialog[0] != null) {
      SwingUtilities.invokeLater(() -> {
        JButton yesButton = findButton(dialog[0], "OptionPane.button");
        if (yesButton == null) {
          javax.swing.JOptionPane optionPane = (javax.swing.JOptionPane) findComponentByName(dialog[0], "OptionPane");
          if (optionPane != null) {
            optionPane.setValue(javax.swing.JOptionPane.YES_OPTION);
          }
        } else {
          yesButton.doClick();
        }
      });
      Thread.sleep(500);
    }
  }

  private String[][] generateProposalOutputContent(List<Meal> mealOutput) {
    String[][] contents = new String[mealOutput.size()][3];
    LocalDate date = now().plusDays(1);
    for (int row = 0; row < mealOutput.size(); row++) {
      contents[row][0] = ofLocalizedDate(SHORT).withLocale(mealplaner.commons.BundleStore.BUNDLES.locale()).format(date);
      contents[row][1] = date.getDayOfWeek().getDisplayName(FULL, mealplaner.commons.BundleStore.BUNDLES.locale());
      contents[row][2] = mealOutput.get(row).getName();
      date = date.plusDays(1);
    }
    return contents;
  }

  private void assertTableContents(JTable table, String[][] expectedContent) {
    for (int row = 0; row < expectedContent.length; row++) {
      for (int col = 0; col < expectedContent[row].length; col++) {
        Object actualValue = table.getValueAt(row, col);
        String expectedValue = expectedContent[row][col];
        if (actualValue instanceof Meal) {
          Assertions.assertThat(((Meal) actualValue).getName()).isEqualTo(expectedValue);
        } else {
          Assertions.assertThat(actualValue.toString()).isEqualTo(expectedValue);
        }
      }
    }
  }

  private JDialog findDialog() {
    for (java.awt.Window window : java.awt.Window.getWindows()) {
      if (window instanceof JDialog && window.isVisible()) {
        return (JDialog) window;
      }
    }
    return null;
  }

  private JDialog findDialog(String nameHint) {
    for (java.awt.Window window : java.awt.Window.getWindows()) {
      if (window instanceof JDialog && window.isVisible()) {
        if (nameHint != null && window.getName() != null && window.getName().contains(nameHint)) {
          return (JDialog) window;
        }
      }
    }
    return null;
  }

private JTable findTableInDialog(JDialog dialog) {
    JTable namedTable = (JTable) findComponentByName(dialog, "ProposalOutputTable");
    if (namedTable != null) {
      return namedTable;
    }
    return findFirstComponentOfClass(dialog, JTable.class);
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
}