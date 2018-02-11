package mealplaner.commons.gui.buttonpanel;

import static mealplaner.commons.BundleStore.BUNDLES;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public final class ButtonPanelBuilder {
  private final JPanel panel;
  private final List<JButton> buttonList = new ArrayList<>();
  private final List<JButton> enablingList = new ArrayList<>();
  private final String name;

  private ButtonPanelBuilder(String name) {
    panel = new JPanel();
    this.name = name;
  }

  public static ButtonPanelBuilder builder(String name) {
    return new ButtonPanelBuilder(name);
  }

  public ButtonPanelBuilder addExitButton(ActionListener listener) {
    buttonList.add(createButton(BUNDLES.message("exitButton"),
        BUNDLES.message("exitButtonMnemonic"),
        listener));
    return this;
  }

  public ButtonPanelBuilder addOkButton(ActionListener listener) {
    buttonList.add(createButton(BUNDLES.message("okButton"),
        BUNDLES.message("okButtonMnemonic"),
        listener));
    return this;
  }

  public ButtonPanelBuilder addSaveButton(ActionListener listener) {
    buttonList.add(createButton(BUNDLES.message("saveButton"),
        BUNDLES.message("saveButtonMnemonic"),
        listener));
    return this;
  }

  public ButtonPanelBuilder addCancelDialogButton(JDialog dialog) {
    buttonList.add(createButton(BUNDLES.message("cancelButton"),
        BUNDLES.message("cancelButtonMnemonic"),
        justDisposeListener(dialog)));
    return this;
  }

  public ButtonPanelBuilder makeLastButtonEnabling() {
    enablingList.add(buttonList.get(buttonList.size() - 1));
    return this;
  }

  public ButtonPanelBuilder addButton(String label, String mnemonic, ActionListener listener) {
    buttonList.add(createButton(label, mnemonic, listener));
    return this;
  }

  public ButtonPanel build() {
    for (JButton button : buttonList) {
      panel.add(button);
    }
    return new ButtonPanel(panel);
  }

  public ButtonPanelEnabling buildEnablingPanel() {
    ButtonPanelEnabling enablingPanel = new ButtonPanelEnabling();
    for (JButton button : buttonList) {
      enablingPanel.addButton(button);
    }
    for (JButton button : enablingList) {
      enablingPanel.addButtonToBeEnabled(button);
    }
    return enablingPanel;

  }

  public static ActionListener justDisposeListener(JDialog dialog) {
    return action -> {
      dialog.setVisible(false);
      dialog.dispose();
    };
  }

  private JButton createButton(String label, String mnemonic, ActionListener listener) {
    JButton button = new JButton(label);
    button.setName("ButtonPanel" + name + buttonList.size());
    button.setMnemonic(KeyStroke.getKeyStroke(mnemonic).getKeyCode());
    button.addActionListener(listener);
    return button;
  }
}
