package mealplaner.shopping;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.of;
import static mealplaner.BundleStore.BUNDLES;
import static mealplaner.recipes.model.Ingredient.emptyIngredient;
import static mealplaner.recipes.model.IngredientType.BAKING_GOODS;
import static mealplaner.recipes.model.IngredientType.CANNED_FRUIT;
import static mealplaner.recipes.model.IngredientType.CANNED_VEGETABLES;
import static mealplaner.recipes.model.IngredientType.DRY_GOODS;
import static mealplaner.recipes.model.IngredientType.FLUID;
import static mealplaner.recipes.model.IngredientType.FRESH_FRUIT;
import static mealplaner.recipes.model.IngredientType.FRESH_VEGETABLES;
import static mealplaner.recipes.model.IngredientType.MEAT_PRODUCTS;
import static mealplaner.recipes.model.IngredientType.MILK_EGG_PRODUCTS;
import static mealplaner.recipes.model.IngredientType.OTHER;
import static mealplaner.recipes.model.IngredientType.RED_MEAT;
import static mealplaner.recipes.model.IngredientType.SPICE;
import static mealplaner.recipes.model.IngredientType.WHITE_MEAT;
import static mealplaner.shopping.ShoppingTablePart.from;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.Measure;

public class ShoppingTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private List<ShoppingTablePart> shoppingParts;

	public ShoppingTableModel(ShoppingList shoppingList) {
		shoppingParts = new ArrayList<>();
		shoppingParts.add(from(shoppingList, FRESH_FRUIT, FRESH_VEGETABLES));
		shoppingParts
				.add(from(shoppingList, MEAT_PRODUCTS, MILK_EGG_PRODUCTS, RED_MEAT, WHITE_MEAT));
		shoppingParts.add(from(shoppingList, CANNED_FRUIT, CANNED_VEGETABLES));
		shoppingParts.add(from(shoppingList, DRY_GOODS, SPICE, BAKING_GOODS));
		shoppingParts.add(from(shoppingList, FLUID, OTHER));
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		return shoppingParts.stream()
				.flatMapToInt(part -> of(part.size()))
				.sum();
	}

	@Override
	public String getColumnName(int col) {
		switch (col) {
		case 0:
			return BUNDLES.message("ingredientNameColumn");
		case 1:
			return BUNDLES.message("ingredientAmountColumn");
		case 2:
			return BUNDLES.message("ingredientMeasureColumn");
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
			return Integer.class;
		case 2:
			return Measure.class;
		default:
			return String.class;
		}
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return !(col == 2 || col == 0 && row != getRowCount() - 1);
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		switch (col) {
		case 0:
			Ingredient ingredient = (Ingredient) value;
			Integer amountAtSameRow = (Integer) getValueAt(row, 1);
			shoppingParts.forEach(
					part -> part.changeIngredient(ingredient, amountAtSameRow));
			break;
		case 1:
			Ingredient ingredientAtSameRow = (Ingredient) getValueAt(row, 0);
			Integer amount = (Integer) value;
			shoppingParts.forEach(
					part -> part.changeIngredient(ingredientAtSameRow, amount));
			break;
		default:
			return;
		}
		fireTableCellUpdated(row, col);
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
		case 0:
			return row == getRowCount() - 1
					? emptyIngredient()
					: shoppingParts.stream()
							.map(ShoppingTablePart::getContent)
							.map(Map::keySet)
							.flatMap(Set::stream)
							.collect(toList())
							.get(row);
		case 1:
			Ingredient oldIngredient = (Ingredient) getValueAt(row, 0);
			return row == getRowCount() - 1
					? 0
					: shoppingParts.stream()
							.filter(part -> part.isPartOf(oldIngredient))
							.map(part -> part.getAmount(oldIngredient))
							.findAny().orElse(0);
		case 2:
			return row == getRowCount() - 1
					? Measure.NONE
					: ((Ingredient) getValueAt(row, 0)).getMeasure();
		default:
			return "";
		}
	}
}
