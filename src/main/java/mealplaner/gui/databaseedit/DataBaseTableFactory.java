package mealplaner.gui.databaseedit;

import static mealplaner.gui.model.EnumToStringRepresentation.getCookingPreferenceStrings;
import static mealplaner.gui.model.EnumToStringRepresentation.getCookingTimeStrings;
import static mealplaner.gui.model.EnumToStringRepresentation.getObligatoryUtensilStrings;
import static mealplaner.gui.model.EnumToStringRepresentation.getSidedishStrings;

import java.util.ResourceBundle;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import mealplaner.gui.commons.SwingUtilityMethods;
import mealplaner.gui.editing.PositiveIntegerCellEditor;
import mealplaner.gui.editing.TextCellEditor;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;

public class DataBaseTableFactory {
	private ResourceBundle messages;
	private JTable table;

	public DataBaseTableFactory(ResourceBundle messages) {
		this.messages = messages;
	}

	public JTable getTable() {
		return table;
	}

	public JTable createTable(DataBaseTableModel tableModel) {
		table = new JTable(tableModel);
		setTableEditors(table);
		setTableRenderers();
		return table;
	}

	private void setTableEditors(JTable table) {
		SwingUtilityMethods.setupComboBoxEditor(getTableColumn(1), CookingTime.class, getCookingTimeStrings(messages));
		SwingUtilityMethods.setupComboBoxEditor(getTableColumn(2), Sidedish.class, getSidedishStrings(messages));
		SwingUtilityMethods.setupComboBoxEditor(getTableColumn(3), ObligatoryUtensil.class,
				getObligatoryUtensilStrings(messages));
		SwingUtilityMethods.setupComboBoxEditor(getTableColumn(5), CookingPreference.class,
				getCookingPreferenceStrings(messages));
		table.setDefaultEditor(Integer.class, new PositiveIntegerCellEditor());
		table.setDefaultEditor(String.class, new TextCellEditor());
	}

	private void setTableRenderers() {
		SwingUtilityMethods.setupEnumColumnRenderer(getTableColumn(1), CookingTime.class,
				getCookingTimeStrings(messages));
		SwingUtilityMethods.setupEnumColumnRenderer(getTableColumn(2), Sidedish.class, getSidedishStrings(messages));
		SwingUtilityMethods.setupEnumColumnRenderer(getTableColumn(3), ObligatoryUtensil.class,
				getObligatoryUtensilStrings(messages));
		SwingUtilityMethods.setupEnumColumnRenderer(getTableColumn(5), CookingPreference.class,
				getCookingPreferenceStrings(messages));
	}

	private TableColumn getTableColumn(int index) {
		return table.getColumnModel().getColumn(index);
	}
}
