// SPDX-License-Identifier: MIT

package mealplaner.io.xml.model.v3;

import static java.util.UUID.nameUUIDFromBytes;
import static mealplaner.commons.NonnegativeInteger.ZERO;
import static mealplaner.model.meal.enums.CourseType.MAIN;
import static mealplaner.model.meal.enums.ObligatoryUtensil.POT;
import static mealplaner.model.meal.enums.Sidedish.NONE;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.io.xml.util.UuidAdapter;
import mealplaner.model.meal.enums.CourseType;
import mealplaner.model.meal.enums.ObligatoryUtensil;
import mealplaner.model.meal.enums.Sidedish;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MealXml {
  @XmlJavaTypeAdapter(value = UuidAdapter.class)
  public final UUID uuid;
  public final String name;
  public final Sidedish sidedish;
  public final ObligatoryUtensil obligatoryUtensil;
  public final CourseType courseType;
  public final int daysPassed;
  public final String comment;
  @XmlAnyElement(lax = true)
  public final List<Object> mealFacts;
  public final RecipeXml recipe;

  public MealXml() {
    this(nameUUIDFromBytes("noname".getBytes(StandardCharsets.UTF_8)),
        "noname", NONE, POT, MAIN, ZERO, "no comment",
        new ArrayList<>(), null);
  }

  public MealXml(UUID uuid,
      String name,
      Sidedish sideDish,
      ObligatoryUtensil obligatoryUtensil,
      CourseType courseType,
      NonnegativeInteger daysPassed,
      String comment,
      List<Object> mealFacts,
      RecipeXml recipe)
      throws MealException {
    this.uuid = uuid;
    this.name = name;
    this.sidedish = sideDish;
    this.obligatoryUtensil = obligatoryUtensil;
    this.courseType = courseType;
    this.daysPassed = daysPassed.value;
    this.comment = comment;
    this.mealFacts = mealFacts;
    this.recipe = recipe;
  }
}
