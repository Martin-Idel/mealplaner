package mealplaner.gui.databaseedit;
/**
 * Martin Idel,
 * DatabaseEdit: display table of meals + GUI for changing the data base of Gerichteplaner.java.
 * This class provides the GUI for one of the two main tabs of MainGUI and allows the user to make any changes to the database
 **/

import java.awt.BorderLayout;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

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

		buttonPanel = createButtonPanelWithEnabling();
		buttonPanel.disableButtons();

		tableModel = new DataBaseTableModel(mealplanerData.getMealListData(), buttonPanel, messages);
		table = new DataBaseTableFactory(messages).createTable(tableModel);
		JScrollPane tablescroll = new JScrollPane(table);

		dataPanel.add(tablescroll, BorderLayout.CENTER);
		dataPanel.add(buttonPanel.getPanel(), BorderLayout.SOUTH);
	}

	private ButtonPanelEnabling createButtonPanelWithEnabling() {
		return new ButtonPanelBuilder(messages)
				.addButton("databankButtonAdd", "databankButtonAddMnemonic",
						action -> {
							Meal newMeal = new SingleMealInput(dataFrame, messages).showDialog();
							insertItem(Optional.of(newMeal));
						})
				.addButton("databankButtonRem", "databankButtonRemMnemonic",
						action -> {
							Arrays.stream(table.getSelectedRows())
									.collect(ArrayDeque<Integer>::new, ArrayDeque<Integer>::add,
											ArrayDeque<Integer>::addAll)
									.descendingIterator()
									.forEachRemaining(number -> tableModel.removeRow(number));
						})
				.addButton("databankButtonSav", "databankButtonSavMnemonic",
						action -> {
							// Override database. Not efficient but presently
							// enough.
							mealplanerData.setMealListData(tableModel.returnContent());
							buttonPanel.disableButtons();
						})
				.makeLastButtonEnabling()
				.addButton("databankButtonEsc", "databankButtonEscMnemonic", action -> updateTable())
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
		tableModel.update(mealplanerData.getMealListData());
	}

	@Override
	public void updateData(DataStoreEventType event) {
		if (event == DataStoreEventType.DATABASE_EDITED) {
			updateTable();
		}
	}
}