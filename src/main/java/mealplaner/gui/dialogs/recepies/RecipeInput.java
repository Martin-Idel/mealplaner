// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.recepies;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.NonnegativeInteger.FOUR;
import static mealplaner.commons.NonnegativeInteger.ONE;
import static mealplaner.commons.gui.GridPanel.gridPanel;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.commons.gui.dialogs.DialogWindow.window;
import static mealplaner.model.recipes.Recipe.createRecipe;

import java.awt.event.ActionListener;
import java.util.ArrayList;
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
import mealplaner.gui.dialogs.ingredients.IngredientsInput;
import mealplaner.model.DataStore;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.Recipe;

public class RecipeInput implements DialogEditing<Optional<Recipe>> {
  private final DialogWindow dialogWindow;
  private RecipeTable recipeTable;
  private InputField<NonnegativeInteger> nonnegativeIntegerInputField;
  private Optional<Recipe> enteredRecipe;
  private Table table;

  public RecipeInput(JFrame parentFrame) {
    dialogWindow = window(parentFrame, BUNDLES.message("recipeInputDialogTitle"),
        "RecipeInputDialog");
  }

  public RecipeInput(JDialog parentDialog) {
    dialogWindow = window(parentDialog, BUNDLES.message("recipeInputDialogTitle"),
        "RecipeInputDialog");
  }

  public static RecipeInput recipeInput(JFrame parentFrame) {
    return new RecipeInput(parentFrame);
  }

  public static RecipeInput recipeInput(JDialog parentDialog) {
    return new RecipeInput(parentDialog);
  }

  @Override
  public Optional<Recipe> showDialog(Optional<Recipe> recipe, DataStore store) {
    enteredRecipe = recipe;
    display(recipe, store);
    dialogWindow.dispose();
    return enteredRecipe;
  }

  private void display(Optional<Recipe> recipe, DataStore store) {
    nonnegativeIntegerInputField = setupInputField(recipe);
    GridPanel inputFieldPanel = gridPanel(1, 2);
    nonnegativeIntegerInputField.addToPanel(inputFieldPanel);

    recipeTable = new RecipeTable(recipe.orElse(createRecipe()), store.getIngredients());
    table = recipeTable.setupTable();

    ButtonPanel buttonPanel = displayButtons(store);

    dialogWindow.addNorth(inputFieldPanel);
    dialogWindow.addCentral(table);
    dialogWindow.addSouth(buttonPanel);
    dialogWindow.arrangeWithSize(400, 300);
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

  private ButtonPanel displayButtons(DataStore store) {
    return builder("RecipeInput")
        .addButton(BUNDLES.message("ingredientInput"),
            BUNDLES.message("ingredientInputMnemonic"),
            action -> saveNewIngredients(store))
        .addCancelDialogButton(dialogWindow)
        .addOkButton(getSaveListener())
        .build();
  }

  private void saveNewIngredients(DataStore store) {
    List<Ingredient> newIngredients = new IngredientsInput(dialogWindow).showDialog(store);
    enteredRecipe = recipeTable.getRecipe(ONE);
    List<Ingredient> allIngredients = new ArrayList<>();
    allIngredients.addAll(store.getIngredients());
    allIngredients.addAll(newIngredients);
    recipeTable = new RecipeTable(enteredRecipe.orElse(createRecipe()), allIngredients);
    Table oldTable = table;
    table = recipeTable.setupTable();
    dialogWindow.swapCentral(oldTable, table);
  }

  private ActionListener getSaveListener() {
    return action -> {
      enteredRecipe = recipeTable.getRecipe(nonnegativeIntegerInputField.getUserInput());
      dialogWindow.dispose();
    };
  }
}
