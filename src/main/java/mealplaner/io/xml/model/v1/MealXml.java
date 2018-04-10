package mealplaner.io.xml.model.v1;

import static mealplaner.commons.NonnegativeInteger.ZERO;
import static mealplaner.model.meal.enums.CookingPreference.NO_PREFERENCE;
import static mealplaner.model.meal.enums.CookingTime.VERY_SHORT;
import static mealplaner.model.meal.enums.ObligatoryUtensil.POT;
import static mealplaner.model.meal.enums.Sidedish.NONE;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.meal.enums.CookingPreference;
import mealplaner.model.meal.enums.CookingTime;
import mealplaner.model.meal.enums.ObligatoryUtensil;
import mealplaner.model.meal.enums.Sidedish;

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
    this("no name", VERY_SHORT, NONE, POT, NO_PREFERENCE, ZERO, "no comment", null);
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
