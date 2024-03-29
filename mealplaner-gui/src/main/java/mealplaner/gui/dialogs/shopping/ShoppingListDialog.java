// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.shopping;

import static javax.swing.JOptionPane.showConfirmDialog;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.commons.gui.dialogs.DialogWindow.window;
import static mealplaner.gui.dialogs.shopping.ShoppingListUtils.createShoppingList;
import static mealplaner.gui.dialogs.shopping.ShoppingListUtils.someRecipesMissingForCompleteList;

import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import mealplaner.commons.gui.buttonpanel.ButtonPanel;
import mealplaner.commons.gui.dialogs.DialogWindow;
import mealplaner.commons.gui.tables.Table;
import mealplaner.gui.dialogs.recepies.IngredientsTable;
import mealplaner.ioapi.FileIoInterface;
import mealplaner.model.DataStore;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.QuantitativeIngredient;
import mealplaner.model.shoppinglist.ShoppingList;

public class ShoppingListDialog {
  private final DialogWindow dialogWindow;
  private FileIoInterface fileIo;
  private Table table;
  private List<QuantitativeIngredient> tableShoppingList;

  public ShoppingListDialog(JFrame parent, FileIoInterface fileIo) {
    dialogWindow = window(parent, BUNDLES.message("createShoppingListDialogTitle"),
        "ShoppingListDialog");
    this.fileIo = fileIo;
  }

  public void showDialog(Proposal proposal, DataStore dataStore) {
    createTable(proposal, dataStore);
    dialogWindow.dispose();
  }

  private void createTable(Proposal proposal, DataStore dataStore) {
    if (someRecipesMissingForCompleteList(proposal, dataStore) && disposeIfUserWantsTo()) {
      return;
    }
    ShoppingList shoppingList = createShoppingList(proposal, dataStore);
    display(shoppingList, dataStore.getIngredients());
  }

  private boolean disposeIfUserWantsTo() {
    int result = showConfirmDialog(dialogWindow.getParent(), BUNDLES.message("notAllRecipesExist"));
    return result == JOptionPane.NO_OPTION;
  }

  private void display(ShoppingList shoppingList, List<Ingredient> ingredients) {
    tableShoppingList = shoppingList.getList();
    table = IngredientsTable.setupTable(shoppingList.getList(), ingredients);

    ButtonPanel buttonPanel = displayButtons();

    dialogWindow.addCentral(table);
    dialogWindow.addSouth(buttonPanel);
    dialogWindow.arrangeWithSize(300, 300);
    dialogWindow.setVisible();
  }

  private ButtonPanel displayButtons() {
    return builder("ShoppingListDialog")
        .addButton(BUNDLES.message("saveJsonButton"),
            BUNDLES.message("saveJsonButtonMnemonic"),
            action -> fileIo.saveShoppingList(tableShoppingList))
        .addButton(BUNDLES.message("printButton"),
            BUNDLES.message("printButtonMnemonic"),
            action -> table.printTable(dialogWindow.getParent()))
        .addOkButton(action -> dialogWindow.dispose())
        .build();
  }
}
