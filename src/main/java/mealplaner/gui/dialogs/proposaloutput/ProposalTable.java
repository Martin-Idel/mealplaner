package mealplaner.gui.dialogs.proposaloutput;

import static java.time.format.DateTimeFormatter.ofLocalizedDate;
import static java.time.format.FormatStyle.SHORT;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.StringArrayCollection.getWeekDays;
import static mealplaner.commons.gui.SwingUtilityMethods.autoCompleteCellEditor;
import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.model.Meal.EMPTY_MEAL;
import static mealplaner.model.Proposal.from;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import mealplaner.commons.gui.tables.Table;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;

public final class ProposalTable {
  private final List<Meal> proposalMeals = new ArrayList<>();
  private final List<Meal> meals;
  private final String[] days;
  private LocalDate newDate;
  private Proposal lastProposal;
  private Table proposalTable;

  private ProposalTable(List<Meal> meals) {
    this.meals = meals;
    days = getWeekDays();
  }

  public static ProposalTable proposalOutput(List<Meal> meals) {
    return new ProposalTable(meals);
  }

  public void setupProposalTable(Proposal lastProposal) {
    this.lastProposal = lastProposal;
    lastProposal.getProposalList().forEach(proposalMeals::add);
    newDate = lastProposal.getDateOfFirstProposedItem();
    proposalTable = createNewTable()
        .withRowCount(proposalMeals::size)
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
            .getValueFromOrderedList(proposalMeals,
                meal -> meal.equals(Meal.EMPTY_MEAL) ? "" : meal.getName())
            .setRowValueToUnderlyingModel((name, row) -> proposalMeals.set(row, findMeal(name)))
            .isEditable()
            .overwriteTableCellEditor(autoCompleteCellEditor(meals, Meal::getName))
            .build())
        .buildTable();
  }

  public Table getTable() {
    return proposalTable;
  }

  public Proposal getProposal() {
    return from(lastProposal.isToday(), proposalMeals, lastProposal.getSettingsList());
  }

  private Meal findMeal(String name) {
    return meals.stream()
        .filter(meal -> meal.getName().equals(name))
        .findFirst()
        .orElse(EMPTY_MEAL);
  }
}
