package mealplaner.gui.editing;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

public class CheckboxCellRenderer implements TableCellRenderer {

	private final JCheckBox checkbox;

	public CheckboxCellRenderer() {
		checkbox = new JCheckBox();
		checkbox.setHorizontalAlignment(SwingConstants.CENTER);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		checkbox.setEnabled(table.isCellEditable(row, column));
		checkbox.setSelected(value != null && (Boolean) value);
		return checkbox;
	}
}
