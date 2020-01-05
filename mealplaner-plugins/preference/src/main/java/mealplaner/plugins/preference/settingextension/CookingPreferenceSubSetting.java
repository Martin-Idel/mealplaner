package mealplaner.plugins.preference.settingextension;

import static mealplaner.plugins.preference.settingextension.PreferenceSettings.NORMAL;

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
public class CookingPreferenceSubSetting implements Setting, SettingXml {
  private final PreferenceSettings preferences;

  public CookingPreferenceSubSetting() {
    this.preferences = NORMAL;
  }

  public CookingPreferenceSubSetting(PreferenceSettings preferences) {
    this.preferences = preferences;
  }

  public PreferenceSettings getPreferences() {
    return preferences;
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
    return "[preferences=" + preferences + ']';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CookingPreferenceSubSetting that = (CookingPreferenceSubSetting) o;
    return preferences == that.preferences;
  }

  @Override
  public int hashCode() {
    return Objects.hash(preferences);
  }

}
