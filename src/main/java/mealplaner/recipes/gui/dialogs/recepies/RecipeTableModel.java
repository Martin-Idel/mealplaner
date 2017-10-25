package mealplaner.recipes.gui.dialogs.recepies;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static mealplaner.BundleStore.BUNDLES;
import static mealplaner.recipes.model.Ingredient.emptyIngredient;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.swing.table.AbstractTableModel;

import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.Measure;
import mealplaner.recipes.model.Recipe;

public class RecipeTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private String[] columnNames;
	private Map<Ingredient, Integer> recipe;

	public RecipeTableModel(Optional<Recipe> recipe) {
		this.recipe = recipe.isPresent()
				? recipe.get().getRecipeFor(recipe.get().getNumberOfPortions())
				: new HashMap<>();
		this.columnNames = getColumnNames();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return recipe.size() + 1;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Class<?> getColumnClass(int col) {
		switch (col) {
		case 0:
			return Ingredient.class;
		case 1:
			return Integer.class;
		case 2:
			return Measure.class;
		default:
			return String.class;
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return row != getRowCount() - 1 && (column == 0 || column == 1) || column == 0;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		Ingredient ingredientAtRow = (row != getRowCount() - 1)
				? (Ingredient) getValueAt(row, 0)
				: emptyIngredient();
		switch (col) {
		case 0:
			Ingredient newIngredient = (Ingredient) value;
			if (newIngredient.getName().equals("")) {
				recipe.remove(ingredientAtRow);
				fireTableRowsDeleted(row, row);
			} else if (ingredientAtRow.getName().equals("")) {
				recipe.put(newIngredient, 0);
				setValueAt(newIngredient.getMeasure(), row, 2);
				fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
			} else if (!recipe.containsKey(value)) {
				recipe.put(newIngredient, (Integer) getValueAt(row, 1));
				recipe.remove(ingredientAtRow);
				setValueAt(newIngredient.getMeasure(), row, 2);
			}
			break;
		case 1:
			recipe.replace((Ingredient) getValueAt(row, 0), Integer.parseInt((String) value));
			break;
		}
		fireTableCellUpdated(row, col);
	}

	@Override
	public Object getValueAt(int row, int column) {
		switch (column) {
		case 0:
			if (row == getRowCount() - 1) {
				return emptyIngredient();
			}
			Ingredient[] ingredients = new Ingredient[recipe.size()];
			return recipe.keySet().toArray(ingredients)[row];
		case 1:
			return row == getRowCount() - 1 ? 0 : recipe.get(getValueAt(row, 0));
		case 2:
			return ((Ingredient) getValueAt(row, 0)).getMeasure();
		default:
			return "";
		}
	}

	private String[] getColumnNames() {
		String[] columnNames = { BUNDLES.message("ingredientNameColumn"),
				BUNDLES.message("ingredientAmountColumn"),
				BUNDLES.message("ingredientMeasureColumn") };
		return columnNames;
	}

	public Optional<Recipe> getRecipe(int numberOfPortions) {
		if (recipe.isEmpty()) {
			return empty();
		} else {
			return of(Recipe.from(numberOfPortions, recipe));
		}
	}
}
