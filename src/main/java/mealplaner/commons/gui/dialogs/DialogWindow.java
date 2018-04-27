// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mealplaner.commons.gui.GuiComponent;

public class DialogWindow extends JDialog {
  private static final long serialVersionUID = 1L;
  private final Window window;
  private final JPanel mainPanel;
  private final BorderLayout borderLayout;

  protected DialogWindow(JFrame frame, String name, String swingName) {
    super(frame, name, true);
    this.setName(swingName);
    this.window = frame;
    mainPanel = new JPanel();
    borderLayout = new BorderLayout();
    mainPanel.setLayout(borderLayout);
  }

  protected DialogWindow(JDialog dialog, String name, String swingName) {
    super(dialog, name, true);
    this.setName(swingName);
    this.window = dialog;
    mainPanel = new JPanel();
    borderLayout = new BorderLayout();
    mainPanel.setLayout(borderLayout);
  }

  public static DialogWindow window(JFrame frame, String name, String swingName) {
    return new DialogWindow(frame, name, swingName);
  }

  public static DialogWindow window(JDialog dialog, String name, String swingName) {
    return new DialogWindow(dialog, name, swingName);
  }

  public void setVisible() {
    setVisible(true);
  }

  public void setHidden() {
    setVisible(false);
  }

  public void addCentral(GuiComponent panel) {
    mainPanel.add(panel.getComponent(), BorderLayout.CENTER);
  }

  public void swapCentral(GuiComponent oldPanel, GuiComponent newPanel) {
    mainPanel.remove(borderLayout.getLayoutComponent(BorderLayout.CENTER));
    mainPanel.add(newPanel.getComponent(), BorderLayout.CENTER);
    mainPanel.validate();
  }

  public void addNorth(GuiComponent panel) {
    mainPanel.add(panel.getComponent(), BorderLayout.NORTH);
  }

  public void addSouth(GuiComponent panel) {
    mainPanel.add(panel.getComponent(), BorderLayout.SOUTH);
  }

  public void addEast(GuiComponent panel) {
    mainPanel.add(panel.getComponent(), BorderLayout.EAST);
  }

  public void addWest(GuiComponent panel) {
    mainPanel.add(panel.getComponent(), BorderLayout.WEST);
  }

  public void arrangeWithSize(int width, int height) {
    getContentPane().add(mainPanel);
    setSize(width, height);
    setLocationRelativeTo(window);
  }
}
