// SPDX-License-Identifier: MIT

package mealplaner.io.xml;

import static java.util.stream.Collectors.toList;
import static mealplaner.io.xml.util.JaxHelper.save;

import java.util.List;

import mealplaner.plugins.PluginStore;
import mealplaner.io.xml.adapters.MealAdapter;
import mealplaner.io.xml.model.v3.MealXml;
import mealplaner.io.xml.model.v3.MealdatabaseXml;
import mealplaner.model.meal.Meal;

public final class MealsWriter {
  private MealsWriter() {
  }

  public static void saveXml(List<Meal> data, String filePath, PluginStore knownPlugins) {
    MealdatabaseXml mealDataBase = convertDataBaseToXml(data);
    save(filePath, MealdatabaseXml.class, mealDataBase, knownPlugins);
  }

  private static MealdatabaseXml convertDataBaseToXml(List<Meal> data) {
    List<MealXml> mealXmls = data.stream()
        .map(MealAdapter::convertMealV3ToXml)
        .collect(toList());
    return new MealdatabaseXml(mealXmls);
  }
}
