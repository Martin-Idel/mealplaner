package mealplaner.xml.model.v2;

import static java.nio.charset.Charset.forName;
import static java.util.UUID.nameUUIDFromBytes;
import static mealplaner.commons.NonnegativeInteger.ZERO;
import static mealplaner.model.enums.CookingPreference.NO_PREFERENCE;
import static mealplaner.model.enums.CookingTime.VERY_SHORT;
import static mealplaner.model.enums.CourseType.MAIN;
import static mealplaner.model.enums.ObligatoryUtensil.POT;
import static mealplaner.model.enums.Sidedish.NONE;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.CourseType;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;
import mealplaner.xml.util.UuidAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MealXml {
  @XmlJavaTypeAdapter(value = UuidAdapter.class)
  public UUID uuid;
  public String name;
  public CookingTime cookingTime;
  public Sidedish sidedish;
  public ObligatoryUtensil obligatoryUtensil;
  public CookingPreference cookingPreference;
  public CourseType courseType;
  public int daysPassed;
  public String comment;
  public RecipeXml recipe;

  public MealXml() {
    this(nameUUIDFromBytes("noname".getBytes(forName("UTF-8"))),
        "noname", VERY_SHORT, NONE, POT, NO_PREFERENCE, MAIN, ZERO, "no comment", null);
  }

  public MealXml(UUID uuid,
      String name,
      CookingTime cookingTime,
      Sidedish sideDish,
      ObligatoryUtensil obligatoryUtensil,
      CookingPreference cookingPreference,
      CourseType courseType,
      NonnegativeInteger daysPassed,
      String comment,
      RecipeXml recipe)
      throws MealException {
    this.uuid = uuid;
    this.name = name;
    this.cookingTime = cookingTime;
    this.sidedish = sideDish;
    this.obligatoryUtensil = obligatoryUtensil;
    this.cookingPreference = cookingPreference;
    this.courseType = courseType;
    this.daysPassed = daysPassed.value;
    this.comment = comment;
    this.recipe = recipe;
  }
}
