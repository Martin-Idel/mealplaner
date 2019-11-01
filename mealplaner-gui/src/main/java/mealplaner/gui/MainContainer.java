// SPDX-License-Identifier: MIT

package mealplaner.gui;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import static mealplaner.commons.BundleStore.BUNDLES;

import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

public class MainContainer {
  private final JFrame frame;
  private final JTabbedPane tabPane;
  private final JMenuBar menuBar;
  private JMenu fileMenu;
  private JMenu helpMenu;

  public MainContainer(JFrame mainFrame) {
    this.frame = mainFrame;
    this.tabPane = new JTabbedPane();
    this.menuBar = new JMenuBar();
    setupFileMenu();
    setupHelpMenu();
  }

  public void addTabbedPane(String name, JPanel pane) {
    tabPane.add(name, pane);
  }

  public void addMenu(JMenu menu) {
    menuBar.add(menu);
  }

  public void addToFileMenu(JMenuItem menuItem) {
    fileMenu.add(menuItem);
  }

  public void addSeparatorToFileMenu() {
    fileMenu.addSeparator();
  }

  public void addToHelpMenu(JMenuItem menuItem) {
    helpMenu.add(menuItem);
  }

  public void addSeparatorToHelpMenu() {
    helpMenu.addSeparator();
  }

  public void setupMainFrame(WindowListener saveExitAction) {
    frame.setTitle(BUNDLES.message("mainFrameTitle"));
    frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    frame.addWindowListener(saveExitAction);
    frame.setJMenuBar(menuBar);
    frame.add(tabPane, CENTER);
    frame.setSize(550, 350);
    frame.setVisible(true);
  }

  private void setupFileMenu() {
    fileMenu = new JMenu(BUNDLES.message("menuData"));
    fileMenu.setName("MenuFile");
    fileMenu.setMnemonic(
        KeyStroke.getKeyStroke(BUNDLES.message("menuDataMnemonic")).getKeyCode());
    fileMenu.getAccessibleContext()
        .setAccessibleDescription(BUNDLES.message("menuDataDescription"));
    menuBar.add(fileMenu);
  }

  private void setupHelpMenu() {
    helpMenu = new JMenu(BUNDLES.message("menuHelp"));
    helpMenu.setName("MenuHelp");
    helpMenu.setMnemonic(
        KeyStroke.getKeyStroke(BUNDLES.message("menuHelpMnemonic")).getKeyCode());
    helpMenu.getAccessibleContext()
        .setAccessibleDescription(BUNDLES.message("menuHelpDescription"));
    menuBar.add(helpMenu);
  }
}
