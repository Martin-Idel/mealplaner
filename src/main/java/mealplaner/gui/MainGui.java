package mealplaner.gui;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.MessageDialog.showSaveExitDialog;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import mealplaner.MealplanerData;
import mealplaner.commons.gui.JMenuBuilder;
import mealplaner.gui.factories.DialogFactory;
import mealplaner.gui.tabbedpanes.databaseedit.DatabaseEditPanel;
import mealplaner.gui.tabbedpanes.proposal.ProposalSummaryPanel;
import mealplaner.io.FileIoGui;
import mealplaner.recipes.provider.IngredientProvider;

public class MainGui {
  private final JFrame frame;
  private final MainContainer container;

  private MealplanerData mealPlan;

  private final FileIoGui fileIoGui;

  public MainGui(JFrame mainFrame, MealplanerData mealPlan, DialogFactory dialogFactory,
      IngredientProvider ingredientProvider) {
    this.frame = mainFrame;
    this.mealPlan = mealPlan;

    fileIoGui = new FileIoGui(frame);
    this.mealPlan = fileIoGui.loadDatabase();
    this.container = new MainContainer(frame);

    ProposalSummaryPanel mealPanel = new ProposalSummaryPanel(mealPlan, dialogFactory, mainFrame,
        ingredientProvider);
    mealPanel.setupPanel(
        action -> {
          fileIoGui.saveDatabase(mealPlan);
          frame.dispose();
        },
        () -> fileIoGui.saveDatabase(mealPlan));
    mealPanel.addElements(container);

    DatabaseEditPanel dbaseEditPanel = new DatabaseEditPanel(this.mealPlan, frame,
        ingredientProvider);
    dbaseEditPanel.addElements(container);

    addToFileMenu();
    container.addToHelpMenu(overallHelpMenu());

    container.setupMainFrame(new SaveExitWindowListener());
  }

  private void addToFileMenu() {
    container.addToFileMenu(createBackupMenu());
    container.addToFileMenu(loadBackupMenu());
    container.addSeparatorToFileMenu();
    container.addToFileMenu(exitMenu());
  }

  private JMenuItem overallHelpMenu() {
    return new JMenuBuilder().addLabelText(BUNDLES.message("menuHelpDatabase"))
        .addMnemonic(BUNDLES.message("menuHelpDatabaseMnemonic"))
        .addActionListener(action -> JOptionPane.showMessageDialog(frame,
            BUNDLES.message("helpDatabaseText"),
            BUNDLES.message("helpDatabaseTitle"), JOptionPane.INFORMATION_MESSAGE))
        .build();
  }

  private JMenuItem createBackupMenu() {
    return new JMenuBuilder().addLabelText(BUNDLES.message("menuDataCreateBackup"))
        .addMnemonic(BUNDLES.message("menuDataCreateBackupMnemonic"))
        .addActionListener(action -> fileIoGui.createBackup(mealPlan))
        .build();
  }

  // TODO: This can't work just yet. Need to reload application
  private JMenuItem loadBackupMenu() {
    return new JMenuBuilder().addLabelText(BUNDLES.message("menuDataLoadBackup"))
        .addMnemonic(BUNDLES.message("menuDataLoadBackupMnemonic"))
        .addActionListener(action -> {
          fileIoGui.loadBackup()
              .ifPresent(loadedMealPlaner -> this.mealPlan = loadedMealPlaner);
        })
        .build();
  }

  private JMenuItem exitMenu() {
    return new JMenuBuilder()
        .addLabelText(BUNDLES.message("menuDataExit"))
        .addMnemonic(BUNDLES.message("menuDataExitMnemonic"))
        .addActionListener(action -> showSaveExitDialog(frame, BUNDLES.message("saveYesNoQuestion"),
            () -> fileIoGui.saveDatabase(mealPlan)))
        .build();
  }

  JFrame getFrame() {
    return frame;
  }

  void saveDataBase() {
    fileIoGui.saveDatabase(mealPlan);
  }

  public class SaveExitWindowListener extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      showSaveExitDialog(getFrame(), BUNDLES.message("saveYesNoQuestion"),
          () -> saveDataBase());
    }
  }
}