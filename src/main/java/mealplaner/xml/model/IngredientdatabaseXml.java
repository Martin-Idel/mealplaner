package mealplaner.xml.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "IngredientData")
public class IngredientdatabaseXml {
  public int version;
  @XmlElementWrapper(name = "ingredients")
  @XmlElement(name = "ingredient")
  public final List<IngredientXml> ingredients;

  public IngredientdatabaseXml() {
    this(new ArrayList<>(), 1);
  }

  public IngredientdatabaseXml(List<IngredientXml> ingredients, int version) {
    this.ingredients = ingredients;
    this.version = version;
  }
}
