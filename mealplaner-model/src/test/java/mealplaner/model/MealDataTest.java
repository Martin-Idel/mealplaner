// SPDX-License-Identifier: MIT

package mealplaner.model;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.of;
import static java.util.UUID.nameUUIDFromBytes;
import static mealplaner.commons.NonnegativeFraction.fraction;
import static mealplaner.commons.NonnegativeFraction.wholeNumber;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.MealData.createData;
import static mealplaner.model.meal.MealBuilder.from;
import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.model.proposal.ProposedMenu.entryAndMain;
import static mealplaner.model.proposal.ProposedMenu.mainAndDesert;
import static mealplaner.model.proposal.ProposedMenu.mainOnly;
import static mealplaner.model.recipes.IngredientBuilder.ingredient;
import static mealplaner.model.recipes.IngredientType.BAKING_GOODS;
import static mealplaner.model.recipes.IngredientType.MEAT_PRODUCTS;
import static mealplaner.model.recipes.Measure.GRAM;
import static mealplaner.model.recipes.Measure.TEASPOON;
import static mealplaner.model.recipes.Measures.createMeasures;
import static mealplaner.model.recipes.QuantitativeIngredient.createQuantitativeIngredient;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static testcommonsmodel.CommonBaseFunctions.getIngredient1;
import static testcommonsmodel.CommonBaseFunctions.getIngredient2;
import static testcommonsmodel.CommonBaseFunctions.getIngredient3;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.meal.Meal;
import mealplaner.model.meal.MealBuilder;
import mealplaner.model.proposal.ProposedMenu;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.Measure;
import mealplaner.model.recipes.QuantitativeIngredient;
import mealplaner.model.recipes.Recipe;
import mealplaner.plugins.PluginStore;
import testcommonsmodel.CommonBaseFunctions;

class MealDataTest {
  private Meal meal1;
  private Meal meal2;
  private Meal meal3;
  private final List<Meal> meals = new ArrayList<>();
  private MealplanerData data;
  private MealData sut;
  private final PluginStore pluginStore = new PluginStore();

  @BeforeEach
  void setUp() {
    data = MealplanerData.getInstance(pluginStore);
    data.clear();
    meals.clear();
  }

  @AfterEach
  void tearDown() {
    data.deregister(sut);
  }

  @Test
  void putAndGetMealsDoesNotChangeMealsWithoutRecipes() {
    addInitializedMeals();
    MealplanerData data = MealplanerData.getInstance(pluginStore);
    sut = createData(data);

    sut.setMeals(meals);

    List<Meal> mealsList = sut.getMealsInList();

    assertThat(mealsList).containsExactly(meal1, meal2, meal3);
  }

  @Test
  void putAndGetMealWithRecipeDoesNotChangeRecipe() {
    meal1 = meal()
        .name("Meal1")
        .addDaysPassed(nonNegative(50))
        .create();
    meals.add(meal1);
    MealplanerData data = MealplanerData.getInstance(pluginStore);
    sut = MealData.createData(data);

    sut.setMeals(meals);

    List<Meal> mealsList = sut.getMealsInList();

    assertThat(mealsList).containsExactly(meal1);
  }

  @Test
  void getMealReturnsCorrectMealIfItCanBeFound() {
    addInitializedMeals();
    MealplanerData data = MealplanerData.getInstance(pluginStore);
    sut = MealData.createData(data);

    sut.setMeals(meals);

    var meal = sut.getMeal(meal1.getId());

    assertThat(meal)
        .isPresent()
        .contains(meal1);
    assertThat(sut.getMeal(CommonBaseFunctions.getMeal3().getId())).isEmpty();
  }

  @Test
  void addingMealAddsAllIngredients() {
    Ingredient ingredient1 = getIngredient1();
    Ingredient ingredient2 = getIngredient2();
    setupOneMeal(ingredient1, ingredient2);
    sut = MealData.createData(data);

    sut.setMeals(meals);

    List<Meal> mealsList = sut.getMealsInList();
    assertThat(mealsList).containsExactly(meal1);

    List<Ingredient> ingredients = data.getIngredients();
    assertThat(ingredients).containsExactly(ingredient1, ingredient2);
  }

  @Test
  void addedIngredientsDoNotChangeRecipe() {
    setupOneMeal(getIngredient1(), getIngredient2());

    sut = MealData.createData(data);
    sut.setMeals(meals);

    data.addIngredient(getIngredient3());

    List<Meal> mealsList = sut.getMealsInList();

    assertThat(mealsList).containsExactly(meal1);
  }

  @Test
  void updatingIngredientsInMealWorksCorrectly() {
    List<Ingredient> ingredients = new ArrayList<>();
    Ingredient ingredient1 = getIngredient1();
    Ingredient ingredient2 = getIngredient2();
    ingredients.add(ingredient1);
    ingredients.add(ingredient2);
    setupOneMeal(ingredient1, ingredient2);
    data.setIngredients(ingredients);

    sut = MealData.createData(data);
    sut.setMeals(meals);

    Ingredient changedIngredient = ingredient()
        .withUuid(ingredient2.getId())
        .withName("Test3")
        .withType(BAKING_GOODS)
        .withMeasures(createMeasures(GRAM))
        .create();

    ingredients.set(1, changedIngredient);

    data.setIngredients(ingredients);

    List<Meal> mealsList = sut.getMealsInList();

    Meal changedMeal = from(meal1)
        .optionalRecipe(of(getRecipe(ingredient1, changedIngredient)))
        .create();

    assertThat(mealsList).containsExactly(changedMeal);
  }

  @Test
  void throwsExceptionIfSomethingWentWrongWithIngredients() {
    List<Ingredient> ingredients = new ArrayList<>();
    Ingredient ingredient1 = getIngredient1();
    Ingredient ingredient2 = getIngredient2();
    ingredients.add(ingredient1);
    ingredients.add(ingredient2);
    setupOneMeal(ingredient1, ingredient2);
    data.setIngredients(ingredients);

    sut = MealData.createData(data);
    sut.setMeals(meals);

    Ingredient changedIngredient = ingredient()
        .withName("Test3")
        .withType(BAKING_GOODS)
        .withMeasures(createMeasures(GRAM))
        .create();

    ingredients.set(1, changedIngredient); // This removes the second ingredient, needed in recipe

    assertThrows(MealException.class, () -> data.setIngredients(ingredients));
  }

  @Test
  void updateAddsCorrectNumberOfDays() {
    addInitializedMeals();
    MealplanerData data = MealplanerData.getInstance(pluginStore);
    sut = createData(data);
    sut.setMeals(meals);
    List<ProposedMenu> updatedList = new ArrayList<>();
    updatedList.add(mainOnly(meal3.getId(), nonNegative(2))); // cooked first
    updatedList.add(mainOnly(meal1.getId(), nonNegative(2))); // cooked later

    sut.updateMeals(updatedList, nonNegative(5));

    List<Meal> mealsList = sut.getMealsInList();

    Meal updatedMeal1 = MealBuilder.from(meal1).daysPassed(nonNegative(0)).create();
    Meal updatedMeal2 = MealBuilder.from(meal2).daysPassed(nonNegative(106)).create();
    Meal updatedMeal3 = MealBuilder.from(meal3).daysPassed(nonNegative(1)).create();
    assertThat(mealsList).containsExactly(updatedMeal1, updatedMeal2, updatedMeal3);
  }

  @Test
  void updateAddsCorrectNumberOfDaysIncludingEntry() {
    addInitializedMeals();
    MealplanerData data = MealplanerData.getInstance(pluginStore);
    sut = createData(data);
    sut.setMeals(meals);
    List<ProposedMenu> updatedList = new ArrayList<>();
    updatedList.add(entryAndMain(meal2.getId(), meal3.getId(), nonNegative(2))); // cooked first
    updatedList.add(mainOnly(meal1.getId(), nonNegative(2))); // cooked later

    sut.updateMeals(updatedList, nonNegative(5));

    List<Meal> mealsList = sut.getMealsInList();

    Meal updatedMeal1 = MealBuilder.from(meal1).daysPassed(nonNegative(0)).create();
    Meal updatedMeal2 = MealBuilder.from(meal2).daysPassed(nonNegative(1)).create();
    Meal updatedMeal3 = MealBuilder.from(meal3).daysPassed(nonNegative(1)).create();
    assertThat(mealsList).containsExactly(updatedMeal1, updatedMeal2, updatedMeal3);
  }

  @Test
  void updateAddsCorrectNumberOfDaysIncludingDesert() {
    addInitializedMeals();
    MealplanerData data = MealplanerData.getInstance(pluginStore);
    sut = createData(data);
    sut.setMeals(meals);
    List<ProposedMenu> updatedList = new ArrayList<>();
    updatedList.add(mainOnly(meal2.getId(), nonNegative(2))); // cooked first
    updatedList.add(mainAndDesert(meal1.getId(), meal3.getId(), nonNegative(2))); // cooked later

    sut.updateMeals(updatedList, nonNegative(5));

    List<Meal> mealsList = sut.getMealsInList();

    Meal updatedMeal1 = MealBuilder.from(meal1).daysPassed(nonNegative(0)).create();
    Meal updatedMeal2 = MealBuilder.from(meal2).daysPassed(nonNegative(1)).create();
    Meal updatedMeal3 = MealBuilder.from(meal3).daysPassed(nonNegative(0)).create();
    assertThat(mealsList).containsExactly(updatedMeal1, updatedMeal2, updatedMeal3);
  }

  @Test
  void ingredientInUseIdentifiesUsedIngredientId() {
    setupOneMeal(getIngredient1(), getIngredient2());
    MealplanerData data = MealplanerData.getInstance(pluginStore);
    sut = createData(data);
    sut.setMeals(meals);

    assertThat(sut.ingredientInUse(getIngredient2())).isTrue();
    assertThat(sut.ingredientInUse(getIngredient3())).isFalse();
  }

  @Test
  void replaceIngredientReplacesIngredientIfPresent() {
    setupOneMeal(getIngredient1(), getIngredient2());
    MealplanerData data = MealplanerData.getInstance(pluginStore);
    sut = createData(data);
    sut.setMeals(meals);

    sut.replaceIngredient(getIngredient2(), getIngredient3());

    assertThat(sut.ingredientInUse(getIngredient2())).isFalse();
    assertThat(sut.ingredientInUse(getIngredient3())).isTrue();

    assertThat(sut.getMealsInList().get(0).getRecipe())
        .contains(getRecipe(getIngredient1(), getIngredient3()));
  }

  @Test
  void replaceIngredientReplacesIngredientEvenForSameIngredients() {
    setupOneMeal(getIngredient1(), getIngredient2());
    MealplanerData data = MealplanerData.getInstance(pluginStore);
    sut = createData(data);
    sut.setMeals(meals);

    sut.replaceIngredient(getIngredient2(), getIngredient1());

    List<QuantitativeIngredient> ingredients = new ArrayList<>();
    ingredients.add(createQuantitativeIngredient(getIngredient1(), wholeNumber(nonNegative(100))));
    ingredients.add(createQuantitativeIngredient(getIngredient1(), wholeNumber(nonNegative(200))));
    Recipe recipe = Recipe.from(nonNegative(2), ingredients);

    assertThat(sut.getMealsInList().get(0).getRecipe())
        .contains(recipe);
  }

  @Test
  void replaceIngredientDoesNothingIfIngredientIsAbsent() {
    setupOneMeal(getIngredient1(), getIngredient3());
    MealplanerData data = MealplanerData.getInstance(pluginStore);
    sut = createData(data);
    sut.setMeals(meals);

    assertThat(sut.ingredientInUse(getIngredient2())).isFalse();
    assertThat(sut.ingredientInUse(getIngredient3())).isTrue();

    assertThat(sut.getMealsInList().get(0).getRecipe())
        .contains(getRecipe(getIngredient1(), getIngredient3()));

    sut.replaceIngredient(getIngredient2(), getIngredient3());

    assertThat(sut.ingredientInUse(getIngredient2())).isFalse();
    assertThat(sut.ingredientInUse(getIngredient3())).isTrue();

    assertThat(sut.getMealsInList().get(0).getRecipe())
        .contains(getRecipe(getIngredient1(), getIngredient3()));
  }

  @Test
  void replaceIngredientKeepsMeasureIfReplacingIngredientHasSameMeasure() {
    var secondaries = new EnumMap<Measure, NonnegativeFraction>(Measure.class);
    secondaries.put(Measure.GRAM, fraction(1, 2));
    setupOneMeal(getIngredient1(), getIngredient3());
    MealplanerData data = MealplanerData.getInstance(pluginStore);
    sut = createData(data);
    sut.setMeals(meals);

    var ingredient4 = ingredient()
        .withUuid(nameUUIDFromBytes("Test4".getBytes(UTF_8)))
        .withName("Test4")
        .withType(MEAT_PRODUCTS)
        .withMeasures(createMeasures(TEASPOON, secondaries))
        .create();

    sut.replaceIngredient(getIngredient3(), ingredient4);

    assertThat(sut.ingredientInUse(getIngredient3())).isFalse();
    assertThat(sut.ingredientInUse(ingredient4)).isTrue();

    // Ingredient 4 has GRAM within its secondaries. So keep it
    List<QuantitativeIngredient> ingredients = new ArrayList<>();
    ingredients.add(createQuantitativeIngredient(
        getIngredient1(), wholeNumber(nonNegative(100))));
    ingredients.add(createQuantitativeIngredient(
        ingredient4, GRAM, wholeNumber(nonNegative(200))));
    assertThat(sut.getMealsInList().get(0).getRecipe())
        .contains(Recipe.from(nonNegative(2), ingredients));
  }

  private void setupOneMeal(Ingredient ingredient1, Ingredient ingredient2) {
    meal1 = meal()
        .name("Meal1")
        .addDaysPassed(nonNegative(50))
        .optionalRecipe(of(getRecipe(ingredient1, ingredient2)))
        .create();
    meals.add(meal1);
  }

  private static Recipe getRecipe(Ingredient ingredient1, Ingredient ingredient2) {
    List<QuantitativeIngredient> ingredients = new ArrayList<>();
    ingredients.add(createQuantitativeIngredient(
        ingredient1, wholeNumber(nonNegative(100))));
    ingredients.add(createQuantitativeIngredient(
        ingredient2, wholeNumber(nonNegative(200))));
    return Recipe.from(nonNegative(2), ingredients);
  }

  private void addInitializedMeals() throws MealException {
    meal1 = meal()
        .name("Meal1")
        .addDaysPassed(nonNegative(50))
        .create();
    meals.add(meal1);
    meal2 = meal()
        .name("Meal2")
        .addDaysPassed(nonNegative(101))
        .create();
    meals.add(meal2);
    meal3 = meal()
        .name("Meal4")
        .addDaysPassed(nonNegative(20))
        .create();
    meals.add(meal3);
  }
}
