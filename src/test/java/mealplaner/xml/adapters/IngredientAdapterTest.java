package mealplaner.xml.adapters;

import static mealplaner.xml.adapters.IngredientAdapter.convertIngredientFromXml;
import static mealplaner.xml.adapters.IngredientAdapter.convertIngredientToXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getIngredient1;
import static testcommons.CommonFunctions.getIngredient2;

import org.junit.Test;

import mealplaner.recipes.model.Ingredient;

public class IngredientAdapterTest {

  @Test
  public void adapterTest() {
    Ingredient ingredient1 = getIngredient1();
    Ingredient ingredient2 = getIngredient2();

    Ingredient convertedIngredients1 = convertIngredientFromXml(
        convertIngredientToXml(ingredient1));
    Ingredient convertedIngredients2 = convertIngredientFromXml(
        convertIngredientToXml(ingredient2));

    assertThat(convertedIngredients1).isEqualTo(ingredient1);
    assertThat(convertedIngredients2).isEqualTo(ingredient2);
  }
}
