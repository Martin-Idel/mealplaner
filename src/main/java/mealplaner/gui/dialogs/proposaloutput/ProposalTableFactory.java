package mealplaner.gui.dialogs.proposaloutput;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JTable;

import mealplaner.gui.model.StringArrayCollection;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;

public class ProposalTableFactory {
	private ResourceBundle messages;
	private Locale currentLocale;
	private JTable table;

	public ProposalTableFactory(ResourceBundle messages, Locale currentLocale) {
		this.messages = messages;
		this.currentLocale = currentLocale;
	}

	public JTable createTable(Proposal lastProposal) {
		String[] columnNames = StringArrayCollection.getProposalOutputColumnNames(messages);
		table = new JTable(new ProposalTableModel(prepareProposalForTable(lastProposal), columnNames));
		return table;
	}

	private String[][] prepareProposalForTable(Proposal proposal) {
		String[] weekDays = StringArrayCollection.getWeekDays(messages);

		List<Meal> mealList = proposal.getProposalList();
		Calendar cal = Calendar.getInstance();
		cal.setTime(proposal.getCalendar().getTime());
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, currentLocale);
		String[][] data = new String[mealList.size()][3];

		if (!proposal.isToday()) {
			cal.add(Calendar.DATE, 1);
		}
		for (int i = 0; i < data.length; i++) {
			data[i][0] = dateFormat.format(cal.getTime());
			data[i][1] = weekDays[cal.get(Calendar.DAY_OF_WEEK) - 1];
			data[i][2] = mealList.get(i).getName();
			cal.add(Calendar.DATE, 1);
		}
		return data;
	}

}
