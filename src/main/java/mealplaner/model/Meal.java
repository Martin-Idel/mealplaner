package mealplaner.model;

import static java.util.Optional.empty;
import static mealplaner.commons.NonnegativeInteger.ZERO;

import java.util.Optional;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;
import mealplaner.recipes.model.Recipe;

public final class Meal implements Comparable<Meal> {
  public static final Meal EMPTY_MEAL = new Meal("EMPTY",
      CookingTime.SHORT,
      Sidedish.NONE,
      ObligatoryUtensil.CASSEROLE,
      CookingPreference.RARE, ZERO, "", empty());

  private String name;
  private final CookingTime cookingTime;
  private final Sidedish sidedish;
  private final ObligatoryUtensil obligatoryUtensil;
  private final CookingPreference cookingPreference;
  private final NonnegativeInteger daysPassed;
  private final String comment;
  private Optional<Recipe> recipe;

  private Meal(String name,
      CookingTime cookingTime,
      Sidedish sideDish,
      ObligatoryUtensil obligatoryUtensil,
      CookingPreference cookingPreference,
      NonnegativeInteger daysPassed,
      String comment,
      Optional<Recipe> recipe)
      throws MealException {
    setName(name);
    this.cookingTime = cookingTime;
    this.sidedish = sideDish;
    this.obligatoryUtensil = obligatoryUtensil;
    this.cookingPreference = cookingPreference;
    this.daysPassed = daysPassed;
    this.comment = comment;
    this.recipe = recipe;
  }

  public static Meal createMeal(String name,
      CookingTime cookingTime,
      Sidedish sideDish,
      ObligatoryUtensil obligatoryUtensil,
      CookingPreference cookingPreference,
      NonnegativeInteger daysPassed,
      String comment,
      Optional<Recipe> recipe) throws MealException {
    return new Meal(name,
        cookingTime,
        sideDish,
        obligatoryUtensil,
        cookingPreference,
        daysPassed,
        comment,
        recipe);
  }

  public static Meal copy(Meal meal) {
    return new Meal(meal.getName(),
        meal.getCookingTime(),
        meal.getSidedish(),
        meal.getObligatoryUtensil(),
        meal.getCookingPreference(),
        meal.getDaysPassed(),
        meal.getComment(),
        meal.getRecipe());
  }

  public Meal addRecipe(Optional<Recipe> recipe) {
    Meal newMeal = copy(this);
    newMeal.recipe = recipe;
    return newMeal;
  }

  public static Meal createEmptyMeal() {
    return EMPTY_MEAL;
  }

  public String getName() {
    return name;
  }

  public NonnegativeInteger getDaysPassed() {
    return daysPassed;
  }

  public int getDaysPassedAsInteger() {
    return daysPassed.value;
  }

  public CookingTime getCookingTime() {
    return cookingTime;
  }

  public Sidedish getSidedish() {
    return sidedish;
  }

  public ObligatoryUtensil getObligatoryUtensil() {
    return obligatoryUtensil;
  }

  public CookingPreference getCookingPreference() {
    return cookingPreference;
  }

  public String getComment() {
    return comment;
  }

  public Optional<Recipe> getRecipe() {
    return recipe;
  }

  @Override
  public int compareTo(Meal otherMeal) {
    return this.getName().compareToIgnoreCase(otherMeal.getName());
  }

  @Override
  public String toString() {
    return "[" + name + ", "
        + cookingTime + ", "
        + sidedish + ", "
        + obligatoryUtensil + ", "
        + cookingPreference + ", "
        + daysPassed + ", "
        + comment + ", "
        + recipe + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((comment == null) ? 0 : comment.hashCode());
    result = prime * result + ((cookingPreference == null) ? 0 : cookingPreference.hashCode());
    result = prime * result + ((cookingTime == null) ? 0 : cookingTime.hashCode());
    result = prime * result + daysPassed.value;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((obligatoryUtensil == null) ? 0 : obligatoryUtensil.hashCode());
    result = prime * result + ((sidedish == null) ? 0 : sidedish.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Meal other = (Meal) obj;
    return name.equals(other.name)
        && comment.equals(other.comment)
        && cookingPreference == other.cookingPreference
        && cookingTime == other.cookingTime
        && obligatoryUtensil == other.obligatoryUtensil
        && sidedish == other.sidedish
        && daysPassed.equals(other.daysPassed);
  }

  private void setName(String name) throws MealException {
    if (name.trim().isEmpty()) {
      throw new MealException("Name is empty or consists only of whitespace");
    } else {
      this.name = name.trim();
    }
  }
}