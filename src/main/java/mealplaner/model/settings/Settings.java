// SPDX-License-Identifier: MIT

package mealplaner.model.settings;

import static mealplaner.commons.NonnegativeInteger.TWO;
import static mealplaner.model.settings.enums.CasseroleSettings.POSSIBLE;
import static mealplaner.model.settings.enums.CourseSettings.ONLY_MAIN;
import static mealplaner.model.settings.enums.PreferenceSettings.NORMAL;
import static mealplaner.model.settings.subsettings.CookingPreferenceSetting.createCookingPreferenceSettings;
import static mealplaner.model.settings.subsettings.CookingTimeSetting.copyCookingTimeSetting;
import static mealplaner.model.settings.subsettings.CookingTimeSetting.defaultCookingTime;
import static mealplaner.model.settings.subsettings.CookingUtensilSetting.copyUtensilSetting;
import static mealplaner.model.settings.subsettings.CookingUtensilSetting.createCookingUtensilSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.model.settings.enums.CasseroleSettings;
import mealplaner.model.settings.enums.CourseSettings;
import mealplaner.model.settings.enums.PreferenceSettings;
import mealplaner.model.settings.subsettings.CookingPreferenceSetting;
import mealplaner.model.settings.subsettings.CookingTimeSetting;
import mealplaner.model.settings.subsettings.CookingUtensilSetting;
import mealplaner.plugins.api.Setting;

public final class Settings {
  private final CasseroleSettings casseroleSettings;
  private final PreferenceSettings preference;
  private final CookingPreferenceSetting cookingPreferences;
  private final CookingTimeSetting cookingTime;
  private final CookingUtensilSetting cookingUtensil;
  private final NonnegativeInteger numberOfPeople;
  private final CourseSettings courseSettings;
  private final Map<Class, Setting> subSettings;
  private final List<Element> hiddenSubSettings;

  private Settings(
      CookingTimeSetting cookingTime,
      NonnegativeInteger numberOfPeople,
      CasseroleSettings casseroleSettings,
      PreferenceSettings preferenceSettings,
      CourseSettings courseSettings,
      Map<Class, Setting> mealFacts,
      List<Element> hiddenSubSettings) {
    this.subSettings = mealFacts;
    this.hiddenSubSettings = hiddenSubSettings;
    this.cookingPreferences = createCookingPreferenceSettings();
    this.cookingTime = cookingTime;
    this.cookingUtensil = createCookingUtensilSettings();
    this.numberOfPeople = numberOfPeople;
    this.cookingUtensil.setNumberOfPeople(numberOfPeople.value);
    this.casseroleSettings = casseroleSettings;
    this.cookingUtensil.setCasseroleSettings(casseroleSettings);
    this.preference = preferenceSettings;
    this.cookingPreferences.setCookingPreferences(preference);
    this.courseSettings = courseSettings;
  }

  private Settings(Settings setting) {
    this.casseroleSettings = setting.getCasserole();
    this.preference = setting.getPreference();
    this.numberOfPeople = setting.getNumberOfPeople();
    this.cookingPreferences = createCookingPreferenceSettings();
    this.cookingPreferences.setCookingPreferences(this.preference);
    this.cookingTime = copyCookingTimeSetting(setting.getCookingTime());
    this.cookingUtensil = copyUtensilSetting(setting.getCookingUtensil());
    this.courseSettings = setting.getCourseSettings();
    this.subSettings = setting.getSubSettings();
    this.hiddenSubSettings = setting.getHiddenSubSettings();
  }

  public static Settings from(
      CookingTimeSetting cookingTime,
      NonnegativeInteger numberOfPeople,
      CasseroleSettings casseroleSettings,
      PreferenceSettings preferenceSettings,
      CourseSettings courseSettings,
      Map<Class, Setting> subSettings,
      List<Element> hiddenSubSettings) {
    return new Settings(cookingTime,
        numberOfPeople,
        casseroleSettings,
        preferenceSettings,
        courseSettings,
        subSettings,
        hiddenSubSettings);
  }

  public static Settings from(
      CookingTimeSetting cookingTime,
      NonnegativeInteger numberOfPeople,
      CasseroleSettings casseroleSettings,
      PreferenceSettings preferenceSettings,
      CourseSettings courseSettings) {
    return new Settings(cookingTime,
        numberOfPeople,
        casseroleSettings,
        preferenceSettings,
        courseSettings, new HashMap<>(), new ArrayList<>());
  }

  public static Settings createSettings() {
    return new Settings(defaultCookingTime(), TWO, POSSIBLE, NORMAL, ONLY_MAIN, new HashMap<>(), new ArrayList<>());
  }

  public static Settings copy(Settings setting) {
    return new Settings(setting);
  }

  public Settings changeCookingTime(CookingTimeSetting cookingTime) {
    return Settings.from(cookingTime, numberOfPeople, casseroleSettings, preference,
        courseSettings);
  }

  public Settings changeNumberOfPeople(NonnegativeInteger numberOfPeople) {
    return Settings.from(cookingTime, numberOfPeople, casseroleSettings, preference,
        courseSettings);
  }

  public Settings changeCasserole(CasseroleSettings casseroleSettings) {
    return Settings.from(cookingTime, numberOfPeople, casseroleSettings, preference,
        courseSettings);
  }

  public Settings changePreference(PreferenceSettings preferenceSettings) {
    return Settings.from(cookingTime, numberOfPeople, casseroleSettings, preferenceSettings,
        courseSettings);
  }

  public Settings changeCourseSettings(CourseSettings courseSettings) {
    return Settings.from(cookingTime, numberOfPeople, casseroleSettings, preference,
        courseSettings);
  }

  public NonnegativeInteger getNumberOfPeople() {
    return numberOfPeople;
  }

  public CookingTimeSetting getCookingTime() {
    return cookingTime;
  }

  public CookingUtensilSetting getCookingUtensil() {
    return cookingUtensil;
  }

  public CookingPreferenceSetting getCookingPreference() {
    return cookingPreferences;
  }

  public CasseroleSettings getCasserole() {
    return casseroleSettings;
  }

  public PreferenceSettings getPreference() {
    return preference;
  }

  public CourseSettings getCourseSettings() {
    return courseSettings;
  }

  public Map<Class, Setting> getSubSettings() {
    return subSettings;
  }

  public List<Element> getHiddenSubSettings() {
    return hiddenSubSettings;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + casseroleSettings.hashCode();
    result = prime * result + cookingPreferences.hashCode();
    result = prime * result + cookingTime.hashCode();
    result = prime * result + cookingUtensil.hashCode();
    result = prime * result + preference.hashCode();
    result = prime * result + courseSettings.hashCode();
    result = prime * result * numberOfPeople.value;
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
    return casseroleSettings == other.casseroleSettings
        && cookingPreferences.equals(other.cookingPreferences)
        && cookingTime.equals(other.cookingTime)
        && cookingUtensil.equals(other.cookingUtensil)
        && preference.equals(other.preference)
        && courseSettings.equals(other.courseSettings)
        && numberOfPeople.equals(other.numberOfPeople);
  }

  @Override
  public String toString() {
    return "Settings [casseroleSettings=" + casseroleSettings + ", preference=" + preference
        + ", cookingPreferences=" + cookingPreferences + ", cookingTime=" + cookingTime
        + ", cookingUtensil=" + cookingUtensil + ", courseSettings=" + courseSettings
        + ", numberOfPeople=" + numberOfPeople + "]";
  }
}