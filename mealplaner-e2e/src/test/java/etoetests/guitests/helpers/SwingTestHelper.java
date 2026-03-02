// SPDX-License-Identifier: MIT

package etoetests.guitests.helpers;

import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SwingTestHelper {
  private final JFrame frame;

  public SwingTestHelper(JFrame frame) {
    this.frame = frame;
  }

  public void waitForCondition(BooleanSupplier condition, long timeoutMillis, String errorMessage) throws Exception {
    long startTime = System.currentTimeMillis();
    while (System.currentTimeMillis() - startTime < timeoutMillis) {
      if (condition.getAsBoolean()) {
        return;
      }
      Thread.sleep(50);
    }
    fail(errorMessage);
  }

  @SuppressWarnings("unchecked")
  public <T extends Component> T findComponentByName(JFrame frame, String name) throws Exception {
    Component component = findComponentByNameRecursive(frame, name);
    if (component == null) {
      throw new AssertionError("Component not found: " + name);
    }
    return (T) component;
  }

  @SuppressWarnings("unchecked")
  public <T extends Component> T findComponentByName(JFrame frame, String name, Class<T> type) throws Exception {
    Component component = findComponentByNameRecursive(frame, name);
    if (component == null) {
      throw new AssertionError("Component not found: " + name);
    }
    if (!type.isInstance(component)) {
      throw new AssertionError("Component " + name + " is not of type " + type.getSimpleName() + 
                             " but " + component.getClass().getSimpleName());
    }
    return (T) component;
  }

  @SuppressWarnings("unchecked")
  public <T extends Component> T findComponentByName(JFrame frame, String name, Class<T> type, long timeoutMillis) throws Exception {
    final Component[] result = new Component[1];
    waitForCondition(() -> {
      try {
        SwingUtilities.invokeAndWait(() -> {
          result[0] = findComponentByNameRecursive(frame, name);
        });
        return result[0] != null;
      } catch (Exception e) {
        return false;
      }
    }, timeoutMillis, "Component not found: " + name + " within " + timeoutMillis + "ms");
    
    if (!type.isInstance(result[0])) {
      throw new AssertionError("Component " + name + " is not of type " + type.getSimpleName() + 
                             " but " + result[0].getClass().getSimpleName());
    }
    return (T) result[0];
  }

  public <T extends Component> T findComponentByName(Container parent, String name, Class<T> type) throws Exception {
    final Component[] result = new Component[1];
    SwingUtilities.invokeAndWait(() -> {
      result[0] = findComponentByNameRecursive(parent, name);
    });
    if (result[0] == null) {
      throw new AssertionError("Component not found: " + name);
    }
    if (!type.isInstance(result[0])) {
      throw new AssertionError("Component " + name + " is not of type " + type.getSimpleName() + 
                             " but " + result[0].getClass().getSimpleName());
    }
    return type.cast(result[0]);
  }

  public <T extends Component> T findComponentByName(JDialog dialog, String name) throws Exception {
    final Component[] result = new Component[1];
    SwingUtilities.invokeAndWait(() -> {
      result[0] = findComponentByNameRecursive(dialog, name);
    });
    if (result[0] == null) {
      throw new AssertionError("Component not found: " + name);
    }
    return (T) result[0];
  }

  public <T extends Component> T findComponentByNameOrNull(JDialog dialog, String name, Class<T> type) throws Exception {
    final Component[] result = new Component[1];
    SwingUtilities.invokeAndWait(() -> {
      result[0] = findComponentByNameRecursive(dialog, name);
    });
    if (result[0] != null && !type.isInstance(result[0])) {
      throw new AssertionError("Component " + name + " is not of type " + type.getSimpleName() + 
                             " but " + result[0].getClass().getSimpleName());
    }
    return result[0] != null ? type.cast(result[0]) : null;
  }

  public Component findComponentByNameRecursive(Container parent, String name) {
    if (name.equals(parent.getName())) {
      return parent;
    }
    for (Component component : parent.getComponents()) {
      if (name.equals(component.getName())) {
        return component;
      }
      if (component instanceof Container) {
        Component found = findComponentByNameRecursive((Container) component, name);
        if (found != null) {
          return found;
        }
      }
    }
    return null;
  }

  public JDialog findDialog() {
    for (java.awt.Window window : java.awt.Window.getWindows()) {
      if (window instanceof JDialog && window.isVisible()) {
        return (JDialog) window;
      }
    }
    return null;
  }

  public JDialog findDialog(long timeoutMillis) throws Exception {
    final JDialog[] result = new JDialog[1];
    waitForCondition(() -> {
      result[0] = findDialog();
      return result[0] != null;
    }, timeoutMillis, "Dialog not found within " + timeoutMillis + "ms");
    return result[0];
  }

  public void waitForDialogToClose(JDialog dialog, long timeoutMillis) throws Exception {
    waitForCondition(() -> {
      return !dialog.isVisible();
    }, timeoutMillis, "Dialog did not close within " + timeoutMillis + "ms");
  }

  public JDialog findDialogContaining(String componentName, long timeoutMillis) throws Exception {
    final JDialog[] result = new JDialog[1];
    waitForCondition(() -> {
      for (java.awt.Window window : java.awt.Window.getWindows()) {
        if (window instanceof JDialog && window.isVisible()) {
          JDialog dialog = (JDialog) window;
          Component component = findComponentByNameRecursive(dialog, componentName);
          if (component != null) {
            result[0] = dialog;
            return true;
          }
        }
      }
      return false;
    }, timeoutMillis, "Dialog containing " + componentName + " not found within " + timeoutMillis + "ms");
    return result[0];
  }

  public JOptionPane findOptionPane() {
    for (java.awt.Window window : java.awt.Window.getWindows()) {
      if (window.isVisible() && window instanceof JDialog) {
        JOptionPane optionPane = findOptionPaneInContainer((JDialog) window);
        if (optionPane != null) {
          return optionPane;
        }
      }
    }
    return null;
  }

  public JOptionPane findOptionPane(long timeoutMillis) throws Exception {
    final JOptionPane[] result = new JOptionPane[1];
    waitForCondition(() -> {
      result[0] = findOptionPane();
      return result[0] != null;
    }, timeoutMillis, "OptionPane not found within " + timeoutMillis + "ms");
    return result[0];
  }

  private JOptionPane findOptionPaneInContainer(JDialog dialog) {
    for (Component component : dialog.getContentPane().getComponents()) {
      if (component instanceof JOptionPane) {
        return (JOptionPane) component;
      }
      if (component instanceof Container) {
        JOptionPane found = findOptionPaneRecursive((Container) component);
        if (found != null) {
          return found;
        }
      }
    }
    return null;
  }

  private JOptionPane findOptionPaneRecursive(Container container) {
    if (container instanceof JOptionPane) {
      return (JOptionPane) container;
    }
    for (Component component : container.getComponents()) {
      if (component instanceof JOptionPane) {
        return (JOptionPane) component;
      }
      if (component instanceof Container) {
        JOptionPane found = findOptionPaneRecursive((Container) component);
        if (found != null) {
          return found;
        }
      }
    }
    return null;
  }

  public List<JButton> findAllButtons(Component component) {
    List<JButton> buttons = new ArrayList<>();
    if (component instanceof JButton) {
      buttons.add((JButton) component);
    }
    if (component instanceof Container) {
      for (Component child : ((Container) component).getComponents()) {
        buttons.addAll(findAllButtons(child));
      }
    }
    return buttons;
  }

  public <T extends Component> T findFirstComponentOfClass(Container parent, Class<T> clazz) {
    for (Component component : parent.getComponents()) {
      if (clazz.isInstance(component)) {
        return clazz.cast(component);
      }
      if (component instanceof Container) {
        T found = findFirstComponentOfClass((Container) component, clazz);
        if (found != null) {
          return found;
        }
      }
    }
    return null;
  }

  public void invokeLaterVoid(Runnable runnable) {
    SwingUtilities.invokeLater(runnable);
  }

  public void invokeAndWaitVoid(Runnable runnable) throws Exception {
    SwingUtilities.invokeAndWait(runnable);
  }

  public void clickButtonOnEdt(JButton button) {
    SwingUtilities.invokeLater(() -> button.doClick());
  }

  public void handleOptionPaneWithOption(int option, long timeoutMillis) throws Exception {
    JOptionPane optionPane = findOptionPane(timeoutMillis);
    if (optionPane != null) {
      invokeLaterVoid(() -> optionPane.setValue(option));
      waitForCondition(() -> findOptionPane() == null, timeoutMillis, "OptionPane did not close within " + timeoutMillis + "ms");
    }
  }

  public void handleOptionPaneWithOptionIfExists(int option, long timeoutMillis) throws Exception {
    JOptionPane optionPane = findOptionPane();
    if (optionPane != null) {
      invokeLaterVoid(() -> optionPane.setValue(option));
      waitForCondition(() -> findOptionPane() == null, timeoutMillis, "OptionPane did not close within " + timeoutMillis + "ms");
    }
  }

  public void waitForOptionPaneToBeClosed(long timeoutMillis) throws Exception {
    waitForCondition(() -> findOptionPane() == null, timeoutMillis, "OptionPane did not close within " + timeoutMillis + "ms");
  }

  public void handleMultipleOptionPanes(int option, int maxAttempts, long timeoutMillis) throws Exception {
    for (int i = 0; i < maxAttempts; i++) {
      JOptionPane optionPane = findOptionPane();
      if (optionPane == null) {
        return;
      }
      handleOptionPaneWithOptionIfExists(option, timeoutMillis);
    }
  }
}