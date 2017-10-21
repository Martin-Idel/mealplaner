package mealplaner.gui.dialogs.settingsinput;

import static java.time.LocalDate.of;
import static mealplaner.model.enums.CasseroleSettings.getCasseroleSettingsStrings;
import static mealplaner.model.enums.PreferenceSettings.getPreferenceSettingsStrings;

import java.time.LocalDate;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import mealplaner.BundleStore;
import mealplaner.gui.commons.SwingUtilityMethods;
import mealplaner.gui.editing.PositiveIntegerCellEditor;
import mealplaner.model.enums.CasseroleSettings;
import mealplaner.model.enums.PreferenceSettings;
import mealplaner.model.settings.Settings;

// TODO: removeDateColumn should be transported to the model. This way, we don't need a crappy default calendar
public class SettingTable {
	private JTable table;
	private SettingTableModel tableModel;
	private LocalDate date = of(2017, 10, 23); // A random Monday
	private BundleStore bundles;

	public SettingTable(Settings[] defaultSettings, BundleStore bundles) {
		this.bundles = bundles;

		tableModel = new SettingTableModel(defaultSettings, date, bundles);
	}

	public SettingTable(Settings[] settings, LocalDate date, BundleStore bundles) {
		this.date = date;
		this.bundles = bundles;

		tableModel = new SettingTableModel(settings, date, bundles);
	}

	public JTable setupTable() {
		table = new JTable(tableModel);
		setPreferredWidthXofColumns(100, 0, 7, 8);
		setPreferredWidthXofColumns(50, 1, 2, 3, 4, 5, 6);
		table.setDefaultEditor(Integer.class, new PositiveIntegerCellEditor());
		SwingUtilityMethods.setupEnumColumnRenderer(getTableColumn(7), CasseroleSettings.class,
				getCasseroleSettingsStrings(bundles));
		SwingUtilityMethods.setupComboBoxEditor(getTableColumn(7), CasseroleSettings.class,
				getCasseroleSettingsStrings(bundles));
		SwingUtilityMethods.setupEnumColumnRenderer(getTableColumn(8), PreferenceSettings.class,
				getPreferenceSettingsStrings(bundles));
		SwingUtilityMethods.setupComboBoxEditor(getTableColumn(8), PreferenceSettings.class,
				getPreferenceSettingsStrings(bundles));
		return table;
	}

	public void useDefaultSettings(Settings[] defaultSettings) {
		Settings[] settings = new Settings[tableModel.getRowCount()];
		int dayOfWeek = date.getDayOfWeek().getValue();
		for (int i = 0; i < settings.length; i++) {
			settings[i] = new Settings(defaultSettings[(dayOfWeek - 1) % 7]);
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
