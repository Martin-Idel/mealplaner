// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.settingsinput;

import static java.time.LocalDate.of;
import static java.time.format.DateTimeFormatter.ofLocalizedDate;
import static java.time.format.FormatStyle.SHORT;
import static java.time.format.TextStyle.FULL;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withNonnegativeIntegerContent;
import static mealplaner.model.settings.SettingsBuilder.from;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import mealplaner.commons.gui.dialogs.DialogWindow;
import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.commons.gui.tables.Table;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.api.SettingsInputDialogExtension;

class SettingTable {
  private Table table;
  private final List<Settings> settings;
  private LocalDate date;

  public SettingTable(DefaultSettings defaultSettings) {
    List<Settings> newSettings = new ArrayList<>(7);
    for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
      newSettings.add(defaultSettings.getDefaultSettingsMap()
          .get(dayOfWeek));
    }
    this.settings = newSettings;
  }

  public SettingTable(List<Settings> settings, LocalDate date) {
    this.date = date;
    this.settings = new ArrayList<>(settings);
  }

  public Settings[] getSettings() {
    Settings[] content = new Settings[settings.size()];
    return settings.toArray(content);
  }

  public void useDefaultSettings(DefaultSettings defaultSettings) {
    List<Settings> newSettings = new ArrayList<>(this.settings.size());
    Map<DayOfWeek, Settings> defaults = defaultSettings.getDefaultSettingsMap();
    DayOfWeek dayOfWeek = date.getDayOfWeek();
    for (int i = 0; i < this.settings.size(); i++) {
      newSettings.add(defaults.get(dayOfWeek));
      dayOfWeek = dayOfWeek.plus(1);
    }
    this.settings.clear();
    this.settings.addAll(newSettings);
    table.update();
  }

  public void addJScrollTableToDialogCentre(
      DialogWindow window, Collection<SettingsInputDialogExtension> settingsInputDialogExtensions) {
    FlexibleTableBuilder tableBuilder = createNewTable();
    addDayOfWeekColumn(tableBuilder);
    if (date != null) {
      addDateColumn(tableBuilder);
    }
    addOtherSettings(tableBuilder);
    for (var setting : settingsInputDialogExtensions) {
      setting.addTableColumns(tableBuilder, settings);
    }

    table = tableBuilder.buildTable();
    window.addCentral(table);
  }

  private void addOtherSettings(FlexibleTableBuilder tableBuilder) {
    tableBuilder
        .addColumn(withNonnegativeIntegerContent()
            .withColumnName(BUNDLES.message("manyColumn"))
            .getValueFromOrderedList(settings, Settings::getNumberOfPeople)
            .setValueToOrderedImmutableList(settings,
                (setting, numberOfPeople) -> from(setting).numberOfPeople(numberOfPeople).create())
            .isEditable()
            .buildWithOrderNumber(20));
  }

  private void addDayOfWeekColumn(FlexibleTableBuilder tableBuilder) {
    // If we don't have a date, take a random Monday for default settings
    final LocalDate newDate = (date == null) ? of(2017, 10, 23) : date;
    tableBuilder
        .withRowCount(settings::size)
        .addColumn(withContent(String.class)
            .withColumnName(BUNDLES.message("weekday"))
            .getRowValueFromUnderlyingModel(
                row -> newDate.plusDays(row)
                    .getDayOfWeek()
                    .getDisplayName(FULL, BUNDLES.locale()))
            .buildWithOrderNumber(0));
  }

  private void addDateColumn(FlexibleTableBuilder tableBuilder) {
    tableBuilder.addColumn(withContent(String.class)
        .withColumnName(BUNDLES.message("date"))
        .getRowValueFromUnderlyingModel(row -> date.plusDays(row)
            .format(ofLocalizedDate(SHORT).withLocale(BUNDLES.locale())))
        .setPreferredSize(50)
        .buildWithOrderNumber(1));
  }
}
