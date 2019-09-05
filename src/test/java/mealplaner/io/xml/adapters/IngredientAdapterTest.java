// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static mealplaner.io.xml.adapters.IngredientAdapter.convertIngredientV2FromXml;
import static mealplaner.io.xml.adapters.IngredientAdapter.convertIngredientV2ToXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getIngredient1;
import static testcommons.CommonFunctions.getIngredient2;

import org.junit.Test;

import mealplaner.model.recipes.Ingredient;

public class IngredientAdapterTest {

  @Test
  public void adapterTest() {
    Ingredient convertedIngredients1 = convertIngredientV2FromXml(
        convertIngredientV2ToXml(getIngredient1()));
    Ingredient convertedIngredients2 = convertIngredientV2FromXml(
        convertIngredientV2ToXml(getIngredient2()));

    assertThat(convertedIngredients1).isEqualTo(getIngredient1());
    assertThat(convertedIngredients2).isEqualTo(getIngredient2());
  }
}
