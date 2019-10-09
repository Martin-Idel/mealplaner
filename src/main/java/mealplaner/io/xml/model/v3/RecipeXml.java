// SPDX-License-Identifier: MIT

package mealplaner.io.xml.model.v3;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RecipeXml {
  public final int numberOfPortions;
  @XmlElementWrapper(name = "ingredients")
  @XmlElement(name = "ingredient")
  public final List<QuantitativeIngredientXml> quantitativeIngredients;

  public RecipeXml() {
    this(1, new ArrayList<>());
  }

  public RecipeXml(Integer numberOfPortions, List<QuantitativeIngredientXml> quantitativeIngredients) {
    this.numberOfPortions = numberOfPortions;
    this.quantitativeIngredients = quantitativeIngredients;
  }
}
