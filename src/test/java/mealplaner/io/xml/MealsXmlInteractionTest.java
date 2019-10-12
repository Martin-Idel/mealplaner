// SPDX-License-Identifier: MIT

package mealplaner.io.xml;

import static java.util.Collections.singletonList;
import static mealplaner.commons.NonnegativeInteger.ONE;
import static mealplaner.io.xml.MealsReader.loadXml;
import static mealplaner.io.xml.MealsWriter.saveXml;
import static mealplaner.model.MealplanerData.getInstance;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;
import static testcommons.CommonFunctions.getMeal3;
import static testcommons.CommonFunctions.setupMealplanerDataWithAllIngredients;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import mealplaner.Kochplaner;
import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;
import mealplaner.model.meal.MealBuilder;
import mealplaner.plugins.PluginStore;
import testcommons.HiddenMealFact;
import testcommons.TestMealFact;
import testcommons.XmlInteraction;

public class MealsXmlInteractionTest extends XmlInteraction {
  private static final String RESOURCE_FILE_WITH_THREE_MEALS_V3 = "src/test/resources/mealsXmlV3.xml";

  @Test
  public void loadingMealsWorksCorrectlyForVersion2() {
    MealplanerData mealPlan = setupMealplanerDataWithAllIngredients();
    List<Meal> meals = fillListWithThreeMeals();

    loadFileWithName(RESOURCE_FILE_WITH_THREE_MEALS_V3);

    List<Meal> database = loadXml(mealPlan, DESTINATION_FILE_PATH, Kochplaner.registerPlugins());

    database.sort(Meal::compareTo);

    assertThat(database).containsExactlyElementsOf(meals);
  }

  @Test
  public void roundTripResultsInSameOutputAsInput() {
    MealplanerData mealPlan = setupMealplanerDataWithAllIngredients();
    List<Meal> meals = fillListWithThreeMeals();

    var pluginStore = Kochplaner.registerPlugins();

    saveXml(meals, DESTINATION_FILE_PATH, pluginStore);
    List<Meal> roundTripMeals = loadXml(mealPlan, DESTINATION_FILE_PATH, pluginStore);

    roundTripMeals.sort(Meal::compareTo);
    meals.sort(Meal::compareTo);
    assertThat(roundTripMeals).containsExactlyElementsOf(meals);
  }

  @Test
  public void roundTripWorksWithHiddenFacts() {
    List<Meal> meals = singletonList(MealBuilder.meal()
        .name("Test1")
        .daysPassed(ONE)
        .addFact(new TestMealFact(TestMealFact.TestEnum.TEST2))
        .addFact(new HiddenMealFact(HiddenMealFact.HiddenEnum.TEST2))
        .create());

    var pluginStore = new PluginStore();
    pluginStore.registerMealExtension(
        TestMealFact.class, TestMealFact.class, TestMealFact::new);
    pluginStore.registerMealExtension(
        HiddenMealFact.class, HiddenMealFact.class, HiddenMealFact::new);
    saveXml(meals, DESTINATION_FILE_PATH, pluginStore);

    var smallerPluginStore = new PluginStore();
    smallerPluginStore.registerMealExtension(
        TestMealFact.class, TestMealFact.class, TestMealFact::new);
    var savedMeals = loadXml(getInstance(smallerPluginStore), DESTINATION_FILE_PATH, smallerPluginStore);

    assertThat(savedMeals).hasSize(1);
    assertThat(savedMeals.get(0).getHiddenFacts()).hasSize(1);
    assertThat(savedMeals.get(0).getMealFacts()).containsKey(TestMealFact.class);

    saveXml(savedMeals, DESTINATION_FILE_PATH, smallerPluginStore);
    var reloadedMeals = loadXml(getInstance(pluginStore), DESTINATION_FILE_PATH, pluginStore);

    assertThat(reloadedMeals).containsExactlyElementsOf(meals);
  }

  private List<Meal> fillListWithThreeMeals() {
    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal1());
    meals.add(getMeal2());
    meals.add(getMeal3());
    meals.sort(Meal::compareTo);
    return meals;
  }
}
