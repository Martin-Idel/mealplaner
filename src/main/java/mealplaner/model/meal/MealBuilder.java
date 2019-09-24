// SPDX-License-Identifier: MIT

package mealplaner.model.meal;

import static mealplaner.commons.NonnegativeInteger.ZERO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.Fact;
import mealplaner.plugins.api.MealFact;

public final class MealBuilder {
  private PluginStore validationStore;
  private UUID uuid = UUID.randomUUID();
  private String name = "";
  private CookingTime cookingTime = CookingTime.SHORT;
  private Sidedish sidedish = Sidedish.NONE;
  private ObligatoryUtensil obligatoryUtensil = ObligatoryUtensil.POT;
  private CookingPreference cookingPreference = CookingPreference.NO_PREFERENCE;
  private CourseType courseType = CourseType.MAIN;
  private NonnegativeInteger daysPassed = ZERO;
  private String comment = "";
  private Optional<Recipe> recipe = Optional.empty();
  private Map<Class, MealFact> mealFactMap = new HashMap<>();
  private List<Element> hiddenMealFacts = new ArrayList<>();

  private MealBuilder() {
    validationStore = new PluginStore();
  }

  private MealBuilder(PluginStore validationStore) {
    this.validationStore = validationStore;
  }

  public static MealBuilder meal() {
    return new MealBuilder();
  }

  public static MealBuilder mealWithValidator(PluginStore pluginStore) {
    return new MealBuilder(pluginStore);
  }

  public static MealBuilder from(Meal meal) {
    return new MealBuilder()
        .id(meal.getId())
        .name(meal.getName())
        .cookingTime(meal.getCookingTime())
        .sidedish(meal.getSidedish())
        .obligatoryUtensil(meal.getObligatoryUtensil())
        .cookingPreference(meal.getCookingPreference())
        .courseType(meal.getCourseType())
        .daysPassed(meal.getDaysPassed())
        .comment(meal.getComment())
        .optionalRecipe(meal.getRecipe())
        .addMealMap(meal.getMealFacts())
        .addHiddenMeals(meal.getHiddenFacts());
  }

  public MealBuilder id(UUID uuid) {
    this.uuid = uuid;
    return this;
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

  public MealBuilder courseType(CourseType courseType) {
    this.courseType = courseType;
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

  public MealBuilder addFact(MealFact mealFact) {
    mealFactMap.putIfAbsent(mealFact.getClass(), mealFact);
    return this;
  }

  public MealBuilder addMealMap(Map<Class, MealFact> facts) {
    this.mealFactMap = facts;
    return this;
  }

  public MealBuilder addHiddenMeals(List<Element> hiddenMealFacts) {
    this.hiddenMealFacts = hiddenMealFacts;
    return this;
  }

  public Meal create() {
    if (validationStore != null) {
      validateFacts();
    }
    return Meal.createMeal(uuid,
        MealMetaData.createMealMetaData(name,
            cookingTime,
            sidedish,
            obligatoryUtensil,
            cookingPreference,
            courseType,
            comment,
            mealFactMap,
            hiddenMealFacts),
        daysPassed,
        recipe);
  }

  private void validateFacts() {
    Set<Class<? extends Fact>> allRegisteredFacts =
        validationStore.getRegisteredMealExtensions().getAllRegisteredFacts();
    for (var fact : allRegisteredFacts) {
      if (!mealFactMap.containsKey(fact)) {
        throw new MealException("Class does not contain fact of type " + fact);
      }
    }
  }
}
