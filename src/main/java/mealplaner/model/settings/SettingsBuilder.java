package mealplaner.model.settings;

import static mealplaner.commons.NonnegativeInteger.TWO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.settings.enums.CourseSettings;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.Fact;
import mealplaner.plugins.api.Setting;
import mealplaner.plugins.plugins.cookingtime.settingextension.CookingTimeSetting;
import mealplaner.plugins.plugins.preference.settingextension.CookingPreferenceSetting;
import mealplaner.plugins.plugins.utensil.settingextension.CasseroleSettings;
import mealplaner.plugins.plugins.utensil.settingextension.CasseroleSubSetting;

public class SettingsBuilder {
  private Set<Class<? extends Fact>> validationStore;
  private NonnegativeInteger numberOfPeople = TWO;
  private CourseSettings courseSettings = CourseSettings.ONLY_MAIN;
  private Map<Class, Setting> subSettings = new HashMap<>();
  private List<Element> hiddenSubSettings = new ArrayList<>();

  private SettingsBuilder() {
  }

  private SettingsBuilder(Set<Class<? extends Fact>> validationStore) {
    this.validationStore = validationStore;
  }

  public static Settings defaultSetting(PluginStore pluginStore) {
    var facts = pluginStore.getRegisteredSettingExtensions().getAllRegisteredFacts();
    var settingsBuilder = new SettingsBuilder(facts);
    for (var settingsFact : facts) {
      settingsBuilder.addSetting(
          pluginStore.getRegisteredSettingExtensions().getDefault(settingsFact));
    }
    return settingsBuilder.create();
  }

  public static SettingsBuilder setting() {
    return new SettingsBuilder();
  }

  public static SettingsBuilder settingsWithValidation(Set<Class<? extends Fact>> validationStore) {
    return new SettingsBuilder(validationStore);
  }

  public static SettingsBuilder from(Settings settings) {
    return new SettingsBuilder()
        .course(settings.getCourseSettings())
        .numberOfPeople(settings.getNumberOfPeople())
        .addSettingsMap(settings.getSubSettings())
        .addHiddenSubSettings(settings.getHiddenSubSettings());
  }

  public SettingsBuilder casserole(CasseroleSettings casseroleSettings) {
    this.subSettings.put(CasseroleSubSetting.class, new CasseroleSubSetting(casseroleSettings));
    return this;
  }

  public SettingsBuilder preference(CookingPreferenceSetting preference) {
    this.subSettings.put(CookingPreferenceSetting.class, preference);
    return this;
  }

  public SettingsBuilder time(CookingTimeSetting cookingTime) {
    this.subSettings.put(CookingTimeSetting.class, cookingTime);
    return this;
  }

  public SettingsBuilder numberOfPeople(NonnegativeInteger numberOfPeople) {
    this.numberOfPeople = numberOfPeople;
    return this;
  }

  public SettingsBuilder course(CourseSettings courseSettings) {
    this.courseSettings = courseSettings;
    return this;
  }

  public SettingsBuilder addSettingsMap(Map<Class, Setting> subSettings) {
    this.subSettings.putAll(subSettings);
    return this;
  }

  public SettingsBuilder addSetting(Setting subSetting) {
    this.subSettings.put(subSetting.getClass(), subSetting);
    return this;
  }

  public SettingsBuilder addHiddenSubSettings(List<Element> hiddenSubSettings) {
    this.hiddenSubSettings = hiddenSubSettings;
    return this;
  }

  public Settings create() {
    if (validationStore != null) {
      validateFacts();
    }
    return new Settings(
        numberOfPeople,
        courseSettings,
        subSettings,
        hiddenSubSettings);
  }

  private void validateFacts() {
    for (var fact : validationStore) {
      if (!subSettings.containsKey(fact)) {
        throw new MealException("Class does not contain fact of type " + fact);
      }
    }
  }
}
