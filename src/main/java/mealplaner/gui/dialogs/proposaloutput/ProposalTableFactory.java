package mealplaner.gui.dialogs.proposaloutput;

import static java.time.format.DateTimeFormatter.ofLocalizedDate;
import static java.time.format.FormatStyle.SHORT;
import static mealplaner.gui.model.StringArrayCollection.getProposalOutputColumnNames;
import static mealplaner.gui.model.StringArrayCollection.getWeekDays;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JTable;

import mealplaner.BundleStore;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;

public class ProposalTableFactory {
	private BundleStore bundles;
	private JTable table;

	public ProposalTableFactory(BundleStore bundles) {
		this.bundles = bundles;
	}

	public JTable createTable(Proposal lastProposal) {
		String[] columnNames = getProposalOutputColumnNames(bundles);
		table = new JTable(
				new ProposalTableModel(prepareProposalForTable(lastProposal), columnNames));
		return table;
	}

	private String[][] prepareProposalForTable(Proposal proposal) {
		String[] weekDays = getWeekDays(bundles);

		List<Meal> mealList = proposal.getProposalList();
		DateTimeFormatter formatter = ofLocalizedDate(SHORT).withLocale(bundles.locale());
		String[][] data = new String[mealList.size()][3];
		LocalDate date = proposal.getTime();

		if (!proposal.isToday()) {
			date = date.plusDays(1);
		}
		for (int i = 0; i < data.length; i++) {
			data[i][0] = date.format(formatter);
			data[i][1] = weekDays[date.getDayOfWeek().getValue() % 7];
			data[i][2] = mealList.get(i).getName();
			date = date.plusDays(1);
		}
		return data;
	}
}
