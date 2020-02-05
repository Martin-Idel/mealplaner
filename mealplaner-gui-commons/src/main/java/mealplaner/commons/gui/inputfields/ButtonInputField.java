// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.inputfields;

import java.util.function.UnaryOperator;
import javax.swing.JButton;
import javax.swing.JLabel;

import mealplaner.commons.gui.GuiPanel;

public class ButtonInputField<T> implements InputField<T> {
  private JButton button;
  private final String label;
  private final String name;
  private final String buttonLabel;
  private final String buttonLabelForDefaultContent;
  private final T defaultContent;
  private final UnaryOperator<T> changeContent;
  private T content;
  private final int orderNumber;


  public ButtonInputField(String label, String name, String buttonLabel,
      String buttonLabelForDefaultContent,
      T defaultContent, UnaryOperator<T> changeContent, int orderNumber) {
    this.label = label;
    this.name = name;
    this.buttonLabel = buttonLabel;
    this.buttonLabelForDefaultContent = buttonLabelForDefaultContent;
    this.content = defaultContent;
    this.defaultContent = defaultContent;
    this.changeContent = changeContent;
    this.orderNumber = orderNumber;
  }

  @Override
  public void addToPanel(GuiPanel component) {
    button = new JButton(
        content.equals(defaultContent) ? buttonLabelForDefaultContent : buttonLabel);
    button.setName("InputFieldButton" + name);
    button.addActionListener(action -> {
      content = changeContent.apply(content);
      button.setText(content.equals(defaultContent)
          ? buttonLabelForDefaultContent
          : buttonLabel);
    });
    component.getComponent().add(new JLabel(label));
    component.getComponent().add(button);
  }

  @Override
  public T getUserInput() {
    return content;
  }

  @Override
  public void resetField() {
    content = defaultContent;
    button.setText(buttonLabelForDefaultContent);
  }

  @Override
  public int getOrdering() {
    return orderNumber;
  }
}
