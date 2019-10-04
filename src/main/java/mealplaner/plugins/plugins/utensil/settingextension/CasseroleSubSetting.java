package mealplaner.plugins.plugins.utensil.settingextension;

import static mealplaner.plugins.plugins.utensil.settingextension.CasseroleSettings.POSSIBLE;

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
public class CasseroleSubSetting implements Setting, SettingXml {
  private final CasseroleSettings casseroleSettings;

  public CasseroleSubSetting() {
    casseroleSettings = POSSIBLE;
  }

  public CasseroleSubSetting(CasseroleSettings casseroleSettings) {
    this.casseroleSettings = casseroleSettings;
  }

  public CasseroleSettings getCasseroleSettings() {
    return casseroleSettings;
  }

  @Override
  public FactXml convertToXml() {
    return this;
  }

  @Override
  public Fact convertToFact() {
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CasseroleSubSetting that = (CasseroleSubSetting) o;
    return casseroleSettings == that.casseroleSettings;
  }

  @Override
  public int hashCode() {
    return Objects.hash(casseroleSettings);
  }

  @Override
  public String toString() {
    return "[casseroleSettings=" + casseroleSettings + ']';
  }
}
