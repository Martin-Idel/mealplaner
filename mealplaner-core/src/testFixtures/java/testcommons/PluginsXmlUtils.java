// SPDX-License-Identifier: MIT

package testcommons;

import static java.time.DayOfWeek.MONDAY;
import static java.util.Collections.singletonList;
import static mealplaner.io.xml.MealsReader.loadXml;
import static mealplaner.io.xml.MealsWriter.saveXml;
import static mealplaner.model.MealplanerData.getInstance;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mealplaner.io.xml.MealplanerDataReader;
import mealplaner.io.xml.MealplanerDataWriter;
import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.PluginDescription;

public final class PluginsXmlUtils {
  private PluginsXmlUtils() {
  }

  public static void assertSaveAndReloadMealWorksCorrectly(Meal meal, String filepath, PluginDescription plugin) {
    var pluginStore = new PluginStore();
    plugin.registerPlugins(pluginStore);
    makeTemporaryDirectory(filepath);
    saveXml(singletonList(meal), filepath, pluginStore);

    var savedMeals = loadXml(getInstance(pluginStore), filepath, pluginStore);

    assertThat(savedMeals).hasSize(1);
    assertThat(savedMeals).containsExactlyInAnyOrder(meal);
  }

  public static void assertSaveAndReloadSettingWorksCorrectly(
      Settings settings, PluginDescription plugin, String filepath) {
    makeTemporaryDirectory(filepath);
    Map<DayOfWeek, Settings> defaultSettings = new HashMap<>();
    defaultSettings.put(MONDAY, settings);

    var pluginStore = new PluginStore();
    plugin.registerPlugins(pluginStore);
    LocalDate time = LocalDate.of(2017, 5, 3);
    Proposal proposal = Proposal.from(false, new ArrayList<>(), time);
    MealplanerData mealPlan = MealplanerData.getInstance(new PluginStore());
    mealPlan.setTime(time);
    mealPlan.setDefaultSettings(DefaultSettings.from(defaultSettings, new PluginStore()));
    mealPlan.setLastProposal(proposal);

    MealplanerDataWriter.saveXml(mealPlan, filepath, pluginStore);

    var reloadedProposal = MealplanerDataReader.loadXml(filepath, pluginStore);

    assertThat(reloadedProposal.getDefaultSettings().getDefaultSettingsMap().get(MONDAY)).isEqualTo(settings);
  }

  private static void makeTemporaryDirectory(String filepath) {
    var tempdir = new File(filepath);
    if (!tempdir.getParentFile().exists()) {
      var mkdirs = tempdir.getParentFile().mkdirs();
      if (!mkdirs) {
        fail("Could not create temporary directories");
      }
    }
  }
}
