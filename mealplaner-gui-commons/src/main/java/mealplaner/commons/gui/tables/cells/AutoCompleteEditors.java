// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.tables.cells;

import java.awt.Color;
import java.awt.Component;
import java.util.List;
import java.util.Vector;
import java.util.function.Function;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

public final class AutoCompleteEditors {
  private static final long serialVersionUID = 1L;

  private AutoCompleteEditors() {
  }

  // TODO: Replace this method by the one below in tables
  public static <T> ComboBoxCellEditor autoCompleteCellEditor(List<T> list, Function<T, String> label) {
    String[] listAndEmptyElement = new String[list.size() + 1];
    for (int i = 0; i < list.size(); i++) {
      listAndEmptyElement[i] = label.apply(list.get(i));
    }
    listAndEmptyElement[listAndEmptyElement.length - 1] = "";
    JComboBox<String> autoCompleteBox = new JComboBox<>(listAndEmptyElement);
    AutoCompleteDecorator.decorate(autoCompleteBox);
    return new ComboBoxCellEditor(autoCompleteBox);
  }

  public static <T> ComboBoxCellEditor autoCompleteCellEditor(
      List<T> list, T emptyObject, Function<Object, String> toRepresentation) {
    JComboBox<T> autoCompleteBox = new JComboBox<>(new Vector<>(list));
    autoCompleteBox.addItem(emptyObject);
    autoCompleteBox.setRenderer(new ObjectRenderer(toRepresentation, emptyObject));
    AutoCompleteDecorator.decorate(autoCompleteBox, new ObjectToStringConverter() {
      @Override
      public String getPreferredStringForItem(Object item) {
        return toRepresentation.apply(item);
      }
    });
    return new ComboBoxCellEditor(autoCompleteBox);
  }

  public static class ObjectRenderer extends BasicComboBoxRenderer {
    private static final long serialVersionUID = 1L;
    private final transient Function<Object, String> getStringRepresentation;
    private final Object emptyObject;

    public ObjectRenderer(Function<Object, String> getStringRepresentation, Object emptyObject) {
      this.getStringRepresentation = getStringRepresentation;
      this.emptyObject = emptyObject;
    }

    @Override
    public Component getListCellRendererComponent(
        JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

      setText(getStringRepresentation.apply(value));
      if (emptyObject.equals(value)) {
        setForeground(Color.GRAY);
      }

      return this;
    }
  }
}
