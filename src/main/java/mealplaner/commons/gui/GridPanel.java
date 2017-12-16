package mealplaner.commons.gui;

import java.awt.GridLayout;
import java.io.Serializable;

import javax.swing.JPanel;

public final class GridPanel implements Serializable {
  private static final long serialVersionUID = 1L;

  private final JPanel gridPanel;

  private GridPanel(int rows, int columns) {
    gridPanel = new JPanel();
    gridPanel.setLayout(new GridLayout(rows, columns));
  }

  public static GridPanel gridPanel(int rows, int columns) {
    return new GridPanel(rows, columns);
  }

  public JPanel getPanel() {
    return gridPanel;
  }
}
