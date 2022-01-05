// SPDX-License-Identifier: MIT

package mealplaner.io.json;

import java.time.LocalDate;
import java.util.List;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import mealplaner.model.recipes.QuantitativeIngredient;

public final class ShoppingListJsonWriter {

  private ShoppingListJsonWriter() {
  }

  public static String serializeShoppingList(LocalDate creationDate, List<QuantitativeIngredient> list) {
    var shoppingListJson = new JsonObject();
    shoppingListJson.put("creationDate", creationDate.toString());
    var shoppingListArray = new JsonArray();
    for (var ingredient: list) {
      JsonObject shoppingListEntry = new JsonObject(); // NOPMD
      shoppingListEntry.put("name", ingredient.getIngredient().getName());
      shoppingListEntry.put("type", ingredient.getIngredient().getType().toString());
      shoppingListEntry.put("amount", ingredient.getAmount().toRoundedString());
      shoppingListEntry.put("measure", ingredient.getMeasure().toString());
      shoppingListArray.add(shoppingListEntry);
    }
    shoppingListJson.put("list", shoppingListArray);
    return shoppingListJson.toJson();
  }
}
