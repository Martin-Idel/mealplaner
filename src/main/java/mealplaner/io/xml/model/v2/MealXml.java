// SPDX-License-Identifier: MIT

package mealplaner.io.xml.model.v2;

import static java.nio.charset.Charset.forName;
import static java.util.UUID.nameUUIDFromBytes;
import static mealplaner.commons.NonnegativeInteger.ZERO;
import static mealplaner.plugins.builtins.courses.CourseType.MAIN;
import static mealplaner.plugins.plugins.cookingtime.mealextension.CookingTime.VERY_SHORT;
import static mealplaner.plugins.plugins.preference.mealextension.CookingPreference.NO_PREFERENCE;
import static mealplaner.plugins.plugins.sidedish.mealextension.Sidedish.NONE;
import static mealplaner.plugins.plugins.utensil.mealextension.ObligatoryUtensil.POT;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.io.xml.util.UuidAdapter;
import mealplaner.plugins.builtins.courses.CourseType;
import mealplaner.plugins.plugins.cookingtime.mealextension.CookingTime;
import mealplaner.plugins.plugins.preference.mealextension.CookingPreference;
import mealplaner.plugins.plugins.sidedish.mealextension.Sidedish;
import mealplaner.plugins.plugins.utensil.mealextension.ObligatoryUtensil;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MealXml {
  @XmlJavaTypeAdapter(value = UuidAdapter.class)
  public final UUID uuid;
  public final String name;
  public final CookingTime cookingTime;
  public final Sidedish sidedish;
  public final ObligatoryUtensil obligatoryUtensil;
  public final CookingPreference cookingPreference;
  public final CourseType courseType;
  public final int daysPassed;
  public final String comment;
  public final RecipeXml recipe;

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
