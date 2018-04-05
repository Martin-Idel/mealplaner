package mealplaner.gui.dialogs.proposaloutput;

import static java.time.format.DateTimeFormatter.ofLocalizedDate;
import static java.time.format.FormatStyle.SHORT;
import static java.time.format.TextStyle.FULL;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.SwingUtilityMethods.autoCompleteCellEditor;
import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.model.Meal.EMPTY_MEAL;
import static mealplaner.model.Proposal.from;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import mealplaner.DataStore;
import mealplaner.commons.gui.tables.Table;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;
import mealplaner.model.ProposedMenu;

public final class ProposalTable {
  private final List<ProposedMenu> proposalMeals = new ArrayList<>();
  private final List<Meal> meals;
  private LocalDate newDate;
  private Proposal lastProposal;
  private Table proposalTable;

  private ProposalTable(List<Meal> meals) {
    this.meals = meals;
  }

  public static ProposalTable proposalOutput(List<Meal> meals) {
    return new ProposalTable(meals);
  }

  public void setupProposalTable(DataStore store, Proposal lastProposal) {
    this.lastProposal = lastProposal;

    proposalMeals.addAll(lastProposal.getProposalList());
    newDate = lastProposal.getDateOfFirstProposedItem();
    proposalTable = createNewTable()
        .withRowCount(proposalMeals::size)
        .addColumn(withContent(String.class)
            .withColumnName(BUNDLES.message("date"))
            .getRowValueFromUnderlyingModel(
                row -> newDate.plusDays(row)
                    .format(ofLocalizedDate(SHORT).withLocale(BUNDLES.locale())))
            .build())
        .addColumn(withContent(String.class)
            .withColumnName(BUNDLES.message("weekday"))
            .getRowValueFromUnderlyingModel(
                row -> newDate.plusDays(row)
                    .getDayOfWeek()
                    .getDisplayName(FULL, BUNDLES.locale()))
            .build())
        .addColumn(withContent(String.class)
            .withColumnName(BUNDLES.message("menu"))
            .getValueFromOrderedList(proposalMeals,
                meal -> meal.main.equals(Meal.EMPTY_MEAL.getId()) ? ""
                    : store.getMeal(meal.main).get().getName())
            .setRowValueToUnderlyingModel(
                (name, row) -> proposalMeals.set(row,
                    changeMain(proposalMeals.get(row), findMeal(name))))
            .isEditable()
            .overwriteTableCellEditor(autoCompleteCellEditor(meals, Meal::getName))
            .build())
        .buildTable();
  }

  public Table getTable() {
    return proposalTable;
  }

  public Proposal getProposal() {
    return from(lastProposal.isToday(), proposalMeals);
  }

  private ProposedMenu changeMain(ProposedMenu oldMenu, Meal newMain) {
    return ProposedMenu.proposed(oldMenu.entry, newMain.getId(), oldMenu.desert,
        oldMenu.numberOfPeople);
  }

  private Meal findMeal(String name) {
    return meals.stream()
        .filter(meal -> meal.getName().equals(name))
        .findFirst()
        .orElse(EMPTY_MEAL);
  }
}
