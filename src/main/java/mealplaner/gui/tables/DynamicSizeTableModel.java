package mealplaner.gui.tables;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class DynamicSizeTableModel extends FlexibleTableModel {
	private static final long serialVersionUID = 1L;
	private Runnable addValue;

	private DynamicSizeTableModel(List<TableColumnData<?>> tableColumns,
			Supplier<Integer> rowCount,
			Runnable addValue) {
		super(tableColumns, rowCount);
		this.addValue = addValue;
	}

	public static DynamicSizeTableModel from(List<TableColumnData<?>> tableColumns,
			Supplier<Integer> rowCount, Runnable addValue) {
		return new DynamicSizeTableModel(tableColumns, rowCount, addValue);
	}

	@Override
	public int getRowCount() {
		return super.getRowCount() + 1;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		if (row != getRowCount() - 1) {
			Optional<Integer[]> fireOtherCellsUpdated = columns.get(col).setValue(value, row);
			fireTableCellUpdated(row, col);
			fireOtherCellsUpdated.ifPresent(otherColumns -> asList(otherColumns)
					.forEach(column -> fireTableCellUpdated(row, column)));
		} else {
			addValue.run();
			insertRows(getRowCount(), getRowCount());
			setValueAt(value, row, col);
		}
	}

	@Override
	public Object getValueAt(int row, int col) {
		return (row != getRowCount() - 1)
				? columns.get(col).getValue(row)
				: columns.get(col).getDefaultValue();
	}
}
