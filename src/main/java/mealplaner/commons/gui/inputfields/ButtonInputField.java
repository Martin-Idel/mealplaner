package mealplaner.commons.gui.inputfields;

import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ButtonInputField<T> implements InputField<T> {
  private JButton button;
  private final String label;
  private final String buttonLabel;
  private final String buttonLabelForDefaultContent;
  private final T defaultContent;
  private final Function<T, T> changeContent;
  private T content;

  public ButtonInputField(String label, String buttonLabel, String buttonLabelForDefaultContent,
      T defaultContent, Function<T, T> changeContent) {
    this.label = label;
    this.buttonLabel = buttonLabel;
    this.buttonLabelForDefaultContent = buttonLabelForDefaultContent;
    this.content = defaultContent;
    this.defaultContent = defaultContent;
    this.changeContent = changeContent;
  }

  @Override
  public void addToPanel(JPanel panel) {
    button = new JButton(
        content.equals(defaultContent) ? buttonLabelForDefaultContent : buttonLabel);
    button.addActionListener(action -> {
      content = changeContent.apply(content);
      button.setText(content.equals(defaultContent)
          ? buttonLabelForDefaultContent
          : buttonLabel);
    });
    panel.add(new JLabel(label));
    panel.add(button);
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
}
