package mealplaner.plugins.plugins.preference.setting;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withEnumContent;
import static mealplaner.model.settings.SettingsBuilder.from;

import java.util.List;

import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.api.SettingsInputDialogExtension;

public class SettingsCookingPreferenceSettings implements SettingsInputDialogExtension {
  @Override
  public FlexibleTableBuilder addTableColumns(FlexibleTableBuilder table, List<Settings> settings) {
    return table.addColumn(withEnumContent(PreferenceSettings.class)
        .withColumnName(BUNDLES.message("preferenceColumn"))
        .getValueFromOrderedList(settings,
            setting -> setting.getTypedSubSetting(CookingPreferenceSetting.class).getPreferences())
        .setValueToOrderedImmutableList(settings,
            (setting, preference) -> from(setting).preference(new CookingPreferenceSetting(preference)).create())
        .isEditable()
        .setPreferredSize(100)
        .buildWithOrderNumber(40));
  }
}
