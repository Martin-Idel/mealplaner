package mealplaner.gui.dialogs.pastupdate;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static mealplaner.commons.BundleStore.BUNDLES;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Optional;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mealplaner.DataStore;
import mealplaner.commons.gui.ButtonPanelBuilder;
import mealplaner.commons.gui.tables.Table;
import mealplaner.model.Meal;

public class UpdatePastMeals extends JDialog {
	private static final long serialVersionUID = 1L;
	private JFrame parentFrame;
	private JPanel dataPanel;
	private UpdateTable updateTable;
	private Optional<List<Meal>> changedMeals = empty();

	public UpdatePastMeals(JFrame parentFrame) {
		super(parentFrame, BUNDLES.message("updatePastMealsDialogTitle"), true);
		this.parentFrame = parentFrame;
		updateTable = new UpdateTable();
	}

	public Optional<List<Meal>> showDialog(DataStore mealPlan) {
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
					changedMeals = of(updateTable.returnContent());
					dispose();
				})
				.addCancelDialogButton(this)
				.build();
	}
}