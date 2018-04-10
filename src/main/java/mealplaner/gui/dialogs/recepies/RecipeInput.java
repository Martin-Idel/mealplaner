package mealplaner.gui.dialogs.recepies;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.NonnegativeInteger.FOUR;
import static mealplaner.commons.gui.GridPanel.gridPanel;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.commons.gui.dialogs.DialogWindow.window;
import static mealplaner.model.recipes.Recipe.createRecipe;

import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;

import javax.swing.JDialog;
import javax.swing.JFrame;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.gui.GridPanel;
import mealplaner.commons.gui.buttonpanel.ButtonPanel;
import mealplaner.commons.gui.dialogs.DialogWindow;
import mealplaner.commons.gui.inputfields.InputField;
import mealplaner.commons.gui.inputfields.NonnegativeIntegerInputField;
import mealplaner.commons.gui.tables.Table;
import mealplaner.gui.dialogs.DialogEditing;
import mealplaner.model.DataStore;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.Recipe;

public class RecipeInput implements DialogEditing<Optional<Recipe>> {
  private final DialogWindow dialogWindow;
  private RecipeTable recipeTable;
  private InputField<NonnegativeInteger> nonnegativeIntegerInputField;
  private Optional<Recipe> enteredRecipe;

  public RecipeInput(JFrame parentFrame, String label) {
    dialogWindow = window(parentFrame, label);
  }

  public RecipeInput(JDialog parentDialog, String label) {
    dialogWindow = window(parentDialog, label);
  }

  @Override
  public Optional<Recipe> showDialog(Optional<Recipe> recipe, DataStore store) {
    enteredRecipe = recipe;
    display(recipe, store.getIngredients());
    dialogWindow.dispose();
    return enteredRecipe;
  }

  private void display(Optional<Recipe> recipe, List<Ingredient> ingredients) {
    nonnegativeIntegerInputField = setupInputField(recipe);
    GridPanel inputFieldPanel = gridPanel(0, 2);
    nonnegativeIntegerInputField.addToPanel(inputFieldPanel);

    recipeTable = new RecipeTable(recipe.orElse(createRecipe()), ingredients);
    Table table = recipeTable.setupTable();

    ButtonPanel buttonPanel = displayButtons();

    dialogWindow.addNorth(inputFieldPanel);
    dialogWindow.addCentral(table);
    dialogWindow.addSouth(buttonPanel);
    dialogWindow.arrangeWithSize(300, 300);
    dialogWindow.setVisible();
  }

  private NonnegativeIntegerInputField setupInputField(Optional<Recipe> recipe) {
    return new NonnegativeIntegerInputField(
        BUNDLES.message("recipeNumberOfPortionsField"),
        "RecipePortions",
        recipe.isPresent()
            ? recipe.get().getNumberOfPortions()
            : FOUR);
  }

  private ButtonPanel displayButtons() {
    return builder("RecipeInput")
        .addCancelDialogButton(dialogWindow)
        .addOkButton(getSaveListener(recipeTable))
        .build();
  }

  private ActionListener getSaveListener(RecipeTable recipeTable) {
    return action -> {
      enteredRecipe = recipeTable.getRecipe(nonnegativeIntegerInputField.getUserInput());
      dialogWindow.dispose();
    };
  }
}