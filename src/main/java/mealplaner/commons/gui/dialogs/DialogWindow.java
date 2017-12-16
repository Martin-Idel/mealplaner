package mealplaner.commons.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

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

  public void addCentral(Component panel) {
    mainPanel.add(panel, BorderLayout.CENTER);
  }

  public void addNorth(Component panel) {
    mainPanel.add(panel, BorderLayout.NORTH);
  }

  public void addSouth(Component panel) {
    mainPanel.add(panel, BorderLayout.SOUTH);
  }

  public void addEast(Component panel) {
    mainPanel.add(panel, BorderLayout.EAST);
  }

  public void addWest(Component panel) {
    mainPanel.add(panel, BorderLayout.WEST);
  }

  public void arrangeWithSize(int width, int height) {
    getContentPane().add(mainPanel);
    setSize(width, height);
    setLocationRelativeTo(frame);
  }
}
