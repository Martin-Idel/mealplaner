package mealplaner.commons.gui;

import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.KeyStroke;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;

public final class SwingUtilityMethods {
  private SwingUtilityMethods() {
  }

  public static JButton createButton(String label, String mnemonic, ActionListener listener) {
    JButton button = new JButton(label);
    button.setMnemonic(KeyStroke.getKeyStroke(mnemonic).getKeyCode());
    button.addActionListener(listener);
    return button;
  }

  public static <T> ComboBoxCellEditor autoCompleteCellEditor(List<T> list,
      Function<T, String> label) {
    String[] listAndEmptyElement = new String[list.size() + 1];
    for (int i = 0; i < list.size(); i++) {
      listAndEmptyElement[i] = label.apply(list.get(i));
    }
    listAndEmptyElement[listAndEmptyElement.length - 1] = "";
    JComboBox<String> autoCompleteBox = new JComboBox<String>(listAndEmptyElement);
    AutoCompleteDecorator.decorate(autoCompleteBox);
    return new ComboBoxCellEditor(autoCompleteBox);

  }
}
