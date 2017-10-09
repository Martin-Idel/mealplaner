package mealplaner.gui.databaseedit;

import java.awt.BorderLayout;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import mealplaner.BundleStore;
import mealplaner.DataStore;
import mealplaner.DataStoreEventType;
import mealplaner.DataStoreListener;
import mealplaner.gui.ButtonPanelEnabling;
import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.gui.dialogs.mealinput.SingleMealInput;
import mealplaner.gui.dialogs.proposaloutput.TablePrinter;
import mealplaner.model.Meal;
import mealplaner.recepies.provider.IngredientProvider;

// TODO: When entering meals but having entered unsaved meals, maybe we want to just add new (saved) meals and not delete the rest?
public class DatabaseEdit implements DataStoreListener {
	private JFrame dataFrame;
	private JPanel dataPanel;
	private JTable table;
	private BundleStore bundles;

	private DataBaseTableModel tableModel;
	private ButtonPanelEnabling buttonPanel;

	private DataStore mealplanerData;

	public DatabaseEdit(DataStore mealPlan, JFrame parentFrame, JPanel parentPanel,
			BundleStore bundleStore) {
		this.mealplanerData = mealPlan;
		mealplanerData.register(this);

		dataFrame = parentFrame;
		dataPanel = parentPanel;
		dataPanel.setLayout(new BorderLayout());
		bundles = bundleStore;
	}

	public void setupPane(Consumer<List<Meal>> setMeals, IngredientProvider ingredientProvider) {
		buttonPanel = createButtonPanelWithEnabling(setMeals, ingredientProvider);
		buttonPanel.disableButtons();

		tableModel = new DataBaseTableModel(mealplanerData.getMeals(), buttonPanel, bundles);
		table = new DataBaseTableFactory(bundles).createTable(tableModel);
		JScrollPane tablescroll = new JScrollPane(table);

		dataPanel.add(tablescroll, BorderLayout.CENTER);
		dataPanel.add(buttonPanel.getPanel(), BorderLayout.SOUTH);
	}

	private ButtonPanelEnabling createButtonPanelWithEnabling(Consumer<List<Meal>> setData,
			IngredientProvider ingredientProvider) {
		return new ButtonPanelBuilder(bundles)
				.addButton(bundles.message("addButton"),
						bundles.message("addButtonMnemonic"),
						action -> {
							Meal newMeal = new SingleMealInput(dataFrame, bundles,
									ingredientProvider).showDialog();
							insertItem(Optional.of(newMeal));
						})
				.addButton(bundles.message("removeSelectedButton"),
						bundles.message("removeSelectedButtonMnemonic"),
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
					buttonPanel.disableButtons();
				})
				.makeLastButtonEnabling()
				.addButton(bundles.message("cancelButton"),
						bundles.message("cancelButtonMnemonic"),
						action -> updateTable())
				.makeLastButtonEnabling()
				.buildEnablingPanel();
	}

	public void printTable() {
		TablePrinter.printTable(table, dataFrame, bundles);
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