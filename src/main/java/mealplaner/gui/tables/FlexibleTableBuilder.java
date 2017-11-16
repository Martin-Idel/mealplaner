package mealplaner.gui.tables;

import static mealplaner.gui.tables.FlexibleTableModel.from;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class FlexibleTableBuilder {
	private List<TableColumnData<?>> columns;
	private JTable table;
	private Supplier<Integer> rowCount = () -> 0;

	private FlexibleTableBuilder() {
		columns = new ArrayList<>();
	}

	public static FlexibleTableBuilder createNewTable() {
		return new FlexibleTableBuilder();
	}

	public <T> FlexibleTableBuilder addColumn(TableColumnData<T> column) {
		columns.add(column);
		return this;
	}

	public <T> FlexibleTableBuilder withRowCount(Supplier<Integer> rowCount) {
		this.rowCount = rowCount;
		return this;
	}

	public JTable buildTable() {
		FlexibleTableModel tableModel = from(columns, rowCount);
		table = new JTable(tableModel);
		for (int i = 0; i < columns.size(); i++) {
			TableColumn column = getTableColumn(i);
			column.setPreferredWidth(columns.get(i).getPreferredSize());
			columns.get(i).getTableCellEditor().ifPresent(column::setCellEditor);
			columns.get(i).getTableCellRenderer().ifPresent(column::setCellRenderer);
		}
		return table;
	}

	private TableColumn getTableColumn(int index) {
		return table.getColumnModel().getColumn(index);
	}
}
