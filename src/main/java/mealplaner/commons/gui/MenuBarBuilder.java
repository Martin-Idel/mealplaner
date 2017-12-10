package mealplaner.commons.gui;

import static mealplaner.commons.BundleStore.BUNDLES;

import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

public class MenuBarBuilder {
  private JFrame frame;
  private JMenu fileMenu;
  private JMenu helpMenu;

  public MenuBarBuilder(JFrame frame) {
    this.frame = frame;
  }

  public JMenuBar createMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    menuBar.add(fileMenu);
    menuBar.add(helpMenu);
    return menuBar;
  }

  public MenuBarBuilder setupFileMenu() {
    fileMenu = new JMenu(BUNDLES.message("menuData"));
    fileMenu.setMnemonic(
        KeyStroke.getKeyStroke(BUNDLES.message("menuDataMnemonic")).getKeyCode());
    fileMenu.getAccessibleContext()
        .setAccessibleDescription(BUNDLES.message("menuDataDescription"));
    return this;
  }

  public MenuBarBuilder setupHelpMenu() {
    helpMenu = new JMenu(BUNDLES.message("menuHelp"));
    helpMenu.setMnemonic(
        KeyStroke.getKeyStroke(BUNDLES.message("menuHelpMnemonic")).getKeyCode());
    helpMenu.getAccessibleContext()
        .setAccessibleDescription(BUNDLES.message("menuHelpDescription"));
    return this;
  }

  public MenuBarBuilder createIngredientsMenu(ActionListener listener) {
    fileMenu.add(new JMenuBuilder().addLabelText(BUNDLES.message("ingredientInsertMenu"))
        .addMnemonic(BUNDLES.message("ingredientInsertMenuMnemonic"))
        .addActionListener(listener)
        .build());
    return this;
  }

  public MenuBarBuilder createMealMenu(ActionListener listener) {
    fileMenu.add(new JMenuBuilder().addLabelText(BUNDLES.message("menuDataCreateMenu"))
        .addMnemonic(BUNDLES.message("menuDataCreateMenuMnemonic"))
        .addActionListener(listener)
        .build());
    return this;
  }

  public MenuBarBuilder viewProposalMenu(ActionListener listener) {
    fileMenu.add(new JMenuBuilder().addLabelText(BUNDLES.message("menuDataLastProposal"))
        .addMnemonic(BUNDLES.message("menuDataLastProposalMnemonic"))
        .addActionListener(listener)
        .build());
    return this;
  }

  public MenuBarBuilder createBackupMenu(ActionListener listener) {
    fileMenu.add(new JMenuBuilder().addLabelText(BUNDLES.message("menuDataCreateBackup"))
        .addMnemonic(BUNDLES.message("menuDataCreateBackupMnemonic"))
        .addActionListener(listener)
        .build());
    return this;
  }

  public MenuBarBuilder loadBackupMenu(ActionListener listener) {
    fileMenu.add(new JMenuBuilder().addLabelText(BUNDLES.message("menuDataLoadBackup"))
        .addMnemonic(BUNDLES.message("menuDataLoadBackupMnemonic"))
        .addActionListener(listener)
        .build());
    return this;
  }

  public MenuBarBuilder printDatabaseMenu(ActionListener listener) {
    fileMenu.add(new JMenuBuilder().addLabelText(BUNDLES.message("menuDataPrintDatabase"))
        .addMnemonic(BUNDLES.message("menuDataPrintDatabaseMnemonic"))
        .addActionListener(listener)
        .build());
    return this;
  }

  public MenuBarBuilder printProposalMenu(ActionListener listener) {
    fileMenu.add(new JMenuBuilder().addLabelText(BUNDLES.message("menuDataPrintProposal"))
        .addMnemonic(BUNDLES.message("menuDataPrintProposalMnemonic"))
        .addActionListener(listener)
        .build());
    return this;
  }

  public MenuBarBuilder exitMenu(ActionListener listener) {
    fileMenu.add(new JMenuBuilder().addLabelText(BUNDLES.message("menuDataExit"))
        .addMnemonic(BUNDLES.message("menuDataExitMnemonic"))
        .addActionListener(listener)
        .build());
    return this;
  }

  public MenuBarBuilder showDatabaseHelpMenu() {
    helpMenu.add(new JMenuBuilder().addLabelText(BUNDLES.message("menuHelpProposal"))
        .addMnemonic(BUNDLES.message("menuHelpProposalMnemonic"))
        .addActionListener(action -> JOptionPane.showMessageDialog(frame,
            BUNDLES.message("helpProposalText"),
            BUNDLES.message("helpProposalTitle"), JOptionPane.INFORMATION_MESSAGE))
        .build());
    return this;
  }

  public MenuBarBuilder showHelpMenu() {
    helpMenu.add(new JMenuBuilder().addLabelText(BUNDLES.message("menuHelpDatabase"))
        .addMnemonic(BUNDLES.message("menuHelpDatabaseMnemonic"))
        .addActionListener(action -> JOptionPane.showMessageDialog(frame,
            BUNDLES.message("helpDatabaseText"),
            BUNDLES.message("helpDatabaseTitle"), JOptionPane.INFORMATION_MESSAGE))
        .build());
    return this;
  }

  public MenuBarBuilder createSeparatorForMenu() {
    fileMenu.addSeparator();
    return this;
  }
}
