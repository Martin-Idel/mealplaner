package mealplaner.xml.model;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.recipes.model.Ingredient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RecipeXml {
  public int numberOfPortions;
  public Map<Ingredient, Integer> ingredients;

  public RecipeXml() {
    // Constructor for JAXB
  }

  public RecipeXml(Integer numberOfPortions,
      Map<Ingredient, Integer> ingredients) {
    this.numberOfPortions = numberOfPortions;
    this.ingredients = ingredients;
  }
}
