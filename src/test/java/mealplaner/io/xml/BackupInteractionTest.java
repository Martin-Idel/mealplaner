// SPDX-License-Identifier: MIT

package mealplaner.io.xml;

import static mealplaner.io.xml.MealplanerDataReader.loadXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getIngredient1;
import static testcommons.CommonFunctions.getIngredient2;
import static testcommons.CommonFunctions.getIngredient3;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;
import static testcommons.CommonFunctions.getMeal3;
import static testcommons.CommonFunctions.getProposal1;
import static testcommons.CommonFunctions.getSettings1;
import static testcommons.CommonFunctions.getSettings2;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.PluginStore;
import testcommons.XmlInteraction;

public class BackupInteractionTest extends XmlInteraction {

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

    MealplanerData data = MealplanerData.getInstance();
    data.setIngredients(ingredients);
    data.setMeals(meals);
    data.setTime(time);
    data.setDefaultSettings(DefaultSettings.from(defaultSettings));
    data.setLastProposal(proposal);

    MealplanerDataWriter.saveXml(data, DESTINATION_FILE_PATH, new PluginStore());
    MealplanerData roundtripData = loadXml(DESTINATION_FILE_PATH, new PluginStore());

    assertThat(roundtripData.getLastProposal()).isEqualTo(proposal);
    assertThat(roundtripData.getTime()).isEqualTo(time);
    assertThat(roundtripData.getMeals()).containsExactlyElementsOf(meals);
    assertThat(roundtripData.getIngredients()).containsExactlyElementsOf(ingredients);
  }
}
