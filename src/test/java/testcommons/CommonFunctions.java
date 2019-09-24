// SPDX-License-Identifier: MIT

package testcommons;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.nameUUIDFromBytes;
import static mealplaner.commons.NonnegativeFraction.fraction;
import static mealplaner.commons.NonnegativeFraction.wholeNumber;
import static mealplaner.commons.NonnegativeInteger.FIVE;
import static mealplaner.commons.NonnegativeInteger.FOUR;
import static mealplaner.commons.NonnegativeInteger.ONE;
import static mealplaner.commons.NonnegativeInteger.THREE;
import static mealplaner.commons.NonnegativeInteger.TWO;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.MealplanerData.getInstance;
import static mealplaner.model.meal.enums.CookingPreference.NO_PREFERENCE;
import static mealplaner.model.meal.enums.CookingPreference.RARE;
import static mealplaner.model.meal.enums.CookingPreference.VERY_POPULAR;
import static mealplaner.model.meal.enums.CookingTime.LONG;
import static mealplaner.model.meal.enums.CookingTime.MEDIUM;
import static mealplaner.model.meal.enums.CookingTime.SHORT;
import static mealplaner.model.meal.enums.CookingTime.VERY_SHORT;
import static mealplaner.model.meal.enums.CourseType.DESERT;
import static mealplaner.model.meal.enums.CourseType.ENTRY;
import static mealplaner.model.meal.enums.CourseType.MAIN;
import static mealplaner.model.meal.enums.ObligatoryUtensil.CASSEROLE;
import static mealplaner.model.meal.enums.ObligatoryUtensil.PAN;
import static mealplaner.model.meal.enums.ObligatoryUtensil.POT;
import static mealplaner.model.meal.enums.Sidedish.PASTA;
import static mealplaner.model.meal.enums.Sidedish.RICE;
import static mealplaner.model.proposal.Proposal.from;
import static mealplaner.model.proposal.ProposedMenu.mainOnly;
import static mealplaner.model.recipes.Ingredient.ingredientWithUuid;
import static mealplaner.model.recipes.Measures.createMeasures;
import static mealplaner.model.recipes.QuantitativeIngredient.createQuantitativeIngredient;
import static mealplaner.model.settings.SettingsBuilder.setting;
import static mealplaner.model.settings.enums.CasseroleSettings.POSSIBLE;
import static mealplaner.model.settings.enums.CourseSettings.MAIN_DESERT;
import static mealplaner.model.settings.enums.CourseSettings.ONLY_MAIN;
import static mealplaner.model.settings.enums.PreferenceSettings.RARE_PREFERED;
import static mealplaner.model.settings.subsettings.CookingTimeSetting.cookingTimeWithProhibited;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;
import mealplaner.model.meal.MealBuilder;
import mealplaner.model.meal.enums.Sidedish;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.proposal.ProposedMenu;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.IngredientType;
import mealplaner.model.recipes.Measure;
import mealplaner.model.recipes.QuantitativeIngredient;
import mealplaner.model.recipes.Recipe;
import mealplaner.model.settings.Settings;
import mealplaner.model.settings.SettingsBuilder;
import mealplaner.model.settings.enums.CasseroleSettings;

public final class CommonFunctions {
  private CommonFunctions() {
  }

  public static Meal getMeal1() {
    return MealBuilder.meal()
        .id(nameUUIDFromBytes("Test1Meal".getBytes(UTF_8)))
        .name("Test1")
        .cookingTime(SHORT)
        .sidedish(PASTA)
        .obligatoryUtensil(PAN)
        .cookingPreference(VERY_POPULAR)
        .courseType(MAIN)
        .daysPassed(FIVE)
        .comment("no comment")
        .create();
  }

  public static Meal getMeal2() {
    return MealBuilder.meal()
        .id(nameUUIDFromBytes("Test2Meal".getBytes(UTF_8)))
        .name("Test2")
        .cookingTime(SHORT)
        .sidedish(Sidedish.NONE)
        .obligatoryUtensil(POT)
        .cookingPreference(NO_PREFERENCE)
        .courseType(MAIN)
        .daysPassed(ONE)
        .comment("")
        .recipe(getRecipe1())
        .create();
  }

  public static Meal getMeal3() {
    return MealBuilder.meal()
        .id(nameUUIDFromBytes("Test3Meal".getBytes(UTF_8)))
        .name("Test3")
        .cookingTime(MEDIUM)
        .sidedish(RICE)
        .obligatoryUtensil(POT)
        .cookingPreference(NO_PREFERENCE)
        .courseType(MAIN)
        .daysPassed(TWO)
        .comment("")
        .recipe(getRecipe2())
        .create();
  }

  public static Meal getMealEntry() {
    return MealBuilder.meal()
        .id(nameUUIDFromBytes("Test4Meal".getBytes(UTF_8)))
        .name("Test4")
        .cookingTime(LONG)
        .sidedish(RICE)
        .obligatoryUtensil(PAN)
        .cookingPreference(NO_PREFERENCE)
        .courseType(ENTRY)
        .daysPassed(TWO)
        .comment("")
        .recipe(getRecipe2())
        .create();
  }

  public static Meal getMealDesert() {
    return MealBuilder.meal()
        .id(nameUUIDFromBytes("Test5Meal".getBytes(UTF_8)))
        .name("Test4")
        .cookingTime(VERY_SHORT)
        .sidedish(RICE)
        .obligatoryUtensil(POT)
        .cookingPreference(VERY_POPULAR)
        .courseType(DESERT)
        .daysPassed(TWO)
        .comment("")
        .recipe(getRecipe2())
        .create();
  }

  public static Meal getMealEntry2() {
    return MealBuilder.meal()
        .id(nameUUIDFromBytes("Test6Meal".getBytes(UTF_8)))
        .name("Test6")
        .cookingTime(VERY_SHORT)
        .sidedish(Sidedish.NONE)
        .obligatoryUtensil(CASSEROLE)
        .cookingPreference(RARE)
        .courseType(ENTRY)
        .daysPassed(TWO)
        .comment("")
        .create();
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
    return ingredientWithUuid(nameUUIDFromBytes("Test1".getBytes(UTF_8)), "Test1",
        IngredientType.FRESH_FRUIT, createMeasures(Measure.GRAM));
  }

  public static Ingredient getIngredient2() {
    return ingredientWithUuid(nameUUIDFromBytes("Test2".getBytes(UTF_8)), "Test2",
        IngredientType.BAKING_GOODS, createMeasures(Measure.MILLILITRE));
  }

  public static Ingredient getIngredient3() {
    return ingredientWithUuid(nameUUIDFromBytes("Test3".getBytes(UTF_8)), "Test3",
        IngredientType.CANNED_FRUIT, createMeasures(Measure.GRAM));
  }

  public static Ingredient getIngredient4() {
    var secondaries = new HashMap<Measure, NonnegativeFraction>();
    secondaries.put(Measure.TEASPOON, fraction(1, 2));
    return ingredientWithUuid(nameUUIDFromBytes("Test4".getBytes(UTF_8)), "Test4",
        IngredientType.MEAT_PRODUCTS, createMeasures(Measure.GRAM, secondaries));
  }

  public static Settings getSettings1() {
    return setting()
        .time(cookingTimeWithProhibited(VERY_SHORT))
        .numberOfPeople(THREE)
        .casserole(CasseroleSettings.NONE)
        .preference(RARE_PREFERED)
        .course(ONLY_MAIN)
        .create();
  }

  public static Settings getSettings2() {
    return setting()
        .time(cookingTimeWithProhibited(SHORT))
        .numberOfPeople(FOUR)
        .casserole(POSSIBLE)
        .preference(RARE_PREFERED)
        .course(ONLY_MAIN)
        .create();
  }

  public static Settings getSettings3() {
    return setting()
        .time(cookingTimeWithProhibited())
        .numberOfPeople(THREE)
        .casserole(CasseroleSettings.NONE)
        .preference(RARE_PREFERED)
        .course(MAIN_DESERT)
        .create();
  }

  public static Settings getSettings4() {
    return setting()
        .time(cookingTimeWithProhibited())
        .numberOfPeople(THREE)
        .casserole(CasseroleSettings.NONE)
        .preference(RARE_PREFERED)
        .course(MAIN_DESERT)
        .create();
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
