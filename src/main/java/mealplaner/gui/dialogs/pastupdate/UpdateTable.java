package mealplaner.gui.dialogs.pastupdate;

import static java.time.format.DateTimeFormatter.ofLocalizedDate;
import static java.time.format.FormatStyle.SHORT;
import static java.time.format.TextStyle.FULL;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.commons.gui.SwingUtilityMethods.autoCompleteCellEditor;
import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.model.ProposedMenu.mainOnly;
import static mealplaner.model.ProposedMenu.proposed;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import mealplaner.DataStore;
import mealplaner.commons.gui.tables.Table;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;
import mealplaner.model.ProposedMenu;

public class UpdateTable {
  private List<ProposedMenu> meals;
  private Table updateTable;

  public void createTable(Proposal lastProposal, DataStore store, int daySince) {
    List<Meal> mealList = store.getMeals();
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
                meal -> meal.main.equals(Meal.EMPTY_MEAL.getId())
                    ? ""
                    : store.getMeal(meal.main).get().getName())
            .setValueToOrderedImmutableList(meals,
                (menuToBeReplace, name) -> replaceMainMenu(name, mealList, menuToBeReplace))
            .isEditable()
            .overwriteTableCellEditor(autoCompleteCellEditor(mealList, Meal::getName))
            .build())
        .buildTable();
  }

  private ProposedMenu replaceMainMenu(String name, List<Meal> meals, ProposedMenu oldMenu) {
    Meal newMeal = meals.stream()
        .filter(meal -> meal.getName().equals(name))
        .findAny()
        .orElse(Meal.EMPTY_MEAL);
    return proposed(oldMenu.entry, newMeal.getId(), oldMenu.desert, oldMenu.numberOfPeople);
  }

  public Table getTable() {
    return updateTable;
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
