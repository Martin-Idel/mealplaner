// SPDX-License-Identifier: MIT

package mealplaner.commons.gui;

import static javax.swing.KeyStroke.getKeyStroke;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

public final class JMenuBuilder {
  private final String name;
  String labelText;
  String mnemonic;
  ActionListener actionListener;

  private JMenuBuilder(String name) {
    this.name = name;
  }

  public static JMenuBuilder builder(String name) {
    return new JMenuBuilder(name);
  }

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
    menuItem.setName("MenuItem" + name);
    menuItem.setMnemonic(getKeyStroke(mnemonic).getKeyCode());
    menuItem.addActionListener(actionListener);
    return menuItem;
  }
}
