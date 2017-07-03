package mealplaner.gui.databaseedit;

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
		return workingCopy.getSize();
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
		case 0:
			return workingCopy.getMeal(row).getName();
		case 1:
			return workingCopy.getMeal(row).getCookingTime();
		case 2:
			return workingCopy.getMeal(row).getSidedish();
		case 3:
			return workingCopy.getMeal(row).getObligatoryUtensil();
		case 4:
			return workingCopy.getMeal(row).getDaysPassed();
		case 5:
			return workingCopy.getMeal(row).getCookingPreference();
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
		workingCopy.removeMeal(workingCopy.getMeal(row));
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
			workingCopy.getMeal(row).setName((String) value);
			break;
		case 1:
			workingCopy.getMeal(row).setCookingTime((CookingTime) value);
			break;
		case 2:
			workingCopy.getMeal(row).setSidedish((Sidedish) value);
			break;
		case 3:
			workingCopy.getMeal(row).setObligatoryUtensil((ObligatoryUtensil) value);
			break;
		case 4:
			workingCopy.getMeal(row).setDaysPassed(Integer.parseInt((String) value));
			break;
		case 5:
			workingCopy.getMeal(row).setCookingPreference((CookingPreference) value);
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

	public MealListData returnContent() {
		return workingCopy;
	}
}