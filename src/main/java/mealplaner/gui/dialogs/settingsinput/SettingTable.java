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
import static mealplaner.model.settings.CookingTimeSetting.copyCookingTimeSetting;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mealplaner.commons.gui.dialogs.DialogWindow;
import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.commons.gui.tables.Table;
import mealplaner.model.enums.CasseroleSettings;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.PreferenceSettings;
import mealplaner.model.settings.CookingTimeSetting;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;

public class SettingTable {
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

  public void addJScrollTableToDialogCentre(DialogWindow window) {
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

    table = tableBuilder.buildTable();
    window.addCentral(table);
  }

  private void addOtherSettings(FlexibleTableBuilder tableBuilder) {
    tableBuilder
        .addColumn(withNonnegativeIntegerContent()
            .withColumnName(BUNDLES.message("manyColumn"))
            .getValueFromOrderedList(settings, setting -> setting.getNumberOfPeople())
            .setValueToOrderedImmutableList(settings,
                (element, value) -> element.changeNumberOfPeople(value))
            .isEditable()
            .build())
        .addColumn(withEnumContent(CasseroleSettings.class)
            .withColumnName(BUNDLES.message("casseroleColumn"))
            .getValueFromOrderedList(settings, setting -> setting.getCasserole())
            .setValueToOrderedImmutableList(settings,
                (element, value) -> element.changeCasserole(value))
            .isEditable()
            .build())
        .addColumn(withEnumContent(PreferenceSettings.class)
            .withColumnName(BUNDLES.message("preferenceColumn"))
            .getValueFromOrderedList(settings, setting -> setting.getPreference())
            .setValueToOrderedImmutableList(settings,
                (element, value) -> element.changePreference(value))
            .isEditable()
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

  private void addCookingTimeColumnFor(CookingTime time, String columnName,
      FlexibleTableBuilder tableBuilder) {
    tableBuilder.addColumn(withBooleanContent()
        .withColumnName(columnName)
        .getValueFromOrderedList(settings,
            setting -> !setting.getCookingTime().contains(time))
        .setValueToOrderedImmutableList(settings,
            (element, value) -> element.changeCookingTime(changeStateOf(time, value,
                copyCookingTimeSetting(element.getCookingTime()))))
        .isEditable()
        .build());
  }

  private CookingTimeSetting changeStateOf(CookingTime cookingTime, Object value,
      CookingTimeSetting newCookingTimeSetting) {
    if ((Boolean) value) {
      newCookingTimeSetting.allowCookingTime(cookingTime);
    } else {
      newCookingTimeSetting.prohibitCookingTime(cookingTime);
    }
    return newCookingTimeSetting;
  }
}
