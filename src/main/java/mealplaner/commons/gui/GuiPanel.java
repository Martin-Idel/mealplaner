package mealplaner.commons.gui;

import javax.swing.JPanel;

public interface GuiPanel extends GuiComponent {
  @Override
  JPanel getComponent();
}
