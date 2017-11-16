package mealplaner.gui.tables;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.swing.table.AbstractTableModel;

public class FlexibleTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private List<TableColumnData<?>> columns = new ArrayList<>();

	private Supplier<Integer> rowCount;

	private FlexibleTableModel(List<TableColumnData<?>> tableColumns, Supplier<Integer> rowCount) {
		columns = tableColumns;
		this.rowCount = rowCount;
	}

	public static FlexibleTableModel from(List<TableColumnData<?>> tableColumns,
			Supplier<Integer> rowCount) {
		return new FlexibleTableModel(tableColumns, rowCount);
	}

	@Override
	public int getColumnCount() {
		return columns.size();
	}

	@Override
	public int getRowCount() {
		return rowCount.get();
	}

	@Override
	public String getColumnName(int col) {
		return columns.get(col).columnName();
	}

	@Override
	public Class<?> getColumnClass(int col) {
		return columns.get(col).getColumnClass();
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return columns.get(col).isEditable(row);
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		Optional<Integer[]> fireOtherCellsUpdated = columns.get(col).setValue(value, row);
		fireTableCellUpdated(row, col);
		fireOtherCellsUpdated.ifPresent(otherColumns -> asList(otherColumns)
				.forEach(column -> fireTableCellUpdated(row, column)));
	}

	@Override
	public Object getValueAt(int row, int col) {
		return columns.get(col).getValue(row);
	}
}
