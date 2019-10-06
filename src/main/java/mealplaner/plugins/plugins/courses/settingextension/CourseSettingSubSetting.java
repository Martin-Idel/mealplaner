package mealplaner.plugins.plugins.courses.settingextension;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withEnumContent;
import static mealplaner.model.settings.SettingsBuilder.from;

import java.util.List;

import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.api.SettingsInputDialogExtension;
import mealplaner.plugins.builtins.courses.CourseSettings;
import mealplaner.plugins.builtins.courses.CourseTypeSetting;

public class CourseSettingSubSetting implements SettingsInputDialogExtension {
  @Override
  public FlexibleTableBuilder addTableColumns(FlexibleTableBuilder table, List<Settings> settings) {
    return table.addColumn(withEnumContent(CourseSettings.class)
        .withColumnName(BUNDLES.message("courseSettingColumn"))
        .getValueFromOrderedList(settings,
            setting -> setting.getTypedSubSetting(CourseTypeSetting.class).getCourseSetting())
        .setValueToOrderedImmutableList(settings,
            (setting, course) -> from(setting).course(course).create())
        .isEditable()
        .setPreferredSize(120)
        .buildWithOrderNumber(50));
  }
}
