// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.settingsinput;

import static java.time.LocalDate.of;
import static java.time.format.DateTimeFormatter.ofLocalizedDate;
import static java.time.format.FormatStyle.SHORT;
import static java.time.format.TextStyle.FULL;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withBooleanContent;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withEnumContent;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withNonnegativeIntegerContent;
import static mealplaner.model.settings.SettingsBuilder.from;
import static mealplaner.model.settings.subsettings.CookingTimeSetting.copyCookingTimeSetting;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import mealplaner.commons.gui.dialogs.DialogWindow;
import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.commons.gui.tables.Table;
import mealplaner.model.meal.enums.CookingTime;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;
import mealplaner.model.settings.enums.CasseroleSettings;
import mealplaner.model.settings.enums.CourseSettings;
import mealplaner.model.settings.enums.PreferenceSettings;
import mealplaner.model.settings.subsettings.CookingTimeSetting;
import mealplaner.plugins.api.SettingsInputDialogExtension;

class SettingTable {
  private Table table;
  private final List<Settings> settings;
  private LocalDate date;

  public SettingTable(DefaultSettings defaultSettings) {
    List<Settings> settings = new ArrayList<>(7);
    for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
      settings.add(defaultSettings.getDefaultSettings()
          .get(dayOfWeek));
    }
    this.settings = settings;
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
    List<Settings> settings = new ArrayList<>(this.settings.size());
    Map<DayOfWeek, Settings> defaults = defaultSettings.getDefaultSettings();
    DayOfWeek dayOfWeek = date.getDayOfWeek();
    for (int i = 0; i < this.settings.size(); i++) {
      settings.add(defaults.get(dayOfWeek));
      dayOfWeek = dayOfWeek.plus(1);
    }
    this.settings.clear();
    this.settings.addAll(settings);
    table.update();
  }

  public void addJScrollTableToDialogCentre(
      DialogWindow window, Collection<SettingsInputDialogExtension> settingsInputDialogExtensions) {
    FlexibleTableBuilder tableBuilder = createNewTable();
    addDayOfWeekColumn(tableBuilder);
    if (date != null) {
      addDateColumn(tableBuilder);
    }
    addCookingTimeColumnFor(CookingTime.VERY_SHORT, BUNDLES.message("veryShortColumn"),
        tableBuilder);
    addCookingTimeColumnFor(CookingTime.SHORT, BUNDLES.message("shortColumn"), tableBuilder);
    addCookingTimeColumnFor(CookingTime.MEDIUM, BUNDLES.message("mediumColumn"), tableBuilder);
    addCookingTimeColumnFor(CookingTime.LONG, BUNDLES.message("longColumn"), tableBuilder);

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
                (setting, numberOfPoeple) -> from(setting).numberOfPeople(numberOfPoeple).create())
            .isEditable()
            .build())
        .addColumn(withEnumContent(CasseroleSettings.class)
            .withColumnName(BUNDLES.message("casseroleColumn"))
            .getValueFromOrderedList(settings, Settings::getCasserole)
            .setValueToOrderedImmutableList(settings,
                (setting, casserole) -> from(setting).casserole(casserole).create())
            .setPreferredSize(100)
            .isEditable()
            .build())
        .addColumn(withEnumContent(PreferenceSettings.class)
            .withColumnName(BUNDLES.message("preferenceColumn"))
            .getValueFromOrderedList(settings, Settings::getPreference)
            .setValueToOrderedImmutableList(settings,
                (setting, preference) -> from(setting).preference(preference).create())
            .isEditable()
            .setPreferredSize(100)
            .build())
        .addColumn(withEnumContent(CourseSettings.class)
            .withColumnName(BUNDLES.message("courseSettingColumn"))
            .getValueFromOrderedList(settings, Settings::getCourseSettings)
            .setValueToOrderedImmutableList(settings,
                (setting, course) -> from(setting).course(course).create())
            .isEditable()
            .setPreferredSize(120)
            .build());
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
            .build());
  }

  private void addDateColumn(FlexibleTableBuilder tableBuilder) {
    tableBuilder.addColumn(withContent(String.class)
        .withColumnName(BUNDLES.message("date"))
        .getRowValueFromUnderlyingModel(row -> date.plusDays(row)
            .format(ofLocalizedDate(SHORT).withLocale(BUNDLES.locale())))
        .setPreferredSize(50)
        .build());
  }

  private void addCookingTimeColumnFor(
      CookingTime time, String columnName, FlexibleTableBuilder tableBuilder) {
    tableBuilder.addColumn(withBooleanContent()
        .withColumnName(columnName)
        .getValueFromOrderedList(settings,
            setting -> !setting.getCookingTime().contains(time))
        .setValueToOrderedImmutableList(settings,
            (element, value) -> from(element).time(changeStateOf(time, value,
                copyCookingTimeSetting(element.getCookingTime()))).create())
        .isEditable()
        .setPreferredSize(50)
        .build());
  }

  private CookingTimeSetting changeStateOf(
      CookingTime cookingTime, Object value,
      CookingTimeSetting newCookingTimeSetting) {
    if ((Boolean) value) {
      newCookingTimeSetting.allowCookingTime(cookingTime);
    } else {
      newCookingTimeSetting.prohibitCookingTime(cookingTime);
    }
    return newCookingTimeSetting;
  }
}
