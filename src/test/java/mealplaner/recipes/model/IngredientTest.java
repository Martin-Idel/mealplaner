package mealplaner.recipes.model;

import static mealplaner.recipes.model.Ingredient.generateXml;
import static mealplaner.recipes.model.Ingredient.ingredient;
import static mealplaner.recipes.model.Ingredient.loadFromXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.createDocument;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;

public class IngredientTest {

  @Test
  public void testCorrectXmlSavingAndLoading() throws ParserConfigurationException {
    Ingredient expected = ingredient("Test", IngredientType.FRESH_FRUIT, Measure.GRAM);
    Document doc = createDocument();

    Ingredient actual = loadFromXml(generateXml(doc, expected, "ingredient"));

    assertThat(actual).isEqualToComparingFieldByField(expected);
  }
}
