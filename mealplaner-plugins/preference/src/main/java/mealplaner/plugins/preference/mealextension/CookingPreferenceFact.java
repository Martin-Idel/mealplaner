// SPDX-License-Identifier: MIT

package mealplaner.plugins.preference.mealextension;

import static mealplaner.plugins.preference.mealextension.CookingPreference.NO_PREFERENCE;

import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealFactXml;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CookingPreferenceFact implements MealFact, MealFactXml {
  private final CookingPreference cookingPreference;

  public CookingPreferenceFact() {
    this.cookingPreference = NO_PREFERENCE;
  }

  public CookingPreferenceFact(CookingPreference preference) {
    this.cookingPreference = preference;
  }

  public CookingPreference getCookingPreference() {
    return cookingPreference;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CookingPreferenceFact that = (CookingPreferenceFact) o;
    return cookingPreference == that.cookingPreference;
  }

  @Override
  public int hashCode() {
    return Objects.hash(cookingPreference);
  }

  @Override
  public String toString() {
    return cookingPreference.toString();
  }
}
