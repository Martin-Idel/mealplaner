// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.inputfields;

import static java.lang.Integer.parseInt;
import static mealplaner.commons.NonnegativeInteger.nonNegative;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.gui.GuiPanel;
import mealplaner.commons.gui.editing.NonnegativeIntegerTextFilter;

public class NonnegativeIntegerInputField implements InputField<NonnegativeInteger> {
  private final String label;
  private final String name;
  private JTextField nonnegativeIntegerTextField;
  private final NonnegativeInteger defaultValue;
  private final int orderNumber;

  public NonnegativeIntegerInputField(String label, String name, NonnegativeInteger defaultValue, int orderNumber) {
    this.label = label;
    this.name = name;
    this.defaultValue = defaultValue;
    this.orderNumber = orderNumber;
  }

  @Override
  public void addToPanel(GuiPanel panel) {
    nonnegativeIntegerTextField = new JTextField(defaultValue.toString());
    nonnegativeIntegerTextField.setName("InputFieldNonnegativeInteger" + name);
    PlainDocument doc = (PlainDocument) nonnegativeIntegerTextField.getDocument();
    doc.setDocumentFilter(new NonnegativeIntegerTextFilter());
    nonnegativeIntegerTextField.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent ev) {
        selectAll();
      }

      @Override
      public void focusLost(FocusEvent ev) {
        // Do nothing.
      }
    });
    panel.getComponent().add(new JLabel(label));
    panel.getComponent().add(nonnegativeIntegerTextField);
  }

  @Override
  public NonnegativeInteger getUserInput() {
    String input = nonnegativeIntegerTextField.getText();
    return nonNegative("".equals(input) ? 0 : parseInt(input));
  }

  @Override
  public void resetField() {
    nonnegativeIntegerTextField.setText(defaultValue.toString());
  }

  void selectAll() {
    nonnegativeIntegerTextField.selectAll();
  }

  @Override
  public int getOrdering() {
    return orderNumber;
  }
}
