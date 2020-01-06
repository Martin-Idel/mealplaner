// SPDX-License-Identifier: MIT

package mealplaner.plugins.utensil.settingextension;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withEnumContent;
import static mealplaner.model.settings.SettingsBuilder.from;

import java.util.List;

import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.api.SettingsInputDialogExtension;

public class SettingsCasseroleSetting implements SettingsInputDialogExtension {
  @Override
  public FlexibleTableBuilder addTableColumns(FlexibleTableBuilder table, List<Settings> settings) {
    return table.addColumn(withEnumContent(CasseroleSettings.class)
        .withColumnName(BUNDLES.message("casseroleColumn"))
        .getValueFromOrderedList(
            settings,
            setting -> setting.getTypedSubSetting(CasseroleSubSetting.class).getCasseroleSettings())
        .setValueToOrderedImmutableList(settings,
            (setting, casserole) -> from(setting).changeSetting(new CasseroleSubSetting(casserole)).create())
        .setPreferredSize(100)
        .isEditable()
        .buildWithOrderNumber(30));
  }
}
