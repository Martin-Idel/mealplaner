// SPDX-License-Identifier: MIT

package mealplaner.io.json;

import static mealplaner.io.json.ShoppingListJsonWriter.serializeShoppingList;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonBaseFunctions.getRecipe1;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class ShoppingListJsonWriterTest {
  @Test
  void simpleShoppingListCorrectlyConvertedToJson() {
    var json = serializeShoppingList(
        LocalDate.of(2020,1,1),
        getRecipe1().getIngredientListAsIs());

    assertThat(json).hasToString(
        "{\"creationDate\":\"2020-01-01\",\"list\":"
            + "[{\"amount\":\"100\",\"measure\":\"g\",\"name\":\"Test1\",\"type\":\"Fresh fruit\"},"
            + "{\"amount\":\"200\",\"measure\":\"ml\",\"name\":\"Test2\",\"type\":\"Baking Goods\"}]}");
  }
}
