package mealplaner.commons.gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonPanelEnabling extends Component {
  private static final long serialVersionUID = 1L;
  private JPanel buttonPanel = new JPanel();
  private List<JButton> buttonList = new ArrayList<>();

  public JPanel getPanel() {
    return buttonPanel;
  }

  public void addButton(JButton button) {
    buttonPanel.add(button);
  }

  public void addButtonToBeEnabled(JButton button) {
    buttonList.add(button);
  }

  public void enableButtons() {
    buttonList.forEach(button -> button.setEnabled(true));
  }

  public void disableButtons() {
    buttonList.forEach(button -> button.setEnabled(false));
  }
}
