// SPDX-License-Identifier: MIT

package mealplaner.io.xml.model.v3;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "IngredientData")
public class IngredientdatabaseXml {
  public int version = 3;
  @XmlElementWrapper(name = "ingredients")
  @XmlElement(name = "ingredient")
  public final List<IngredientXml> ingredients;

  public IngredientdatabaseXml() {
    this(new ArrayList<>());
  }

  public IngredientdatabaseXml(List<IngredientXml> ingredients) {
    this.ingredients = ingredients;
  }
}
