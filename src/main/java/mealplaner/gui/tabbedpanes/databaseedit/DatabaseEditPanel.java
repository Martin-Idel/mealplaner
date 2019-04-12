// SPDX-License-Identifier: MIT

package mealplaner.gui.tabbedpanes.databaseedit;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.HelpPanel.mealPlanerHelpScrollPane;
import static mealplaner.commons.gui.JMenuBuilder.builder;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mealplaner.gui.MainContainer;
import mealplaner.io.FileIoGui;
import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;

public class DatabaseEditPanel {
  private final MealplanerData mealPlan;
  private final JFrame frame;
  private final FileIoGui fileIoGui;
  private DatabaseEdit dbaseEdit;

  public DatabaseEditPanel(MealplanerData mealPlan,
      JFrame frame, FileIoGui fileIoGui) {
    this.mealPlan = mealPlan;
    this.frame = frame;
    this.fileIoGui = fileIoGui;
  }

  public void addElements(MainContainer container) {
    container.addTabbedPane(BUNDLES.message("dataPanelName"), setupDatabasePanel());
    container.addToHelpMenu(helpDatabaseMenu());
  }

  private JMenuItem helpDatabaseMenu() {
    return builder("HelpDataBase").addLabelText(BUNDLES.message("menuHelpDatabase"))
        .addMnemonic(BUNDLES.message("menuHelpDatabaseMnemonic"))
        .addActionListener(action -> JOptionPane.showMessageDialog(frame,
            mealPlanerHelpScrollPane("DatabaseEditHelp"),
            BUNDLES.message("menuHelp"), JOptionPane.PLAIN_MESSAGE))
        .build();
  }

  private JPanel setupDatabasePanel() {
    JPanel databasePanel = new JPanel();
    dbaseEdit = new DatabaseEdit(this.mealPlan, frame, databasePanel, fileIoGui);
    dbaseEdit.setupPane(mealPlan::setMeals);
    return databasePanel;
  }

  public void saveDatabase() {
    if (unsavedChanges()) {
      mealPlan.setMeals(dbaseEdit.getMeals());
    }
  }

  private boolean unsavedChanges() {
    List<Meal> meals = dbaseEdit.getMeals();
    List<Meal> savedMeals = mealPlan.getMeals();
    return !meals.stream()
        .allMatch(meal -> savedMeals.stream().anyMatch(savedMeal -> savedMeal.equals(meal)))
        || !savedMeals.stream()
            .allMatch(savedMeal -> meals.stream().anyMatch(meal -> meal.equals(savedMeal)));
  }
}
