package mealplaner.gui.databaseedit;

import static mealplaner.BundleStore.BUNDLES;
import static mealplaner.model.Meal.copy;
import static mealplaner.model.Meal.createMeal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.table.AbstractTableModel;

import mealplaner.gui.ButtonPanelEnabling;
import mealplaner.model.Meal;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;
import mealplaner.recipes.model.Recipe;

public class DataBaseTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private String[] columnNames;
	private List<Meal> workingCopy;
	private ButtonPanelEnabling onlyActiveOnChangedButtons;
	private String editString;
	private String createString;

	DataBaseTableModel(List<Meal> mealListData, ButtonPanelEnabling onlyActiveOnChangedButtons) {
		this.onlyActiveOnChangedButtons = onlyActiveOnChangedButtons;
		columnNames = getDatabaseColumnNames();
		workingCopy = createWorkingCopy(mealListData);
		createString = BUNDLES.message("createRecipeButtonLabel");
		editString = BUNDLES.message("editRecipeButtonLabel");
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
		case 6:
			return workingCopy.get(row).getComment();
		case 7:
			return workingCopy.get(row).getRecipe().isPresent() ? editString : createString;
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
		case 6:
			return String.class;
		case 7:
			return String.class;
		default:
			return String.class;
		}
	}

	public void removeRow(int row) {
		workingCopy.remove(workingCopy.get(row));
		fireTableRowsDeleted(row, row);
		onlyActiveOnChangedButtons.enableButtons();
	}

	public void addRow(Meal newMeal) {
		int row = 0;
		while (row < workingCopy.size() && newMeal.compareTo(workingCopy.get(row)) >= 0) {
			row++;
		}
		workingCopy.add(row, newMeal);
		fireTableRowsInserted(row, row);
		onlyActiveOnChangedButtons.enableButtons();
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return col != 7;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		Meal meal = workingCopy.get(row);
		String name = meal.getName();
		CookingTime cookingTime = meal.getCookingTime();
		Sidedish sideDish = meal.getSidedish();
		ObligatoryUtensil obligatoryUtensil = meal.getObligatoryUtensil();
		int daysPassed = meal.getDaysPassed();
		CookingPreference cookingPreference = meal.getCookingPreference();
		String comment = meal.getComment();
		switch (col) {
		case 0:
			name = (String) value;
			break;
		case 1:
			cookingTime = (CookingTime) value;
			break;
		case 2:
			sideDish = (Sidedish) value;
			break;
		case 3:
			obligatoryUtensil = (ObligatoryUtensil) value;
			break;
		case 4:
			daysPassed = Integer.parseInt((String) value);
			break;
		case 5:
			cookingPreference = (CookingPreference) value;
			break;
		case 6:
			comment = (String) value;
			break;
		default:
			break;
		}
		workingCopy.set(row,
				createMeal(name, cookingTime, sideDish, obligatoryUtensil,
						cookingPreference, daysPassed, comment, meal.getRecipe()));
		fireTableCellUpdated(row, col);
		onlyActiveOnChangedButtons.enableButtons();
	}

	public void update(List<Meal> mealListData) {
		workingCopy = createWorkingCopy(mealListData);
		fireTableDataChanged();
		onlyActiveOnChangedButtons.disableButtons();
	}

	public List<Meal> returnContent() {
		return workingCopy;
	}

	private List<Meal> createWorkingCopy(List<Meal> meals) {
		List<Meal> workingCopy = new ArrayList<>();
		meals.forEach(meal -> {
			Meal copy = copy(meal);
			workingCopy.add(copy);
		});
		return workingCopy;
	}

	private String[] getDatabaseColumnNames() {
		String[] columnNames = { BUNDLES.message("mealNameColumn"),
				BUNDLES.message("cookingLengthColumn"),
				BUNDLES.message("sidedishColumn"),
				BUNDLES.message("utensilColumn"),
				BUNDLES.message("cookedLastTimeColumn"),
				BUNDLES.message("popularityColumn"),
				BUNDLES.message("commentInsertColumn"),
				BUNDLES.message("recipeEditColum") };
		return columnNames;
	}

	public void addRecipe(Optional<Recipe> editedRecipe, int row) {
		workingCopy.set(row, workingCopy.get(row).addRecipe(editedRecipe));
		fireTableCellUpdated(row, 7);
		onlyActiveOnChangedButtons.enableButtons();
	}
}