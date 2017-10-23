package mealplaner.gui.dialogs.pastupdate;

import static mealplaner.model.Meal.createEmptyMeal;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JTable;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;

import mealplaner.BundleStore;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;

public class UpdateTable {
	private BundleStore bundles;
	private UpdateTableModel tableModel;

	public UpdateTable(BundleStore bundles) {
		this.bundles = bundles;
	}

	public JTable createTable(LocalDate date, Proposal lastProposal, Meal[] mealList,
			int daySince) {
		Meal[] tableList = setupMealList(lastProposal, daySince);
		LocalDate proposalStartDate = lastProposal.isToday() ? date.plusDays(1) : date;
		tableModel = new UpdateTableModel(proposalStartDate, tableList, bundles);
		JTable table = new JTable(tableModel);

		addAutocompletionToNameColumn(mealList, table);
		return table;
	}

	public List<Meal> returnContent() {
		return Arrays.asList(tableModel.returnContent());
	}

	private Meal[] setupMealList(Proposal proposal, int daySinceLastProposal) {
		List<Meal> proposalList = proposal.getProposalList();

		Meal[] updateMeals = proposal.isToday() ? new Meal[daySinceLastProposal + 1]
				: new Meal[daySinceLastProposal];
		for (int i = 0; i < updateMeals.length; i++) {
			updateMeals[i] = (i < proposalList.size()) ? proposalList.get(i) : createEmptyMeal();
		}
		return updateMeals;
	}

	private void addAutocompletionToNameColumn(Meal[] meal, JTable table) {
		Meal[] mealAndEmptyMeal = new Meal[meal.length + 1];
		for (int i = 0; i < meal.length; i++) {
			mealAndEmptyMeal[i] = meal[i];
		}
		mealAndEmptyMeal[mealAndEmptyMeal.length - 1] = createEmptyMeal();
		JComboBox<Meal> autoCompleteBox = new JComboBox<Meal>(mealAndEmptyMeal);
		AutoCompleteDecorator.decorate(autoCompleteBox);
		table.getColumnModel().getColumn(2).setCellEditor(new ComboBoxCellEditor(autoCompleteBox));
	}
}
