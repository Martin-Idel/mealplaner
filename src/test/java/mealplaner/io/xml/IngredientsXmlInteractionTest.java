// SPDX-License-Identifier: MIT

package mealplaner.io.xml;

import static java.util.Collections.singletonList;
import static mealplaner.io.xml.IngredientsReader.loadXml;
import static mealplaner.io.xml.IngredientsWriter.saveXml;
import static mealplaner.model.recipes.IngredientBuilder.ingredient;
import static mealplaner.model.recipes.IngredientType.MEAT_PRODUCTS;
import static mealplaner.model.recipes.Measures.DEFAULT_MEASURES;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getIngredient1;
import static testcommons.CommonFunctions.getIngredient2;
import static testcommons.CommonFunctions.getIngredient3;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import mealplaner.model.recipes.Ingredient;
import mealplaner.plugins.PluginStore;
import testcommons.HiddenIngredientFact;
import testcommons.TestIngredientFact;
import testcommons.XmlInteraction;

public class IngredientsXmlInteractionTest extends XmlInteraction {
  private static final String RESOURCE_FILE_WITH_THREE_INGREDIENTS_V3 = "src/test/resources/ingredientsXmlV3.xml";

  @Test
  public void loadingIngredientsWorksCorrectlyWithVersion2() {
    List<Ingredient> ingredients = fillIngredientsArray();

    loadFileWithName(RESOURCE_FILE_WITH_THREE_INGREDIENTS_V3);

    List<Ingredient> database = loadXml(DESTINATION_FILE_PATH, new PluginStore());

    assertThat(database).containsExactlyElementsOf(ingredients);
  }

  @Test
  public void roundTripResultsInSameOutputAsInput() {
    List<Ingredient> ingredients = fillIngredientsArray();
    saveXml(ingredients, DESTINATION_FILE_PATH, new PluginStore());
    List<Ingredient> roundTripMeals = loadXml(DESTINATION_FILE_PATH, new PluginStore());

    assertThat(roundTripMeals).containsExactlyElementsOf(ingredients);
  }

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

  private List<Ingredient> fillIngredientsArray() {
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(getIngredient1());
    ingredients.add(getIngredient2());
    ingredients.add(getIngredient3());
    return ingredients;
  }
}
