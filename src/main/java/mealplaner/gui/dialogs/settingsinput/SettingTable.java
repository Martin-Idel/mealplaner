package mealplaner.gui.dialogs.settingsinput;

import static java.time.LocalDate.of;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import mealplaner.gui.commons.SwingUtilityMethods;
import mealplaner.gui.editing.PositiveIntegerCellEditor;
import mealplaner.model.enums.CasseroleSettings;
import mealplaner.model.enums.PreferenceSettings;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;

// TODO: removeDateColumn should be transported to the model. This way, we don't need a crappy default calendar
public class SettingTable {
	private JTable table;
	private SettingTableModel tableModel;
	private LocalDate date = of(2017, 10, 23); // A random Monday

	public SettingTable(DefaultSettings defaultSettings) {
		Settings[] settings = new Settings[7];
		for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
			settings[dayOfWeek.getValue() - 1] = defaultSettings.getDefaultSettings()
					.get(dayOfWeek);
		}
		tableModel = new SettingTableModel(settings, date);
	}

	public SettingTable(Settings[] settings, LocalDate date) {
		this.date = date;

		tableModel = new SettingTableModel(settings, date);
	}

	public JTable setupTable() {
		table = new JTable(tableModel);
		setPreferredWidthXofColumns(100, 0, 7, 8);
		setPreferredWidthXofColumns(50, 1, 2, 3, 4, 5, 6);
		table.setDefaultEditor(Integer.class, new PositiveIntegerCellEditor());
		SwingUtilityMethods.setupComboBoxEditor(getTableColumn(7), CasseroleSettings.class);
		SwingUtilityMethods.setupComboBoxEditor(getTableColumn(8), PreferenceSettings.class);
		return table;
	}

	public void useDefaultSettings(DefaultSettings defaultSettings) {
		Settings[] settings = new Settings[tableModel.getRowCount()];
		Map<DayOfWeek, Settings> defaults = defaultSettings.getDefaultSettings();
		DayOfWeek dayOfWeek = date.getDayOfWeek();
		for (int i = 0; i < settings.length; i++) {
			settings[i] = defaults.get(dayOfWeek);
			dayOfWeek = dayOfWeek.plus(1);
		}
		tableModel.update(settings);
	}

	public Settings[] getSettings() {
		return tableModel.returnContent();
	}

	public void removeDateColumn() {
		table.removeColumn(getTableColumn(1));
	}

	private void setPreferredWidthXofColumns(int preferredWidth, Integer... columns) {
		for (int column : columns) {
			getTableColumn(column).setPreferredWidth(preferredWidth);
		}
	}

	private TableColumn getTableColumn(int index) {
		return table.getColumnModel().getColumn(index);
	}
}
