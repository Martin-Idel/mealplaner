// SPDX-License-Identifier: MIT

package mealplaner.plugins.vegetarian.mealextension;

import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealFactXml;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class VegetarianFact implements MealFact, MealFactXml {
  private final boolean vegetarian;

  public VegetarianFact() {
    vegetarian = false;
  }

  public VegetarianFact(boolean vegetarian) {
    this.vegetarian = vegetarian;
  }

  public static VegetarianFact containsMeat() {
    return new VegetarianFact(false);
  }

  public static VegetarianFact vegetarian() {
    return new VegetarianFact(true);
  }

  public boolean isVegetarian() {
    return vegetarian;
  }

  @Override
  public MealFactXml convertToXml() {
    return this;
  }

  @Override
  public MealFact convertToFact() {
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VegetarianFact that = (VegetarianFact) o;
    return vegetarian == that.vegetarian;
  }

  @Override
  public int hashCode() {
    return Objects.hash(vegetarian);
  }

  @Override
  public String toString() {
    return "VegetarianFact{vegetarian=" + vegetarian + "}";
  }
}
