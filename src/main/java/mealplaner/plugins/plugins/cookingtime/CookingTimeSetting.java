// SPDX-License-Identifier: MIT

package mealplaner.plugins.plugins.cookingtime;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import mealplaner.model.meal.Meal;
import mealplaner.model.settings.subsettings.CookingSetting;
import mealplaner.plugins.api.FactXml;
import mealplaner.plugins.api.Setting;

public final class CookingTimeSetting implements CookingSetting, Setting {
  private final Set<CookingTime> prohibitedCookingTime;

  private CookingTimeSetting(CookingTimeSetting cookingTimeSetting) {
    this(new HashSet<>(cookingTimeSetting.prohibitedCookingTime));
  }

  public static CookingTimeSetting cookingTimeWithProhibited(CookingTime... cookingTime) {
    HashSet<CookingTime> prohibitedCookingTime = new HashSet<>(Arrays.asList(cookingTime));
    return new CookingTimeSetting(prohibitedCookingTime);
  }

  public static CookingTimeSetting defaultCookingTime() {
    return new CookingTimeSetting(new HashSet<>());
  }

  public static CookingTimeSetting copyCookingTimeSetting(CookingTimeSetting cookingTimeSetting) {
    return new CookingTimeSetting(cookingTimeSetting);
  }

  public CookingTimeSetting(Set<CookingTime> prohibitedCookingTime) {
    this.prohibitedCookingTime = prohibitedCookingTime;
  }

  public CookingTimeSetting prohibitCookingTime(CookingTime cookingTime) {
    var newSet = new HashSet<>(prohibitedCookingTime);
    newSet.add(cookingTime);
    return new CookingTimeSetting(newSet);
  }

  public CookingTimeSetting allowCookingTime(CookingTime cookingTime) {
    var newSet = new HashSet<>(prohibitedCookingTime);
    newSet.remove(cookingTime);
    return new CookingTimeSetting(newSet);
  }

  public boolean isTimeProhibited(CookingTime cookingTime) {
    return prohibitedCookingTime.contains(cookingTime);
  }

  @Override
  public boolean prohibits(Meal meal) {
    return prohibitedCookingTime.contains(meal.getTypedMealFact(CookingTimeFact.class).getCookingTime());
  }

  public boolean contains(CookingTime cookingTime) {
    return prohibitedCookingTime.contains(cookingTime);
  }

  @Override
  public FactXml convertToXml() {
    return new CookingTimeSettingXml(prohibitedCookingTime.stream().collect(Collectors.toUnmodifiableList()));
  }

  @Override
  public String toString() {
    return "CookingTimeSetting [prohibitedCookingTime=" + prohibitedCookingTime + "]";
  }

  @Override
  public int hashCode() {
    return 31 + (prohibitedCookingTime == null ? 0 : prohibitedCookingTime.hashCode());
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
    return prohibitedCookingTime.equals(other.prohibitedCookingTime);
  }
}
