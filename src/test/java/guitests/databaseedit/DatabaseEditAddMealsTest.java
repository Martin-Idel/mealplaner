package guitests.databaseedit;

import static java.nio.charset.Charset.forName;
import static java.util.UUID.nameUUIDFromBytes;
import static mealplaner.commons.NonnegativeFraction.fraction;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.meal.MealBuilder.from;
import static mealplaner.model.recipes.Ingredient.ingredientWithUuid;
import static testcommons.CommonFunctions.getIngredient1;
import static testcommons.CommonFunctions.getIngredient2;
import static testcommons.CommonFunctions.getIngredient3;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import guitests.helpers.AssertJMealplanerTestCase;
import guitests.pageobjects.MealsEditPageObject;
import mealplaner.commons.NonnegativeFraction;
import mealplaner.model.meal.Meal;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.IngredientType;
import mealplaner.model.recipes.Measure;
import mealplaner.model.recipes.Recipe;

public class DatabaseEditAddMealsTest extends AssertJMealplanerTestCase {
  @Test
  public void addMealAddsMealsInCorrectOrder() {
    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal1());
    meals.add(getMeal2());
    MealsEditPageObject database = windowHelpers.getMealsPane()
        .addMeal(getMeal2())
        .addMeal(getMeal1())
        .compareDatabaseInTable(meals);
    database.cancelButton().requireEnabled();
    database.saveButton().requireEnabled();
  }

  @Test
  public void addMealWithNewIngredientsAddsIngredientsToIngredientsList() {
    List<Meal> meals = new ArrayList<>();
    meals.add(getMealWithNewIngredient());
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(getIngredient1());
    ingredients.add(getIngredient2());
    ingredients.add(getIngredient3());
    ingredients.add(getIngredient4());
    windowHelpers.getMealsPane()
        .addMeal(getMealWithNewIngredient(), getIngredient4())
        .save()
        .compareDatabaseInTable(meals);
    windowHelpers.getIngredientsPane()
        .compareIngredientsInTable(ingredients);
  }

  private static Meal getMealWithNewIngredient() {
    Map<Ingredient, NonnegativeFraction> recipeMap = new HashMap<>();
    recipeMap.put(getIngredient1(), fraction(10, 3));
    recipeMap.put(getIngredient4(), fraction(2, 4));
    Recipe recipe = Recipe.from(nonNegative(1), recipeMap);
    return from(getMeal2()).recipe(recipe).create();
  }

  private static Ingredient getIngredient4() {
    return ingredientWithUuid(nameUUIDFromBytes("Test4".getBytes(forName("UTF-8"))), "Test4",
        IngredientType.CANNED_FRUIT, Measure.GRAM);
  }
}
