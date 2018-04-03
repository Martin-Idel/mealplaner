package mealplaner.model;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.CourseType;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;

public final class MealMetaData {
  private static final MealMetaData EMPTY_METADATA = new MealMetaData(
      "EMPTY",
      CookingTime.SHORT,
      Sidedish.NONE,
      ObligatoryUtensil.CASSEROLE,
      CookingPreference.RARE,
      CourseType.MAIN, "");

  private String name;
  private final CookingTime cookingTime;
  private final Sidedish sidedish;
  private final ObligatoryUtensil obligatoryUtensil;
  private final CookingPreference cookingPreference;
  private final CourseType courseType;
  private final String comment;

  private MealMetaData(String name,
      CookingTime cookingTime,
      Sidedish sideDish,
      ObligatoryUtensil obligatoryUtensil,
      CookingPreference cookingPreference,
      CourseType courseType,
      String comment)
      throws MealException {
    setName(name);
    this.cookingTime = cookingTime;
    this.sidedish = sideDish;
    this.obligatoryUtensil = obligatoryUtensil;
    this.cookingPreference = cookingPreference;
    this.courseType = courseType;
    this.comment = comment;
  }

  public static MealMetaData createMealMetaData(String name,
      CookingTime cookingTime,
      Sidedish sideDish,
      ObligatoryUtensil obligatoryUtensil,
      CookingPreference cookingPreference,
      CourseType courseType,
      String comment) throws MealException {
    return new MealMetaData(name,
        cookingTime,
        sideDish,
        obligatoryUtensil,
        cookingPreference,
        courseType,
        comment);
  }

  public static MealMetaData copy(MealMetaData meal) {
    return new MealMetaData(meal.getName(),
        meal.getCookingTime(),
        meal.getSidedish(),
        meal.getObligatoryUtensil(),
        meal.getCookingPreference(),
        meal.getCourseType(),
        meal.getComment());
  }

  public static MealMetaData createEmptyMealMetaData() {
    return EMPTY_METADATA;
  }

  public String getName() {
    return name;
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

  public CourseType getCourseType() {
    return courseType;
  }

  public String getComment() {
    return comment;
  }

  @Override
  public String toString() {
    return "["
        + name + ", "
        + cookingTime + ", "
        + sidedish + ", "
        + obligatoryUtensil + ", "
        + cookingPreference + ", "
        + courseType + ", "
        + comment + ", "
        + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + comment.hashCode();
    result = prime * result + cookingPreference.hashCode();
    result = prime * result + cookingTime.hashCode();
    result = prime * result + name.hashCode();
    result = prime * result + obligatoryUtensil.hashCode();
    result = prime * result + sidedish.hashCode();
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
        && comment.equals(other.comment)
        && cookingPreference == other.cookingPreference
        && cookingTime == other.cookingTime
        && obligatoryUtensil == other.obligatoryUtensil
        && courseType == other.courseType
        && sidedish == other.sidedish;
  }

  private void setName(String name) throws MealException {
    if (name.trim().isEmpty()) {
      throw new MealException("Name is empty or consists only of whitespace");
    } else {
      this.name = name.trim();
    }
  }
}
