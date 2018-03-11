package mealplaner.xml;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
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
import java.util.Map;

import org.junit.After;
import org.junit.Test;

import mealplaner.MealplanerData;
import mealplaner.model.Proposal;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;

public class MealplanerDataXmlInteractionTest {
  private static final String DESTINATION_FILE_PATH = "src/test/resources/saveTemp.xml";
  private static final String RESOURCE_FILE_WITH_THREE_MEALS = "src/test/resources/mealplanerDataCorrectXml.xml";

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
    Map<DayOfWeek, Settings> defaultSettings = new HashMap<>();
    defaultSettings.put(DayOfWeek.MONDAY, getSettings1());
    defaultSettings.put(DayOfWeek.WEDNESDAY, getSettings2());
    defaultSettings.put(DayOfWeek.FRIDAY, getSettings1());
    Proposal proposal = getProposal1();
    LocalDate time = LocalDate.of(2017, 5, 3);

    File originalFile = new File(RESOURCE_FILE_WITH_THREE_MEALS);
    File temporaryFile = new File(DESTINATION_FILE_PATH);
    try {
      Files.copy(originalFile.toPath(), temporaryFile.toPath(), REPLACE_EXISTING);
    } catch (IOException exc) {
      fail("Could not load file");
    }

    MealplanerData loadedMealplaner = MealplanerDataReader.loadXml(DESTINATION_FILE_PATH);

    assertThat(loadedMealplaner.getLastProposal()).isEqualTo(proposal);
    assertThat(loadedMealplaner.getTime()).isEqualTo(time);
    assertThat(loadedMealplaner.getMeals()).isEmpty();
    assertThat(loadedMealplaner.getDefaultSettings().getDefaultSettings())
        .containsAllEntriesOf(defaultSettings);
  }

  @Test
  public void roundTripResultsInSameOutputAsInput() {
    Map<DayOfWeek, Settings> defaultSettings = new HashMap<>();

    defaultSettings.put(DayOfWeek.MONDAY, getSettings1());
    defaultSettings.put(DayOfWeek.WEDNESDAY, getSettings2());
    defaultSettings.put(DayOfWeek.FRIDAY, getSettings1());

    Proposal proposal = getProposal1();

    LocalDate time = LocalDate.of(2017, 5, 3);

    MealplanerData data = new MealplanerData(new ArrayList<>(),
        time,
        DefaultSettings.from(defaultSettings),
        proposal);

    MealplanerDataWriter.saveXml(data, DESTINATION_FILE_PATH);
    MealplanerData roundTripMealplaner = MealplanerDataReader.loadXml(DESTINATION_FILE_PATH);

    assertThat(roundTripMealplaner.getLastProposal()).isEqualTo(proposal);
    assertThat(roundTripMealplaner.getTime()).isEqualTo(time);
    assertThat(roundTripMealplaner.getMeals()).isEmpty();
    assertThat(roundTripMealplaner.getDefaultSettings().getDefaultSettings())
        .containsAllEntriesOf(defaultSettings);
  }
}
