package mealplaner.model.settings;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import mealplaner.model.Meal;
import mealplaner.model.enums.CookingTime;

public class CookingTimeSetting implements CookingSetting {
  private final Set<CookingTime> prohibitedCookingTime;

  private CookingTimeSetting(CookingTimeSetting cookingTimeSetting) {
    this(new HashSet<CookingTime>(cookingTimeSetting.prohibitedCookingTime));
  }

  public static CookingTimeSetting cookingTimeWithProhibited(CookingTime... cookingTime) {
    HashSet<CookingTime> prohibitedCookingTime = new HashSet<CookingTime>();
    prohibitedCookingTime.addAll(Arrays.asList(cookingTime));
    return new CookingTimeSetting(prohibitedCookingTime);
  }

  public static CookingTimeSetting defaultCookingTime() {
    return new CookingTimeSetting(new HashSet<>());
  }

  public static CookingTimeSetting copyCookingTimeSetting(CookingTimeSetting cookingTimeSetting) {
    return new CookingTimeSetting(cookingTimeSetting);
  }

  public CookingTimeSetting(Set<CookingTime> prohibitedUtensil) {
    this.prohibitedCookingTime = prohibitedUtensil;
  }

  public void prohibitCookingTime(CookingTime cookingTime) {
    prohibitedCookingTime.add(cookingTime);
  }

  public void allowCookingTime(CookingTime cookingTime) {
    prohibitedCookingTime.remove(cookingTime);
  }

  public void reset() {
    prohibitedCookingTime.removeAll(Arrays.asList(CookingTime.values()));
  }

  public boolean isTimeProhibited(CookingTime cookingTime) {
    return prohibitedCookingTime.contains(cookingTime);
  }

  @Override
  public boolean prohibits(Meal meal) {
    return prohibitedCookingTime.contains(meal.getCookingTime());
  }

  public boolean contains(CookingTime cookingTime) {
    return prohibitedCookingTime.contains(cookingTime);
  }

  @Override
  public String toString() {
    return "CookingTimeSetting [prohibitedCookingTime=" + prohibitedCookingTime + "]";
  }

  @Override
  public int hashCode() {
    return 31 + ((prohibitedCookingTime == null) ? 0 : prohibitedCookingTime.hashCode());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    CookingTimeSetting other = (CookingTimeSetting) obj;
    if (!prohibitedCookingTime.equals(other.prohibitedCookingTime)) {
      return false;
    }
    return true;
  }
}
