package mealplaner.plugins.plugins.preference.settingextension;

import static mealplaner.plugins.plugins.preference.settingextension.PreferenceSettings.NORMAL;

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
public class CookingPreferenceSetting implements Setting, SettingXml {
  private final PreferenceSettings preferences;

  public CookingPreferenceSetting() {
    this.preferences = NORMAL;
  }

  public CookingPreferenceSetting(PreferenceSettings preferences) {
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
    CookingPreferenceSetting that = (CookingPreferenceSetting) o;
    return preferences == that.preferences;
  }

  @Override
  public int hashCode() {
    return Objects.hash(preferences);
  }

}
