// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static mealplaner.io.xml.adapters.IngredientAdapter.convertIngredientV3FromXml;
import static mealplaner.io.xml.adapters.IngredientAdapter.convertIngredientV3ToXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getIngredient1;
import static testcommons.CommonFunctions.getIngredient2;

import org.junit.Test;

import mealplaner.Kochplaner;
import mealplaner.model.recipes.Ingredient;

public class IngredientAdapterTest {

  @Test
  public void adapterTest() {
    Ingredient convertedIngredients1 = convertIngredientV3FromXml(
        convertIngredientV3ToXml(getIngredient1()), Kochplaner.registerPlugins());
    Ingredient convertedIngredients2 = convertIngredientV3FromXml(
        convertIngredientV3ToXml(getIngredient2()), Kochplaner.registerPlugins());

    assertThat(convertedIngredients1).isEqualTo(getIngredient1());
    assertThat(convertedIngredients2).isEqualTo(getIngredient2());
  }
}
