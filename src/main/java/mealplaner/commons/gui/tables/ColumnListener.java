package mealplaner.commons.gui.tables;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import javax.swing.JTable;

public class ColumnListener extends MouseAdapter {

	private final int columnNumber;
	private final Consumer<Integer> onClick;

	private ColumnListener(int columnNumber, Consumer<Integer> onClick) {
		this.columnNumber = columnNumber;
		this.onClick = onClick;
	}

	public static ColumnListener createColumnListener(int columnNumber, Consumer<Integer> onClick) {
		return new ColumnListener(columnNumber, onClick);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		JTable tableSource = (JTable) event.getSource();
		int row = tableSource.getSelectedRow();
		int column = tableSource.getSelectedColumn();
		if (column == columnNumber) {
			onClick.accept(row);
		}
	}
}
