// SPDX-License-Identifier: MIT

package mealplaner.plugins.childpreference.mealextension;

import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealFactXml;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ChildPreferenceFact implements MealFact, MealFactXml {
  private final boolean childFriendly;

  public ChildPreferenceFact() {
    childFriendly = false;
  }

  public ChildPreferenceFact(boolean childFriendly) {
    this.childFriendly = childFriendly;
  }

  public static ChildPreferenceFact nonChildFriendly() {
    return new ChildPreferenceFact(false);
  }

  public static ChildPreferenceFact childFriendly() {
    return new ChildPreferenceFact(true);
  }

  public boolean isChildFriendly() {
    return childFriendly;
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
    ChildPreferenceFact that = (ChildPreferenceFact) o;
    return childFriendly == that.childFriendly;
  }

  @Override
  public int hashCode() {
    return Objects.hash(childFriendly);
  }

  @Override
  public String toString() {
    return "ChildPreferenceFact{childFriendly=" + childFriendly + "}";
  }
}