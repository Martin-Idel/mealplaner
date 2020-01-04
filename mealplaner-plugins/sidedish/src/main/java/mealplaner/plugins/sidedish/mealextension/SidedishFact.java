package mealplaner.plugins.sidedish.mealextension;

import static mealplaner.plugins.sidedish.mealextension.Sidedish.NONE;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealFactXml;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SidedishFact implements MealFact, MealFactXml {
  private final Sidedish sidedish;

  public SidedishFact() {
    sidedish = NONE;
  }

  public SidedishFact(Sidedish setting) {
    this.sidedish = setting;
  }

  public Sidedish getSidedish() {
    return sidedish;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SidedishFact other = (SidedishFact) o;
    return sidedish == other.sidedish;
  }

  @Override
  public int hashCode() {
    return Objects.hash(sidedish);
  }

  @Override
  public String toString() {
    return sidedish.toString();
  }
}
