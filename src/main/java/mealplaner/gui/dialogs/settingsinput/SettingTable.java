package mealplaner.gui.dialogs.settingsinput;

import static mealplaner.gui.model.EnumToStringRepresentation.getCasseroleSettingsStrings;
import static mealplaner.gui.model.EnumToStringRepresentation.getPreferenceSettingsStrings;

import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import mealplaner.gui.commons.SwingUtilityMethods;
import mealplaner.model.enums.CasseroleSettings;
import mealplaner.model.enums.PreferenceSettings;
import mealplaner.model.settings.Settings;

// TODO: Maybe have better column names. This could also be useful for other table models.
// TODO: removeDateColumn should be transported to the model. This way, we don't need a crappy default calendar
public class SettingTable {
	private JTable table;
	private SettingTableModel tableModel;
	private Calendar calendar;
	private ResourceBundle messages;

	public SettingTable(Settings[] defaultSettings, ResourceBundle messages) {
		this.messages = messages;
		calendar = Calendar.getInstance();
		calendar.setWeekDate(2000, 1, 2);

		tableModel = new SettingTableModel(defaultSettings, calendar, messages, Locale.getDefault());
	}

	public SettingTable(Settings[] settings, Calendar calendar, ResourceBundle messages, Locale currentLocale) {
		this.calendar = calendar;
		this.messages = messages;

		tableModel = new SettingTableModel(settings, calendar, messages, currentLocale);
	}

	public JTable setupTable() {
		table = new JTable(tableModel);
		setPreferredWidthXofColumns(100, 0, 6, 7);
		setPreferredWidthXofColumns(50, 1, 2, 3, 4, 5);
		SwingUtilityMethods.setupEnumColumnRenderer(getTableColumn(6), CasseroleSettings.class,
				getCasseroleSettingsStrings(messages));
		SwingUtilityMethods.setupComboBoxEditor(getTableColumn(6), CasseroleSettings.class,
				getCasseroleSettingsStrings(messages));
		SwingUtilityMethods.setupEnumColumnRenderer(getTableColumn(7), PreferenceSettings.class,
				getPreferenceSettingsStrings(messages));
		SwingUtilityMethods.setupComboBoxEditor(getTableColumn(7), PreferenceSettings.class,
				getPreferenceSettingsStrings(messages));
		return table;
	}

	public void useDefaultSettings(Settings[] defaultSettings) {
		Settings[] settings = new Settings[tableModel.getRowCount()];
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		for (int i = 0; i < settings.length; i++) {
			settings[i] = new Settings(defaultSettings[(dayOfWeek - 2) % 7]);
			dayOfWeek++;
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
