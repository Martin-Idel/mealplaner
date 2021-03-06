// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.mealinput;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.commons.gui.dialogs.DialogWindow.window;
import static mealplaner.gui.dialogs.mealinput.MealInputGrid.inputGrid;

import java.util.Optional;
import javax.swing.JFrame;

import mealplaner.commons.gui.GridPanel;
import mealplaner.commons.gui.MessageDialog;
import mealplaner.commons.gui.buttonpanel.ButtonPanel;
import mealplaner.commons.gui.dialogs.DialogWindow;
import mealplaner.gui.dialogs.DialogCreating;
import mealplaner.model.DataStore;
import mealplaner.model.meal.Meal;
import mealplaner.plugins.PluginStore;

public class MealInput implements DialogCreating<Optional<Meal>> {
  private final JFrame parentFrame;
  private final DialogWindow dialogWindow;
  private final MealInputGrid inputGrid;

  private Optional<Meal> newMeal = Optional.empty();

  public MealInput(JFrame parent) {
    dialogWindow = window(parent, BUNDLES.message("mealInputDialogTitle"), "MealInput");
    this.parentFrame = parent;
    inputGrid = inputGrid(dialogWindow);
  }

  public static MealInput mealinput(JFrame parent) {
    return new MealInput(parent);
  }

  @Override
  public Optional<Meal> showDialog(DataStore store, PluginStore pluginStore) {
    display(store, pluginStore);
    return newMeal;
  }

  private void saveMeal(PluginStore pluginStore) {
    newMeal = getMealAndShowDialog(pluginStore);
    newMeal.ifPresent(meal -> dispose());
  }

  private void display(DataStore mealPlan, PluginStore pluginStore) {
    GridPanel mealCreationPanel = inputGrid.initialiseInputFields(mealPlan, pluginStore);

    ButtonPanel buttonPanel = buildButtonPanel(pluginStore);

    dialogWindow.addCentral(mealCreationPanel);
    dialogWindow.addSouth(buttonPanel);
    dialogWindow.arrangeWithSize(350, 400);
    resetFields();
    dialogWindow.setVisible();
  }

  private ButtonPanel buildButtonPanel(PluginStore pluginStore) {
    return builder("MealInput")
        .addSaveAndCloseButton(action -> saveMeal(pluginStore))
        .addCancelDialogButton(dialogWindow)
        .build();
  }

  private void resetFields() {
    inputGrid.resetFields();
  }

  private void dispose() {
    dialogWindow.dispose();
  }

  private Optional<Meal> getMealAndShowDialog(PluginStore pluginStore) {
    Optional<Meal> mealFromInput = inputGrid.getMealFromUserInput(pluginStore);
    if (mealFromInput.isEmpty()) {
      MessageDialog.userErrorMessage(parentFrame, BUNDLES.message("menuNameChoiceEmpty"));
    }
    return mealFromInput;
  }
}