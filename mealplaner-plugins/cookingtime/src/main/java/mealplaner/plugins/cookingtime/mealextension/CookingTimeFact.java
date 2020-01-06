// SPDX-License-Identifier: MIT

package mealplaner.plugins.cookingtime.mealextension;

import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealFactXml;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CookingTimeFact implements MealFact, MealFactXml {
  private final CookingTime cookingTime;

  public CookingTimeFact() {
    cookingTime = CookingTime.SHORT;
  }

  public CookingTimeFact(CookingTime setting) {
    this.cookingTime = setting;
  }

  public CookingTime getCookingTime() {
    return cookingTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CookingTimeFact other = (CookingTimeFact) o;
    return cookingTime == other.cookingTime;
  }

  @Override
  public int hashCode() {
    return Objects.hash(cookingTime);
  }

  @Override
  public String toString() {
    return cookingTime.toString();
  }
}
