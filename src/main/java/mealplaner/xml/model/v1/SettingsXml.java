package mealplaner.xml.model.v1;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.model.enums.CasseroleSettings;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.PreferenceSettings;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SettingsXml {
  public CasseroleSettings casseroleSettings;
  public PreferenceSettings preference;
  @XmlElementWrapper(name = "cookingTimes")
  @XmlElement(name = "cookingTime")
  public List<CookingTime> cookingTime;
  public int numberOfPeople;

  public SettingsXml() {
    this(new ArrayList<>(), 1, CasseroleSettings.POSSIBLE, PreferenceSettings.NORMAL);
  }

  public SettingsXml(List<CookingTime> cookingTime,
      int numberOfPeople,
      CasseroleSettings casseroleSettings,
      PreferenceSettings preferenceSettings) {
    this.cookingTime = cookingTime;
    this.numberOfPeople = numberOfPeople;
    this.casseroleSettings = casseroleSettings;
    this.preference = preferenceSettings;
  }
}
