package mealplaner.xml;

import static java.util.stream.Collectors.toList;
import static mealplaner.xml.adapters.IngredientAdapter.convertIngredientFromXml;
import static mealplaner.xml.adapters.MealAdapter.convertMealFromXml;
import static mealplaner.xml.adapters.ProposalAdapter.convertProposalFromXml;
import static mealplaner.xml.adapters.SettingsAdapter.convertDefaultSettingsFromXml;
import static mealplaner.xml.adapters.SettingsAdapter.convertDefaultSettingsFromXmlV1;
import static mealplaner.xml.util.JaxHelper.read;
import static mealplaner.xml.util.VersionControl.getVersion;

import java.util.List;

import mealplaner.MealplanerData;
import mealplaner.model.Meal;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.recipes.model.Ingredient;
import mealplaner.xml.model.v2.MealplanerDataXml;

// TODO: Cleanup
public final class MealplanerDataReader {
  private MealplanerDataReader() {
  }

  public static MealplanerData loadXml(String filePath) {
    int versionNumber = getVersion(filePath);
    if (versionNumber == 1) {
      mealplaner.xml.model.v1.MealplanerDataXml database = read(filePath,
          mealplaner.xml.model.v1.MealplanerDataXml.class);
      return convertToData(database);
    } else if (versionNumber == 2) {
      MealplanerDataXml database = read(filePath, MealplanerDataXml.class);
      return convertToData(database);
    } else {
      MealplanerData data = MealplanerData.getInstance();
      data.clear();
      return data;
    }
  }

  private static MealplanerData convertToData(MealplanerDataXml database) {
    DefaultSettings defaultSettings = convertDefaultSettingsFromXml(database.defaultSettings);
    MealplanerData data = MealplanerData.getInstance();

    List<Ingredient> ingredients = database.ingredients.stream()
        .map(ingredient -> convertIngredientFromXml(ingredient))
        .collect(toList());
    data.setIngredients(ingredients);

    List<Meal> modelMeals = database.meals.stream()
        .map(meal -> convertMealFromXml(data, meal))
        .collect(toList());
    data.setMeals(modelMeals);

    data.setTime(database.date);
    data.setDefaultSettings(defaultSettings);
    data.setLastProposal(convertProposalFromXml(data, database.proposal));
    return data;
  }

  private static MealplanerData convertToData(mealplaner.xml.model.v1.MealplanerDataXml database) {
    DefaultSettings defaultSettings = convertDefaultSettingsFromXmlV1(database.defaultSettings);
    MealplanerData data = MealplanerData.getInstance();

    List<Ingredient> ingredients = database.ingredients.stream()
        .map(ingredient -> convertIngredientFromXml(ingredient))
        .collect(toList());
    data.setIngredients(ingredients);

    List<Meal> modelMeals = database.meals.stream()
        .map(meal -> convertMealFromXml(data, meal))
        .collect(toList());
    data.setMeals(modelMeals);

    data.setTime(database.date);
    data.setDefaultSettings(defaultSettings);
    data.setLastProposal(convertProposalFromXml(data, database.proposal));
    return data;
  }
}