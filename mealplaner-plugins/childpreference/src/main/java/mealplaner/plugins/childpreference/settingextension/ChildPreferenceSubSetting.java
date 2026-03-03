// SPDX-License-Identifier: MIT

package mealplaner.plugins.childpreference.settingextension;

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
public class ChildPreferenceSubSetting implements Setting, SettingXml {
  private final boolean onlyChildFriendly;

  public ChildPreferenceSubSetting() {
    this.onlyChildFriendly = false;
  }

  public ChildPreferenceSubSetting(boolean onlyChildFriendly) {
    this.onlyChildFriendly = onlyChildFriendly;
  }

  public boolean isOnlyChildFriendly() {
    return onlyChildFriendly;
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
    return "[onlyChildFriendly=" + onlyChildFriendly + ']';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChildPreferenceSubSetting that = (ChildPreferenceSubSetting) o;
    return onlyChildFriendly == that.onlyChildFriendly;
  }

  @Override
  public int hashCode() {
    return Objects.hash(onlyChildFriendly);
  }
}