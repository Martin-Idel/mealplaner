// SPDX-License-Identifier: MIT

package mealplaner.io.xml.model.v2;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.model.meal.enums.CookingTime;
import mealplaner.model.settings.enums.CasseroleSettings;
import mealplaner.model.settings.enums.CourseSettings;
import mealplaner.model.settings.enums.PreferenceSettings;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SettingsXml {
  public CasseroleSettings casseroleSettings;
  public PreferenceSettings preferenceSettings;
  public CourseSettings courseSettings;
  @XmlElementWrapper(name = "cookingTimes")
  @XmlElement(name = "cookingTime")
  public List<CookingTime> cookingTime;
  public int numberOfPeople;

  public SettingsXml() {
    this(new ArrayList<>(), 1, CasseroleSettings.POSSIBLE, PreferenceSettings.NORMAL,
        CourseSettings.ONLY_MAIN);
  }

  public SettingsXml(List<CookingTime> cookingTime,
      int numberOfPeople,
      CasseroleSettings casseroleSettings,
      PreferenceSettings preferenceSettings,
      CourseSettings courseSettings) {
    this.cookingTime = cookingTime;
    this.numberOfPeople = numberOfPeople;
    this.casseroleSettings = casseroleSettings;
    this.preferenceSettings = preferenceSettings;
    this.courseSettings = courseSettings;
  }
}
