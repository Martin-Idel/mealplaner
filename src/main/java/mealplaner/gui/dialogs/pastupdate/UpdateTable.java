package mealplaner.gui.dialogs.pastupdate;

import static java.time.format.DateTimeFormatter.ofLocalizedDate;
import static java.time.format.FormatStyle.SHORT;
import static java.time.format.TextStyle.FULL;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.SwingUtilityMethods.autoCompleteCellEditor;
import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import mealplaner.commons.gui.tables.Table;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;

public class UpdateTable {
  private List<Meal> meals;
  private Table updateTable;

  public void createTable(Proposal lastProposal, List<Meal> mealList, int daySince) {
    setupMeals(lastProposal, daySince);
    LocalDate date = lastProposal.getDateOfFirstProposedItem();
    updateTable = createNewTable()
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
            .overwriteTableCellEditor(autoCompleteCellEditor(mealList, Meal::getName))
            .build())
        .buildTable();
  }

  public Table getTable() {
    return updateTable;
  }

  public List<Meal> returnContent() {
    return meals;
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
}
