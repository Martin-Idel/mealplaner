package mealplaner.plugins.plugins.cookingtime;

import static mealplaner.plugins.plugins.cookingtime.CookingTimeSetting.cookingTimeWithProhibited;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.plugins.api.Fact;
import mealplaner.plugins.api.SettingXml;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CookingTimeSettingXml implements SettingXml {
  @XmlElementWrapper(name = "cookingTimes")
  @XmlElement(name = "cookingTime")
  public final List<CookingTime> cookingTimes;

  public CookingTimeSettingXml() {
    cookingTimes = new ArrayList<>();
  }

  public CookingTimeSettingXml(List<CookingTime> cookingTimes) {
    this.cookingTimes = cookingTimes;
  }

  @Override
  public Fact convertToFact() {
    return cookingTimeWithProhibited(
        cookingTimes.toArray(new CookingTime[0]));  // NOPMD
  }
}
