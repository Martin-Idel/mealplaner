package mealplaner.io.xml;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.singletonList;
import static java.util.UUID.nameUUIDFromBytes;
import static mealplaner.commons.NonnegativeInteger.ONE;
import static mealplaner.io.xml.MealsReader.loadXml;
import static mealplaner.io.xml.MealsWriter.saveXml;
import static mealplaner.model.MealplanerData.getInstance;
import static mealplaner.model.meal.MealBuilder.meal;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.TestMealFact.TestEnum.TEST1;

import java.util.List;

import org.junit.Test;

import mealplaner.model.meal.Meal;
import mealplaner.plugins.PluginStore;
import testcommons.HiddenMealFact;
import testcommons.TestMealFact;
import testcommons.XmlInteraction;

public class MealFactXmlInteractionTest extends XmlInteraction {
  @Test
  public void roundTripWorksWithHiddenFacts() {
    List<Meal> meals = singletonList(meal()
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

  @Test
  public void roundTripInitialisesDefaultsCorrectly() {
    Meal meal = meal()
        .id(nameUUIDFromBytes("Test1Meal".getBytes(UTF_8)))
        .name("Test1")
        .daysPassed(ONE)
        .create();

    Meal expected = meal()
        .id(nameUUIDFromBytes("Test1Meal".getBytes(UTF_8)))
        .name("Test1")
        .daysPassed(ONE)
        .addFact(new TestMealFact(TEST1))
        .create();

    var pluginStore = new PluginStore();
    pluginStore.registerMealExtension(
        TestMealFact.class, TestMealFact.class, TestMealFact::new);
    saveXml(singletonList(meal), DESTINATION_FILE_PATH, pluginStore);

    var reloadedMeals = loadXml(getInstance(pluginStore), DESTINATION_FILE_PATH, pluginStore);

    assertThat(reloadedMeals).containsExactlyInAnyOrder(expected);
  }
}
