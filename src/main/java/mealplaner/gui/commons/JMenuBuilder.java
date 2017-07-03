package mealplaner.gui.commons;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class JMenuBuilder {
	String labelText;
	String mnemonic;
	ActionListener actionListener;

	public JMenuBuilder addLabelText(String labelText) {
		this.labelText = labelText;
		return this;
	}

	public JMenuBuilder addMnemonic(String mnemonic) {
		this.mnemonic = mnemonic;
		return this;
	}

	public JMenuBuilder addActionListener(ActionListener listener) {
		this.actionListener = listener;
		return this;
	}

	public JMenuItem build() {
		JMenuItem menuItem = new JMenuItem(labelText);
		menuItem.setMnemonic(KeyStroke.getKeyStroke(mnemonic).getKeyCode());
		menuItem.addActionListener(actionListener);
		return menuItem;
	}
}
