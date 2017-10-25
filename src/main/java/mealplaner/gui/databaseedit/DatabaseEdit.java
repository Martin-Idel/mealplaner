package mealplaner.gui.databaseedit;

import static mealplaner.BundleStore.BUNDLES;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import mealplaner.DataStore;
import mealplaner.DataStoreEventType;
import mealplaner.DataStoreListener;
import mealplaner.gui.ButtonPanelEnabling;
import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.gui.dialogs.mealinput.SingleMealInput;
import mealplaner.gui.dialogs.proposaloutput.TablePrinter;
import mealplaner.model.Meal;
import mealplaner.recipes.gui.dialogs.recepies.RecipeInput;
import mealplaner.recipes.model.Recipe;
import mealplaner.recipes.provider.IngredientProvider;

// TODO: When entering meals but having entered unsaved meals, maybe we want to just add new (saved) meals and not delete the rest?
public class DatabaseEdit implements DataStoreListener {
	private JFrame dataFrame;
	private JPanel dataPanel;
	private JTable table;

	private DataBaseTableModel tableModel;
	private ButtonPanelEnabling buttonPanel;

	private DataStore mealplanerData;

	public DatabaseEdit(DataStore mealPlan, JFrame parentFrame, JPanel parentPanel) {
		this.mealplanerData = mealPlan;
		mealplanerData.register(this);

		dataFrame = parentFrame;
		dataPanel = parentPanel;
		dataPanel.setLayout(new BorderLayout());
	}

	public void setupPane(Consumer<List<Meal>> setMeals, IngredientProvider ingredientProvider) {
		buttonPanel = createButtonPanelWithEnabling(setMeals, ingredientProvider);
		buttonPanel.disableButtons();

		tableModel = new DataBaseTableModel(mealplanerData.getMeals(), buttonPanel);
		table = new DataBaseTableFactory().createTable(tableModel);
		table.addMouseListener(constructEditRecipeListener(ingredientProvider));
		JScrollPane tablescroll = new JScrollPane(table);

		dataPanel.add(tablescroll, BorderLayout.CENTER);
		dataPanel.add(buttonPanel.getPanel(), BorderLayout.SOUTH);
	}

	private MouseAdapter constructEditRecipeListener(IngredientProvider ingredientProvider) {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				JTable tableSource = (JTable) event.getSource();
				int row = tableSource.getSelectedRow();
				int column = tableSource.getSelectedColumn();
				if (column == 7) {
					Optional<Recipe> recipe = tableModel.returnContent().get(row).getRecipe();
					Optional<Recipe> editedRecipe = new RecipeInput(dataFrame,
							BUNDLES.message("recipeInputDialogTitle"))
									.showDialog(recipe, ingredientProvider);
					if (editedRecipe != null) {
						tableModel.addRecipe(editedRecipe, row);
					}
				}
			}
		};
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
									.forEachRemaining(number -> tableModel.removeRow(number));
						})
				.addSaveButton(action -> {
					// Override database. Not efficient but presently enough.
					setData.accept(tableModel.returnContent());
					tableModel.returnContent().stream()
							.forEach(meal -> System.out.println(meal.getRecipe()));
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
		TablePrinter.printTable(table, dataFrame);
	}

	public void insertItem(Optional<Meal> optionalMeal) {
		optionalMeal.ifPresent(meal -> tableModel.addRow(meal));
	}

	public void updateTable() {
		tableModel.update(mealplanerData.getMeals());
	}

	@Override
	public void updateData(DataStoreEventType event) {
		if (event == DataStoreEventType.DATABASE_EDITED) {
			updateTable();
		}
	}
}