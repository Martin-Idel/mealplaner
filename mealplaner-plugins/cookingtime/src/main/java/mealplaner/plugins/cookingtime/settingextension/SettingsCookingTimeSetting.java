// SPDX-License-Identifier: MIT

package mealplaner.plugins.cookingtime.settingextension;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withBooleanContent;
import static mealplaner.model.settings.SettingsBuilder.from;

import java.util.List;

import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.api.SettingsInputDialogExtension;
import mealplaner.plugins.cookingtime.mealextension.CookingTime;

public class SettingsCookingTimeSetting implements SettingsInputDialogExtension {
  @Override
  public FlexibleTableBuilder addTableColumns(FlexibleTableBuilder table, List<Settings> settings) {
    addCookingTimeColumnFor(CookingTime.VERY_SHORT, BUNDLES.message("veryShortColumn"), table, settings, 10);
    addCookingTimeColumnFor(CookingTime.SHORT, BUNDLES.message("shortColumn"), table, settings, 11);
    addCookingTimeColumnFor(CookingTime.MEDIUM, BUNDLES.message("mediumColumn"), table, settings, 12);
    addCookingTimeColumnFor(CookingTime.LONG, BUNDLES.message("longColumn"), table, settings, 13);
    return table;
  }

  private void addCookingTimeColumnFor(
      CookingTime time,
      String columnName,
      FlexibleTableBuilder tableBuilder,
      List<Settings> settings,
      int orderNumber) {
    tableBuilder.addColumn(withBooleanContent()
        .withColumnName(columnName)
        .getValueFromOrderedList(settings,
            setting -> !setting.getTypedSubSetting(CookingTimeSubSetting.class).contains(time))
        .setValueToOrderedImmutableList(settings,
            (element, value) -> from(element).changeSetting(changeStateOf(time, value,
                element.getTypedSubSetting(CookingTimeSubSetting.class))).create())
        .isEditable()
        .setPreferredSize(50)
        .buildWithOrderNumber(orderNumber));
  }

  private CookingTimeSubSetting changeStateOf(
      CookingTime cookingTime, Object value,
      CookingTimeSubSetting cookingTimeSubSetting) {
    if (Boolean.TRUE.equals(value)) {
      return cookingTimeSubSetting.allowCookingTime(cookingTime);
    } else {
      return cookingTimeSubSetting.prohibitCookingTime(cookingTime);
    }
  }
}
