package mealplaner.gui.dialogs.pastupdate;

import static mealplaner.BundleStore.BUNDLES;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mealplaner.DataStore;
import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.gui.tables.Table;
import mealplaner.model.Meal;

public class UpdatePastMeals extends JDialog {
	private static final long serialVersionUID = 1L;
	private JFrame parentFrame;
	private JPanel dataPanel;
	private UpdateTable updateTable;
	private List<Meal> changedMeals = new ArrayList<>();

	public UpdatePastMeals(JFrame parentFrame) {
		super(parentFrame, BUNDLES.message("updatePastMealsDialogTitle"), true);
		this.parentFrame = parentFrame;
		updateTable = new UpdateTable();
	}

	public List<Meal> showDialog(DataStore mealPlan) {
		display(mealPlan);
		return changedMeals;
	}

	private void display(DataStore mealPlan) {
		Table table = updateTable.createTable(mealPlan.getLastProposal(),
				mealPlan.getMeals(), mealPlan.getDaysPassed());
		JPanel buttonPanel = displayButtons();
		dataPanel = new JPanel();
		dataPanel.setLayout(new BorderLayout());
		table.addScrollingTableToPane(dataPanel);
		dataPanel.add(buttonPanel, BorderLayout.SOUTH);
		getContentPane().add(dataPanel);

		if (mealPlan.getDaysPassed() != 0) {
			setSize(300, 300);
			setLocationRelativeTo(parentFrame);
			setVisible(true);
		}
	}

	private JPanel displayButtons() {
		return new ButtonPanelBuilder()
				.addSaveButton(action -> {
					changedMeals = updateTable.returnContent();
					dispose();
				})
				.addCancelDialogButton(this)
				.build();
	}
}