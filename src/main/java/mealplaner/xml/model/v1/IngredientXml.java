package mealplaner.xml.model.v1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.recipes.model.IngredientType;
import mealplaner.recipes.model.Measure;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class IngredientXml {
  public String name;
  public IngredientType type;
  public Measure measure;

  public IngredientXml() {
    this("no name", IngredientType.OTHER, Measure.NONE);
  }

  public IngredientXml(String name, IngredientType type, Measure measure) {
    this.name = name;
    this.type = type;
    this.measure = measure;
  }
}
