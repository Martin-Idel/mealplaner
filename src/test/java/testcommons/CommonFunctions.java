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
import static mealplaner.model.proposal.Proposal.from;
import static mealplaner.model.proposal.ProposedMenu.mainOnly;
import static mealplaner.model.recipes.IngredientBuilder.ingredient;
import static mealplaner.model.recipes.IngredientType.BAKING_GOODS;
import static mealplaner.model.recipes.IngredientType.CANNED_FRUIT;
import static mealplaner.model.recipes.IngredientType.FRESH_FRUIT;
import static mealplaner.model.recipes.IngredientType.MEAT_PRODUCTS;
import static mealplaner.model.recipes.Measure.GRAM;
import static mealplaner.model.recipes.Measure.MILLILITRE;
import static mealplaner.model.recipes.Measures.createMeasures;
import static mealplaner.model.recipes.QuantitativeIngredient.createQuantitativeIngredient;
import static mealplaner.model.settings.SettingsBuilder.setting;
import static mealplaner.plugins.builtins.courses.CourseSettings.MAIN_DESERT;
import static mealplaner.plugins.builtins.courses.CourseSettings.ONLY_MAIN;
import static mealplaner.plugins.builtins.courses.CourseType.DESERT;
import static mealplaner.plugins.builtins.courses.CourseType.ENTRY;
import static mealplaner.plugins.builtins.courses.CourseType.MAIN;
import static mealplaner.plugins.plugins.cookingtime.mealextension.CookingTime.LONG;
import static mealplaner.plugins.plugins.cookingtime.mealextension.CookingTime.MEDIUM;
import static mealplaner.plugins.plugins.cookingtime.mealextension.CookingTime.SHORT;
import static mealplaner.plugins.plugins.cookingtime.mealextension.CookingTime.VERY_SHORT;
import static mealplaner.plugins.plugins.cookingtime.settingextension.CookingTimeSubSetting.cookingTimeWithProhibited;
import static mealplaner.plugins.plugins.preference.mealextension.CookingPreference.NO_PREFERENCE;
import static mealplaner.plugins.plugins.preference.mealextension.CookingPreference.RARE;
import static mealplaner.plugins.plugins.preference.mealextension.CookingPreference.VERY_POPULAR;
import static mealplaner.plugins.plugins.preference.settingextension.PreferenceSettings.RARE_PREFERED;
import static mealplaner.plugins.plugins.sidedish.mealextension.Sidedish.PASTA;
import static mealplaner.plugins.plugins.sidedish.mealextension.Sidedish.RICE;
import static mealplaner.plugins.plugins.utensil.mealextension.ObligatoryUtensil.CASSEROLE;
import static mealplaner.plugins.plugins.utensil.mealextension.ObligatoryUtensil.PAN;
import static mealplaner.plugins.plugins.utensil.mealextension.ObligatoryUtensil.POT;
import static mealplaner.plugins.plugins.utensil.settingextension.CasseroleSettings.NONE;
import static mealplaner.plugins.plugins.utensil.settingextension.CasseroleSettings.POSSIBLE;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mealplaner.Kochplaner;
import mealplaner.commons.NonnegativeFraction;
import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;
import mealplaner.model.meal.MealBuilder;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.proposal.ProposedMenu;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.Measure;
import mealplaner.model.recipes.QuantitativeIngredient;
import mealplaner.model.recipes.Recipe;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.builtins.courses.CourseTypeFact;
import mealplaner.plugins.builtins.courses.CourseTypeSetting;
import mealplaner.plugins.plugins.comment.mealextension.CommentFact;
import mealplaner.plugins.plugins.cookingtime.mealextension.CookingTimeFact;
import mealplaner.plugins.plugins.preference.mealextension.CookingPreferenceFact;
import mealplaner.plugins.plugins.preference.settingextension.CookingPreferenceSubSetting;
import mealplaner.plugins.plugins.sidedish.mealextension.Sidedish;
import mealplaner.plugins.plugins.sidedish.mealextension.SidedishFact;
import mealplaner.plugins.plugins.utensil.mealextension.ObligatoryUtensilFact;
import mealplaner.plugins.plugins.utensil.settingextension.CasseroleSettings;
import mealplaner.plugins.plugins.utensil.settingextension.CasseroleSubSetting;

public final class CommonFunctions {
  private CommonFunctions() {
  }

  public static Meal getMeal1() {
    return MealBuilder.meal()
        .id(nameUUIDFromBytes("Test1Meal".getBytes(UTF_8)))
        .name("Test1")
        .addFact(new CookingTimeFact(SHORT))
        .addFact(new SidedishFact(PASTA))
        .addFact(new ObligatoryUtensilFact(PAN))
        .addFact(new CookingPreferenceFact(VERY_POPULAR))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new CommentFact("no comment"))
        .daysPassed(FIVE)
        .create();
  }

  public static Meal getMeal2() {
    return MealBuilder.meal()
        .id(nameUUIDFromBytes("Test2Meal".getBytes(UTF_8)))
        .name("Test2")
        .addFact(new CookingTimeFact(SHORT))
        .addFact(new SidedishFact(Sidedish.NONE))
        .addFact(new ObligatoryUtensilFact(POT))
        .addFact(new CookingPreferenceFact(NO_PREFERENCE))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new CommentFact(""))
        .daysPassed(ONE)
        .recipe(getRecipe1())
        .create();
  }

  public static Meal getMeal3() {
    return MealBuilder.meal()
        .id(nameUUIDFromBytes("Test3Meal".getBytes(UTF_8)))
        .name("Test3")
        .addFact(new CookingTimeFact(MEDIUM))
        .addFact(new SidedishFact(RICE))
        .addFact(new ObligatoryUtensilFact(POT))
        .addFact(new CookingPreferenceFact(NO_PREFERENCE))
        .addFact(new CourseTypeFact(MAIN))
        .addFact(new CommentFact(""))
        .daysPassed(TWO)
        .recipe(getRecipe2())
        .create();
  }

  public static Meal getMealEntry() {
    return MealBuilder.meal()
        .id(nameUUIDFromBytes("Test4Meal".getBytes(UTF_8)))
        .name("Test4")
        .addFact(new CookingTimeFact(LONG))
        .addFact(new SidedishFact(RICE))
        .addFact(new ObligatoryUtensilFact(PAN))
        .addFact(new CookingPreferenceFact(NO_PREFERENCE))
        .addFact(new CourseTypeFact(ENTRY))
        .addFact(new CommentFact(""))
        .daysPassed(TWO)
        .recipe(getRecipe2())
        .create();
  }

  public static Meal getMealDesert() {
    return MealBuilder.meal()
        .id(nameUUIDFromBytes("Test5Meal".getBytes(UTF_8)))
        .name("Test4")
        .addFact(new CookingTimeFact(VERY_SHORT))
        .addFact(new SidedishFact(RICE))
        .addFact(new ObligatoryUtensilFact(POT))
        .addFact(new CookingPreferenceFact(VERY_POPULAR))
        .addFact(new CourseTypeFact(DESERT))
        .addFact(new CommentFact(""))
        .daysPassed(TWO)
        .recipe(getRecipe2())
        .create();
  }

  public static Meal getMealEntry2() {
    return MealBuilder.meal()
        .id(nameUUIDFromBytes("Test6Meal".getBytes(UTF_8)))
        .name("Test6")
        .addFact(new CookingTimeFact(VERY_SHORT))
        .addFact(new SidedishFact(Sidedish.NONE))
        .addFact(new ObligatoryUtensilFact(CASSEROLE))
        .addFact(new CookingPreferenceFact(RARE))
        .addFact(new CourseTypeFact(ENTRY))
        .addFact(new CommentFact(""))
        .daysPassed(TWO)
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
    return ingredient()
        .withUuid(nameUUIDFromBytes("Test1".getBytes(UTF_8)))
        .withName("Test1")
        .withType(FRESH_FRUIT)
        .withMeasures(createMeasures(GRAM))
        .create();
  }

  public static Ingredient getIngredient2() {
    return ingredient()
        .withUuid(nameUUIDFromBytes("Test2".getBytes(UTF_8)))
        .withName("Test2")
        .withType(BAKING_GOODS)
        .withMeasures(createMeasures(MILLILITRE))
        .create();
  }

  public static Ingredient getIngredient3() {
    return ingredient()
        .withUuid(nameUUIDFromBytes("Test3".getBytes(UTF_8)))
        .withName("Test3")
        .withType(CANNED_FRUIT)
        .withMeasures(createMeasures(GRAM))
        .create();
  }

  public static Ingredient getIngredient4() {
    var secondaries = new HashMap<Measure, NonnegativeFraction>();
    secondaries.put(Measure.TEASPOON, fraction(1, 2));
    return ingredient()
        .withUuid(nameUUIDFromBytes("Test4".getBytes(UTF_8)))
        .withName("Test4")
        .withType(MEAT_PRODUCTS)
        .withMeasures(createMeasures(GRAM, secondaries))
        .create();
  }

  public static Settings getSettings1() {
    return setting()
        .addSetting(cookingTimeWithProhibited(VERY_SHORT))
        .addSetting(new CasseroleSubSetting(CasseroleSettings.NONE))
        .addSetting(new CookingPreferenceSubSetting(RARE_PREFERED))
        .addSetting(new CourseTypeSetting(ONLY_MAIN))
        .numberOfPeople(THREE)
        .create();
  }

  public static Settings getSettings2() {
    return setting()
        .addSetting(cookingTimeWithProhibited(SHORT))
        .addSetting(new CasseroleSubSetting(POSSIBLE))
        .addSetting(new CookingPreferenceSubSetting(RARE_PREFERED))
        .addSetting(new CourseTypeSetting(ONLY_MAIN))
        .numberOfPeople(FOUR)
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
    var pluginStore = Kochplaner.registerPlugins();
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(CommonFunctions.getIngredient1());
    ingredients.add(CommonFunctions.getIngredient2());
    ingredients.add(CommonFunctions.getIngredient3());
    MealplanerData mealPlan = getInstance(pluginStore);
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
