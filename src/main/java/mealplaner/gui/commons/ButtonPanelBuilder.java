package mealplaner.gui.commons;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import mealplaner.gui.ButtonPanelEnabling;

public class ButtonPanelBuilder {
	JPanel panel;
	ResourceBundle messages;
	List<JButton> buttonList = new ArrayList<>();
	List<JButton> enablingList = new ArrayList<>();

	public ButtonPanelBuilder(ResourceBundle messages) {
		this.messages = messages;
		panel = new JPanel();
	}

	public ButtonPanelBuilder addPanel(JPanel buttonPanel) {
		this.panel = buttonPanel;
		return this;
	}

	public ButtonPanelBuilder addExitButton(ActionListener listener) {
		buttonList.add(createButton("exitButton", "exitButtonMnemonic", listener));
		return this;
	}

	public ButtonPanelBuilder addOkButton(ActionListener listener) {
		buttonList.add(createButton("okButton", "okButtonMnemonic", listener));
		return this;
	}

	public ButtonPanelBuilder addSaveButton(ActionListener listener) {
		buttonList.add(createButton("saveButton", "saveButtonMnemonic", listener));
		return this;
	}

	public ButtonPanelBuilder addCancelDialogButton(JDialog dialog) {
		buttonList.add(createButton("cancelButton", "cancelButtonMnemonic", justDisposeListener(dialog)));
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

	public JPanel build() {
		for (JButton button : buttonList) {
			panel.add(button);
		}
		return panel;
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
		JButton button = new JButton(messages.getString(label));
		button.setMnemonic(KeyStroke.getKeyStroke(messages.getString(mnemonic)).getKeyCode());
		button.addActionListener(listener);
		return button;
	}
}