package mealplaner.gui.dialogs.pastupdate;

import static java.time.format.DateTimeFormatter.ofLocalizedDate;
import static java.time.format.FormatStyle.SHORT;
import static mealplaner.BundleStore.BUNDLES;
import static mealplaner.gui.model.StringArrayCollection.getWeekDays;
import static mealplaner.gui.tables.TableColumnBuilder.withContent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;

import mealplaner.gui.tables.FlexibleTableBuilder;
import mealplaner.gui.tables.Table;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;

public class UpdateTable {
	private String[] days;
	private List<Meal> meals;

	public Table createTable(Proposal lastProposal, List<Meal> mealList, int daySince) {
		days = getWeekDays();
		setupMeals(lastProposal, daySince);
		LocalDate date = lastProposal.getDateOfFirstProposedItem();
		return FlexibleTableBuilder.createNewTable()
				.withRowCount(() -> lastProposal.isToday() ? daySince + 1 : daySince)
				.addColumn(withContent(String.class)
						.withColumnName(BUNDLES.message("date"))
						.getRowValueFromUnderlyingModel(
								row -> date.plusDays(row).format(ofLocalizedDate(SHORT)
										.withLocale(BUNDLES.locale())))
						.build())
				.addColumn(withContent(String.class)
						.withColumnName(BUNDLES.message("weekday"))
						.getRowValueFromUnderlyingModel(
								row -> days[date.plusDays(row).getDayOfWeek().getValue() % 7])
						.build())
				.addColumn(withContent(String.class)
						.withColumnName(BUNDLES.message("menu"))
						.getValueFromOrderedList(meals,
								meal -> meal.equals(Meal.EMPTY_MEAL)
										? ""
										: meal.getName())
						.setValueToOrderedImmutableList(meals,
								(mealToBeReplaced, name) -> mealList.stream()
										.filter(meal -> meal.getName().equals(name))
										.findAny()
										.orElse(Meal.EMPTY_MEAL))
						.isEditable()
						.overwriteTableCellEditor(
								new ComboBoxCellEditor(createAutoCompletion(mealList)))
						.build())
				.buildTable();
	}

	private void setupMeals(Proposal lastProposal, int daySince) {
		meals = new ArrayList<Meal>();
		meals.addAll(lastProposal.getProposalList());
		if (meals.size() > daySince) {
			meals.removeAll(meals.subList(daySince + 1, meals.size()));
		} else if (meals.size() < daySince) {
			for (int i = meals.size(); i <= daySince; i++) {
				meals.add(Meal.EMPTY_MEAL);
			}
		}
	}

	public List<Meal> returnContent() {
		return meals;
	}

	private JComboBox<String> createAutoCompletion(List<Meal> meals) {
		String[] mealAndEmptyMeal = new String[meals.size() + 1];
		for (int i = 0; i < meals.size(); i++) {
			mealAndEmptyMeal[i] = meals.get(i).getName();
		}
		mealAndEmptyMeal[mealAndEmptyMeal.length - 1] = "";
		JComboBox<String> autoCompleteBox = new JComboBox<String>(mealAndEmptyMeal);
		AutoCompleteDecorator.decorate(autoCompleteBox);
		return autoCompleteBox;
	}
}
