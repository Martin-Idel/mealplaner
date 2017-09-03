package mealplaner.gui.dialogs.settingsinput;

import static mealplaner.gui.model.EnumToStringRepresentation.getCasseroleSettingsStrings;
import static mealplaner.gui.model.EnumToStringRepresentation.getPreferenceSettingsStrings;

import java.util.Calendar;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import mealplaner.BundleStore;
import mealplaner.gui.commons.SwingUtilityMethods;
import mealplaner.model.enums.CasseroleSettings;
import mealplaner.model.enums.PreferenceSettings;
import mealplaner.model.settings.Settings;

// TODO: removeDateColumn should be transported to the model. This way, we don't need a crappy default calendar
public class SettingTable {
	private JTable table;
	private SettingTableModel tableModel;
	private Calendar calendar;
	private BundleStore bundles;

	public SettingTable(Settings[] defaultSettings, BundleStore bundles) {
		this.bundles = bundles;
		calendar = Calendar.getInstance();
		calendar.setWeekDate(2000, 1, 2);

		tableModel = new SettingTableModel(defaultSettings, calendar, bundles);
	}

	public SettingTable(Settings[] settings, Calendar calendar, BundleStore bundles) {
		this.calendar = calendar;
		this.bundles = bundles;

		tableModel = new SettingTableModel(settings, calendar, bundles);
	}

	public JTable setupTable() {
		table = new JTable(tableModel);
		setPreferredWidthXofColumns(100, 0, 6, 7);
		setPreferredWidthXofColumns(50, 1, 2, 3, 4, 5);
		SwingUtilityMethods.setupEnumColumnRenderer(getTableColumn(6), CasseroleSettings.class,
				getCasseroleSettingsStrings(bundles));
		SwingUtilityMethods.setupComboBoxEditor(getTableColumn(6), CasseroleSettings.class,
				getCasseroleSettingsStrings(bundles));
		SwingUtilityMethods.setupEnumColumnRenderer(getTableColumn(7), PreferenceSettings.class,
				getPreferenceSettingsStrings(bundles));
		SwingUtilityMethods.setupComboBoxEditor(getTableColumn(7), PreferenceSettings.class,
				getPreferenceSettingsStrings(bundles));
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
