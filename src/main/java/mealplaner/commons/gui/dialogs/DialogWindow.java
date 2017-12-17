package mealplaner.commons.gui.dialogs;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mealplaner.commons.gui.GuiComponent;

public class DialogWindow extends JDialog {
  private static final long serialVersionUID = 1L;
  private final JFrame frame;
  private final JPanel mainPanel;

  protected DialogWindow(JFrame frame, String name) {
    super(frame, name, true);
    this.frame = frame;
    mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
  }

  public static DialogWindow window(JFrame frame, String name) {
    return new DialogWindow(frame, name);
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
    setLocationRelativeTo(frame);
  }
}
