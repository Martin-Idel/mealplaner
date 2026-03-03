// SPDX-License-Identifier: MIT

package mealplaner.plugins.childpreference.settingextension;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withBooleanContent;
import static mealplaner.model.settings.SettingsBuilder.from;

import java.util.List;

import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.api.SettingsInputDialogExtension;

public class SettingsChildPreferenceSettings implements SettingsInputDialogExtension {
  @Override
  public FlexibleTableBuilder addTableColumns(FlexibleTableBuilder table, List<Settings> settings) {
    table.addColumn(withBooleanContent()
        .withColumnName(BUNDLES.message("childFriendlyColumn"))
        .getValueFromOrderedList(settings, setting ->
            setting.getTypedSubSetting(ChildPreferenceSubSetting.class).isOnlyChildFriendly())
        .setValueToOrderedImmutableList(settings, (element, value) ->
            from(element).changeSetting(new ChildPreferenceSubSetting(Boolean.TRUE.equals(value))).create())
        .isEditable()
        .setPreferredSize(80)
        .buildWithOrderNumber(10));
    return table;
  }
}