package mealplaner.gui.dialogs.proposaloutput;

import static java.time.format.DateTimeFormatter.ofLocalizedDate;
import static java.time.format.FormatStyle.SHORT;
import static mealplaner.BundleStore.BUNDLES;
import static mealplaner.gui.model.StringArrayCollection.getWeekDays;
import static mealplaner.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.gui.tables.TableColumnBuilder.withContent;

import java.time.LocalDate;
import java.util.List;

import mealplaner.gui.tables.Table;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;

public class ProposalTableFactory {
	private List<Meal> meals;
	private String[] days;
	private LocalDate newDate;

	private ProposalTableFactory() {
		days = getWeekDays();
	}

	public static ProposalTableFactory proposalOutput() {
		return new ProposalTableFactory();
	}

	public Table createProposalTable(Proposal lastProposal) {
		meals = lastProposal.getProposalList();
		newDate = lastProposal.getDateOfFirstProposedItem();
		return createNewTable()
				.withRowCount(meals::size)
				.addColumn(withContent(String.class)
						.withColumnName(BUNDLES.message("date"))
						.getRowValueFromUnderlyingModel(
								row -> newDate.plusDays(row).format(ofLocalizedDate(SHORT)
										.withLocale(BUNDLES.locale())))
						.build())
				.addColumn(withContent(String.class)
						.withColumnName(BUNDLES.message("weekday"))
						.getRowValueFromUnderlyingModel(
								row -> days[newDate.plusDays(row).getDayOfWeek().getValue() % 7])
						.build())
				.addColumn(withContent(String.class)
						.withColumnName(BUNDLES.message("menu"))
						.getRowValueFromUnderlyingModel(row -> meals.get(row).getName())
						.build())
				.buildTable();
	}
}
