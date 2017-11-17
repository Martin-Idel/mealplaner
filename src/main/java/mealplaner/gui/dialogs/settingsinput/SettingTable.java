package mealplaner.gui.dialogs.settingsinput;

import static java.time.LocalDate.of;
import static java.time.format.DateTimeFormatter.ofLocalizedDate;
import static java.time.format.FormatStyle.SHORT;
import static java.util.stream.Collectors.toList;
import static mealplaner.BundleStore.BUNDLES;
import static mealplaner.gui.model.StringArrayCollection.getWeekDays;
import static mealplaner.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.gui.tables.TableColumnBuilder.newColumnWithEnumContent;
import static mealplaner.gui.tables.TableColumnBuilder.withBooleanContent;
import static mealplaner.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.gui.tables.TableColumnBuilder.withNonnegativeIntegerContent;
import static mealplaner.model.settings.CookingTimeSetting.copyCookingTimeSetting;
import static mealplaner.model.settings.Settings.copy;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;

import mealplaner.gui.tables.FlexibleTableBuilder;
import mealplaner.model.enums.CasseroleSettings;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.PreferenceSettings;
import mealplaner.model.settings.CookingTimeSetting;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;

public class SettingTable {
	private JTable table;
	private List<Settings> settings;
	private String[] days = getWeekDays();
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
		this.settings = settings.stream().map(setting -> copy(setting)).collect(toList());
	}

	public Settings[] getSettings() {
		Settings[] content = new Settings[settings.size()];
		return settings.toArray(content);
	}

	public void useDefaultSettings(DefaultSettings defaultSettings) {
		List<Settings> settings = new ArrayList<>(table.getRowCount());
		Map<DayOfWeek, Settings> defaults = defaultSettings.getDefaultSettings();
		DayOfWeek dayOfWeek = date.getDayOfWeek();
		for (int i = 0; i < table.getRowCount(); i++) {
			settings.add(defaults.get(dayOfWeek));
			dayOfWeek = dayOfWeek.plus(1);
		}
		this.settings = settings;
		table.repaint();
	}

	public JTable setupTable() {
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
		return table;
	}

	private void addOtherSettings(FlexibleTableBuilder tableBuilder) {
		tableBuilder
				.addColumn(withNonnegativeIntegerContent()
						.withColumnName(BUNDLES.message("manyColumn"))
						.getRowValueFromUnderlyingModel(
								row -> settings.get(row).getNumberOfPeople())
						.setRowValueToUnderlyingModel((value, row) -> settings.set(row,
								settings.get(row).changeNumberOfPeople(value)))
						.isEditable()
						.build())
				.addColumn(newColumnWithEnumContent(CasseroleSettings.class)
						.withColumnName(BUNDLES.message("casseroleColumn"))
						.getRowValueFromUnderlyingModel(row -> settings.get(row).getCasserole())
						.setRowValueToUnderlyingModel(
								(val, row) -> settings.set(row,
										settings.get(row).changeCasserole(val)))
						.isEditable()
						.build())
				.addColumn(newColumnWithEnumContent(PreferenceSettings.class)
						.withColumnName(BUNDLES.message("preferenceColumn"))
						.getRowValueFromUnderlyingModel(row -> settings.get(row).getPreference())
						.setRowValueToUnderlyingModel(
								(val, row) -> settings.set(row,
										settings.get(row).changePreference(val)))
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
								row -> days[newDate.plusDays(row).getDayOfWeek().getValue() % 7])
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
				.getRowValueFromUnderlyingModel(row -> !settings.get(row).getCookingTime()
						.contains(time))
				.setRowValueToUnderlyingModel((value, row) -> settings.set(row,
						settings.get(row).changeCookingTime(changeStateOf(time, value,
								copyCookingTimeSetting(settings.get(row).getCookingTime())))))
				.isEditable()
				.build());
	}

	private CookingTimeSetting changeStateOf(CookingTime cookingTime, Object value,
			CookingTimeSetting newCookingTimeSetting) {
		if ((Boolean) value == true) {
			newCookingTimeSetting.allowCookingTime(cookingTime);
		} else {
			newCookingTimeSetting.prohibitCookingTime(cookingTime);
		}
		return newCookingTimeSetting;
	}
}
