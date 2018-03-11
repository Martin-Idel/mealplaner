package mealplaner.xml.model;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RecipeXml {
  public int numberOfPortions;
  public Map<IngredientXml, Integer> ingredients;

  public RecipeXml() {
    this(1, new HashMap<>());
  }

  public RecipeXml(Integer numberOfPortions,
      Map<IngredientXml, Integer> ingredients) {
    this.numberOfPortions = numberOfPortions;
    this.ingredients = ingredients;
  }
}
