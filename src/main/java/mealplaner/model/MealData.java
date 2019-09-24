// SPDX-License-Identifier: MIT

package mealplaner.model;

import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.meal.Meal.createMeal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.meal.Meal;
import mealplaner.model.meal.MealBuilder;
import mealplaner.model.meal.MealMetaData;
import mealplaner.model.proposal.ProposedMenu;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.Measure;
import mealplaner.model.recipes.QuantitativeIngredient;
import mealplaner.model.recipes.Recipe;

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
    meals.sort(Meal::compareTo);
    return meals;
  }

  public void setMeals(List<Meal> meals) {
    clear();
    updating = true;
    for (Meal meal : meals) {
      List<Ingredient> ingredients = data.getIngredients();
      meal.getRecipe().ifPresent(recipe -> recipe.getIngredientsAsIs()
          .stream()
          .filter(quantitativeIngredient -> !ingredients.contains(quantitativeIngredient.getIngredient()))
          .map(QuantitativeIngredient::getIngredient)
          .forEach(data::addIngredient));
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

  public void updateMeals(List<ProposedMenu> mealsCookedLast,
                          NonnegativeInteger daysSinceLastUpdate) {
    List<UUID> mealsCooked = mealsCookedLast.stream().map(menu -> menu.main).collect(toList());
    addDaysPassedToAllMeals(daysSinceLastUpdate);
    resetMealsCookedLast(mealsCookedLast, mealsCooked);
  }

  private void addDaysPassedToAllMeals(NonnegativeInteger daysSinceLastUpdate) {
    for (Entry<UUID, NonnegativeInteger> mealEntry : daysPassedSinceLastUpdate.entrySet()) {
      daysPassedSinceLastUpdate.compute(mealEntry.getKey(),
          (key, daysSinceUpdate) -> daysSinceUpdate.add(daysSinceLastUpdate));
    }
  }

  private void resetMealsCookedLast(List<ProposedMenu> mealsCookedLast, List<UUID> mealsCooked) {
    for (int i = 0; i < mealsCookedLast.size(); i++) {
      ProposedMenu menu = mealsCookedLast.get(i);
      NonnegativeInteger daysPassed = nonNegative(mealsCooked.size() - i - 1);
      menu.entry.ifPresent(entryMeal -> daysPassedSinceLastUpdate.put(entryMeal, daysPassed));
      daysPassedSinceLastUpdate.put(menu.main, daysPassed);
      menu.desert.ifPresent(entryMeal -> daysPassedSinceLastUpdate.put(entryMeal, daysPassed));
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
    List<QuantitativeIngredient> newIngredientsMap = oldRecipe.getIngredientsAsIs()
        .stream()
        .map(quantitativeIngredient -> QuantitativeIngredient.createQuantitativeIngredient(
            ingredients.get(quantitativeIngredient.getIngredient().getId()),
            setOldMeasureOrPrimaryMeasureIfOldMeasureIsNotPossible(ingredients, quantitativeIngredient),
            quantitativeIngredient.getAmount()))
        .collect(toList());
    return Recipe.from(oldRecipe.getNumberOfPortions(), newIngredientsMap);
  }

  private Measure setOldMeasureOrPrimaryMeasureIfOldMeasureIsNotPossible(
      Map<UUID, Ingredient> ingredients, QuantitativeIngredient quantitativeIngredient) {
    return ingredients.get(quantitativeIngredient.getIngredient().getId())
        .getMeasures()
        .contains(quantitativeIngredient.getMeasure())
        ? quantitativeIngredient.getMeasure()
        : ingredients.get(quantitativeIngredient.getIngredient().getId()).getPrimaryMeasure();
  }

  private void validateIngredients(Map<UUID, Ingredient> ingredients, Recipe oldRecipe) {
    for (QuantitativeIngredient ingredient : oldRecipe.getIngredientsAsIs()) {
      if (ingredients.get(ingredient.getIngredient().getId()) == null && !updating) {
        throw new MealException("An ingredient used is not contained in the ingredient list");
      }
    }
  }

  public Optional<Meal> getMeal(UUID uuid) {
    return ofNullable(metadata.get(uuid))
        .map(metadata -> {
          NonnegativeInteger daysSinceCooked = daysPassedSinceLastUpdate.get(uuid);
          Optional<Recipe> recipe = recipeData.get(uuid);
          return createMeal(uuid, metadata, daysSinceCooked, recipe);
        });
  }

  public boolean ingredientInUse(Ingredient ingredient) {
    return recipeData.values()
        .stream()
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(Recipe::getIngredientListAsIs)
        .flatMap(List::stream)
        .map(QuantitativeIngredient::getIngredient)
        .anyMatch(ingredient::equalIds);
  }

  public void replaceIngredient(Ingredient toBeReplaced, Ingredient replacingIngredient) {
    for (Entry<UUID, MealMetaData> mealMetaData : metadata.entrySet()) {
      recipeData.compute(mealMetaData.getKey(),
          (key, value) -> value
              .map(recipe -> replaceIngredientInRecipe(recipe, toBeReplaced, replacingIngredient)));
    }
  }

  private Recipe replaceIngredientInRecipe(Recipe oldRecipe, Ingredient toBeReplaced,
                                           Ingredient replacingIngredient) {
    List<QuantitativeIngredient> newIngredients = oldRecipe.getIngredientsAsIs();
    for (int i = 0; i < newIngredients.size(); ++i) {
      var quantitativeIngredient = newIngredients.get(i);
      if (quantitativeIngredient.getIngredient().equals(toBeReplaced)) {
        newIngredients.set(i, QuantitativeIngredient.createQuantitativeIngredient(
            replacingIngredient,
            replacingIngredient.getMeasures().contains(quantitativeIngredient.getMeasure())
                ? quantitativeIngredient.getMeasure()
                : replacingIngredient.getPrimaryMeasure(),
            quantitativeIngredient.getAmount()
        ));
      }
    }
    return Recipe.from(oldRecipe.getNumberOfPortions(), newIngredients);
  }
}
