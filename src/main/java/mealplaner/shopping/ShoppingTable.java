package mealplaner.shopping;

import static mealplaner.recipes.model.Ingredient.emptyIngredient;

import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTable;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;

import mealplaner.gui.dialogs.proposaloutput.TablePrinter;
import mealplaner.gui.editing.PositiveIntegerCellEditor;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.provider.IngredientProvider;

public class ShoppingTable {
	private JTable table;
	private ShoppingTableModel tableModel;

	public JTable setupTable(ShoppingList shoppingList, IngredientProvider ingredientProvider) {
		tableModel = new ShoppingTableModel(shoppingList);
		table = new JTable(tableModel);
		table.setDefaultEditor(Integer.class, new PositiveIntegerCellEditor());
		setupIngredientsAutoCompleteBox(ingredientProvider);
		return table;
	}

	public void printTable(JFrame frame) {
		TablePrinter.printTable(table, frame);
	}

	private void setupIngredientsAutoCompleteBox(IngredientProvider ingredientProvider) {
		List<Ingredient> ingredients = ingredientProvider.getIngredients();
		Ingredient[] ingredientsArray = new Ingredient[ingredientProvider.size() + 1];
		for (int i = 0; i < ingredients.size(); i++) {
			ingredientsArray[i] = ingredients.get(i);
		}
		ingredientsArray[ingredientsArray.length - 1] = emptyIngredient();
		JComboBox<Ingredient> autoCompleteBox = new JComboBox<>(ingredientsArray);
		AutoCompleteDecorator.decorate(autoCompleteBox);
		table.getColumnModel().getColumn(0).setCellEditor(new ComboBoxCellEditor(autoCompleteBox));
	}
}
