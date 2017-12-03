package mealplaner.gui.databaseedit;

import static java.util.stream.Collectors.toList;
import static mealplaner.BundleStore.BUNDLES;
import static mealplaner.commons.Utils.not;
import static mealplaner.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.gui.tables.TableColumnBuilder.withEnumContent;
import static mealplaner.gui.tables.TableColumnBuilder.withNonnegativeIntegerContent;
import static mealplaner.model.MealBuilder.from;

import java.awt.BorderLayout;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mealplaner.DataStore;
import mealplaner.DataStoreEventType;
import mealplaner.DataStoreListener;
import mealplaner.gui.ButtonPanelEnabling;
import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.gui.dialogs.mealinput.SingleMealInput;
import mealplaner.gui.editing.TextCellEditor;
import mealplaner.gui.tables.Table;
import mealplaner.model.Meal;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;
import mealplaner.recipes.gui.dialogs.recepies.RecipeInput;
import mealplaner.recipes.model.Recipe;
import mealplaner.recipes.provider.IngredientProvider;

// TODO: When entering meals but having entered unsaved meals, maybe we want to just add new (saved) meals and not delete the rest?
public class DatabaseEdit implements DataStoreListener {
	private JFrame dataFrame;
	private JPanel dataPanel;
	private Table table;

	private ButtonPanelEnabling buttonPanel;

	private DataStore mealplanerData;
	private List<Meal> meals;

	public DatabaseEdit(DataStore mealPlan, JFrame parentFrame, JPanel parentPanel) {
		this.mealplanerData = mealPlan;
		this.meals = mealPlan.getMeals().stream().map(Meal::copy).collect(toList());
		mealplanerData.register(this);

		dataFrame = parentFrame;
		dataPanel = parentPanel;
		dataPanel.setLayout(new BorderLayout());
	}

	public void setupPane(Consumer<List<Meal>> setMeals, IngredientProvider ingredientProvider) {
		buttonPanel = createButtonPanelWithEnabling(setMeals, ingredientProvider);
		buttonPanel.disableButtons();

		table = createTable(ingredientProvider);
		table.addScrollingTableToPane(dataPanel);

		dataPanel.add(buttonPanel.getPanel(), BorderLayout.SOUTH);
	}

	private Table createTable(IngredientProvider ingredientProvider) {
		return createNewTable()
				.withRowCount(meals::size)
				.addColumn(withContent(String.class)
						.withColumnName(BUNDLES.message("mealNameColumn"))
						.setValueToOrderedImmutableList(meals,
								(meal, name) -> from(meal).name(name).create())
						.getValueFromOrderedList(meals, meal -> meal.getName())
						.isEditable()
						.overwriteTableCellEditor(new TextCellEditor())
						.build())
				.addColumn(withEnumContent(CookingTime.class)
						.withColumnName(BUNDLES.message("cookingLengthColumn"))
						.setValueToOrderedImmutableList(meals,
								(meal, cookingTime) -> from(meal).cookingTime(cookingTime).create())
						.getValueFromOrderedList(meals, meal -> meal.getCookingTime())
						.isEditable()
						.build())
				.addColumn(withEnumContent(Sidedish.class)
						.withColumnName(BUNDLES.message("sidedishColumn"))
						.setValueToOrderedImmutableList(meals,
								(meal, sidedish) -> from(meal).sidedish(sidedish).create())
						.getValueFromOrderedList(meals, meal -> meal.getSidedish())
						.isEditable()
						.build())
				.addColumn(withEnumContent(ObligatoryUtensil.class)
						.withColumnName(BUNDLES.message("utensilColumn"))
						.setValueToOrderedImmutableList(meals,
								(meal, utensil) -> from(meal).obligatoryUtensil(utensil).create())
						.getValueFromOrderedList(meals, meal -> meal.getObligatoryUtensil())
						.isEditable()
						.build())
				.addColumn(withNonnegativeIntegerContent()
						.withColumnName(BUNDLES.message("cookedLastTimeColumn"))
						.setValueToOrderedImmutableList(meals,
								(meal, day) -> from(meal).daysPassed(day).create())
						.getValueFromOrderedList(meals, meal -> meal.getDaysPassed())
						.isEditable()
						.build())
				.addColumn(withEnumContent(CookingPreference.class)
						.withColumnName(BUNDLES.message("popularityColumn"))
						.setValueToOrderedImmutableList(meals,
								(meal, preference) -> from(meal).cookingPreference(preference)
										.create())
						.getValueFromOrderedList(meals, meal -> meal.getCookingPreference())
						.isEditable()
						.build())
				.addColumn(withContent(String.class)
						.withColumnName(BUNDLES.message("commentInsertColumn"))
						.setValueToOrderedImmutableList(meals,
								(meal, comment) -> from(meal).comment(comment).create())
						.getValueFromOrderedList(meals, meal -> meal.getComment())
						.isEditable()
						.overwriteTableCellEditor(new TextCellEditor())
						.build())
				.addColumn(withContent(String.class)
						.withColumnName(BUNDLES.message("recipeEditColum"))
						.getRowValueFromUnderlyingModel(
								row -> meals.get(row).getRecipe().isPresent()
										? BUNDLES.message("editRecipeButtonLabel")
										: BUNDLES.message("createRecipeButtonLabel"))
						.overwriteTableCellEditor(new TextCellEditor())
						.build())
				.addListenerToThisColumn((row) -> {
					Optional<Recipe> recipe = meals.get(row).getRecipe();
					Optional<Recipe> editedRecipe = new RecipeInput(
							dataFrame, BUNDLES.message("recipeInputDialogTitle"))
									.showDialog(recipe, ingredientProvider);
					if (editedRecipe != null) {
						Meal newMeal = from(meals.get(row)).optionalRecipe(editedRecipe).create();
						meals.set(row, newMeal);
						buttonPanel.enableButtons();
					}
				})
				.buildTable();
	}

	private ButtonPanelEnabling createButtonPanelWithEnabling(Consumer<List<Meal>> setData,
			IngredientProvider ingredientProvider) {
		return new ButtonPanelBuilder()
				.addButton(BUNDLES.message("addButton"),
						BUNDLES.message("addButtonMnemonic"),
						action -> {
							Meal newMeal = new SingleMealInput(dataFrame, ingredientProvider)
									.showDialog();
							insertItem(Optional.of(newMeal));
						})
				.addButton(BUNDLES.message("removeSelectedButton"),
						BUNDLES.message("removeSelectedButtonMnemonic"),
						action -> {
							Arrays.stream(table.getSelectedRows())
									.collect(ArrayDeque<Integer>::new, ArrayDeque<Integer>::add,
											ArrayDeque<Integer>::addAll)
									.descendingIterator()
									.forEachRemaining(number -> {
										meals.remove((int) number);
										table.deleteRows(number, number);
									});
							buttonPanel.enableButtons();
						})
				.addSaveButton(action -> {
					setData.accept(meals);
					buttonPanel.disableButtons();
				})
				.makeLastButtonEnabling()
				.addButton(BUNDLES.message("cancelButton"),
						BUNDLES.message("cancelButtonMnemonic"),
						action -> updateTable())
				.makeLastButtonEnabling()
				.buildEnablingPanel();
	}

	public void printTable() {
		table.printTable(dataFrame);
	}

	public void insertItem(Optional<Meal> optionalMeal) {
		optionalMeal.ifPresent(meal -> addAtSortedPosition(meal));
	}

	private void addAtSortedPosition(Meal meal) {
		int row = 0;
		while (row < meals.size() && meal.compareTo(meals.get(row)) >= 0) {
			row++;
		}
		meals.add(row, meal);
		table.insertRows(row, row);
		buttonPanel.enableButtons();
	}

	public void updateTable() {
		// We have to update the reference meals as we can't replace it - it's fixed to
		// the table
		List<Meal> newMeals = mealplanerData.getMeals();
		meals.removeAll(meals.stream().filter(not(newMeals::contains)).collect(toList()));
		newMeals.stream().filter(not(meals::contains)).map(Meal::copy).forEach(meals::add);
		table.update();
		buttonPanel.disableButtons();
	}

	@Override
	public void updateData(DataStoreEventType event) {
		if (event == DataStoreEventType.DATABASE_EDITED) {
			updateTable();
		}
	}
}