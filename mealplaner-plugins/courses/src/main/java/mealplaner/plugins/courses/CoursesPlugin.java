package mealplaner.plugins.courses;

import java.util.Optional;
import java.util.ResourceBundle;

import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.PluginDescription;
import mealplaner.plugins.courses.mealextension.MealEditCourseType;
import mealplaner.plugins.courses.mealextension.MealInputCourseType;
import mealplaner.plugins.courses.proposal.CourseTypeProposalStep;
import mealplaner.plugins.courses.settingextension.CourseSettingSubSetting;

public class CoursesPlugin implements PluginDescription {
  @Override
  public void registerPlugins(PluginStore pluginStore) {
    pluginStore.registerMealGuiExtension(new MealInputCourseType(), new MealEditCourseType());
    pluginStore.registerProposalExtension(new CourseSettingSubSetting(), new CourseTypeProposalStep());
  }

  @Override
  public Optional<ResourceBundle> getMessageBundle() {
    return Optional.empty();
  }

  @Override
  public Optional<ResourceBundle> getErrorBundle() {
    return Optional.empty();
  }
}
