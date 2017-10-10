package mealplaner.recipes.gui.dialogs.recepies;

import static mealplaner.recipes.model.Ingredient.emptyIngredient;
import static mealplaner.recipes.model.Measure.getMeasureStrings;

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
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.Measure;
import mealplaner.recipes.model.Recipe;
import mealplaner.recipes.provider.IngredientProvider;

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
		ingredientsArray[ingredientsArray.length - 1] = emptyIngredient();
		JComboBox<Ingredient> autoCompleteBox = new JComboBox<>(ingredientsArray);
		AutoCompleteDecorator.decorate(autoCompleteBox);
		table.getColumnModel().getColumn(0).setCellEditor(new ComboBoxCellEditor(autoCompleteBox));
	}

	private TableColumn getTableColumn(int index) {
		return table.getColumnModel().getColumn(index);
	}
}
