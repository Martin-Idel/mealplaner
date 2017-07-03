package mealplaner.gui.dialogs.pastupdate;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JComboBox;
import javax.swing.JTable;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;

import mealplaner.model.EmptyMeal;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;

public class UpdateTable {
	private ResourceBundle messages;
	private Locale currentLocale;
	private UpdateTableModel tableModel;

	public UpdateTable(ResourceBundle messages, Locale parentLocale) {
		this.messages = messages;
		this.currentLocale = parentLocale;
	}

	public JTable createTable(Date date, Proposal lastProposal, Meal[] mealList, int daySince) {
		Meal[] tableList = setupMealList(lastProposal, daySince);
		Date proposalStartDate = addDayOnToday(date, lastProposal.isToday());
		tableModel = new UpdateTableModel(proposalStartDate, tableList, messages, currentLocale);
		JTable table = new JTable(tableModel);

		addAutocompletionToNameColumn(mealList, table);
		return table;
	}

	public List<Meal> returnContent() {
		return Arrays.asList(tableModel.returnContent());
	}

	private Date addDayOnToday(Date date, boolean isToday) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (isToday) {
			cal.add(Calendar.DATE, 1);
		}
		return cal.getTime();
	}

	private Meal[] setupMealList(Proposal proposal, int daySinceLastProposal) {
		List<Meal> proposalList = proposal.getProposalList();

		Meal[] updateMeals = proposal.isToday() ? new Meal[daySinceLastProposal + 1] : new Meal[daySinceLastProposal];
		for (int i = 0; i < updateMeals.length; i++) {
			updateMeals[i] = (i < proposalList.size()) ? proposalList.get(i) : new EmptyMeal();
		}
		return updateMeals;
	}

	private void addAutocompletionToNameColumn(Meal[] meal, JTable table) {
		Meal[] mealAndEmptyMeal = new Meal[meal.length + 1];
		for (int i = 0; i < meal.length; i++) {
			mealAndEmptyMeal[i] = meal[i];
		}
		mealAndEmptyMeal[mealAndEmptyMeal.length - 1] = new EmptyMeal();
		JComboBox<Meal> autoCompleteBox = new JComboBox<Meal>(mealAndEmptyMeal);
		AutoCompleteDecorator.decorate(autoCompleteBox);
		table.getColumnModel().getColumn(2).setCellEditor(new ComboBoxCellEditor(autoCompleteBox));
	}
}
