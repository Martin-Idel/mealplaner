// SPDX-License-Identifier: MIT

package mealplaner.commons.gui;

import java.awt.Component;
import java.awt.GridLayout;
import java.io.Serializable;
import javax.swing.JPanel;

public final class GridPanel implements Serializable, GuiPanel {
  private static final long serialVersionUID = 1L;

  private final JPanel grid;

  private GridPanel(int rows, int columns) {
    grid = new JPanel();
    grid.setLayout(new GridLayout(rows, columns));
  }

  public static GridPanel gridPanel(int rows, int columns) {
    return new GridPanel(rows, columns);
  }

  @Override
  public JPanel getComponent() {
    return grid;
  }

  public void add(Component panel) {
    grid.add(panel);
  }
}
