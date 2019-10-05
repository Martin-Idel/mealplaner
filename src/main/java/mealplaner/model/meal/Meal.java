// SPDX-License-Identifier: MIT

package mealplaner.model.meal;

import static java.util.Optional.empty;
import static java.util.UUID.nameUUIDFromBytes;
import static mealplaner.commons.NonnegativeInteger.ZERO;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.w3c.dom.Element;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.model.meal.enums.CourseType;
import mealplaner.model.meal.enums.Sidedish;
import mealplaner.model.recipes.Recipe;
import mealplaner.plugins.api.MealFact;

public final class Meal implements Comparable<Meal> {
  public static final Meal EMPTY_MEAL = new Meal(
      nameUUIDFromBytes("EMPTY".getBytes(StandardCharsets.UTF_8)),
      MealMetaData.createEmptyMealMetaData(),
      ZERO,
      empty());

  private final UUID uuid;
  private final MealMetaData metadata;
  private final NonnegativeInteger daysPassed;
  private Optional<Recipe> recipe;

  private Meal(
      UUID uuid,
      MealMetaData metadata,
      NonnegativeInteger daysPassed,
      Optional<Recipe> recipe) {
    this.uuid = uuid;
    this.metadata = metadata;
    this.daysPassed = daysPassed;
    this.recipe = recipe;
  }

  public static Meal createMeal(
      UUID uuid,
      MealMetaData metadata,
      NonnegativeInteger daysPassed,
      Optional<Recipe> recipe) {
    return new Meal(uuid, metadata, daysPassed, recipe);
  }

  public static Meal copy(Meal meal) {
    return new Meal(meal.getId(),
        MealMetaData.copy(meal.getMetaData()),
        meal.getDaysPassed(),
        meal.getRecipe());
  }

  public MealMetaData getMetaData() {
    return metadata;
  }

  public Map<Class, MealFact> getMealFacts() {
    return new HashMap<>(metadata.getMealFacts());
  }

  public List<Element> getHiddenFacts() {
    return new ArrayList<>(metadata.getHiddenMealFacts());
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
    return metadata.getMealFacts().get(name);
  }

  @SuppressWarnings("unchecked")
  public <T extends MealFact> T getTypedMealFact(Class<T> name) {
    return (T) metadata.getMealFacts().get(name);
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

  public Sidedish getSidedish() {
    return metadata.getSidedish();
  }

  public CourseType getCourseType() {
    return metadata.getCourseType();
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
    return "[" + "uuid=" + uuid + ", "
        + "metadata=" + metadata + ", "
        + "daysPassed=" + daysPassed + ", "
        + "recipe=" + recipe + ", " + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + uuid.hashCode();
    result = prime * result + metadata.hashCode();
    result = prime * result + daysPassed.value;
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
        && recipe.equals(other.recipe);
  }
}