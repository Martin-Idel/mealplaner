// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.pastupdate;

import static java.time.format.DateTimeFormatter.ofLocalizedDate;
import static java.time.format.FormatStyle.SHORT;
import static java.time.format.TextStyle.FULL;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.commons.gui.tables.cells.AutoCompleteEditors.autoCompleteCellEditor;
import static mealplaner.model.proposal.ProposedMenu.mainOnly;
import static mealplaner.model.proposal.ProposedMenu.proposed;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import mealplaner.commons.gui.tables.Table;
import mealplaner.commons.gui.tables.cells.FlexibleClassRenderer;
import mealplaner.model.DataStore;
import mealplaner.model.meal.Meal;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.proposal.ProposedMenu;

class UpdateTable {
  private List<ProposedMenu> meals;
  private Table actualTable;

  public void createTable(Proposal lastProposal, DataStore store, int daySince) {
    List<Meal> mealList = store.getMeals();
    setupMeals(lastProposal, daySince);
    LocalDate date = lastProposal.getDateOfFirstProposedItem();
    actualTable = createNewTable()
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
                row -> date.plusDays(row)
                    .getDayOfWeek()
                    .getDisplayName(FULL, BUNDLES.locale()))
            .build())
        .addColumn(withContent(Meal.class)
            .withColumnName(BUNDLES.message("menu"))
            .getValueFromOrderedList(meals,
                meal -> store.getMeal(meal.main).orElse(Meal.EMPTY_MEAL))
            .setValueToOrderedImmutableList(meals,
                (menuToBeReplace, meal) -> replaceMainMenu(meal, menuToBeReplace))
            .isEditable()
            .overwriteTableCellEditor(autoCompleteCellEditor(
                mealList, Meal.EMPTY_MEAL, getMealStringRepresentation()))
            .overwriteTableCellRenderer(
                new FlexibleClassRenderer(getMealStringRepresentation(), Meal.EMPTY_MEAL
            ))
            .build())
        .buildTable();
  }

  private Function<Object, String> getMealStringRepresentation() {
    return object -> object instanceof Meal ? ((Meal) object).getName() : "";
  }

  private ProposedMenu replaceMainMenu(Meal newMeal, ProposedMenu oldMenu) {
    return proposed(oldMenu.entry, newMeal.getId(), oldMenu.desert, oldMenu.numberOfPeople);
  }

  public Table getTable() {
    return actualTable;
  }

  public List<ProposedMenu> returnContent() {
    return meals;
  }

  private void setupMeals(Proposal lastProposal, int daySince) {
    meals = new ArrayList<>();
    meals.addAll(lastProposal.getProposalList());
    if (meals.size() > daySince) {
      meals.removeAll(meals.subList(daySince + 1, meals.size()));
    } else if (meals.size() < daySince) {
      for (int i = meals.size(); i <= daySince; i++) {
        meals.add(mainOnly(Meal.EMPTY_MEAL.getId(), nonNegative(1)));
      }
    }
  }
}
