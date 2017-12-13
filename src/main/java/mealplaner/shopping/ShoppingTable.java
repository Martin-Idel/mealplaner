package mealplaner.shopping;

import mealplaner.commons.gui.tables.Table;
import mealplaner.recipes.gui.dialogs.recepies.IngredientsTable;
import mealplaner.recipes.provider.IngredientProvider;

public class ShoppingTable {
  public Table setupTable(ShoppingList shoppingList, IngredientProvider ingredientProvider) {
    return IngredientsTable.setupTable(shoppingList.getList(), ingredientProvider);
  }
}
