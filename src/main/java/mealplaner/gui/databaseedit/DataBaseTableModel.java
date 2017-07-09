package mealplaner.gui.databaseedit;

import java.util.List;
import java.util.ResourceBundle;

import javax.swing.table.AbstractTableModel;

import mealplaner.gui.ButtonPanelEnabling;
import mealplaner.gui.model.StringArrayCollection;
import mealplaner.model.Meal;
import mealplaner.model.MealListData;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;

public class DataBaseTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private String[] columnNames;
	private MealListData workingCopy;
	private ButtonPanelEnabling onlyActiveOnChangedButtons;

	DataBaseTableModel(MealListData mealListData, ButtonPanelEnabling onlyActiveOnChangedButtons,
			ResourceBundle messages) {
		this.onlyActiveOnChangedButtons = onlyActiveOnChangedButtons;
		columnNames = StringArrayCollection.getDatabaseColumnNames(messages);
		workingCopy = new MealListData(mealListData);
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return workingCopy.size();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
		case 0:
			return workingCopy.get(row).getName();
		case 1:
			return workingCopy.get(row).getCookingTime();
		case 2:
			return workingCopy.get(row).getSidedish();
		case 3:
			return workingCopy.get(row).getObligatoryUtensil();
		case 4:
			return workingCopy.get(row).getDaysPassed();
		case 5:
			return workingCopy.get(row).getCookingPreference();
		default:
			return "";
		}
	}

	@Override
	public Class<?> getColumnClass(int col) {
		switch (col) {
		case 0:
			return String.class;
		case 1:
			return CookingTime.class;
		case 2:
			return Sidedish.class;
		case 3:
			return ObligatoryUtensil.class;
		case 4:
			return Integer.class;
		case 5:
			return CookingPreference.class;
		default:
			return String.class;
		}
	}

	public void removeRow(int row) {
		workingCopy.removeMeal(workingCopy.get(row));
		fireTableRowsDeleted(row, row);
		onlyActiveOnChangedButtons.enableButtons();
	}

	public void addRow(Meal newMeal) {
		int row = workingCopy.addMealAtSortedPosition(newMeal);
		fireTableRowsInserted(row, row);
		onlyActiveOnChangedButtons.enableButtons();
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return true;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		switch (col) {
		case 0:
			workingCopy.get(row).setName((String) value);
			break;
		case 1:
			workingCopy.get(row).setCookingTime((CookingTime) value);
			break;
		case 2:
			workingCopy.get(row).setSidedish((Sidedish) value);
			break;
		case 3:
			workingCopy.get(row).setObligatoryUtensil((ObligatoryUtensil) value);
			break;
		case 4:
			workingCopy.get(row).setDaysPassed(Integer.parseInt((String) value));
			break;
		case 5:
			workingCopy.get(row).setCookingPreference((CookingPreference) value);
			break;
		default:
			return;
		}
		fireTableCellUpdated(row, col);
		onlyActiveOnChangedButtons.enableButtons();
	}

	public void update(MealListData mealListData) {
		workingCopy = new MealListData(mealListData);
		fireTableDataChanged();
		onlyActiveOnChangedButtons.disableButtons();
	}

	public List<Meal> returnContent() {
		return workingCopy;
	}
}