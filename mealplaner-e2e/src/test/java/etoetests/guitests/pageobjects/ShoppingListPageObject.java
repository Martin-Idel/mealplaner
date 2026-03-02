// SPDX-License-Identifier: MIT

package etoetests.guitests.pageobjects;

import static etoetests.guitests.constants.ComponentNames.BUTTON_SHOPPINGLIST_DIALOG_BUTTON1;
import static etoetests.guitests.constants.ComponentNames.BUTTON_SHOPPINGLIST_CLOSE;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JOptionPane;

import etoetests.guitests.helpers.SwingTestHelper;
import mealplaner.model.recipes.QuantitativeIngredient;
import mealplaner.model.shoppinglist.ShoppingList;
import org.assertj.core.api.Assertions;

public class ShoppingListPageObject extends BasePageObject {
  private static final int MAX_MISSING_RECIPE_DIALOGS = 10;

  ShoppingListPageObject(JFrame frame) {
    super(frame);
  }

  public ShoppingListPageObject assertMissingRecipes() throws Exception {
    helper.handleOptionPaneWithOptionIfExists(JOptionPane.YES_OPTION, 2000);
    return this;
  }

  public void requireShoppingListContentAndClose(ShoppingList shoppingList) throws Exception {
    helper.handleMultipleOptionPanes(JOptionPane.YES_OPTION, MAX_MISSING_RECIPE_DIALOGS, 1500);

    JDialog dialog = helper.findDialogContaining(BUTTON_SHOPPINGLIST_DIALOG_BUTTON1, 3000);
    JTable table = findTableInDialog(dialog);

    helper.invokeAndWaitVoid(() -> {
      Assertions.assertThat(table.getRowCount()).isGreaterThan(0);
      String[][] expectedContent = generateShoppingListContent(shoppingList);
      assertTableContents(table, expectedContent);
    });

    JButton closeButton = helper.findComponentByName(dialog, BUTTON_SHOPPINGLIST_CLOSE);
    helper.clickButtonOnEdt(closeButton);

    helper.waitForDialogToClose(dialog, 10000);
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
    contents[shoppings.size()][1] = "";
    contents[shoppings.size()][2] = "";
    return contents;
  }

  @Override
  protected void assertTableContents(JTable table, String[][] expectedContent) {
    for (int row = 0; row < expectedContent.length; row++) {
      for (int col = 0; col < expectedContent[row].length; col++) {
        Object actualValue = table.getValueAt(row, col);
        String expectedValue = expectedContent[row][col];
        String actualValueString = (actualValue != null) ? actualValue.toString() : null;
        if (actualValueString != null && actualValue.getClass().getName().contains("Ingredient")) {
          actualValueString = extractIngredientName(actualValueString);
        }
        Assertions.assertThat(actualValueString).isEqualTo(expectedValue);
      }
    }
  }

  private String extractIngredientName(String ingredientString) {
    int nameStart = ingredientString.indexOf("name=\'");
    if (nameStart == -1) {
      return ingredientString;
    }
    nameStart += 6;
    int nameEnd = ingredientString.indexOf("\'", nameStart);
    if (nameEnd == -1) {
      return ingredientString;
    }
    return ingredientString.substring(nameStart, nameEnd);
  }

  @Override
  protected JTable findTableInDialog(JDialog dialog) {
    return findNamedTableInDialogOrFallback(dialog, "ShoppingListTable");
  }
}