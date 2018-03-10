package mealplaner.xml;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static mealplaner.xml.IngredientsReader.loadXml;
import static mealplaner.xml.IngredientsWriter.saveXml;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static testcommons.CommonFunctions.getIngredient1;
import static testcommons.CommonFunctions.getIngredient2;
import static testcommons.CommonFunctions.getIngredient3;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import mealplaner.recipes.model.Ingredient;

public class IngredientsXmlInteractionTest {
  private static final String DESTINATION_FILE_PATH = "src/test/resources/saveTemp.xml";
  private static final String RESOURCE_FILE_WITH_THREE_MEALS = "src/test/resources/ingredientsXml.xml";

  @After
  public void tearDown() {
    try {
      File file = new File(DESTINATION_FILE_PATH);
      if (file.exists()) {
        Files.delete(file.toPath());
      }
    } catch (IOException ioex) {
      fail("Something went wrong with the TearDown");
    }
  }

  @Test
  public void loadingMealsWorksCorrectly() {
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(getIngredient1());
    ingredients.add(getIngredient2());
    ingredients.add(getIngredient3());

    File originalFile = new File(RESOURCE_FILE_WITH_THREE_MEALS);
    File temporaryFile = new File(DESTINATION_FILE_PATH);
    try {
      Files.copy(originalFile.toPath(), temporaryFile.toPath(), REPLACE_EXISTING);
    } catch (IOException exc) {
      fail("Could not load file");
    }

    List<Ingredient> database = loadXml(DESTINATION_FILE_PATH);

    assertThat(database).containsExactlyElementsOf(ingredients);
  }

  @Test
  public void roundTripResultsInSameOutputAsInput() {
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(getIngredient1());
    ingredients.add(getIngredient2());
    ingredients.add(getIngredient3());

    saveXml(ingredients, DESTINATION_FILE_PATH);
    List<Ingredient> roundTripMeals = loadXml(DESTINATION_FILE_PATH);

    assertThat(roundTripMeals).containsExactlyElementsOf(ingredients);
  }
}
