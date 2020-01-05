// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static mealplaner.io.xml.adapters.IngredientAdapter.convertIngredientV3FromXml;
import static mealplaner.io.xml.adapters.IngredientAdapter.convertIngredientV3ToXml;
import static mealplaner.model.recipes.IngredientBuilder.ingredient;
import static mealplaner.model.recipes.IngredientType.MEAT_PRODUCTS;
import static mealplaner.model.recipes.Measures.DEFAULT_MEASURES;
import static org.assertj.core.api.Assertions.assertThat;
import static xmlcommons.TestIngredientFact.TestEnum.TEST1;

import org.junit.jupiter.api.Test;

import mealplaner.model.recipes.Ingredient;
import mealplaner.plugins.PluginStore;
import xmlcommons.TestIngredientFact;

public class IngredientAdapterTest {

  @Test
  public void adapterTest() {
    Ingredient ingredient = ingredient()
        .withName("Test1")
        .withType(MEAT_PRODUCTS)
        .withMeasures(DEFAULT_MEASURES)
        .addFact(new TestIngredientFact(TEST1))
        .create();

    var pluginStore = new PluginStore();
    pluginStore.registerIngredientExtension(
        TestIngredientFact.class, TestIngredientFact.class, TestIngredientFact::new);
    Ingredient convertedIngredients1 = convertIngredientV3FromXml(convertIngredientV3ToXml(ingredient), pluginStore);

    assertThat(convertedIngredients1).isEqualTo(ingredient);
  }
}
