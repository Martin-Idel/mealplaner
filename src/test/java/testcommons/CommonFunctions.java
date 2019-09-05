// SPDX-License-Identifier: MIT

package testcommons;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.nameUUIDFromBytes;
import static mealplaner.commons.NonnegativeFraction.fraction;
import static mealplaner.commons.NonnegativeFraction.wholeNumber;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.MealplanerData.getInstance;
import static mealplaner.model.meal.Meal.createMeal;
import static mealplaner.model.proposal.Proposal.from;
import static mealplaner.model.proposal.ProposedMenu.mainOnly;
import static mealplaner.model.recipes.Ingredient.ingredientWithUuid;
import static mealplaner.model.recipes.Measures.createMeasures;
import static mealplaner.model.recipes.QuantitativeIngredient.createQuantitativeIngredient;
import static mealplaner.model.settings.Settings.from;
import static mealplaner.model.settings.subsettings.CookingTimeSetting.cookingTimeWithProhibited;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;
import mealplaner.model.meal.enums.CookingPreference;
import mealplaner.model.meal.enums.CookingTime;
import mealplaner.model.meal.enums.CourseType;
import mealplaner.model.meal.enums.ObligatoryUtensil;
import mealplaner.model.meal.enums.Sidedish;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.proposal.ProposedMenu;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.IngredientType;
import mealplaner.model.recipes.Measure;
import mealplaner.model.recipes.QuantitativeIngredient;
import mealplaner.model.recipes.Recipe;
import mealplaner.model.settings.Settings;
import mealplaner.model.settings.enums.CasseroleSettings;
import mealplaner.model.settings.enums.CourseSettings;
import mealplaner.model.settings.enums.PreferenceSettings;

public final class CommonFunctions {
  private CommonFunctions() {
  }

  public static Meal getMeal1() {
    return createMeal(nameUUIDFromBytes("Test1Meal".getBytes(StandardCharsets.UTF_8)),
        "Test1",
        CookingTime.SHORT,
        Sidedish.PASTA,
        ObligatoryUtensil.PAN,
        CookingPreference.VERY_POPULAR,
        CourseType.MAIN,
        nonNegative(5),
        "no comment",
        empty());
  }

  public static Meal getMeal2() {
    return createMeal(nameUUIDFromBytes("Test2Meal".getBytes(StandardCharsets.UTF_8)),
        "Test2",
        CookingTime.SHORT,
        Sidedish.NONE,
        ObligatoryUtensil.POT,
        CookingPreference.NO_PREFERENCE,
        CourseType.MAIN,
        nonNegative(1),
        "",
        of(getRecipe1()));
  }

  public static Meal getMeal3() {
    return createMeal(nameUUIDFromBytes("Test3Meal".getBytes(StandardCharsets.UTF_8)),
        "Test3",
        CookingTime.MEDIUM,
        Sidedish.RICE,
        ObligatoryUtensil.POT,
        CookingPreference.NO_PREFERENCE,
        CourseType.MAIN,
        nonNegative(2),
        "",
        of(getRecipe2()));
  }

  public static Meal getMealEntry() {
    return createMeal(nameUUIDFromBytes("Test4Meal".getBytes(StandardCharsets.UTF_8)),
        "Test4",
        CookingTime.LONG,
        Sidedish.RICE,
        ObligatoryUtensil.PAN,
        CookingPreference.NO_PREFERENCE,
        CourseType.ENTRY,
        nonNegative(2),
        "",
        of(getRecipe2()));
  }

  public static Meal getMealDesert() {
    return createMeal(nameUUIDFromBytes("Test5Meal".getBytes(StandardCharsets.UTF_8)),
        "Test5",
        CookingTime.VERY_SHORT,
        Sidedish.RICE,
        ObligatoryUtensil.POT,
        CookingPreference.VERY_POPULAR,
        CourseType.DESERT,
        nonNegative(2),
        "",
        of(getRecipe2()));
  }

  public static Meal getMealEntry2() {
    return createMeal(nameUUIDFromBytes("Test6Meal".getBytes(StandardCharsets.UTF_8)),
        "Test6",
        CookingTime.VERY_SHORT,
        Sidedish.NONE,
        ObligatoryUtensil.CASSEROLE,
        CookingPreference.RARE,
        CourseType.ENTRY,
        nonNegative(2),
        "",
        empty());
  }

  public static Recipe getRecipe1() {
    var ingredients = new ArrayList<QuantitativeIngredient>();
    ingredients.add(createQuantitativeIngredient(
        getIngredient1(), getIngredient1().getPrimaryMeasure(), wholeNumber(nonNegative(100))));
    ingredients.add(createQuantitativeIngredient(
        getIngredient2(), getIngredient2().getPrimaryMeasure(), wholeNumber(nonNegative(200))));
    return Recipe.from(nonNegative(2), ingredients);
  }

  public static Recipe getRecipe2() {
    var ingredients = new ArrayList<QuantitativeIngredient>();
    ingredients.add(createQuantitativeIngredient(
        getIngredient1(), getIngredient1().getPrimaryMeasure(), wholeNumber(nonNegative(100))));
    ingredients.add(createQuantitativeIngredient(
        getIngredient3(), getIngredient3().getPrimaryMeasure(), wholeNumber(nonNegative(400))));
    return Recipe.from(nonNegative(4), ingredients);
  }

  public static Recipe getRecipe3() {
    var ingredients = new ArrayList<QuantitativeIngredient>();
    ingredients.add(createQuantitativeIngredient(
        getIngredient1(), getIngredient1().getPrimaryMeasure(), wholeNumber(nonNegative(100))));
    ingredients.add(createQuantitativeIngredient(
        getIngredient2(), getIngredient2().getPrimaryMeasure(), wholeNumber(nonNegative(50))));
    return Recipe.from(nonNegative(1), ingredients);
  }

  public static Ingredient getIngredient1() {
    return ingredientWithUuid(nameUUIDFromBytes("Test1".getBytes(StandardCharsets.UTF_8)), "Test1",
        IngredientType.FRESH_FRUIT, createMeasures(Measure.GRAM));
  }

  public static Ingredient getIngredient2() {
    return ingredientWithUuid(nameUUIDFromBytes("Test2".getBytes(StandardCharsets.UTF_8)), "Test2",
        IngredientType.BAKING_GOODS, createMeasures(Measure.MILLILITRE));
  }

  public static Ingredient getIngredient3() {
    return ingredientWithUuid(nameUUIDFromBytes("Test3".getBytes(StandardCharsets.UTF_8)), "Test3",
        IngredientType.CANNED_FRUIT, createMeasures(Measure.GRAM));
  }

  public static Ingredient getIngredient4() {
    var secondaries = new HashMap<Measure, NonnegativeFraction>();
    secondaries.put(Measure.TEASPOON, fraction(1, 2));
    return ingredientWithUuid(nameUUIDFromBytes("Test4".getBytes(StandardCharsets.UTF_8)), "Test4",
        IngredientType.MEAT_PRODUCTS, createMeasures(Measure.GRAM, secondaries));
  }

  public static Settings getSettings1() {
    return from(cookingTimeWithProhibited(CookingTime.VERY_SHORT), nonNegative(3),
        CasseroleSettings.NONE, PreferenceSettings.RARE_PREFERED, CourseSettings.ONLY_MAIN);
  }

  public static Settings getSettings2() {
    return from(cookingTimeWithProhibited(CookingTime.SHORT), nonNegative(4),
        CasseroleSettings.POSSIBLE, PreferenceSettings.RARE_PREFERED, CourseSettings.ONLY_MAIN);
  }

  public static Proposal getProposal1() {
    List<ProposedMenu> meals = new ArrayList<>();
    meals.add(mainOnly(getMeal1().getId(), getSettings1().getNumberOfPeople()));
    meals.add(mainOnly(getMeal2().getId(), getSettings2().getNumberOfPeople()));
    LocalDate date = LocalDate.of(2017, 7, 5);
    return from(true, meals, date);
  }

  public static Proposal getProposal2() {
    List<ProposedMenu> meals = new ArrayList<>();
    meals.add(mainOnly(getMeal2().getId(), getSettings1().getNumberOfPeople()));
    meals.add(mainOnly(getMeal3().getId(), getSettings2().getNumberOfPeople()));

    LocalDate date = LocalDate.of(2017, 7, 5);
    return from(true, meals, date);
  }

  public static MealplanerData setupMealplanerDataWithAllIngredients() {
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(CommonFunctions.getIngredient1());
    ingredients.add(CommonFunctions.getIngredient2());
    ingredients.add(CommonFunctions.getIngredient3());
    MealplanerData mealPlan = getInstance();
    mealPlan.setIngredients(ingredients);
    return mealPlan;
  }

  public static MealplanerData setupMealplanerDataWithAllMealsAndIngredients() {
    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal1());
    meals.add(getMeal2());
    meals.add(getMeal3());
    MealplanerData mealPlan = setupMealplanerDataWithAllIngredients();
    mealPlan.setMeals(meals);
    return mealPlan;
  }
}
