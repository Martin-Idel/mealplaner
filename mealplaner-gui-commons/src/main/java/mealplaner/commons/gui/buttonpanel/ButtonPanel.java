// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.buttonpanel;

import java.awt.Component;
import javax.swing.JPanel;

import mealplaner.commons.gui.GuiComponent;

public class ButtonPanel implements GuiComponent {
  private final JPanel panel;

  ButtonPanel(JPanel panel) {
    this.panel = panel;
  }

  @Override
  public Component getComponent() {
    return panel;
  }
}
