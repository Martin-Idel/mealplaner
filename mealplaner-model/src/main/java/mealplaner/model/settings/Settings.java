// SPDX-License-Identifier: MIT

package mealplaner.model.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.plugins.api.Setting;

public final class Settings {
  private final NonnegativeInteger numberOfPeople;
  private final Map<Class, Setting> subSettings;
  private final List<Element> hiddenSubSettings;

  Settings(
      NonnegativeInteger numberOfPeople,
      Map<Class, Setting> settingFacts,
      List<Element> hiddenSubSettings) {
    this.subSettings = settingFacts;
    this.hiddenSubSettings = hiddenSubSettings;
    this.numberOfPeople = numberOfPeople;
  }

  public NonnegativeInteger getNumberOfPeople() {
    return numberOfPeople;
  }

  public Map<Class, Setting> getSubSettings() {
    return new HashMap<>(subSettings);
  }

  public List<Element> getHiddenSubSettings() {
    return new ArrayList<>(hiddenSubSettings);
  }


  /**
   * N.B.: Return value may be null
   *
   * @param name class of the meal fact
   * @return Setting corresponding to the class if available (null otherwise)
   */
  public Setting getSubSetting(Class name) {
    return subSettings.get(name);
  }

  @SuppressWarnings("unchecked")
  public <T extends Setting> T getTypedSubSetting(Class<T> name) {
    return (T) subSettings.get(name);
  }

  public <T extends Setting> T getTypedSubSettingOrDefault(Class<T> name, T defaultObject) {
    T typedSubSetting = getTypedSubSetting(name);
    return typedSubSetting != null ? typedSubSetting : defaultObject;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + numberOfPeople.value;
    result = prime * result + subSettings.hashCode();
    result = prime * result + hiddenSubSettings.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Settings other = (Settings) obj;
    return numberOfPeople.equals(other.numberOfPeople)
        && subSettings.equals(other.subSettings)
        && hiddenSubSettings.equals(other.hiddenSubSettings);
  }

  @Override
  public String toString() {
    return "Settings{"
        + "numberOfPeople=" + numberOfPeople
        + ", subSettings=" + subSettings
        + ", hiddenSubSettings=" + hiddenSubSettings + "}";
  }
}
