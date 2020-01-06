// SPDX-License-Identifier: MIT

package mealplaner.plugins.builtins.courses;

import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.plugins.api.Fact;
import mealplaner.plugins.api.FactXml;
import mealplaner.plugins.api.Setting;
import mealplaner.plugins.api.SettingXml;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CourseTypeSetting implements Setting, SettingXml {
  private final CourseSettings courseSetting;

  public CourseTypeSetting() {
    this.courseSetting = CourseSettings.ONLY_MAIN;
  }

  public CourseTypeSetting(CourseSettings preferences) {
    this.courseSetting = preferences;
  }

  public CourseSettings getCourseSetting() {
    return courseSetting;
  }

  @Override
  public Fact convertToFact() {
    return this;
  }

  @Override
  public FactXml convertToXml() {
    return this;
  }

  @Override
  public String toString() {
    return "[preferences=" + courseSetting + ']';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CourseTypeSetting that = (CourseTypeSetting) o;
    return courseSetting == that.courseSetting;
  }

  @Override
  public int hashCode() {
    return Objects.hash(courseSetting);
  }

}
