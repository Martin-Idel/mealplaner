package mealplaner.plugins.plugins.cookingtime.settingextension;

import static mealplaner.plugins.plugins.cookingtime.settingextension.CookingTimeSubSetting.cookingTimeWithProhibited;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.plugins.api.Fact;
import mealplaner.plugins.api.SettingXml;
import mealplaner.plugins.plugins.cookingtime.mealextension.CookingTime;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CookingTimeSubSettingXml implements SettingXml {
  @XmlElementWrapper(name = "cookingTimes")
  @XmlElement(name = "cookingTime")
  public final List<CookingTime> cookingTimes;

  public CookingTimeSubSettingXml() {
    cookingTimes = new ArrayList<>();
  }

  public CookingTimeSubSettingXml(List<CookingTime> cookingTimes) {
    this.cookingTimes = cookingTimes;
  }

  @Override
  public Fact convertToFact() {
    return cookingTimeWithProhibited(
        cookingTimes.toArray(new CookingTime[0]));  // NOPMD
  }
}
