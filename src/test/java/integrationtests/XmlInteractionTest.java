package integrationtests;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static mealplaner.xml.XmlReading.loadXml;
import static mealplaner.xml.XmlWriting.saveXml;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;
import static testcommons.CommonFunctions.getMeal3;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import mealplaner.model.Meal;

public class XmlInteractionTest {
  private static final String DESTINATION_FILE_PATH = "src/test/resources/saveTemp.xml";
  private static final String RESOURCE_FILE_WITH_THREE_MEALS = "src/test/resources/correctXml.xml";
  private static final String UNKNOWN_VERSION_FILE = "src/test/resources/unknownVersion.xml";

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
    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal1());
    meals.add(getMeal2());
    meals.add(getMeal3());

    File originalFile = new File(RESOURCE_FILE_WITH_THREE_MEALS);
    File temporaryFile = new File(DESTINATION_FILE_PATH);
    try {
      Files.copy(originalFile.toPath(), temporaryFile.toPath(), REPLACE_EXISTING);
    } catch (IOException exc) {
      fail("Could not load file");
    }

    List<Meal> database = loadXml(DESTINATION_FILE_PATH);

    database.sort((meal1, meal2) -> meal1.compareTo(meal2));
    meals.sort((meal1, meal2) -> meal1.compareTo(meal2));
    assertThat(database).containsExactlyElementsOf(meals);
  }

  @Test
  public void roundTripResultsInSameOutputAsInput() {
    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal1());
    meals.add(getMeal2());
    meals.add(getMeal3());

    saveXml(meals, DESTINATION_FILE_PATH);
    List<Meal> roundTripMeals = loadXml(DESTINATION_FILE_PATH);

    roundTripMeals.sort((meal1, meal2) -> meal1.compareTo(meal2));
    meals.sort((meal1, meal2) -> meal1.compareTo(meal2));
    assertThat(roundTripMeals).containsExactlyElementsOf(meals);
  }

  @Test
  public void versionNumberIsReadCorrectly() {
    File originalFile = new File(UNKNOWN_VERSION_FILE);
    File temporaryFile = new File(DESTINATION_FILE_PATH);
    try {
      Files.copy(originalFile.toPath(), temporaryFile.toPath(), REPLACE_EXISTING);
    } catch (IOException exc) {
      fail("Could not load file");
    }

    List<Meal> database = loadXml(DESTINATION_FILE_PATH);

    assertThat(database).isEmpty();

    originalFile = new File(RESOURCE_FILE_WITH_THREE_MEALS);
    try {
      Files.copy(originalFile.toPath(), temporaryFile.toPath(), REPLACE_EXISTING);
    } catch (IOException exc) {
      fail("Could not load file");
    }

    List<Meal> newDatabase = loadXml(DESTINATION_FILE_PATH);

    assertThat(newDatabase).isNotEmpty();
  }
}
