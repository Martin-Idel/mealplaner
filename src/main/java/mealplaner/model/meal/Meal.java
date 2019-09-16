// SPDX-License-Identifier: MIT

package mealplaner.model.meal;

import static java.util.Optional.empty;
import static java.util.UUID.nameUUIDFromBytes;
import static mealplaner.commons.NonnegativeInteger.ZERO;
import static mealplaner.model.meal.MealMetaData.createMealMetaData;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.w3c.dom.Element;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.meal.enums.CookingPreference;
import mealplaner.model.meal.enums.CookingTime;
import mealplaner.model.meal.enums.CourseType;
import mealplaner.model.meal.enums.ObligatoryUtensil;
import mealplaner.model.meal.enums.Sidedish;
import mealplaner.model.recipes.Recipe;
import mealplaner.plugins.api.MealFact;

public final class Meal implements Comparable<Meal> {
  public static final Meal EMPTY_MEAL = new Meal(
      nameUUIDFromBytes("EMPTY".getBytes(StandardCharsets.UTF_8)),
      MealMetaData.createEmptyMealMetaData(),
      ZERO,
      new HashMap<>(), new ArrayList<>(), empty());

  private final UUID uuid;
  private final MealMetaData metadata;
  private final NonnegativeInteger daysPassed;
  private final Map<Class, MealFact> mealFacts;
  private final List<Element> hiddenMealFacts;
  private Optional<Recipe> recipe;

  private Meal(
      UUID uuid,
      MealMetaData metadata,
      NonnegativeInteger daysPassed,
      Map<Class, MealFact> mealFacts,
      List<Element> hiddenMealFacts,
      Optional<Recipe> recipe) {
    this.uuid = uuid;
    this.metadata = metadata;
    this.daysPassed = daysPassed;
    this.mealFacts = mealFacts;
    this.hiddenMealFacts = hiddenMealFacts;
    this.recipe = recipe;
  }

  public static Meal createMeal(
      UUID uuid,
      MealMetaData metadata,
      NonnegativeInteger daysPassed,
      Optional<Recipe> recipe) {
    return new Meal(uuid, metadata, daysPassed, new HashMap<>(), new ArrayList<>(), recipe);
  }

  public static Meal createMeal(
      UUID uuid,
      String name,
      CookingTime cookingTime,
      Sidedish sideDish,
      ObligatoryUtensil obligatoryUtensil,
      CookingPreference cookingPreference,
      CourseType courseType,
      NonnegativeInteger daysPassed,
      String comment,
      Optional<Recipe> recipe) throws MealException {
    return new Meal(uuid,
        createMealMetaData(name,
            cookingTime,
            sideDish,
            obligatoryUtensil,
            cookingPreference,
            courseType,
            comment),
        daysPassed,
        new HashMap<>(), new ArrayList<>(), recipe);
  }

  public static Meal createMeal(
      UUID uuid,
      String name,
      CookingTime cookingTime,
      Sidedish sideDish,
      ObligatoryUtensil obligatoryUtensil,
      CookingPreference cookingPreference,
      CourseType courseType,
      NonnegativeInteger daysPassed,
      String comment,
      Map<Class, MealFact> mealFacts,
      List<Element> hiddenMealFacts,
      Optional<Recipe> recipe) throws MealException {
    return new Meal(uuid,
        createMealMetaData(name,
            cookingTime,
            sideDish,
            obligatoryUtensil,
            cookingPreference,
            courseType,
            comment),
        daysPassed, mealFacts, hiddenMealFacts, recipe);
  }

  public static Meal copy(Meal meal) {
    return new Meal(meal.getId(),
        MealMetaData.copy(meal.getMetaData()),
        meal.getDaysPassed(),
        meal.getMealFacts(), meal.hiddenMealFacts, meal.getRecipe());
  }

  public MealMetaData getMetaData() {
    return metadata;
  }

  public Map<Class, MealFact> getMealFacts() {
    return new HashMap<>(mealFacts);
  }

  public List<Element> getHiddenFacts() {
    return hiddenMealFacts;
  }

  public Meal addRecipe(Optional<Recipe> recipe) {
    Meal newMeal = copy(this);
    newMeal.recipe = recipe;
    return newMeal;
  }

  /**
   * N.B.: Return value may be null
   *
   * @param name class of the meal fact
   * @return MealFact corresponding to the class if available (null otherwise)
   */
  public MealFact getMealFact(Class name) {
    return mealFacts.get(name);
  }

  @SuppressWarnings("unchecked")
  public <T> T getTypedMealFact(Class<T> name) {
    return (T) mealFacts.get(name);
  }

  public UUID getId() {
    return uuid;
  }

  public String getName() {
    return metadata.getName();
  }

  public NonnegativeInteger getDaysPassed() {
    return daysPassed;
  }

  public int getDaysPassedAsInteger() {
    return daysPassed.value;
  }

  public CookingTime getCookingTime() {
    return metadata.getCookingTime();
  }

  public Sidedish getSidedish() {
    return metadata.getSidedish();
  }

  public ObligatoryUtensil getObligatoryUtensil() {
    return metadata.getObligatoryUtensil();
  }

  public CookingPreference getCookingPreference() {
    return metadata.getCookingPreference();
  }

  public CourseType getCourseType() {
    return metadata.getCourseType();
  }

  public String getComment() {
    return metadata.getComment();
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
    return "[" + uuid + ", "
        + metadata + ", "
        + daysPassed + ", "
        + recipe + ", "
        + mealFacts + ", "
        + hiddenMealFacts + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + uuid.hashCode();
    result = prime * result + metadata.hashCode();
    result = prime * result + daysPassed.value;
    result = prime * result + mealFacts.hashCode();
    result = prime * result + hiddenMealFacts.hashCode();
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
    return uuid.equals(other.uuid)
        && metadata.equals(other.metadata)
        && daysPassed.equals(other.daysPassed)
        && recipe.equals(other.recipe)
        && mealFacts.equals(other.mealFacts)
        && hiddenMealFacts.equals(other.mealFacts);
  }
}