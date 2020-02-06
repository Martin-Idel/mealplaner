// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.proposaloutput;

import static java.time.format.DateTimeFormatter.ofLocalizedDate;
import static java.time.format.FormatStyle.SHORT;
import static java.time.format.TextStyle.FULL;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.commons.gui.tables.cells.AutoCompleteEditors.autoCompleteCellEditor;
import static mealplaner.model.meal.Meal.EMPTY_MEAL;
import static mealplaner.model.proposal.Proposal.from;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.commons.gui.tables.Table;
import mealplaner.commons.gui.tables.cells.FlexibleClassRenderer;
import mealplaner.model.DataStore;
import mealplaner.model.meal.Meal;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.proposal.ProposedMenu;

public final class ProposalTable {
  private final List<ProposedMenu> proposalMenues = new ArrayList<>();
  private final List<Meal> meals = new ArrayList<>();
  private Proposal lastProposal;
  private Table actualTable;

  private ProposalTable() {
  }

  public static ProposalTable proposalOutput() {
    return new ProposalTable();
  }

  public void setupProposalTable(DataStore store, Proposal lastProposal) {
    this.lastProposal = lastProposal;
    this.meals.addAll(store.getMeals());

    proposalMenues.addAll(lastProposal.getProposalList());

    var newDate = lastProposal.getDateOfFirstProposedItem();
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
    if (needsEntryColumn()) {
      builder.addColumn(withContent(Meal.class)
          .withColumnName(BUNDLES.message("entry"))
          .getValueFromOrderedList(proposalMenues,
              meal -> store
                  .getMeal(meal.entry.orElse(
                      UUID.nameUUIDFromBytes("No_ENTRY".getBytes(StandardCharsets.UTF_8))))
                  .orElse(EMPTY_MEAL))
          .setRowValueToUnderlyingModel(
              (meal, row) -> proposalMenues.set(row,
                  changeEntry(proposalMenues.get(row), meal)))
          .isEditable()
          .overwriteTableCellEditor(autoCompleteCellEditor(meals, EMPTY_MEAL, getMealStringRepresentation()))
          .overwriteTableCellRenderer(new FlexibleClassRenderer(getMealStringRepresentation(), EMPTY_MEAL))
          .build());
    }
    builder.addColumn(withContent(Meal.class)
        .withColumnName(BUNDLES.message("main"))
        .getValueFromOrderedList(proposalMenues,
            meal -> store.getMeal(meal.main).orElse(EMPTY_MEAL))
        .setRowValueToUnderlyingModel(
            (meal, row) -> proposalMenues.set(row,
                changeMain(proposalMenues.get(row), meal)))
        .isEditable()
        .overwriteTableCellEditor(autoCompleteCellEditor(meals, EMPTY_MEAL, getMealStringRepresentation()))
        .overwriteTableCellRenderer(new FlexibleClassRenderer(getMealStringRepresentation(), EMPTY_MEAL))
        .build());
    if (needsDesertColumn()) {
      builder.addColumn(withContent(Meal.class)
          .withColumnName(BUNDLES.message("desert"))
          .getValueFromOrderedList(proposalMenues,
              meal -> store
                  .getMeal(meal.desert.orElse(
                      UUID.nameUUIDFromBytes("No_ENTRY".getBytes(StandardCharsets.UTF_8))))
                  .orElse(EMPTY_MEAL))
          .setRowValueToUnderlyingModel(
              (meal, row) -> proposalMenues.set(row,
                  changeDesert(proposalMenues.get(row), meal)))
          .isEditable()
          .overwriteTableCellEditor(autoCompleteCellEditor(meals, EMPTY_MEAL, getMealStringRepresentation()))
          .overwriteTableCellRenderer(new FlexibleClassRenderer(getMealStringRepresentation(), EMPTY_MEAL))
          .build());
    }
    actualTable = builder.buildTable();
  }

  private Function<Object, String> getMealStringRepresentation() {
    return object -> object instanceof Meal ? ((Meal) object).getName() : "";
  }

  private boolean needsDesertColumn() {
    return proposalMenues.stream().anyMatch(proposedMenu -> proposedMenu.desert.isPresent());
  }

  private boolean needsEntryColumn() {
    return proposalMenues.stream().anyMatch(proposedMenu -> proposedMenu.entry.isPresent());
  }

  public Table getTable() {
    return actualTable;
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
}
