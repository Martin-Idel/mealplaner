package mealplaner.gui.commons;

import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import mealplaner.BundleStore;

// TODO: rewrite to avoid duplicate code
public class MenuBarBuilder {
	private JFrame frame;
	private BundleStore bundles;
	private JMenu fileMenu;
	private JMenu helpMenu;

	public MenuBarBuilder(JFrame frame, BundleStore bundles) {
		this.frame = frame;
		this.bundles = bundles;
	}

	public JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		return menuBar;
	}

	public MenuBarBuilder setupFileMenu() {
		fileMenu = new JMenu(bundles.message("menuData"));
		fileMenu.setMnemonic(
				KeyStroke.getKeyStroke(bundles.message("menuDataMnemonic")).getKeyCode());
		fileMenu.getAccessibleContext()
				.setAccessibleDescription(bundles.message("menuDataDescription"));
		return this;
	}

	public MenuBarBuilder setupHelpMenu() {
		helpMenu = new JMenu(bundles.message("menuHelp"));
		helpMenu.setMnemonic(
				KeyStroke.getKeyStroke(bundles.message("menuHelpMnemonic")).getKeyCode());
		helpMenu.getAccessibleContext()
				.setAccessibleDescription(bundles.message("menuHelpDescription"));
		return this;
	}

	public MenuBarBuilder createIngredientsMenu(ActionListener listener) {
		fileMenu.add(new JMenuBuilder().addLabelText(bundles.message("ingredientInsertMenu"))
				.addMnemonic(bundles.message("ingredientInsertMenuMnemonic"))
				.addActionListener(listener)
				.build());
		return this;
	}

	public MenuBarBuilder createMealMenu(ActionListener listener) {
		fileMenu.add(new JMenuBuilder().addLabelText(bundles.message("menuDataCreateMenu"))
				.addMnemonic(bundles.message("menuDataCreateMenuMnemonic"))
				.addActionListener(listener)
				.build());
		return this;
	}

	public MenuBarBuilder viewProposalMenu(ActionListener listener) {
		fileMenu.add(new JMenuBuilder().addLabelText(bundles.message("menuDataLastProposal"))
				.addMnemonic(bundles.message("menuDataLastProposalMnemonic"))
				.addActionListener(listener)
				.build());
		return this;
	}

	public MenuBarBuilder createBackupMenu(ActionListener listener) {
		fileMenu.add(new JMenuBuilder().addLabelText(bundles.message("menuDataCreateBackup"))
				.addMnemonic(bundles.message("menuDataCreateBackupMnemonic"))
				.addActionListener(listener)
				.build());
		return this;
	}

	public MenuBarBuilder loadBackupMenu(ActionListener listener) {
		fileMenu.add(new JMenuBuilder().addLabelText(bundles.message("menuDataLoadBackup"))
				.addMnemonic(bundles.message("menuDataLoadBackupMnemonic"))
				.addActionListener(listener)
				.build());
		return this;
	}

	public MenuBarBuilder printDatabaseMenu(ActionListener listener) {
		fileMenu.add(new JMenuBuilder().addLabelText(bundles.message("menuDataPrintDatabase"))
				.addMnemonic(bundles.message("menuDataPrintDatabaseMnemonic"))
				.addActionListener(listener)
				.build());
		return this;
	}

	public MenuBarBuilder printProposalMenu(ActionListener listener) {
		fileMenu.add(new JMenuBuilder().addLabelText(bundles.message("menuDataPrintProposal"))
				.addMnemonic(bundles.message("menuDataPrintProposalMnemonic"))
				.addActionListener(listener)
				.build());
		return this;
	}

	public MenuBarBuilder exitMenu(ActionListener listener) {
		fileMenu.add(new JMenuBuilder().addLabelText(bundles.message("menuDataExit"))
				.addMnemonic(bundles.message("menuDataExitMnemonic"))
				.addActionListener(listener)
				.build());
		return this;
	}

	public MenuBarBuilder showDatabaseHelpMenu() {
		helpMenu.add(new JMenuBuilder().addLabelText(bundles.message("menuHelpProposal"))
				.addMnemonic(bundles.message("menuHelpProposalMnemonic"))
				.addActionListener(action -> JOptionPane.showMessageDialog(frame,
						bundles.message("helpProposalText"),
						bundles.message("helpProposalTitle"), JOptionPane.INFORMATION_MESSAGE))
				.build());
		return this;
	}

	public MenuBarBuilder showHelpMenu() {
		helpMenu.add(new JMenuBuilder().addLabelText(bundles.message("menuHelpDatabase"))
				.addMnemonic(bundles.message("menuHelpDatabaseMnemonic"))
				.addActionListener(action -> JOptionPane.showMessageDialog(frame,
						bundles.message("helpDatabaseText"),
						bundles.message("helpDatabaseTitle"), JOptionPane.INFORMATION_MESSAGE))
				.build());
		return this;
	}

	public MenuBarBuilder createSeparatorForMenu() {
		fileMenu.addSeparator();
		return this;
	}
}
