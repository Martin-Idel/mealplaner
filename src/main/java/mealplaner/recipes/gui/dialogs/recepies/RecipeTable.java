package mealplaner.recipes.gui.dialogs.recepies;

import static java.util.stream.Collectors.toList;
import static mealplaner.BundleStore.BUNDLES;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.gui.tables.TableColumnBuilder.withNonnegativeIntegerContent;
import static mealplaner.recipes.model.QuantitativeIngredientBuilder.builder;
import static mealplaner.recipes.model.QuantitativeIngredientBuilder.from;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JComboBox;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.gui.tables.Table;
import mealplaner.gui.tables.TableColumnBuilder;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.Measure;
import mealplaner.recipes.model.QuantitativeIngredient;
import mealplaner.recipes.model.Recipe;
import mealplaner.recipes.provider.IngredientProvider;

public class RecipeTable {
	private List<QuantitativeIngredient> ingredients;
	private IngredientProvider ingredientProvider;
	private NonnegativeInteger numberOfPeople;

	public RecipeTable(Recipe recipe, IngredientProvider ingredientProvider) {
		numberOfPeople = nonNegative(recipe.getNumberOfPortions());
		this.ingredients = recipe.getRecipeFor(recipe.getNumberOfPortions()).entrySet().stream()
				.map(entry -> builder()
						.ingredient(entry.getKey())
						.amount(nonNegative(entry.getValue()))
						.forPeople(numberOfPeople)
						.build())
				.sorted((ingredient1, ingredient2) -> ingredient1.getIngredient().getName()
						.compareTo(ingredient2.getIngredient().getName()))
				.collect(toList());
		this.ingredientProvider = ingredientProvider;
	}

	public Table setupTable() {
		JComboBox<String> autoCompleteBox = setupIngredientsAutoCompleteBox();
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
									return from(ingredient).ingredient(newIngredient).build();
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
								ingredient -> ingredient.getAmountFor(numberOfPeople))
						.setValueToOrderedImmutableList(ingredients,
								(ingredient, amount) -> from(ingredient).amount(amount).build())
						.isEditable()
						.setDefaultValueForEmptyRow(nonNegative(0))
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
				.buildDynamicTable();
	}

	// TODO: Rewrite with better Recipeinterface
	public Optional<Recipe> getRecipe(NonnegativeInteger numberOfPeople) {
		this.numberOfPeople = numberOfPeople;
		Map<Ingredient, Integer> ing = new HashMap<>();
		ingredients.stream()
				.forEach(ingredient -> {
					if (ing.containsKey(ingredient.getIngredient())) {
						ing.put(ingredient.getIngredient(),
								ing.get(ingredient.getIngredient())
										+ ingredient.getAmountFor(numberOfPeople).value);
					} else {
						ing.put(ingredient.getIngredient(),
								ingredient.getAmountFor(ingredient.getNumberOfPeople()).value);
					}
				});
		if (ing.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(Recipe.from(numberOfPeople.value, ing));
		}
	}

	private JComboBox<String> setupIngredientsAutoCompleteBox() {
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
