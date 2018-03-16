package mealplaner.xml;

import static java.util.stream.Collectors.toList;
import static mealplaner.xml.adapters.MealAdapter.convertMealToXml;
import static mealplaner.xml.util.JaxHelper.save;

import java.util.List;

import mealplaner.model.Meal;
import mealplaner.xml.model.MealXml;
import mealplaner.xml.model.MealdatabaseXml;

public final class MealsWriter {
  private MealsWriter() {
  }

  public static void saveXml(List<Meal> data, String filePath) {
    MealdatabaseXml mealDataBase = convertDataBaseToXml(data);
    save(MealdatabaseXml.class, mealDataBase, filePath);
  }

  private static MealdatabaseXml convertDataBaseToXml(List<Meal> data) {
    List<MealXml> mealXmls = data.stream()
        .map(meal -> convertMealToXml(meal))
        .collect(toList());
    return new MealdatabaseXml(mealXmls, 1);
  }
}
