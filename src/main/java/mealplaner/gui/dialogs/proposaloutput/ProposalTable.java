package mealplaner.gui.dialogs.proposaloutput;

import static java.time.format.DateTimeFormatter.ofLocalizedDate;
import static java.time.format.FormatStyle.SHORT;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.StringArrayCollection.getWeekDays;
import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.model.Meal.EMPTY_MEAL;
import static mealplaner.model.Proposal.from;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;

import mealplaner.commons.gui.tables.Table;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;

// TODO: Clean up, this is a bit messy
// TODO: Make row non-editable if this is called from JMenu or else persist state.
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

  public void createProposalTable(Proposal lastProposal) {
    this.lastProposal = lastProposal;
    lastProposal.getProposalList().forEach(proposalMeals::add);
    newDate = lastProposal.getDateOfFirstProposedItem();
    JComboBox<String> autoCompleteComboBox = createAutoCompletion(meals);
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
            .overwriteTableCellEditor(new ComboBoxCellEditor(autoCompleteComboBox))
            .build())
        .buildTable();
  }

  public void addToScrollingPane(JPanel panel) {
    proposalTable.addScrollingTableToPane(panel);
  }

  public void printTable(JFrame frame) {
    proposalTable.printTable(frame);
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
