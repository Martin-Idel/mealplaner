// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static mealplaner.io.xml.adapters.IngredientAdapter.convertIngredientFromXml;
import static mealplaner.io.xml.adapters.IngredientAdapter.convertIngredientToXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getIngredient1;
import static testcommons.CommonFunctions.getIngredient2;

import org.junit.Test;

import mealplaner.model.recipes.Ingredient;

public class IngredientAdapterTest {

  @Test
  public void adapterTest() {
    Ingredient convertedIngredients1 = convertIngredientFromXml(
        convertIngredientToXml(getIngredient1()));
    Ingredient convertedIngredients2 = convertIngredientFromXml(
        convertIngredientToXml(getIngredient2()));

    assertThat(convertedIngredients1).isEqualTo(getIngredient1());
    assertThat(convertedIngredients2).isEqualTo(getIngredient2());
  }
}
