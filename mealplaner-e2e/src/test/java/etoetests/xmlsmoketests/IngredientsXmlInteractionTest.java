// SPDX-License-Identifier: MIT

package etoetests.xmlsmoketests;

import static etoetests.CommonFunctions.getIngredient1;
import static etoetests.CommonFunctions.getIngredient2;
import static etoetests.CommonFunctions.getIngredient3;
import static mealplaner.io.xml.IngredientsReader.loadXml;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import mealplaner.model.recipes.Ingredient;

class IngredientsXmlInteractionTest extends EtoeXmlInteraction {
  private static final String RESOURCE_FILE_WITH_THREE_INGREDIENTS_V3 = "src/test/resources/ingredientsXmlV3.xml";

  @Test
  void loadingIngredientsWorksCorrectlyWithVersion3() {
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(getIngredient1());
    ingredients.add(getIngredient2());
    ingredients.add(getIngredient3());

    loadFileWithName(RESOURCE_FILE_WITH_THREE_INGREDIENTS_V3);

    List<Ingredient> database = loadXml(DESTINATION_FILE_PATH, pluginStore);

    assertThat(database).containsExactlyElementsOf(ingredients);
  }
}
