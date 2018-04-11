package mealplaner.gui.dialogs.mealinput;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.gui.dialogs.mealinput.MealInputGrid.inputGrid;

import java.awt.event.ActionListener;
import java.util.Optional;

import javax.swing.JFrame;

import mealplaner.commons.gui.GridPanel;
import mealplaner.commons.gui.MessageDialog;
import mealplaner.commons.gui.buttonpanel.ButtonPanel;
import mealplaner.commons.gui.dialogs.DialogWindow;
import mealplaner.gui.dialogs.DialogCreating;
import mealplaner.model.DataStore;
import mealplaner.model.meal.Meal;

public class MealInput implements DialogCreating<Meal> {
  private final JFrame parentFrame;
  private final DialogWindow dialogWindow;
  private final MealInputGrid inputGrid;

  private Meal newMeal;

  public MealInput(JFrame parent) {
    dialogWindow = DialogWindow.window(parent, BUNDLES.message("mealInputDialogTitle"));
    this.parentFrame = parent;
    inputGrid = inputGrid(dialogWindow);
  }

  @Override
  public Meal showDialog(DataStore store) {
    display(store, action -> saveMeal());
    return newMeal;
  }

  private void saveMeal() {
    Optional<Meal> mealFromInput = getMealAndShowDialog();
    mealFromInput.ifPresent(meal -> {
      newMeal = meal;
      dispose();
    });
  }

  private void display(DataStore mealPlan, ActionListener saveListener) {
    GridPanel mealCreationPanel = inputGrid.initialiseInputFields(mealPlan);

    ButtonPanel buttonPanel = buildButtonPanel(saveListener);

    dialogWindow.addCentral(mealCreationPanel);
    dialogWindow.addSouth(buttonPanel);
    dialogWindow.arrangeWithSize(300, 400);
    resetFields();
    dialogWindow.setVisible();
  }

  private ButtonPanel buildButtonPanel(ActionListener saveListener) {
    return builder("MealInput")
        .addSaveButton(saveListener)
        .addCancelDialogButton(dialogWindow)
        .build();
  }

  private void resetFields() {
    inputGrid.resetFields();
  }

  private void dispose() {
    dialogWindow.dispose();
  }

  private Optional<Meal> getMealAndShowDialog() {
    Optional<Meal> mealFromInput = inputGrid.getMealFromUserInput();
    if (!mealFromInput.isPresent()) {
      MessageDialog.userErrorMessage(parentFrame, BUNDLES.message("menuNameChoiceEmpty"));
    }
    return mealFromInput;
  }
}