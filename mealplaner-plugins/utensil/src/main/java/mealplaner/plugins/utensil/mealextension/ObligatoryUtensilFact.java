package mealplaner.plugins.utensil.mealextension;

import static mealplaner.plugins.utensil.mealextension.ObligatoryUtensil.POT;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealFactXml;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ObligatoryUtensilFact implements MealFact, MealFactXml {
  private final ObligatoryUtensil obligatoryUtensil;

  public ObligatoryUtensilFact() {
    this.obligatoryUtensil = POT;
  }

  public ObligatoryUtensilFact(ObligatoryUtensil preference) {
    this.obligatoryUtensil = preference;
  }

  public ObligatoryUtensil getObligatoryUtensil() {
    return obligatoryUtensil;
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
    ObligatoryUtensilFact that = (ObligatoryUtensilFact) o;
    return obligatoryUtensil == that.obligatoryUtensil;
  }

  @Override
  public int hashCode() {
    return Objects.hash(obligatoryUtensil);
  }

  @Override
  public String toString() {
    return obligatoryUtensil.toString();
  }
}
