package mealplaner.recipes.gui.dialogs.recepies;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.NonnegativeInteger.ZERO;
import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withNonnegativeIntegerContent;
import static mealplaner.recipes.model.QuantitativeIngredient.create;

import java.util.List;

import javax.swing.JComboBox;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;

import mealplaner.commons.gui.tables.Table;
import mealplaner.commons.gui.tables.TableColumnBuilder;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.Measure;
import mealplaner.recipes.model.QuantitativeIngredient;
import mealplaner.recipes.provider.IngredientProvider;

public class IngredientsTable {

	public static Table setupTable(List<QuantitativeIngredient> ingredients,
			IngredientProvider ingredientProvider) {
		JComboBox<String> autoCompleteBox = setupIngredientsAutoCompleteBox(ingredientProvider);
		return createNewTable()
				.withRowCount(ingredients::size)
				.addColumn(withContent(String.class)
						.isEditable()
						.withColumnName(BUNDLES.message("ingredientNameColumn"))
						.setValueToOrderedImmutableList(ingredients,
								(ingredient, name) -> {
									Ingredient newIngredient = ingredientProvider.getIngredients()
											.stream()
											.filter(ing -> ing.getName().equals(name))
											.findAny()
											.orElse(Ingredient.emptyIngredient());
									return create(newIngredient, ingredient.getAmount());
								})
						.alsoUpdatesCellsOfColumns(2)
						.getValueFromOrderedList(ingredients,
								ingredient -> ingredient.getIngredient().getName())
						.isEditable()
						.setDefaultValueForEmptyRow("")
						.overwriteTableCellEditor(new ComboBoxCellEditor(autoCompleteBox))
						.build())
				.addColumn(withNonnegativeIntegerContent()
						.withColumnName(BUNDLES.message("ingredientAmountColumn"))
						.getValueFromOrderedList(ingredients,
								ingredient -> ingredient.getAmount())
						.setValueToOrderedImmutableList(ingredients,
								(ingredient, amount) -> create(ingredient.getIngredient(), amount))
						.isEditable()
						.setDefaultValueForEmptyRow(ZERO)
						.build())
				.addColumn(TableColumnBuilder.withEnumContent(Measure.class)
						.withColumnName(BUNDLES.message("ingredientMeasureColumn"))
						.getValueFromOrderedList(ingredients,
								ingredient -> ingredient.getIngredient().getMeasure())
						.setDefaultValueForEmptyRow(Measure.NONE)
						.build())
				.addDefaultRowToUnderlyingModel(() -> {
					ingredients.add(QuantitativeIngredient.DEFAULT);
				})
				.deleteRowsOnDelete(row -> ingredients.remove((int) row))
				.buildDynamicTable();
	}

	private static JComboBox<String> setupIngredientsAutoCompleteBox(
			IngredientProvider ingredientProvider) {
		List<Ingredient> ingredients = ingredientProvider.getIngredients();
		String[] ingredientsArray = new String[ingredientProvider.size() + 1];
		for (int i = 0; i < ingredients.size(); i++) {
			ingredientsArray[i] = ingredients.get(i).getName();
		}
		ingredientsArray[ingredientsArray.length - 1] = "";
		JComboBox<String> autoCompleteBox = new JComboBox<>(ingredientsArray);
		AutoCompleteDecorator.decorate(autoCompleteBox);
		return autoCompleteBox;
	}
}
