package mealplaner.gui.databaseedit;

import java.awt.BorderLayout;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import mealplaner.DataStore;
import mealplaner.DataStoreEventType;
import mealplaner.DataStoreListener;
import mealplaner.errorhandling.ErrorKeys;
import mealplaner.gui.ButtonPanelEnabling;
import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.gui.dialogs.mealinput.SingleMealInput;
import mealplaner.gui.dialogs.proposaloutput.TablePrinter;
import mealplaner.model.Meal;

// TODO: When entering meals but having entered unsaved meals, maybe we want to just add new (saved) meals and not delete the rest?
public class DatabaseEdit implements DataStoreListener, ErrorKeys {
	private JFrame dataFrame;
	private JPanel dataPanel;
	private JTable table;
	private ResourceBundle messages;

	private DataBaseTableModel tableModel;
	private ButtonPanelEnabling buttonPanel;

	private DataStore mealplanerData;

	public DatabaseEdit(DataStore mealPlan, JFrame parentFrame, JPanel parentPanel,
			ResourceBundle parentMessages) {
		this.mealplanerData = mealPlan;
		mealplanerData.register(this);

		dataFrame = parentFrame;
		dataPanel = parentPanel;
		dataPanel.setLayout(new BorderLayout());
		messages = parentMessages;
	}

	public void setupPane(Consumer<List<Meal>> setMeals) {
		buttonPanel = createButtonPanelWithEnabling(setMeals);
		buttonPanel.disableButtons();

		tableModel = new DataBaseTableModel(mealplanerData.getMeals(), buttonPanel,
				messages);
		table = new DataBaseTableFactory(messages).createTable(tableModel);
		JScrollPane tablescroll = new JScrollPane(table);

		dataPanel.add(tablescroll, BorderLayout.CENTER);
		dataPanel.add(buttonPanel.getPanel(), BorderLayout.SOUTH);
	}

	private ButtonPanelEnabling createButtonPanelWithEnabling(Consumer<List<Meal>> setData) {
		return new ButtonPanelBuilder(messages)
				.addButton("addButton", "addButtonMnemonic",
						action -> {
							Meal newMeal = new SingleMealInput(dataFrame, messages).showDialog();
							insertItem(Optional.of(newMeal));
						})
				.addButton("removeSelectedButton", "removeSelectedButtonMnemonic",
						action -> {
							Arrays.stream(table.getSelectedRows())
									.collect(ArrayDeque<Integer>::new, ArrayDeque<Integer>::add,
											ArrayDeque<Integer>::addAll)
									.descendingIterator()
									.forEachRemaining(number -> tableModel.removeRow(number));
						})
				.addButton("saveButton", "saveButtonMnemonic",
						action -> {
							// Override database. Not efficient but presently
							// enough.
							setData.accept(tableModel.returnContent());
							buttonPanel.disableButtons();
						})
				.makeLastButtonEnabling()
				.addButton("cancelButton", "cancelButton",
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