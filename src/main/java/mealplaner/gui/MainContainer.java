package mealplaner.gui;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import static mealplaner.commons.BundleStore.BUNDLES;

import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class MainContainer {
  private final JFrame frame;
  private JTabbedPane tabPane;
  private JMenuBar menuBar;

  public MainContainer(JFrame mainFrame) {
    this.frame = mainFrame;
  }

  public void addTabbedPane(String name, JPanel pane) {
    tabPane.add(name, pane);
  }

  public void addMenu(JMenu menu) {
    menuBar.add(menu);
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
}
