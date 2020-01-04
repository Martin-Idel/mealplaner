// SPDX-License-Identifier: MIT

package mealplaner.plugins.cookingtime.settingextension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import mealplaner.model.meal.Meal;
import mealplaner.plugins.api.FactXml;
import mealplaner.plugins.api.Setting;
import mealplaner.plugins.cookingtime.mealextension.CookingTime;
import mealplaner.plugins.cookingtime.mealextension.CookingTimeFact;

public final class CookingTimeSubSetting implements Setting {
  private final Set<CookingTime> prohibitedCookingTime;

  private CookingTimeSubSetting(CookingTimeSubSetting cookingTimeSubSetting) {
    this(new HashSet<>(cookingTimeSubSetting.prohibitedCookingTime));
  }

  public static CookingTimeSubSetting cookingTimeWithProhibited(CookingTime... cookingTime) {
    HashSet<CookingTime> prohibitedCookingTime = new HashSet<>(Arrays.asList(cookingTime));
    return new CookingTimeSubSetting(prohibitedCookingTime);
  }

  public static CookingTimeSubSetting defaultCookingTime() {
    return new CookingTimeSubSetting(new HashSet<>());
  }

  public static CookingTimeSubSetting copyCookingTimeSetting(CookingTimeSubSetting cookingTimeSubSetting) {
    return new CookingTimeSubSetting(cookingTimeSubSetting);
  }

  public CookingTimeSubSetting(Set<CookingTime> prohibitedCookingTime) {
    this.prohibitedCookingTime = prohibitedCookingTime;
  }

  public CookingTimeSubSetting prohibitCookingTime(CookingTime cookingTime) {
    var newSet = new HashSet<>(prohibitedCookingTime);
    newSet.add(cookingTime);
    return new CookingTimeSubSetting(newSet);
  }

  public CookingTimeSubSetting allowCookingTime(CookingTime cookingTime) {
    var newSet = new HashSet<>(prohibitedCookingTime);
    newSet.remove(cookingTime);
    return new CookingTimeSubSetting(newSet);
  }

  public boolean isTimeProhibited(CookingTime cookingTime) {
    return prohibitedCookingTime.contains(cookingTime);
  }

  public boolean prohibits(Meal meal) {
    return prohibitedCookingTime.contains(meal.getTypedMealFact(CookingTimeFact.class).getCookingTime());
  }

  public boolean contains(CookingTime cookingTime) {
    return prohibitedCookingTime.contains(cookingTime);
  }

  @Override
  public FactXml convertToXml() {
    return new CookingTimeSubSettingXml(prohibitedCookingTime.stream().collect(Collectors.toUnmodifiableList()));
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
    CookingTimeSubSetting other = (CookingTimeSubSetting) obj;
    return prohibitedCookingTime.equals(other.prohibitedCookingTime);
  }
}
