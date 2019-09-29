package mealplaner.plugins.plugins.cookingtime;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withBooleanContent;
import static mealplaner.model.settings.SettingsBuilder.from;

import java.util.List;

import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.api.SettingsInputDialogExtension;

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
            setting -> !setting.getTypedSubSetting(CookingTimeSetting.class).contains(time))
        .setValueToOrderedImmutableList(settings,
            (element, value) -> from(element).time(changeStateOf(time, value,
                element.getTypedSubSetting(CookingTimeSetting.class))).create())
        .isEditable()
        .setPreferredSize(50)
        .buildWithOrderNumber(orderNumber));
  }

  private CookingTimeSetting changeStateOf(
      CookingTime cookingTime, Object value,
      CookingTimeSetting cookingTimeSetting) {
    if ((Boolean) value) {
      return cookingTimeSetting.allowCookingTime(cookingTime);
    } else {
      return cookingTimeSetting.prohibitCookingTime(cookingTime);
    }
  }
}
