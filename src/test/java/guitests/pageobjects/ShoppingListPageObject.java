// SPDX-License-Identifier: MIT

package guitests.pageobjects;

import java.util.List;

import org.assertj.swing.fixture.FrameFixture;

import mealplaner.model.recipes.QuantitativeIngredient;
import mealplaner.model.shoppinglist.ShoppingList;

public class ShoppingListPageObject {
  private final FrameFixture window;

  ShoppingListPageObject(FrameFixture window) {
    this.window = window;
  }

  public ShoppingListPageObject assertMissingRecipes() {
    window.optionPane().yesButton().click();
    return this;
  }

  public void requireShoppingListContentAndClose(ShoppingList shoppingList) {
    window.dialog().table().requireContents(generateShoppingListContent(shoppingList));
    window.dialog().button("ButtonPanelShoppingListDialog1").click();
  }

  private String[][] generateShoppingListContent(ShoppingList shoppingList) {
    List<QuantitativeIngredient> shoppings = shoppingList.getList();
    String[][] contents = new String[shoppings.size() + 1][3];
    for (int row = 0; row < shoppings.size(); row++) {
      QuantitativeIngredient ingredient = shoppings.get(row);
      contents[row][0] = ingredient.getIngredient().getName();
      contents[row][1] = ingredient.getAmount().toString();
      contents[row][2] = ingredient.getIngredient().getPrimaryMeasure().toString();
    }
    contents[shoppings.size()][0] = "";
    contents[shoppings.size()][1] = "0";
    contents[shoppings.size()][2] = "-";
    return contents;
  }
}
