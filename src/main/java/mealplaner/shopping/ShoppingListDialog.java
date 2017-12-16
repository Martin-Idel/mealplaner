package mealplaner.shopping;

import static javax.swing.JOptionPane.showConfirmDialog;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.dialogs.DialogWindow.window;
import static mealplaner.recipes.gui.dialogs.recepies.IngredientsTable.setupTable;
import static mealplaner.shopping.ShoppingListUtils.createShoppingList;
import static mealplaner.shopping.ShoppingListUtils.missingRecipesForCompleteList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mealplaner.commons.gui.ButtonPanelBuilder;
import mealplaner.commons.gui.dialogs.DialogWindow;
import mealplaner.commons.gui.tables.Table;
import mealplaner.model.Proposal;
import mealplaner.recipes.provider.IngredientProvider;

public class ShoppingListDialog {
  private final DialogWindow dialogWindow;
  private Table table;

  public ShoppingListDialog(JFrame parent) {
    dialogWindow = window(parent, BUNDLES.message("createShoppingListDialogTitle"));
  }

  public void showDialog(Proposal proposal, IngredientProvider ingredientProvider) {
    createTable(proposal, ingredientProvider);
    dialogWindow.dispose();
  }

  private void createTable(Proposal proposal, IngredientProvider ingredientProvider) {
    if (missingRecipesForCompleteList(proposal) && disposeIfUserWantsTo()) {
      return;
    }
    ShoppingList shoppingList = createShoppingList(proposal);
    display(shoppingList, ingredientProvider);
  }

  private boolean disposeIfUserWantsTo() {
    int result = showConfirmDialog(dialogWindow.getParent(), BUNDLES.message("notAllRecipesExist"));
    return result == JOptionPane.NO_OPTION;
  }

  private void display(ShoppingList shoppingList, IngredientProvider ingredientProvider) {
    table = setupTable(shoppingList.getList(), ingredientProvider);

    JPanel buttonPanel = displayButtons();

    dialogWindow.addCentral(table.getTableInScrollPane());
    dialogWindow.addSouth(buttonPanel);
    dialogWindow.arrangeWithSize(300, 300);
    dialogWindow.setVisible();
  }

  private JPanel displayButtons() {
    return new ButtonPanelBuilder()
        .addButton(BUNDLES.message("printButton"),
            BUNDLES.message("printButtonMnemonic"),
            action -> table.printTable(dialogWindow.getParent()))
        .addOkButton(action -> dialogWindow.dispose())
        .build();
  }
}
