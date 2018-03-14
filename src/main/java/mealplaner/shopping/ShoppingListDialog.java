package mealplaner.shopping;

import static javax.swing.JOptionPane.showConfirmDialog;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.commons.gui.dialogs.DialogWindow.window;
import static mealplaner.recipes.gui.dialogs.recepies.IngredientsTable.setupTable;
import static mealplaner.shopping.ShoppingListUtils.createShoppingList;
import static mealplaner.shopping.ShoppingListUtils.missingRecipesForCompleteList;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import mealplaner.commons.gui.buttonpanel.ButtonPanel;
import mealplaner.commons.gui.dialogs.DialogWindow;
import mealplaner.commons.gui.tables.Table;
import mealplaner.model.Proposal;
import mealplaner.recipes.model.Ingredient;

public class ShoppingListDialog {
  private final DialogWindow dialogWindow;
  private Table table;

  public ShoppingListDialog(JFrame parent) {
    dialogWindow = window(parent, BUNDLES.message("createShoppingListDialogTitle"));
  }

  public void showDialog(Proposal proposal, List<Ingredient> ingredientProvider) {
    createTable(proposal, ingredientProvider);
    dialogWindow.dispose();
  }

  private void createTable(Proposal proposal, List<Ingredient> ingredients) {
    if (missingRecipesForCompleteList(proposal) && disposeIfUserWantsTo()) {
      return;
    }
    ShoppingList shoppingList = createShoppingList(proposal);
    display(shoppingList, ingredients);
  }

  private boolean disposeIfUserWantsTo() {
    int result = showConfirmDialog(dialogWindow.getParent(), BUNDLES.message("notAllRecipesExist"));
    return result == JOptionPane.NO_OPTION;
  }

  private void display(ShoppingList shoppingList, List<Ingredient> ingredients) {
    table = setupTable(shoppingList.getList(), ingredients);

    ButtonPanel buttonPanel = displayButtons();

    dialogWindow.addCentral(table);
    dialogWindow.addSouth(buttonPanel);
    dialogWindow.arrangeWithSize(300, 300);
    dialogWindow.setVisible();
  }

  private ButtonPanel displayButtons() {
    return builder("ShoppingListDialog")
        .addButton(BUNDLES.message("printButton"),
            BUNDLES.message("printButtonMnemonic"),
            action -> table.printTable(dialogWindow.getParent()))
        .addOkButton(action -> dialogWindow.dispose())
        .build();
  }
}
