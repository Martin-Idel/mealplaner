package mealplaner.xml;

import static java.util.stream.Collectors.toMap;
import static mealplaner.xml.JaxHelper.save;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import mealplaner.model.Meal;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.Recipe;
import mealplaner.xml.model.IngredientXml;
import mealplaner.xml.model.MealXml;
import mealplaner.xml.model.MealdatabaseXml;
import mealplaner.xml.model.RecipeXml;

public final class MealsWriter {
  private MealsWriter() {
  }

  public static void saveXml(List<Meal> data, String filePath) {
    MealdatabaseXml mealDataBase = convertDataBaseToXml(data);
    save(MealdatabaseXml.class, mealDataBase, filePath);
  }

  private static MealdatabaseXml convertDataBaseToXml(List<Meal> data) {
    List<MealXml> mealXmls = new ArrayList<>();
    data.stream()
        .map(meal -> new MealXml(
            meal.getName(),
            meal.getCookingTime(),
            meal.getSidedish(),
            meal.getObligatoryUtensil(),
            meal.getCookingPreference(),
            meal.getDaysPassed(),
            meal.getComment(),
            convertRecipeToXml(meal.getRecipe())))
        .forEach(mealXml -> mealXmls.add(mealXml));
    return new MealdatabaseXml(mealXmls, 1);
  }

  private static RecipeXml convertRecipeToXml(Optional<Recipe> optionalRecipe) {
    if (optionalRecipe.isPresent()) {
      Recipe recipe = optionalRecipe.get();
      Map<IngredientXml, Integer> integerMap = recipe.getIngredientsAsIs()
          .entrySet()
          .stream()
          .collect(toMap(e -> convertIngredientToXml(e.getKey()), e -> e.getValue().value));
      return new RecipeXml(recipe.getNumberOfPortions().value,
          integerMap);
    }
    return null;
  }

  private static IngredientXml convertIngredientToXml(Ingredient ingredient) {
    return new IngredientXml(ingredient.getName(), ingredient.getType(), ingredient.getMeasure());
  }
}
