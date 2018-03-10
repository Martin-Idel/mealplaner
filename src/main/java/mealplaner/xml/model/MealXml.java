package mealplaner.xml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MealXml {
  public String name;
  public CookingTime cookingTime;
  public Sidedish sidedish;
  public ObligatoryUtensil obligatoryUtensil;
  public CookingPreference cookingPreference;
  public int daysPassed;
  public String comment;
  public RecipeXml recipe;

  public MealXml() {
    // Constructor for JAXB
  }

  public MealXml(String name,
      CookingTime cookingTime,
      Sidedish sideDish,
      ObligatoryUtensil obligatoryUtensil,
      CookingPreference cookingPreference,
      NonnegativeInteger daysPassed,
      String comment,
      RecipeXml recipe)
      throws MealException {
    this.name = name;
    this.cookingTime = cookingTime;
    this.sidedish = sideDish;
    this.obligatoryUtensil = obligatoryUtensil;
    this.cookingPreference = cookingPreference;
    this.daysPassed = daysPassed.value;
    this.comment = comment;
    this.recipe = recipe;
  }
}
