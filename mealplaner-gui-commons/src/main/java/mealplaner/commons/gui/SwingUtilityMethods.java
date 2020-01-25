// SPDX-License-Identifier: MIT

package mealplaner.commons.gui;

import static javax.swing.KeyStroke.getKeyStroke;

import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Function;
import javax.swing.JButton;
import javax.swing.JComboBox;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;

public final class SwingUtilityMethods {
  private SwingUtilityMethods() {
  }

  public static JButton createButton(String name,
      String label,
      String mnemonic,
      ActionListener listener) {
    JButton button = new JButton(label);
    button.setName(name);
    button.setMnemonic(getKeyStroke(mnemonic).getKeyCode());
    button.addActionListener(listener);
    return button;
  }

}
