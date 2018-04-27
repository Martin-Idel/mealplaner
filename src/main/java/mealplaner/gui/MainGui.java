// SPDX-License-Identifier: MIT

package mealplaner.gui;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.JMenuBuilder.builder;
import static mealplaner.commons.gui.MessageDialog.showSaveExitDialog;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;

import mealplaner.gui.factories.DialogFactory;
import mealplaner.gui.tabbedpanes.databaseedit.DatabaseEditPanel;
import mealplaner.gui.tabbedpanes.ingredientsedit.IngredientsEditPanel;
import mealplaner.gui.tabbedpanes.proposal.ProposalSummaryPanel;
import mealplaner.io.FileIoGui;
import mealplaner.model.MealplanerData;

public class MainGui {
  private final JFrame frame;
  private final MainContainer container;

  private final MealplanerData mealPlan;

  private final FileIoGui fileIoGui;
  private final DatabaseEditPanel dbaseEditPanel;
  private final ProposalSummaryPanel mealPanel;
  private final IngredientsEditPanel ingredientsPanel;

  public MainGui(JFrame mainFrame, MealplanerData mealPlan, DialogFactory dialogFactory,
      FileIoGui fileIoGui) {
    this.frame = mainFrame;
    this.mealPlan = mealPlan;
    this.fileIoGui = fileIoGui;

    this.container = new MainContainer(frame);
    mealPanel = new ProposalSummaryPanel(this.mealPlan, dialogFactory,
        mainFrame, fileIoGui);
    dbaseEditPanel = new DatabaseEditPanel(this.mealPlan, frame, fileIoGui);
    ingredientsPanel = new IngredientsEditPanel(this.mealPlan, frame, fileIoGui);
    setupTabbedPanes();
  }

  private void setupTabbedPanes() {
    mealPanel.setupPanel();
    mealPanel.addElements(container);
    dbaseEditPanel.addElements(container);
    ingredientsPanel.addElements(container);

    addToFileMenu();

    container.setupMainFrame(new SaveExitWindowListener());
  }

  private void addToFileMenu() {
    container.addToFileMenu(createBackupMenu());
    container.addToFileMenu(loadBackupMenu());
    container.addSeparatorToFileMenu();
    container.addToFileMenu(exitMenu());
  }

  private JMenuItem createBackupMenu() {
    return builder("CreateBackup").addLabelText(BUNDLES.message("menuDataCreateBackup"))
        .addMnemonic(BUNDLES.message("menuDataCreateBackupMnemonic"))
        .addActionListener(action -> fileIoGui.createBackup(mealPlan))
        .build();
  }

  private JMenuItem loadBackupMenu() {
    return builder("LoadBackup").addLabelText(BUNDLES.message("menuDataLoadBackup"))
        .addMnemonic(BUNDLES.message("menuDataLoadBackupMnemonic"))
        .addActionListener(action -> {
          fileIoGui.loadBackup()
              .ifPresent(loadedMealPlaner -> {
                this.mealPlan.setMeals(loadedMealPlaner.getMeals());
                this.mealPlan.setIngredients(loadedMealPlaner.getIngredients());
                this.mealPlan.setDefaultSettings(loadedMealPlaner.getDefaultSettings());
                this.mealPlan.setLastProposal(loadedMealPlaner.getLastProposal());
                this.mealPlan.setTime(loadedMealPlaner.getTime());
              });
        })
        .build();
  }

  private JMenuItem exitMenu() {
    return builder("Exit")
        .addLabelText(BUNDLES.message("menuDataExit"))
        .addMnemonic(BUNDLES.message("menuDataExitMnemonic"))
        .addActionListener(action -> showSaveExitDialog(frame, BUNDLES.message("saveYesNoQuestion"),
            () -> fileIoGui.saveDatabase(mealPlan)))
        .build();
  }

  public JFrame getFrame() {
    return frame;
  }

  void saveDataBase() {
    dbaseEditPanel.saveDatabase();
    ingredientsPanel.saveDatabase();
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