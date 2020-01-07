// SPDX-License-Identifier: MIT

package mealplaner.plugins.builtins.courses;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.PluginDescription;

public class BuiltinCoursesPlugin implements PluginDescription {
  @Override
  public void registerPlugins(PluginStore pluginStore) {
    pluginStore.registerMealExtension(CourseTypeFact.class, CourseTypeFact.class, CourseTypeFact::new);
    pluginStore.registerSettingExtension(CourseTypeSetting.class, CourseTypeSetting.class, CourseTypeSetting::new);
  }

  @Override
  public Optional<ResourceBundle> getMessageBundle(Locale locale) {
    return Optional.empty();
  }

  @Override
  public Optional<ResourceBundle> getErrorBundle(Locale locale) {
    return Optional.empty();
  }
}
