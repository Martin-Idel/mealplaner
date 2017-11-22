package mealplaner.gui.tables;

import static mealplaner.gui.tables.FlexibleTableModel.from;

import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class FlexibleTableBuilder {
	private List<TableColumnData<?>> columns;
	private List<MouseAdapter> columnListeners;
	private JTable table;
	private Supplier<Integer> rowCount = () -> 0;

	private FlexibleTableBuilder() {
		columns = new ArrayList<>();
		columnListeners = new ArrayList<>();
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

	public <T> FlexibleTableBuilder addListenerToThisColumn(Consumer<Integer> onClick) {
		columnListeners.add(ColumnListener.createColumnListener(columns.size() - 1, onClick));
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
		for (MouseAdapter columnListener : columnListeners) {
			table.addMouseListener(columnListener);
		}
		return table;
	}

	private TableColumn getTableColumn(int index) {
		return table.getColumnModel().getColumn(index);
	}
}
