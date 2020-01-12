// SPDX-License-Identifier: MIT

package mealplaner.plugins.builtins.courses;

import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealFactXml;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class CourseTypeFact implements MealFact, MealFactXml {
  private final CourseType courseType;

  public CourseTypeFact() {
    this.courseType = CourseType.MAIN;
  }

  public CourseTypeFact(CourseType preference) {
    this.courseType = preference;
  }

  public CourseType getCourseType() {
    return courseType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CourseTypeFact that = (CourseTypeFact) o;
    return courseType == that.courseType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(courseType);
  }

  @Override
  public String toString() {
    return courseType.toString();
  }
}
