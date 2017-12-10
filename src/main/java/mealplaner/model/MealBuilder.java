package mealplaner.model;

import static mealplaner.commons.NonnegativeInteger.ZERO;

import java.util.Optional;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;
import mealplaner.recipes.model.Recipe;

public class MealBuilder {
  private String name = "";
  private CookingTime cookingTime = CookingTime.SHORT;
  private Sidedish sidedish = Sidedish.NONE;
  private ObligatoryUtensil obligatoryUtensil = ObligatoryUtensil.POT;
  private CookingPreference cookingPreference = CookingPreference.NO_PREFERENCE;
  private NonnegativeInteger daysPassed = ZERO;
  private String comment = "";
  private Optional<Recipe> recipe = Optional.empty();

  private MealBuilder() {
  }

  public static MealBuilder meal() {
    return new MealBuilder();
  }

  public static MealBuilder from(Meal meal) {
    return new MealBuilder()
        .name(meal.getName())
        .cookingTime(meal.getCookingTime())
        .sidedish(meal.getSidedish())
        .obligatoryUtensil(meal.getObligatoryUtensil())
        .cookingPreference(meal.getCookingPreference())
        .daysPassed(meal.getDaysPassed())
        .comment(meal.getComment())
        .optionalRecipe(meal.getRecipe());
  }

  public MealBuilder name(String name) {
    this.name = name;
    return this;
  }

  public MealBuilder cookingTime(CookingTime cookingTime) {
    this.cookingTime = cookingTime;
    return this;
  }

  public MealBuilder sidedish(Sidedish sidedish) {
    this.sidedish = sidedish;
    return this;
  }

  public MealBuilder obligatoryUtensil(ObligatoryUtensil obligatoryUtensil) {
    this.obligatoryUtensil = obligatoryUtensil;
    return this;
  }

  public MealBuilder cookingPreference(CookingPreference cookingPreference) {
    this.cookingPreference = cookingPreference;
    return this;
  }

  public MealBuilder daysPassed(NonnegativeInteger daysPassed) {
    this.daysPassed = daysPassed;
    return this;
  }

  public MealBuilder addDaysPassed(NonnegativeInteger daysPassed) {
    this.daysPassed = this.daysPassed.add(daysPassed);
    return this;
  }

  public MealBuilder comment(String comment) {
    this.comment = comment;
    return this;
  }

  public MealBuilder recipe(Recipe recipe) {
    this.recipe = Optional.of(recipe);
    return this;
  }

  public MealBuilder optionalRecipe(Optional<Recipe> recipe) {
    this.recipe = recipe;
    return this;
  }

  public Meal create() {
    return Meal.createMeal(name,
        cookingTime,
        sidedish,
        obligatoryUtensil,
        cookingPreference,
        daysPassed,
        comment,
        recipe);
  }
}
