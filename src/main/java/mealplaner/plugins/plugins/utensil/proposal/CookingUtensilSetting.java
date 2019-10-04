// SPDX-License-Identifier: MIT

package mealplaner.plugins.plugins.utensil.proposal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.model.meal.Meal;
import mealplaner.plugins.plugins.utensil.mealextension.ObligatoryUtensil;
import mealplaner.plugins.plugins.utensil.mealextension.ObligatoryUtensilFact;
import mealplaner.plugins.plugins.utensil.settingextension.CasseroleSettings;

public final class CookingUtensilSetting {
  private final Set<ObligatoryUtensil> prohibitedUtensil;

  private CookingUtensilSetting(Set<ObligatoryUtensil> prohibitedUtensil) {
    this.prohibitedUtensil = prohibitedUtensil;
  }

  public static CookingUtensilSetting createCookingUtensilSettings() {
    return new CookingUtensilSetting(new HashSet<>());
  }

  public static CookingUtensilSetting from(Set<ObligatoryUtensil> utensils) {
    return new CookingUtensilSetting(utensils);
  }

  public static CookingUtensilSetting copyUtensilSetting(
      CookingUtensilSetting cookingUtensilSetting) {
    return new CookingUtensilSetting(new HashSet<>(cookingUtensilSetting.prohibitedUtensil));
  }

  public void setCasseroleSettings(CasseroleSettings casserole) {
    if (casserole == CasseroleSettings.NONE) {
      prohibitedUtensil.add(ObligatoryUtensil.CASSEROLE);
    } else if (casserole == CasseroleSettings.ONLY) {
      prohibitedUtensil.addAll(Arrays.asList(ObligatoryUtensil.values()));
      prohibitedUtensil.remove(ObligatoryUtensil.CASSEROLE);
    }
  }

  public void setNumberOfPeople(NonnegativeInteger number) {
    if (number.value > 3) {
      prohibitedUtensil.add(ObligatoryUtensil.PAN);
    } else {
      prohibitedUtensil.remove(ObligatoryUtensil.PAN);
    }
  }

  public void reset() {
    prohibitedUtensil.removeAll(Arrays.asList(ObligatoryUtensil.values()));
  }

  public boolean isUtensilProhibited(ObligatoryUtensil prohibited) {
    return prohibitedUtensil.contains(prohibited);
  }

  public boolean prohibits(Meal meal) {
    return prohibitedUtensil.contains(
        meal.getTypedMealFact(ObligatoryUtensilFact.class).getObligatoryUtensil());
  }

  public boolean contains(ObligatoryUtensil obligatoryUtensil) {
    return prohibitedUtensil.contains(obligatoryUtensil);
  }

  @Override
  public String toString() {
    return "CookingUtensilSetting [prohibitedUtensil=" + prohibitedUtensil + "]";
  }

  @Override
  public int hashCode() {
    return 31 + ((prohibitedUtensil == null) ? 0 : prohibitedUtensil.hashCode());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    CookingUtensilSetting other = (CookingUtensilSetting) obj;
    return prohibitedUtensil.equals(other.prohibitedUtensil);
  }
}
