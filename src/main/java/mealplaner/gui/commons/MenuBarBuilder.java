package mealplaner.gui.commons;

import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

public class MenuBarBuilder {
	private JFrame frame;
	private ResourceBundle messages;
	private JMenu fileMenu;
	private JMenu helpMenu;

	public MenuBarBuilder(JFrame frame, ResourceBundle messages) {
		this.frame = frame;
		this.messages = messages;
	}

	public JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		return menuBar;
	}

	public MenuBarBuilder setupFileMenu() {
		fileMenu = new JMenu(messages.getString("menuData"));
		fileMenu.setMnemonic(KeyStroke.getKeyStroke(messages.getString("menuDataMnemonic")).getKeyCode());
		fileMenu.getAccessibleContext().setAccessibleDescription(messages.getString("menuDataDescription"));
		return this;
	}

	public MenuBarBuilder setupHelpMenu() {
		helpMenu = new JMenu(messages.getString("menuHelp"));
		helpMenu.setMnemonic(KeyStroke.getKeyStroke(messages.getString("menuHelpMnemonic")).getKeyCode());
		helpMenu.getAccessibleContext().setAccessibleDescription(messages.getString("menuHelpDescription"));
		return this;
	}

	public MenuBarBuilder createMealMenu(ActionListener listener) {
		fileMenu.add(new JMenuBuilder().addLabelText(messages.getString("menuDataItem1"))
				.addMnemonic(messages.getString("menuDataItem1Mnemonic"))
				.addActionListener(listener)
				.build());
		return this;
	}

	public MenuBarBuilder viewProposalMenu(ActionListener listener) {
		fileMenu.add(new JMenuBuilder().addLabelText(messages.getString("menuDataItem2"))
				.addMnemonic(messages.getString("menuDataItem2Mnemonic"))
				.addActionListener(listener)
				.build());
		return this;
	}

	public MenuBarBuilder createBackupMenu(ActionListener listener) {
		fileMenu.add(new JMenuBuilder().addLabelText(messages.getString("menuDataItem3"))
				.addMnemonic(messages.getString("menuDataItem3Mnemonic"))
				.addActionListener(listener)
				.build());
		return this;
	}

	public MenuBarBuilder loadBackupMenu(ActionListener listener) {
		fileMenu.add(new JMenuBuilder().addLabelText(messages.getString("menuDataItem4"))
				.addMnemonic(messages.getString("menuDataItem4Mnemonic"))
				.addActionListener(listener)
				.build());
		return this;
	}

	public MenuBarBuilder printDatabaseMenu(ActionListener listener) {
		fileMenu.add(new JMenuBuilder().addLabelText(messages.getString("menuDataItem5"))
				.addMnemonic(messages.getString("menuDataItem5Mnemonic"))
				.addActionListener(listener)
				.build());
		return this;
	}

	public MenuBarBuilder printProposalMenu(ActionListener listener) {
		fileMenu.add(new JMenuBuilder().addLabelText(messages.getString("menuDataItem6"))
				.addMnemonic(messages.getString("menuDataItem6Mnemonic"))
				.addActionListener(listener)
				.build());
		return this;
	}

	public MenuBarBuilder exitMenu(ActionListener listener) {
		fileMenu.add(new JMenuBuilder().addLabelText(messages.getString("menuDataItem7"))
				.addMnemonic(messages.getString("menuDataItem7Mnemonic"))
				.addActionListener(listener)
				.build());
		return this;
	}

	public MenuBarBuilder showDatabaseHelpMenu() {
		helpMenu.add(new JMenuBuilder().addLabelText(messages.getString("menuHelpItem1"))
				.addMnemonic(messages.getString("menuHelpItem1Mnemonic"))
				.addActionListener(action -> JOptionPane.showMessageDialog(frame, messages.getString("help1text"),
						messages.getString("help1title"), JOptionPane.INFORMATION_MESSAGE))
				.build());
		return this;
	}

	public MenuBarBuilder showHelpMenu() {
		helpMenu.add(new JMenuBuilder().addLabelText(messages.getString("menuHelpItem2"))
				.addMnemonic(messages.getString("menuHelpItem2Mnemonic"))
				.addActionListener(action -> JOptionPane.showMessageDialog(frame, messages.getString("help2text"),
						messages.getString("help2title"), JOptionPane.INFORMATION_MESSAGE))
				.build());
		return this;
	}

	public MenuBarBuilder createSeparatorForMenu() {
		fileMenu.addSeparator();
		return this;
	}
}
