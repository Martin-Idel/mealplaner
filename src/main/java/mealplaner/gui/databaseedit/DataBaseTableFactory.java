package mealplaner.gui.databaseedit;

import static mealplaner.gui.commons.SwingUtilityMethods.setupComboBoxEditor;
import static mealplaner.gui.commons.SwingUtilityMethods.setupEnumColumnRenderer;
import static mealplaner.gui.model.EnumToStringRepresentation.getCookingPreferenceStrings;
import static mealplaner.gui.model.EnumToStringRepresentation.getCookingTimeStrings;
import static mealplaner.gui.model.EnumToStringRepresentation.getObligatoryUtensilStrings;
import static mealplaner.gui.model.EnumToStringRepresentation.getSidedishStrings;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import mealplaner.BundleStore;
import mealplaner.gui.editing.PositiveIntegerCellEditor;
import mealplaner.gui.editing.TextCellEditor;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;

public class DataBaseTableFactory {
	private BundleStore bundles;
	private JTable table;

	public DataBaseTableFactory(BundleStore bundles) {
		this.bundles = bundles;
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
		setupComboBoxEditor(getTableColumn(1), CookingTime.class, getCookingTimeStrings(bundles));
		setupComboBoxEditor(getTableColumn(2), Sidedish.class, getSidedishStrings(bundles));
		setupComboBoxEditor(getTableColumn(3), ObligatoryUtensil.class,
				getObligatoryUtensilStrings(bundles));
		setupComboBoxEditor(getTableColumn(5), CookingPreference.class,
				getCookingPreferenceStrings(bundles));
		table.setDefaultEditor(Integer.class, new PositiveIntegerCellEditor());
		table.setDefaultEditor(String.class, new TextCellEditor());
	}

	private void setTableRenderers() {
		setupEnumColumnRenderer(getTableColumn(1), CookingTime.class,
				getCookingTimeStrings(bundles));
		setupEnumColumnRenderer(getTableColumn(2), Sidedish.class, getSidedishStrings(bundles));
		setupEnumColumnRenderer(getTableColumn(3), ObligatoryUtensil.class,
				getObligatoryUtensilStrings(bundles));
		setupEnumColumnRenderer(getTableColumn(5), CookingPreference.class,
				getCookingPreferenceStrings(bundles));
	}

	private TableColumn getTableColumn(int index) {
		return table.getColumnModel().getColumn(index);
	}
}
