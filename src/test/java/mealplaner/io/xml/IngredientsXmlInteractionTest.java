// SPDX-License-Identifier: MIT

package mealplaner.io.xml;

import static mealplaner.io.xml.IngredientsReader.loadXml;
import static mealplaner.io.xml.IngredientsWriter.saveXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getIngredient1;
import static testcommons.CommonFunctions.getIngredient2;
import static testcommons.CommonFunctions.getIngredient3;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import mealplaner.model.recipes.Ingredient;
import mealplaner.plugins.PluginStore;
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

  private List<Ingredient> fillIngredientsArray() {
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(getIngredient1());
    ingredients.add(getIngredient2());
    ingredients.add(getIngredient3());
    return ingredients;
  }
}
