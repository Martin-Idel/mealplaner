package mealplaner.gui;

import static mealplaner.commons.BundleStore.BUNDLES;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import mealplaner.commons.gui.JMenuBuilder;

public final class HelpMenu {

  private HelpMenu() {
  }

  public static JMenu createHelpMenu(JFrame frame) {
    JMenu helpMenu = setupHelpMenu();
    addDatabaseHelpMenu(helpMenu, frame);
    addHelpMenu(helpMenu, frame);
    return helpMenu;
  }

  private static JMenu setupHelpMenu() {
    JMenu helpMenu = new JMenu(BUNDLES.message("menuHelp"));
    helpMenu.setMnemonic(
        KeyStroke.getKeyStroke(BUNDLES.message("menuHelpMnemonic")).getKeyCode());
    helpMenu.getAccessibleContext()
        .setAccessibleDescription(BUNDLES.message("menuHelpDescription"));
    return helpMenu;
  }

  private static void addDatabaseHelpMenu(JMenu helpMenu, JFrame frame) {
    helpMenu.add(new JMenuBuilder().addLabelText(BUNDLES.message("menuHelpProposal"))
        .addMnemonic(BUNDLES.message("menuHelpProposalMnemonic"))
        .addActionListener(action -> JOptionPane.showMessageDialog(frame,
            BUNDLES.message("helpProposalText"),
            BUNDLES.message("helpProposalTitle"), JOptionPane.INFORMATION_MESSAGE))
        .build());
  }

  private static void addHelpMenu(JMenu helpMenu, JFrame frame) {
    helpMenu.add(new JMenuBuilder().addLabelText(BUNDLES.message("menuHelpDatabase"))
        .addMnemonic(BUNDLES.message("menuHelpDatabaseMnemonic"))
        .addActionListener(action -> JOptionPane.showMessageDialog(frame,
            BUNDLES.message("helpDatabaseText"),
            BUNDLES.message("helpDatabaseTitle"), JOptionPane.INFORMATION_MESSAGE))
        .build());
  }
}
