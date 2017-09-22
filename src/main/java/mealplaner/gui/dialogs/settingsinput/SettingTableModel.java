package mealplaner.gui.dialogs.settingsinput;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.table.AbstractTableModel;

import mealplaner.BundleStore;
import mealplaner.gui.model.StringArrayCollection;
import mealplaner.model.enums.CasseroleSettings;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.PreferenceSettings;
import mealplaner.model.settings.CookingTimeSetting;
import mealplaner.model.settings.Settings;

public class SettingTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private String[] columnNames;
	private String[] days;
	private Settings[] workingCopy;
	private Calendar cal;
	private Locale currentLocale;

	public SettingTableModel(Settings[] settings, Calendar calendar, BundleStore bundles) {
		currentLocale = bundles.locale();
		columnNames = StringArrayCollection.getSettingsInputColumnNames(bundles);
		days = StringArrayCollection.getWeekDays(bundles);
		cal = Calendar.getInstance();
		cal.setTime(calendar.getTime());
		workingCopy = new Settings[settings.length];
		for (int i = 0; i < settings.length; i++) {
			workingCopy[i] = new Settings(settings[i]);
		}
		update(workingCopy);
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return workingCopy.length;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Class<?> getColumnClass(int col) {
		switch (col) {
		case 0:
			return String.class;
		case 1:
			return String.class;
		case 2:
			return Boolean.class;
		case 3:
			return Boolean.class;
		case 4:
			return Boolean.class;
		case 5:
			return Boolean.class;
		case 6:
			return Boolean.class;
		case 7:
			return CasseroleSettings.class;
		case 8:
			return PreferenceSettings.class;
		default:
			return String.class;
		}
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return !(col == 0 || col == 1
				|| (col == 6
						&& (CasseroleSettings) getValueAt(row, col + 1) == CasseroleSettings.ONLY));
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		CookingTimeSetting newCookingTimeSetting = workingCopy[row].getCookingTime();
		boolean newIsMany = workingCopy[row].getCookingUtensil().containsMany();
		CasseroleSettings newCasseroleSettings = workingCopy[row].getCasserole();
		PreferenceSettings newPreferenceSettings = workingCopy[row].getPreference();
		switch (col) {
		case 2:
			changeStateOf(CookingTime.VERY_SHORT, value, newCookingTimeSetting);
			break;
		case 3:
			changeStateOf(CookingTime.SHORT, value, newCookingTimeSetting);
			break;
		case 4:
			changeStateOf(CookingTime.MEDIUM, value, newCookingTimeSetting);
			break;
		case 5:
			changeStateOf(CookingTime.LONG, value, newCookingTimeSetting);
			break;
		case 6:
			newIsMany = (boolean) value;
			break;
		case 7:
			newCasseroleSettings = (CasseroleSettings) value;
			break;
		case 8:
			newPreferenceSettings = (PreferenceSettings) value;
			break;
		default:
			return;
		}
		workingCopy[row] = new Settings(newCookingTimeSetting, newIsMany, newCasseroleSettings,
				newPreferenceSettings);
		fireTableCellUpdated(row, col);
	}

	private void changeStateOf(CookingTime cookingTime, Object value,
			CookingTimeSetting newCookingTimeSetting) {
		if ((Boolean) value == true) {
			newCookingTimeSetting.allowCookingTime(cookingTime);
		} else {
			newCookingTimeSetting.prohibitCookingTime(cookingTime);
		}
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
		case 0:
			int dayofWeek = (cal.get(Calendar.DAY_OF_WEEK) - 1 + row) % 7;
			return days[dayofWeek];
		case 1:
			DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, currentLocale);
			Calendar newCalCopy = Calendar.getInstance();
			newCalCopy.setTime(cal.getTime());
			newCalCopy.add(Calendar.DATE, row);
			return dateFormat.format(newCalCopy.getTime());
		case 2:
			return !workingCopy[row].getCookingTime().contains(CookingTime.VERY_SHORT);
		case 3:
			return !workingCopy[row].getCookingTime().contains(CookingTime.SHORT);
		case 4:
			return !workingCopy[row].getCookingTime().contains(CookingTime.MEDIUM);
		case 5:
			return !workingCopy[row].getCookingTime().contains(CookingTime.LONG);
		case 6:
			return workingCopy[row].getCookingUtensil().containsMany();
		case 7:
			return workingCopy[row].getCasserole();
		case 8:
			return workingCopy[row].getPreference();
		default:
			return "";
		}
	}

	public void update(Settings[] settings) {
		this.workingCopy = settings;
		fireTableDataChanged();
	}

	public Settings[] returnContent() {
		return workingCopy;
	}
}
