package mealplaner.gui.commons;

import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

// TODO: rewrite to avoid duplicate code
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
		fileMenu.setMnemonic(
				KeyStroke.getKeyStroke(messages.getString("menuDataMnemonic")).getKeyCode());
		fileMenu.getAccessibleContext()
				.setAccessibleDescription(messages.getString("menuDataDescription"));
		return this;
	}

	public MenuBarBuilder setupHelpMenu() {
		helpMenu = new JMenu(messages.getString("menuHelp"));
		helpMenu.setMnemonic(
				KeyStroke.getKeyStroke(messages.getString("menuHelpMnemonic")).getKeyCode());
		helpMenu.getAccessibleContext()
				.setAccessibleDescription(messages.getString("menuHelpDescription"));
		return this;
	}

	public MenuBarBuilder createMealMenu(ActionListener listener) {
		fileMenu.add(new JMenuBuilder().addLabelText(messages.getString("menuDataCreateMenu"))
				.addMnemonic(messages.getString("menuDataCreateMenuMnemonic"))
				.addActionListener(listener)
				.build());
		return this;
	}

	public MenuBarBuilder viewProposalMenu(ActionListener listener) {
		fileMenu.add(new JMenuBuilder().addLabelText(messages.getString("menuDataLastProposal"))
				.addMnemonic(messages.getString("menuDataLastProposalMnemonic"))
				.addActionListener(listener)
				.build());
		return this;
	}

	public MenuBarBuilder createBackupMenu(ActionListener listener) {
		fileMenu.add(new JMenuBuilder().addLabelText(messages.getString("menuDataCreateBackup"))
				.addMnemonic(messages.getString("menuDataCreateBackupMnemonic"))
				.addActionListener(listener)
				.build());
		return this;
	}

	public MenuBarBuilder loadBackupMenu(ActionListener listener) {
		fileMenu.add(new JMenuBuilder().addLabelText(messages.getString("menuDataLoadBackup"))
				.addMnemonic(messages.getString("menuDataLoadBackupMnemonic"))
				.addActionListener(listener)
				.build());
		return this;
	}

	public MenuBarBuilder printDatabaseMenu(ActionListener listener) {
		fileMenu.add(new JMenuBuilder().addLabelText(messages.getString("menuDataPrintDatabase"))
				.addMnemonic(messages.getString("menuDataPrintDatabaseMnemonic"))
				.addActionListener(listener)
				.build());
		return this;
	}

	public MenuBarBuilder printProposalMenu(ActionListener listener) {
		fileMenu.add(new JMenuBuilder().addLabelText(messages.getString("menuDataPrintProposal"))
				.addMnemonic(messages.getString("menuDataPrintProposalMnemonic"))
				.addActionListener(listener)
				.build());
		return this;
	}

	public MenuBarBuilder exitMenu(ActionListener listener) {
		fileMenu.add(new JMenuBuilder().addLabelText(messages.getString("menuDataExit"))
				.addMnemonic(messages.getString("menuDataExitMnemonic"))
				.addActionListener(listener)
				.build());
		return this;
	}

	public MenuBarBuilder showDatabaseHelpMenu() {
		helpMenu.add(new JMenuBuilder().addLabelText(messages.getString("menuHelpProposal"))
				.addMnemonic(messages.getString("menuHelpProposalMnemonic"))
				.addActionListener(action -> JOptionPane.showMessageDialog(frame,
						messages.getString("helpProposalText"),
						messages.getString("helpProposalTitle"), JOptionPane.INFORMATION_MESSAGE))
				.build());
		return this;
	}

	public MenuBarBuilder showHelpMenu() {
		helpMenu.add(new JMenuBuilder().addLabelText(messages.getString("menuHelpDatabase"))
				.addMnemonic(messages.getString("menuHelpDatabaseMnemonic"))
				.addActionListener(action -> JOptionPane.showMessageDialog(frame,
						messages.getString("helpDatabaseText"),
						messages.getString("helpDatabaseTitle"), JOptionPane.INFORMATION_MESSAGE))
				.build());
		return this;
	}

	public MenuBarBuilder createSeparatorForMenu() {
		fileMenu.addSeparator();
		return this;
	}
}
