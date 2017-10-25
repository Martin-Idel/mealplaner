package mealplaner.gui.databaseedit;

import static mealplaner.gui.commons.SwingUtilityMethods.setupComboBoxEditor;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import mealplaner.gui.editing.PositiveIntegerCellEditor;
import mealplaner.gui.editing.TextCellEditor;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;

public class DataBaseTableFactory {
	private JTable table;

	public JTable getTable() {
		return table;
	}

	public JTable createTable(DataBaseTableModel tableModel) {
		table = new JTable(tableModel);
		setTableEditors(table);
		return table;
	}

	private void setTableEditors(JTable table) {
		setupComboBoxEditor(getTableColumn(1), CookingTime.class);
		setupComboBoxEditor(getTableColumn(2), Sidedish.class);
		setupComboBoxEditor(getTableColumn(3), ObligatoryUtensil.class);
		setupComboBoxEditor(getTableColumn(5), CookingPreference.class);
		table.setDefaultEditor(Integer.class, new PositiveIntegerCellEditor());
		table.setDefaultEditor(String.class, new TextCellEditor());
	}

	private TableColumn getTableColumn(int index) {
		return table.getColumnModel().getColumn(index);
	}
}
