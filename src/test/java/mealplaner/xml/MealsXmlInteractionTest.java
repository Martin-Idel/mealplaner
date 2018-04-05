package mealplaner.xml;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static mealplaner.xml.MealsReader.loadXml;
import static mealplaner.xml.MealsWriter.saveXml;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;
import static testcommons.CommonFunctions.getMeal3;
import static testcommons.CommonFunctions.setupMealplanerDataWithAllIngredients;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import mealplaner.MealplanerData;
import mealplaner.model.Meal;

public class MealsXmlInteractionTest {
  private static final String DESTINATION_FILE_PATH = "src/test/resources/saveTemp.xml";
  private static final String RESOURCE_FILE_WITH_THREE_MEALS_V1 = "src/test/resources/mealsXmlV1.xml";
  private static final String RESOURCE_FILE_WITH_THREE_MEALS_V2 = "src/test/resources/mealsXmlV2.xml";

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
  public void loadingMealsWorksCorrectlyForVersion1() {
    MealplanerData mealPlan = setupMealplanerDataWithAllIngredients();
    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal1());
    meals.add(getMeal2());
    meals.add(getMeal3());

    File originalFile = new File(RESOURCE_FILE_WITH_THREE_MEALS_V1);
    File temporaryFile = new File(DESTINATION_FILE_PATH);
    try {
      Files.copy(originalFile.toPath(), temporaryFile.toPath(), REPLACE_EXISTING);
    } catch (IOException exc) {
      fail("Could not load file");
    }

    List<Meal> database = loadXml(mealPlan, DESTINATION_FILE_PATH);

    database.sort((meal1, meal2) -> meal1.compareTo(meal2));
    meals.sort((meal1, meal2) -> meal1.compareTo(meal2));

    assertTrue(assertMealsEqualWithoutUuids(database.get(0), meals.get(0)));
    assertTrue(assertMealsEqualWithoutUuids(database.get(1), meals.get(1)));
    assertTrue(assertMealsEqualWithoutUuids(database.get(2), meals.get(2)));
  }

  @Test
  public void loadingMealsWorksCorrectlyForVersion2() {
    MealplanerData mealPlan = setupMealplanerDataWithAllIngredients();
    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal1());
    meals.add(getMeal2());
    meals.add(getMeal3());
    meals.sort((meal1, meal2) -> meal1.compareTo(meal2));

    File originalFile = new File(RESOURCE_FILE_WITH_THREE_MEALS_V2);
    File temporaryFile = new File(DESTINATION_FILE_PATH);
    try {
      Files.copy(originalFile.toPath(), temporaryFile.toPath(), REPLACE_EXISTING);
    } catch (IOException exc) {
      fail("Could not load file");
    }

    List<Meal> database = loadXml(mealPlan, DESTINATION_FILE_PATH);

    database.sort((meal1, meal2) -> meal1.compareTo(meal2));

    assertThat(database).containsExactlyElementsOf(meals);
  }

  @Test
  public void roundTripResultsInSameOutputAsInput() {
    MealplanerData mealPlan = setupMealplanerDataWithAllIngredients();
    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal1());
    meals.add(getMeal2());
    meals.add(getMeal3());

    saveXml(meals, DESTINATION_FILE_PATH);
    List<Meal> roundTripMeals = loadXml(mealPlan, DESTINATION_FILE_PATH);

    roundTripMeals.sort((meal1, meal2) -> meal1.compareTo(meal2));
    meals.sort((meal1, meal2) -> meal1.compareTo(meal2));
    assertThat(roundTripMeals).containsExactlyElementsOf(meals);
  }

  private static boolean assertMealsEqualWithoutUuids(Meal meal1, Meal meal2) {
    return meal1.getName().equals(meal2.getName())
        && meal1.getCookingTime().equals(meal2.getCookingTime())
        && meal1.getSidedish().equals(meal2.getSidedish())
        && meal1.getObligatoryUtensil().equals(meal2.getObligatoryUtensil())
        && meal1.getCourseType().equals(meal2.getCourseType())
        && meal1.getCookingPreference().equals(meal2.getCookingPreference())
        && meal1.getComment().equals(meal2.getComment())
        && meal1.getDaysPassed().equals(meal2.getDaysPassed())
        && meal1.getRecipe().equals(meal2.getRecipe());
  }
}
