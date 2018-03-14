package mealplaner.gui.dialogs.mealinput;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.gui.dialogs.mealinput.MealInputGrid.inputGrid;

import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;

import javax.swing.JFrame;

import mealplaner.commons.gui.GridPanel;
import mealplaner.commons.gui.MessageDialog;
import mealplaner.commons.gui.buttonpanel.ButtonPanel;
import mealplaner.commons.gui.dialogs.DialogWindow;
import mealplaner.model.Meal;
import mealplaner.recipes.model.Ingredient;

public abstract class MealInput<T> {
  private final JFrame parentFrame;
  private final DialogWindow dialogWindow;

  private final MealInputGrid inputGrid;

  public MealInput(JFrame parent) {
    dialogWindow = DialogWindow.window(parent, BUNDLES.message("mealInputDialogTitle"));
    this.parentFrame = parent;
    inputGrid = inputGrid(dialogWindow);
  }

  public abstract T showDialog(List<Ingredient> ingredients);

  protected void display(List<Ingredient> ingredients, ActionListener saveListener) {
    GridPanel mealCreationPanel = inputGrid.initialiseInputFields(ingredients);

    ButtonPanel buttonPanel = buildButtonPanel(saveListener);

    dialogWindow.addCentral(mealCreationPanel);
    dialogWindow.addSouth(buttonPanel);
    dialogWindow.arrangeWithSize(300, 400);
    dialogWindow.setVisible();
  }

  private ButtonPanel buildButtonPanel(ActionListener saveListener) {
    return builder("MealInput")
        .addSaveButton(saveListener)
        .addCancelDialogButton(dialogWindow)
        .build();
  }

  protected void resetFields() {
    inputGrid.resetFields();
  }

  protected void dispose() {
    dialogWindow.dispose();
  }

  protected Optional<Meal> getMealAndShowDialog() {
    Optional<Meal> mealFromInput = inputGrid.getMealFromUserInput();
    if (!mealFromInput.isPresent()) {
      MessageDialog.userErrorMessage(parentFrame, BUNDLES.message("menuNameChoiceEmpty"));
    }
    return mealFromInput;
  }
}