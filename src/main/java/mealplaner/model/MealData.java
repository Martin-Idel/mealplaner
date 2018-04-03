package mealplaner.model;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static mealplaner.commons.NonnegativeInteger.nonNegative;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import mealplaner.DataStoreEventType;
import mealplaner.DataStoreListener;
import mealplaner.MealplanerData;
import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.Recipe;

public final class MealData implements DataStoreListener {
  private final MealplanerData data;
  private final Map<UUID, MealMetaData> metadata;
  private final Map<UUID, NonnegativeInteger> daysPassedSinceLastUpdate;
  private final Map<UUID, Optional<Recipe>> recipeData;
  private boolean updating;

  private MealData(MealplanerData data) {
    this.data = data;
    this.data.register(this);
    metadata = new HashMap<>();
    daysPassedSinceLastUpdate = new HashMap<>();
    recipeData = new HashMap<>();
  }

  public static MealData createData(MealplanerData data) {
    return new MealData(data);
  }

  public List<Meal> getMealsInList() {
    List<Meal> meals = new ArrayList<>();
    for (Entry<UUID, MealMetaData> entry : metadata.entrySet()) {
      UUID id = entry.getKey();
      MealMetaData data = entry.getValue();
      NonnegativeInteger daysSinceCooked = daysPassedSinceLastUpdate.get(id);
      Optional<Recipe> recipe = recipeData.get(id);
      Meal meal = Meal.createMeal(id, data, daysSinceCooked, recipe);
      meals.add(meal);
    }
    meals.sort((meal1, meal2) -> meal1.compareTo(meal2));
    return meals;
  }

  public void setMeals(List<Meal> meals) {
    clear();
    updating = true;
    for (Meal meal : meals) {
      List<Ingredient> ingredients = data.getIngredients();
      meal.getRecipe().ifPresent(recipe -> recipe.getIngredientsAsIs().keySet()
          .stream()
          .filter(ingredient -> !ingredients.contains(ingredient))
          .forEach(ingredient -> data.addIngredient(ingredient)));
      addMeal(meal);
    }
    updating = false;
  }

  public void addMeal(Meal meal) {
    metadata.put(meal.getId(), meal.getMetaData());
    daysPassedSinceLastUpdate.put(meal.getId(), meal.getDaysPassed());
    recipeData.put(meal.getId(), meal.getRecipe());
  }

  public void clear() {
    metadata.clear();
    daysPassedSinceLastUpdate.clear();
    recipeData.clear();
  }

  public void updateMeals(List<Meal> mealsCookedLast, NonnegativeInteger daysSinceLastUpdate) {
    List<UUID> mealsCooked = mealsCookedLast.stream().map(Meal::getId).collect(toList());
    for (Entry<UUID, NonnegativeInteger> mealEntry : daysPassedSinceLastUpdate.entrySet()) {
      daysPassedSinceLastUpdate.compute(mealEntry.getKey(),
          (key, daysSinceUpdate) -> mealsCooked.contains(key)
              ? nonNegative(mealsCooked.size() - mealsCooked.indexOf(key) - 1)
              : daysSinceUpdate.add(daysSinceLastUpdate));
    }
  }

  @Override
  public void updateData(DataStoreEventType event) {
    if (event.equals(DataStoreEventType.INGREDIENTS_CHANGED)) {
      recomputeRecipes();
    }
  }

  private void recomputeRecipes() {
    Map<UUID, Ingredient> ingredients = data.getIngredients().stream()
        .collect(toMap(Ingredient::getId, identity()));
    for (Entry<UUID, MealMetaData> mealMetaData : metadata.entrySet()) {
      recipeData.compute(mealMetaData.getKey(),
          (key, value) -> value.map(recipe -> recomputeRecipe(ingredients, recipe)));
    }
  }

  private Recipe recomputeRecipe(Map<UUID, Ingredient> ingredients, Recipe oldRecipe) {
    validateIngredients(ingredients, oldRecipe);
    Map<Ingredient, NonnegativeInteger> newIngredientsMap = oldRecipe.getIngredientsAsIs()
        .entrySet()
        .stream()
        .collect(toMap(entry -> ingredients.get(entry.getKey().getId()), Entry::getValue));
    return Recipe.from(oldRecipe.getNumberOfPortions(), newIngredientsMap);
  }

  private void validateIngredients(Map<UUID, Ingredient> ingredients, Recipe oldRecipe) {
    for (Ingredient ingredient : oldRecipe.getIngredientsAsIs().keySet()) {
      if (ingredients.get(ingredient.getId()) == null && !updating) {
        throw new MealException("An ingredient used is not contained in the ingredient list");
      }
    }
  }
}
