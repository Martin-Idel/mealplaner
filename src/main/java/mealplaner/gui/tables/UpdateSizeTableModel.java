package mealplaner.gui.tables;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class UpdateSizeTableModel extends FlexibleTableModel {
	private static final long serialVersionUID = 1L;

	private UpdateSizeTableModel(List<TableColumnData<?>> tableColumns, Supplier<Integer> rowCount) {
		super(tableColumns, rowCount);
	}

	public static UpdateSizeTableModel from(List<TableColumnData<?>> tableColumns,
			Supplier<Integer> rowCount) {
		return new UpdateSizeTableModel(tableColumns, rowCount);
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
