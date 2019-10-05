// SPDX-License-Identifier: MIT

package mealplaner.model.meal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.meal.enums.CourseType;
import mealplaner.model.meal.enums.Sidedish;
import mealplaner.plugins.api.MealFact;

public final class MealMetaData {
  private static final MealMetaData EMPTY_METADATA = new MealMetaData(
      "EMPTY",
      Sidedish.NONE,
      CourseType.MAIN,
      new HashMap<>(),
      new ArrayList<>());

  private String name;
  private final Sidedish sidedish;
  private final CourseType courseType;
  private final Map<Class, MealFact> mealFacts;
  private final List<Element> hiddenMealFacts;

  private MealMetaData(
      String name,
      Sidedish sideDish,
      CourseType courseType,
      Map<Class, MealFact> mealFacts,
      List<Element> hiddenMealFacts)
      throws MealException {
    setName(name);
    this.sidedish = sideDish;
    this.courseType = courseType;
    this.mealFacts = mealFacts;
    this.hiddenMealFacts = hiddenMealFacts;
  }

  public static MealMetaData createMealMetaData(
      String name,
      Sidedish sideDish,
      CourseType courseType,
      Map<Class, MealFact> mealFacts,
      List<Element> hiddenMealFacts) throws MealException {
    return new MealMetaData(name,
        sideDish,
        courseType,
        mealFacts,
        hiddenMealFacts);
  }

  public static MealMetaData copy(MealMetaData meal) {
    return new MealMetaData(meal.getName(),
        meal.getSidedish(),
        meal.getCourseType(),
        meal.getMealFacts(),
        meal.getHiddenMealFacts());
  }

  public static MealMetaData createEmptyMealMetaData() {
    return EMPTY_METADATA;
  }

  public String getName() {
    return name;
  }

  public Sidedish getSidedish() {
    return sidedish;
  }

  public CourseType getCourseType() {
    return courseType;
  }

  public Map<Class, MealFact> getMealFacts() {
    return mealFacts;
  }

  public List<Element> getHiddenMealFacts() {
    return hiddenMealFacts;
  }

  @Override
  public String toString() {
    return "["
        + name + ", "
        + sidedish + ", "
        + courseType + ", "
        + mealFacts + ", "
        + hiddenMealFacts + ", "
        + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + name.hashCode();
    result = prime * result + sidedish.hashCode();
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
    MealMetaData other = (MealMetaData) obj;
    return name.equals(other.name)
        && courseType == other.courseType
        && sidedish == other.sidedish
        && mealFacts.equals(other.mealFacts)
        && hiddenMealFacts.equals(other.hiddenMealFacts);
  }

  private void setName(String name) throws MealException {
    if (name.trim().isEmpty()) {
      throw new MealException("Name is empty or consists only of whitespace");
    } else {
      this.name = name.trim();
    }
  }
}
