package mealplaner.shopping;

import java.util.List;

import mealplaner.commons.gui.tables.Table;
import mealplaner.recipes.gui.dialogs.recepies.IngredientsTable;
import mealplaner.recipes.model.QuantitativeIngredient;
import mealplaner.recipes.provider.IngredientProvider;

public class ShoppingTable {
  private List<QuantitativeIngredient> listContents;

  public Table setupTable(ShoppingList shoppingList, IngredientProvider ingredientProvider) {
    listContents = shoppingList.getList();
    return IngredientsTable.setupTable(listContents, ingredientProvider);
  }
}
