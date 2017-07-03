package mealplaner.gui.dialogs.pastupdate;

import static mealplaner.gui.model.StringArrayCollection.getUpdatePastMealColumnNames;
import static mealplaner.gui.model.StringArrayCollection.getWeekDays;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.table.AbstractTableModel;

import mealplaner.model.Meal;

public class UpdateTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private String[] columnNames;
	private Meal[] dataForDaysPassed;
	private String[] days;
	private Calendar cal;
	private Locale currentLocale;

	public UpdateTableModel(Date proposalDate, Meal[] dataForDaysPassed, ResourceBundle messages,
			Locale currentLocale) {
		this.dataForDaysPassed = dataForDaysPassed;
		this.columnNames = getUpdatePastMealColumnNames(messages);
		this.currentLocale = currentLocale;
		days = getWeekDays(messages);
		cal = Calendar.getInstance();
		cal.setTime(proposalDate);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column == 2;
	}

	public Meal[] returnContent() {
		return dataForDaysPassed;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return dataForDaysPassed.length;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Class<?> getColumnClass(int col) {
		switch (col) {
		case 0:
			return DateFormat.class;
		case 1:
			return String.class;
		case 2:
			return Meal.class;
		default:
			return String.class;
		}
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		switch (col) {
		case 2:
			dataForDaysPassed[row] = (Meal) value;
			break;
		default:
			return;
		}
		fireTableCellUpdated(row, col);
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
		case 0:
			DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, currentLocale);
			Calendar newCalCopy = Calendar.getInstance();
			newCalCopy.setTime(cal.getTime());
			newCalCopy.add(Calendar.DATE, row);
			return dateFormat.format(newCalCopy.getTime());
		case 1:
			int dayofWeek = (cal.get(Calendar.DAY_OF_WEEK) - 1 + row) % 7;
			return days[dayofWeek];
		case 2:
			return dataForDaysPassed[row];
		default:
			return "";
		}
	}
}
