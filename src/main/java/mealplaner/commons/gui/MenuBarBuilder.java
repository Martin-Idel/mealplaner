package mealplaner.commons.gui;

import static mealplaner.commons.BundleStore.BUNDLES;

import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.KeyStroke;

public class MenuBarBuilder {
  private JMenu fileMenu;

  public MenuBarBuilder(JFrame frame) {
  }

  public JMenu createMenuBar() {
    return fileMenu;
  }

  public MenuBarBuilder setupFileMenu() {
    fileMenu = new JMenu(BUNDLES.message("menuData"));
    fileMenu.setMnemonic(
        KeyStroke.getKeyStroke(BUNDLES.message("menuDataMnemonic")).getKeyCode());
    fileMenu.getAccessibleContext()
        .setAccessibleDescription(BUNDLES.message("menuDataDescription"));
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

  public MenuBarBuilder createSeparatorForMenu() {
    fileMenu.addSeparator();
    return this;
  }
}
