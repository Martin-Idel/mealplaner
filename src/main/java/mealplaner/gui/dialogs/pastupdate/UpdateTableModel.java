package mealplaner.gui.dialogs.pastupdate;

import static java.time.format.DateTimeFormatter.ofLocalizedDate;
import static java.time.format.FormatStyle.SHORT;
import static mealplaner.gui.model.StringArrayCollection.getUpdatePastMealColumnNames;
import static mealplaner.gui.model.StringArrayCollection.getWeekDays;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Locale;

import javax.swing.table.AbstractTableModel;

import mealplaner.BundleStore;
import mealplaner.model.Meal;

public class UpdateTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private String[] columnNames;
	private Meal[] dataForDaysPassed;
	private String[] days;
	private LocalDate date;
	private Locale currentLocale;

	public UpdateTableModel(LocalDate proposalDate, Meal[] dataForDaysPassed, BundleStore bundles) {
		this.dataForDaysPassed = dataForDaysPassed;
		this.columnNames = getUpdatePastMealColumnNames(bundles);
		this.currentLocale = bundles.locale();
		days = getWeekDays(bundles);
		this.date = proposalDate;
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
			return date.plusDays(row)
					.format(ofLocalizedDate(SHORT).withLocale(currentLocale));
		case 1:
			int dayofWeek = (date.getDayOfWeek().getValue() + row) % 7;
			return days[dayofWeek];
		case 2:
			return dataForDaysPassed[row];
		default:
			return "";
		}
	}
}
