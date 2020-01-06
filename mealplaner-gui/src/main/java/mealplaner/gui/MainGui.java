// SPDX-License-Identifier: MIT

package mealplaner.gui;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.JMenuBuilder.builder;
import static mealplaner.commons.gui.MessageDialog.showSaveExitDialog;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;

import mealplaner.api.ProposalBuilderFactory;
import mealplaner.gui.factories.DialogFactory;
import mealplaner.gui.tabbedpanes.databaseedit.DatabaseEditPanel;
import mealplaner.gui.tabbedpanes.ingredientsedit.IngredientsEditPanel;
import mealplaner.gui.tabbedpanes.proposal.ProposalSummaryPanel;
import mealplaner.ioapi.FileIoInterface;
import mealplaner.model.MealplanerData;
import mealplaner.plugins.PluginStore;

public class MainGui {
  private final JFrame frame;
  private final MainContainer container;

  private final MealplanerData mealPlan;

  private final FileIoInterface fileIo;
  private final DatabaseEditPanel dbaseEditPanel;
  private final ProposalSummaryPanel mealPanel;
  private final IngredientsEditPanel ingredientsPanel;

  public MainGui(JFrame mainFrame, MealplanerData mealPlan, DialogFactory dialogFactory,
                 FileIoInterface fileIo, ProposalBuilderFactory proposalFactory, PluginStore pluginStore) {
    this.frame = mainFrame;
    this.mealPlan = mealPlan;
    this.fileIo = fileIo;

    this.container = new MainContainer(frame);
    mealPanel = new ProposalSummaryPanel(this.mealPlan, dialogFactory,
        mainFrame, fileIo, proposalFactory);
    dbaseEditPanel = new DatabaseEditPanel(this.mealPlan, frame, fileIo);
    ingredientsPanel = new IngredientsEditPanel(this.mealPlan, frame, fileIo);
    setupTabbedPanes(pluginStore);
  }

  private void setupTabbedPanes(PluginStore pluginStore) {
    mealPanel.setupPanel(pluginStore);
    mealPanel.addElements(container, pluginStore);
    dbaseEditPanel.addElements(container, pluginStore);
    ingredientsPanel.addElements(container, pluginStore);

    addToFileMenu(pluginStore);

    container.setupMainFrame(new SaveExitWindowListener());
  }

  private void addToFileMenu(PluginStore pluginStore) {
    container.addToFileMenu(createBackupMenu());
    container.addToFileMenu(loadBackupMenu(pluginStore));
    container.addSeparatorToFileMenu();
    container.addToFileMenu(exitMenu());
  }

  private JMenuItem createBackupMenu() {
    return builder("CreateBackup").addLabelText(BUNDLES.message("menuDataCreateBackup"))
        .addMnemonic(BUNDLES.message("menuDataCreateBackupMnemonic"))
        .addActionListener(action -> fileIo.createBackup(mealPlan))
        .build();
  }

  private JMenuItem loadBackupMenu(PluginStore pluginStore) {
    return builder("LoadBackup").addLabelText(BUNDLES.message("menuDataLoadBackup"))
        .addMnemonic(BUNDLES.message("menuDataLoadBackupMnemonic"))
        .addActionListener(action -> fileIo.loadBackup(pluginStore)
            .ifPresent(loadedMealPlaner -> {
              this.mealPlan.setMeals(loadedMealPlaner.getMeals());
              this.mealPlan.setIngredients(loadedMealPlaner.getIngredients());
              this.mealPlan.setDefaultSettings(loadedMealPlaner.getDefaultSettings());
              this.mealPlan.setLastProposal(loadedMealPlaner.getLastProposal());
              this.mealPlan.setTime(loadedMealPlaner.getTime());
            }))
        .build();
  }

  private JMenuItem exitMenu() {
    return builder("Exit")
        .addLabelText(BUNDLES.message("menuDataExit"))
        .addMnemonic(BUNDLES.message("menuDataExitMnemonic"))
        .addActionListener(action -> showSaveExitDialog(frame, BUNDLES.message("saveYesNoQuestion"),
            () -> fileIo.saveDatabase(mealPlan)))
        .build();
  }

  public JFrame getFrame() {
    return frame;
  }

  void saveDataBase() {
    dbaseEditPanel.saveDatabase();
    ingredientsPanel.saveDatabase();
    fileIo.saveDatabase(mealPlan);
  }

  class SaveExitWindowListener extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      showSaveExitDialog(getFrame(), BUNDLES.message("saveYesNoQuestion"),
              MainGui.this::saveDataBase);
    }
  }
}