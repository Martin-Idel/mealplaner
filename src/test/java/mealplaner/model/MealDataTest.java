package mealplaner.model;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static mealplaner.commons.NonnegativeFraction.wholeNumber;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.Meal.createMeal;
import static mealplaner.model.MealData.createData;
import static mealplaner.model.ProposedMenu.entryAndMain;
import static mealplaner.model.ProposedMenu.mainAndDesert;
import static mealplaner.model.ProposedMenu.mainOnly;
import static mealplaner.recipes.model.Ingredient.ingredient;
import static mealplaner.recipes.model.Ingredient.ingredientWithUuid;
import static mealplaner.recipes.model.IngredientType.BAKING_GOODS;
import static mealplaner.recipes.model.Measure.GRAM;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getIngredient1;
import static testcommons.CommonFunctions.getIngredient2;
import static testcommons.CommonFunctions.getIngredient3;
import static testcommons.CommonFunctions.getRecipe1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mealplaner.MealplanerData;
import mealplaner.commons.NonnegativeFraction;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.CourseType;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.Recipe;

public class MealDataTest {
  private Meal meal1;
  private Meal meal2;
  private Meal meal3;
  private final List<Meal> meals = new ArrayList<>();
  private MealplanerData data;
  private MealData sut;

  @Before
  public void setUp() {
    data = MealplanerData.getInstance();
    data.clear();
    meals.clear();
  }

  @After
  public void tearDown() {
    data.deregister(sut);
  }

  @Test
  public void putAndGetMealsDoesNotChangeMealsWithoutRecipes() {
    addInitializedMeals();
    MealplanerData data = MealplanerData.getInstance();
    sut = createData(data);

    sut.setMeals(meals);

    List<Meal> mealsList = sut.getMealsInList();

    assertThat(mealsList).containsExactly(meal1, meal2, meal3);
  }

  @Test
  public void putAndGetMealWithRecipeDoesNotChangeRecipe() {
    meal1 = createMeal(randomUUID(), "Meal1", CookingTime.SHORT, Sidedish.NONE,
        ObligatoryUtensil.PAN, CookingPreference.NO_PREFERENCE, CourseType.MAIN, nonNegative(50),
        "", of(getRecipe1()));
    meals.add(meal1);
    MealplanerData data = MealplanerData.getInstance();
    sut = MealData.createData(data);

    sut.setMeals(meals);

    List<Meal> mealsList = sut.getMealsInList();

    assertThat(mealsList).containsExactly(meal1);
  }

  @Test
  public void addingMealAddsAllIngredients() {
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
  public void addedIngredientsDoNotChangeRecipe() {
    setupOneMeal(getIngredient1(), getIngredient2());

    sut = MealData.createData(data);
    sut.setMeals(meals);

    data.addIngredient(getIngredient3());

    List<Meal> mealsList = sut.getMealsInList();

    assertThat(mealsList).containsExactly(meal1);
  }

  @Test
  public void updatingIngredientsInMealWorksCorrectly() {
    List<Ingredient> ingredients = new ArrayList<>();
    Ingredient ingredient1 = getIngredient1();
    Ingredient ingredient2 = getIngredient2();
    ingredients.add(ingredient1);
    ingredients.add(ingredient2);
    setupOneMeal(ingredient1, ingredient2);
    data.setIngredients(ingredients);

    sut = MealData.createData(data);
    sut.setMeals(meals);

    Ingredient changedIngredient = ingredientWithUuid(ingredient2.getId(), "Test3",
        BAKING_GOODS, GRAM);

    ingredients.set(1, changedIngredient);

    data.setIngredients(ingredients);

    List<Meal> mealsList = sut.getMealsInList();

    Meal changedMeal = createMeal(meal1.getId(), "Meal1", CookingTime.SHORT, Sidedish.NONE,
        ObligatoryUtensil.PAN, CookingPreference.NO_PREFERENCE, CourseType.MAIN, nonNegative(50),
        "", of(getRecipe(ingredient1, changedIngredient)));

    assertThat(mealsList).containsExactly(changedMeal);
  }

  @Test(expected = MealException.class)
  public void throwsExceptionIfSomethingWentWrongWithIngredients() {
    List<Ingredient> ingredients = new ArrayList<>();
    Ingredient ingredient1 = getIngredient1();
    Ingredient ingredient2 = getIngredient2();
    ingredients.add(ingredient1);
    ingredients.add(ingredient2);
    setupOneMeal(ingredient1, ingredient2);
    data.setIngredients(ingredients);

    sut = MealData.createData(data);
    sut.setMeals(meals);

    Ingredient changedIngredient = ingredient("Test3", BAKING_GOODS, GRAM);

    ingredients.set(1, changedIngredient); // This removes the second ingredient, needed in recipe

    data.setIngredients(ingredients);
  }

  @Test
  public void updateAddsCorrectNumberOfDays() {
    addInitializedMeals();
    MealplanerData data = MealplanerData.getInstance();
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
  public void updateAddsCorrectNumberOfDaysIncludingEntry() {
    addInitializedMeals();
    MealplanerData data = MealplanerData.getInstance();
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
  public void updateAddsCorrectNumberOfDaysIncludingDesert() {
    addInitializedMeals();
    MealplanerData data = MealplanerData.getInstance();
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

  private void setupOneMeal(Ingredient ingredient1, Ingredient ingredient2) {
    meal1 = createMeal(randomUUID(), "Meal1", CookingTime.SHORT, Sidedish.NONE,
        ObligatoryUtensil.PAN, CookingPreference.NO_PREFERENCE, CourseType.MAIN, nonNegative(50),
        "", of(getRecipe(ingredient1, ingredient2)));
    meals.add(meal1);
  }

  private static Recipe getRecipe(Ingredient ingredient1, Ingredient ingredient2) {
    Map<Ingredient, NonnegativeFraction> ingredients = new HashMap<>();
    ingredients.put(ingredient1, wholeNumber(nonNegative(100)));
    ingredients.put(ingredient2, wholeNumber(nonNegative(200)));
    return Recipe.from(nonNegative(2), ingredients);
  }

  private void addInitializedMeals() throws MealException {
    meal1 = createMeal(randomUUID(), "Meal1", CookingTime.SHORT, Sidedish.NONE,
        ObligatoryUtensil.PAN, CookingPreference.NO_PREFERENCE, CourseType.MAIN, nonNegative(50),
        "", empty());
    meals.add(meal1);
    meal2 = createMeal(randomUUID(), "Meal2", CookingTime.MEDIUM, Sidedish.PASTA,
        ObligatoryUtensil.CASSEROLE, CookingPreference.RARE, CourseType.MAIN, nonNegative(101), "",
        empty());
    meals.add(meal2);
    meal3 = createMeal(randomUUID(), "Meal4", CookingTime.LONG, Sidedish.RICE,
        ObligatoryUtensil.POT, CookingPreference.VERY_POPULAR, CourseType.MAIN, nonNegative(20), "",
        empty());
    meals.add(meal3);
  }
}
