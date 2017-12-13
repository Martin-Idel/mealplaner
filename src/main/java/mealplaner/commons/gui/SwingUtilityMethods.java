package mealplaner.commons.gui;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.KeyStroke;

public final class SwingUtilityMethods {
  private SwingUtilityMethods() {
  }

  public static JButton createButton(String label, String mnemonic, ActionListener listener) {
    JButton button = new JButton(label);
    button.setMnemonic(KeyStroke.getKeyStroke(mnemonic).getKeyCode());
    button.addActionListener(listener);
    return button;
  }
}
