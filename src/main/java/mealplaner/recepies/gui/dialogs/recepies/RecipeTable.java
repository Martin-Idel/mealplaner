package mealplaner.recepies.gui.dialogs.recepies;

import static mealplaner.recepies.model.Measure.getMeasureStrings;

import java.util.List;
import java.util.Optional;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;

import mealplaner.BundleStore;
import mealplaner.gui.commons.SwingUtilityMethods;
import mealplaner.gui.editing.PositiveIntegerCellEditor;
import mealplaner.recepies.model.Ingredient;
import mealplaner.recepies.model.Measure;
import mealplaner.recepies.model.Recipe;
import mealplaner.recepies.provider.IngredientProvider;

public class RecipeTable {
	private JTable table;
	private RecipeTableModel tableModel;

	public Optional<Recipe> getRecipe(int numberOfPortions) {
		return tableModel.getRecipe(numberOfPortions);
	}

	public JTable setupTable(Optional<Recipe> recipe, BundleStore bundles,
			IngredientProvider ingredientProvider) {
		tableModel = new RecipeTableModel(bundles, recipe);
		table = new JTable(tableModel);
		SwingUtilityMethods.setupEnumColumnRenderer(getTableColumn(2), Measure.class,
				getMeasureStrings(bundles));
		table.setDefaultEditor(Integer.class, new PositiveIntegerCellEditor());
		setupIngredientsAutoCompleteBox(ingredientProvider);

		return table;
	}

	private void setupIngredientsAutoCompleteBox(IngredientProvider ingredientProvider) {
		List<Ingredient> ingredients = ingredientProvider.getIngredients();
		Ingredient[] ingredientsArray = new Ingredient[ingredientProvider.size() + 1];
		for (int i = 0; i < ingredients.size(); i++) {
			ingredientsArray[i] = ingredients.get(i);
		}
		ingredientsArray[ingredientsArray.length - 1] = new Ingredient.EmptyIngredient();
		JComboBox<Ingredient> autoCompleteBox = new JComboBox<>(ingredientsArray);
		AutoCompleteDecorator.decorate(autoCompleteBox);
		table.getColumnModel().getColumn(0).setCellEditor(new ComboBoxCellEditor(autoCompleteBox));
	}

	private TableColumn getTableColumn(int index) {
		return table.getColumnModel().getColumn(index);
	}
}
