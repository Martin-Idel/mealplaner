// SPDX-License-Identifier: MIT

package mealplaner.io.xml.model.v3;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.model.settings.enums.CasseroleSettings;
import mealplaner.model.settings.enums.CourseSettings;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SettingsXml {
  public final CasseroleSettings casseroleSettings;
  public final CourseSettings courseSettings;
  public final int numberOfPeople;
  @XmlAnyElement(lax = true)
  public final List<Object> settings;

  public SettingsXml() {
    this(1, CasseroleSettings.POSSIBLE,
        CourseSettings.ONLY_MAIN, new ArrayList<>());
  }

  public SettingsXml(
      int numberOfPeople,
      CasseroleSettings casseroleSettings,
      CourseSettings courseSettings,
      List<Object> settings) {
    this.numberOfPeople = numberOfPeople;
    this.casseroleSettings = casseroleSettings;
    this.courseSettings = courseSettings;
    this.settings = settings;
  }
}
