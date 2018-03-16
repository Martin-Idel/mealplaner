package mealplaner.xml;

import static mealplaner.xml.MealplanerDataReader.loadXml;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static testcommons.CommonFunctions.getIngredient1;
import static testcommons.CommonFunctions.getIngredient2;
import static testcommons.CommonFunctions.getIngredient3;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;
import static testcommons.CommonFunctions.getMeal3;
import static testcommons.CommonFunctions.getProposal1;
import static testcommons.CommonFunctions.getSettings1;
import static testcommons.CommonFunctions.getSettings2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Test;

import mealplaner.MealplanerData;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;
import mealplaner.recipes.model.Ingredient;

public class BackupInteractionTest {
  private static final String DESTINATION_FILE_PATH = "src/test/resources/saveTemp.xml";

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
  public void roundTripWorks() {
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(getIngredient1());
    ingredients.add(getIngredient2());
    ingredients.add(getIngredient3());

    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal1());
    meals.add(getMeal2());
    meals.add(getMeal3());

    Map<DayOfWeek, Settings> defaultSettings = new HashMap<>();

    defaultSettings.put(DayOfWeek.MONDAY, getSettings1());
    defaultSettings.put(DayOfWeek.WEDNESDAY, getSettings2());
    defaultSettings.put(DayOfWeek.FRIDAY, getSettings1());

    Proposal proposal = getProposal1();

    LocalDate time = LocalDate.of(2017, 5, 3);

    MealplanerData data = new MealplanerData(
        ingredients, meals, time, DefaultSettings.from(defaultSettings), proposal);

    MealplanerDataWriter.saveXml(data, DESTINATION_FILE_PATH);
    MealplanerData roundtripData = loadXml(DESTINATION_FILE_PATH);

    assertThat(roundtripData.getLastProposal()).isEqualTo(proposal);
    assertThat(roundtripData.getTime()).isEqualTo(time);
    assertThat(roundtripData.getMeals()).containsExactlyElementsOf(meals);
    assertThat(roundtripData.getIngredients()).containsExactlyElementsOf(ingredients);
  }
}
