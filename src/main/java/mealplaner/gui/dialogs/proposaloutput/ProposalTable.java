package mealplaner.gui.dialogs.proposaloutput;

import static java.time.format.DateTimeFormatter.ofLocalizedDate;
import static java.time.format.FormatStyle.SHORT;
import static java.time.format.TextStyle.FULL;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.SwingUtilityMethods.autoCompleteCellEditor;
import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.model.meal.Meal.EMPTY_MEAL;
import static mealplaner.model.proposal.Proposal.from;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.commons.gui.tables.Table;
import mealplaner.model.DataStore;
import mealplaner.model.meal.Meal;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.proposal.ProposedMenu;

public final class ProposalTable {
  private final List<ProposedMenu> proposalMenues = new ArrayList<>();
  private final List<Meal> meals = new ArrayList<>();
  private LocalDate newDate;
  private Proposal lastProposal;
  private Table proposalTable;

  private ProposalTable() {
  }

  public static ProposalTable proposalOutput() {
    return new ProposalTable();
  }

  public void setupProposalTable(DataStore store, Proposal lastProposal) {
    this.lastProposal = lastProposal;
    this.meals.addAll(store.getMeals());

    proposalMenues.addAll(lastProposal.getProposalList());

    boolean needsEntryColumn = proposalMenues.stream()
        .anyMatch(proposedMenu -> proposedMenu.entry.isPresent());
    boolean needsDesertColumn = proposalMenues.stream()
        .anyMatch(proposedMenu -> proposedMenu.desert.isPresent());

    newDate = lastProposal.getDateOfFirstProposedItem();
    FlexibleTableBuilder builder = createNewTable()
        .withRowCount(proposalMenues::size)
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
            .build());
    if (needsEntryColumn) {
      builder.addColumn(withContent(String.class)
          .withColumnName(BUNDLES.message("entry"))
          .getValueFromOrderedList(proposalMenues,
              meal -> meal.entry.isPresent()
                  ? store.getMeal(meal.entry.get()).get().getName()
                  : "")
          .setRowValueToUnderlyingModel(
              (name, row) -> proposalMenues.set(row,
                  changeEntry(proposalMenues.get(row), findMeal(name))))
          .isEditable()
          .overwriteTableCellEditor(autoCompleteCellEditor(meals, Meal::getName))
          .build());
    }
    builder.addColumn(withContent(String.class)
        .withColumnName(BUNDLES.message("main"))
        .getValueFromOrderedList(proposalMenues,
            meal -> meal.main.equals(Meal.EMPTY_MEAL.getId())
                ? ""
                : store.getMeal(meal.main).get().getName())
        .setRowValueToUnderlyingModel(
            (name, row) -> proposalMenues.set(row,
                changeMain(proposalMenues.get(row), findMeal(name))))
        .isEditable()
        .overwriteTableCellEditor(autoCompleteCellEditor(meals, Meal::getName))
        .build());
    if (needsDesertColumn) {
      builder.addColumn(withContent(String.class)
          .withColumnName(BUNDLES.message("desert"))
          .getValueFromOrderedList(proposalMenues,
              meal -> meal.desert.isPresent()
                  ? store.getMeal(meal.desert.get()).get().getName()
                  : "")
          .setRowValueToUnderlyingModel(
              (name, row) -> proposalMenues.set(row,
                  changeDesert(proposalMenues.get(row), findMeal(name))))
          .isEditable()
          .overwriteTableCellEditor(autoCompleteCellEditor(meals, Meal::getName))
          .build());
    }
    proposalTable = builder.buildTable();
  }

  public Table getTable() {
    return proposalTable;
  }

  public Proposal getProposal() {
    return from(lastProposal.isToday(), proposalMenues);
  }

  private ProposedMenu changeEntry(ProposedMenu oldMenu, Meal newEntry) {
    Optional<UUID> newEntryId = newEntry.equals(Meal.EMPTY_MEAL)
        ? empty()
        : of(newEntry.getId());
    return ProposedMenu.proposed(newEntryId, oldMenu.main, oldMenu.desert, oldMenu.numberOfPeople);
  }

  private ProposedMenu changeDesert(ProposedMenu oldMenu, Meal newDesert) {
    Optional<UUID> newDesertId = newDesert.equals(Meal.EMPTY_MEAL)
        ? empty()
        : of(newDesert.getId());
    return ProposedMenu.proposed(oldMenu.entry, oldMenu.main, newDesertId, oldMenu.numberOfPeople);
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
