// SPDX-License-Identifier: MIT

package mealplaner.io.xml;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.singletonList;
import static java.util.UUID.nameUUIDFromBytes;
import static mealplaner.io.xml.IngredientsReader.loadXml;
import static mealplaner.io.xml.IngredientsWriter.saveXml;
import static mealplaner.model.recipes.IngredientBuilder.ingredient;
import static mealplaner.model.recipes.IngredientType.MEAT_PRODUCTS;
import static mealplaner.model.recipes.Measures.DEFAULT_MEASURES;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.TestIngredientFact.TestEnum.TEST1;

import java.util.List;

import org.junit.Test;

import mealplaner.model.recipes.Ingredient;
import mealplaner.plugins.PluginStore;
import testcommons.HiddenIngredientFact;
import testcommons.TestIngredientFact;
import testcommons.XmlInteraction;

public class IngredientFactXmlInteractionTest extends XmlInteraction {
  @Test
  public void roundTripWorksWithHiddenFacts() {
    List<Ingredient> ingredients = singletonList(ingredient()
        .withName("Test1")
        .withMeasures(DEFAULT_MEASURES)
        .withType(MEAT_PRODUCTS)
        .addFact(new TestIngredientFact(TestIngredientFact.TestEnum.TEST2))
        .addFact(new HiddenIngredientFact(HiddenIngredientFact.HiddenEnum.TEST2))
        .create());

    var pluginStore = new PluginStore();
    pluginStore.registerIngredientExtension(
        TestIngredientFact.class, TestIngredientFact.class, TestIngredientFact::new);
    pluginStore.registerIngredientExtension(
        HiddenIngredientFact.class, HiddenIngredientFact.class, HiddenIngredientFact::new);
    saveXml(ingredients, DESTINATION_FILE_PATH, pluginStore);

    var smallerPluginStore = new PluginStore();
    smallerPluginStore.registerIngredientExtension(
        TestIngredientFact.class, TestIngredientFact.class, TestIngredientFact::new);
    var savedIngredients = loadXml(DESTINATION_FILE_PATH, smallerPluginStore);

    assertThat(savedIngredients).hasSize(1);
    assertThat(savedIngredients.get(0).getHiddenFacts()).hasSize(1);
    assertThat(savedIngredients.get(0).getIngredientFacts()).containsKey(TestIngredientFact.class);

    saveXml(savedIngredients, DESTINATION_FILE_PATH, smallerPluginStore);
    var reloadedIngredients = loadXml(DESTINATION_FILE_PATH, pluginStore);

    assertThat(reloadedIngredients).containsExactlyElementsOf(ingredients);
  }

  @Test
  public void roundTripInitialisesDefaultsCorrectly() {
    Ingredient ingredient = ingredient()
        .withUuid(nameUUIDFromBytes("Test1Meal".getBytes(UTF_8)))
        .withName("Test1")
        .withMeasures(DEFAULT_MEASURES)
        .create();

    Ingredient expected = ingredient()
        .withUuid(nameUUIDFromBytes("Test1Meal".getBytes(UTF_8)))
        .withName("Test1")
        .withMeasures(DEFAULT_MEASURES)
        .addFact(new TestIngredientFact(TEST1))
        .create();

    var pluginStore = new PluginStore();
    pluginStore.registerIngredientExtension(
        TestIngredientFact.class, TestIngredientFact.class, TestIngredientFact::new);
    saveXml(singletonList(ingredient), DESTINATION_FILE_PATH, pluginStore);

    var reloadedIngredients = loadXml(DESTINATION_FILE_PATH, pluginStore);

    assertThat(reloadedIngredients).containsExactlyInAnyOrder(expected);
  }
}
