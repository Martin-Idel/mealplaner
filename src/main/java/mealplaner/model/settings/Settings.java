package mealplaner.model.settings;

import static mealplaner.commons.NonnegativeInteger.TWO;
import static mealplaner.model.enums.CasseroleSettings.POSSIBLE;
import static mealplaner.model.enums.CourseSettings.ONLY_MAIN;
import static mealplaner.model.enums.PreferenceSettings.NORMAL;
import static mealplaner.model.settings.CookingPreferenceSetting.createCookingPreferenceSettings;
import static mealplaner.model.settings.CookingTimeSetting.copyCookingTimeSetting;
import static mealplaner.model.settings.CookingTimeSetting.defaultCookingTime;
import static mealplaner.model.settings.CookingUtensilSetting.copyUtensilSetting;
import static mealplaner.model.settings.CookingUtensilSetting.createCookingUtensilSettings;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.model.enums.CasseroleSettings;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.CourseSettings;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.PreferenceSettings;

public final class Settings {
  private final CasseroleSettings casseroleSettings;
  private final PreferenceSettings preference;
  private final CookingPreferenceSetting cookingPreferences;
  private final CookingTimeSetting cookingTime;
  private final CookingUtensilSetting cookingUtensil;
  private final NonnegativeInteger numberOfPeople;
  private final CourseSettings courseSettings;

  private Settings(CookingTimeSetting cookingTime,
      NonnegativeInteger numberOfPeople,
      CasseroleSettings casseroleSettings,
      PreferenceSettings preferenceSettings,
      CourseSettings courseSettings) {
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
  }

  public static Settings from(CookingTimeSetting cookingTime,
      NonnegativeInteger numberOfPeople,
      CasseroleSettings casseroleSettings,
      PreferenceSettings preferenceSettings,
      CourseSettings courseSettings) {
    return new Settings(cookingTime,
        numberOfPeople,
        casseroleSettings,
        preferenceSettings,
        courseSettings);
  }

  public static Settings createSettings() {
    return new Settings(defaultCookingTime(), TWO, POSSIBLE, NORMAL, ONLY_MAIN);
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

  public boolean isTimeProhibited(CookingTime cookingTime) {
    return this.cookingTime.isTimeProhibited(cookingTime);
  }

  public boolean isTimePossible(CookingTime cookingTime) {
    return !isTimeProhibited(cookingTime);
  }

  public boolean shallManyPeopleEat() {
    return cookingUtensil.isUtensilProhibited(ObligatoryUtensil.PAN);
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