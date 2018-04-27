// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.proposaloutput;

import static mealplaner.commons.NonnegativeInteger.ONE;
import static mealplaner.model.proposal.ProposedMenu.mainAndDesert;
import static mealplaner.model.proposal.ProposedMenu.mainOnly;
import static mealplaner.model.proposal.ProposedMenu.threeCourseMeal;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;
import static testcommons.CommonFunctions.getMeal3;
import static testcommons.CommonFunctions.getMealDesert;
import static testcommons.CommonFunctions.getMealEntry;
import static testcommons.CommonFunctions.getMealEntry2;
import static testcommons.CommonFunctions.getProposal1;
import static testcommons.CommonFunctions.setupMealplanerDataWithAllMealsAndIngredients;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import org.junit.Test;

import mealplaner.model.MealplanerData;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.proposal.ProposedMenu;

public class ProposalTableTest {

  @Test
  public void tableHasCorrectNumberOfRowsForOnlyMainCourses() {
    MealplanerData data = setupMealplanerDataWithAllMealsAndIngredients();

    JTable proposalSwingTable = createProposalTable(data, getProposal1());

    assertThat(proposalSwingTable.getColumnCount()).isEqualTo(3);
    assertThat(proposalSwingTable.getRowCount()).isEqualTo(2);
    assertThat(proposalSwingTable.getValueAt(0, 2)).isEqualTo(getMeal1().getName());
    assertThat(proposalSwingTable.getValueAt(1, 2)).isEqualTo(getMeal2().getName());
  }

  @Test
  public void tableHasCorrectNumberOfRowsIncludingADesert() {

    JTable proposalSwingTable = setupProposalTableWithDesert();

    assertThat(proposalSwingTable.getColumnCount()).isEqualTo(4);
    assertThat(proposalSwingTable.getRowCount()).isEqualTo(2);
    assertThat(proposalSwingTable.getValueAt(0, 2)).isEqualTo(getMeal1().getName());
    assertThat(proposalSwingTable.getValueAt(0, 3)).isEqualTo(getMealDesert().getName());
    assertThat(proposalSwingTable.getValueAt(1, 2)).isEqualTo(getMeal2().getName());
    assertThat(proposalSwingTable.getValueAt(1, 3)).isEqualTo("");
  }

  @Test
  public void tableHasCorrectNumberOfRowsIncludingThreeCourseMenu() {

    JTable proposalSwingTable = setupProposalTableWithThreeCourseMeal();

    assertThat(proposalSwingTable.getColumnCount()).isEqualTo(5);
    assertThat(proposalSwingTable.getRowCount()).isEqualTo(2);
    assertThat(proposalSwingTable.getValueAt(0, 2)).isEqualTo(getMealEntry().getName());
    assertThat(proposalSwingTable.getValueAt(0, 3)).isEqualTo(getMeal1().getName());
    assertThat(proposalSwingTable.getValueAt(0, 4)).isEqualTo(getMealDesert().getName());
    assertThat(proposalSwingTable.getValueAt(1, 2)).isEqualTo("");
    assertThat(proposalSwingTable.getValueAt(1, 3)).isEqualTo(getMeal2().getName());
    assertThat(proposalSwingTable.getValueAt(1, 4)).isEqualTo("");
  }

  @Test
  public void changingMainCorrectlyChangesMeal() {
    MealplanerData data = setupMealplanerDataWithAllMealsAndIngredients();

    JTable proposalSwingTable = createProposalTable(data, getProposal1());
    proposalSwingTable.setValueAt(getMeal3().getName(), 1, 2);

    assertThat(proposalSwingTable.getColumnCount()).isEqualTo(3);
    assertThat(proposalSwingTable.getRowCount()).isEqualTo(2);
    assertThat(proposalSwingTable.getValueAt(0, 2)).isEqualTo(getMeal1().getName());
    assertThat(proposalSwingTable.getValueAt(1, 2)).isEqualTo(getMeal3().getName());
  }

  @Test
  public void changingEntryCorrectlyChangesEntry() {
    JTable proposalSwingTable = setupProposalTableWithThreeCourseMeal();

    proposalSwingTable.setValueAt(getMealEntry2().getName(), 0, 2);

    assertThat(proposalSwingTable.getColumnCount()).isEqualTo(5);
    assertThat(proposalSwingTable.getRowCount()).isEqualTo(2);
    assertThat(proposalSwingTable.getValueAt(0, 2)).isEqualTo(getMealEntry2().getName());
    assertThat(proposalSwingTable.getValueAt(0, 3)).isEqualTo(getMeal1().getName());
    assertThat(proposalSwingTable.getValueAt(0, 4)).isEqualTo(getMealDesert().getName());
    assertThat(proposalSwingTable.getValueAt(1, 2)).isEqualTo("");
    assertThat(proposalSwingTable.getValueAt(1, 3)).isEqualTo(getMeal2().getName());
    assertThat(proposalSwingTable.getValueAt(1, 4)).isEqualTo("");
  }

  @Test
  public void changingPreviouslyEmptyEntryCorrectlyChangesEntry() {
    JTable proposalSwingTable = setupProposalTableWithThreeCourseMeal();

    proposalSwingTable.setValueAt(getMealEntry2().getName(), 1, 2);

    assertThat(proposalSwingTable.getColumnCount()).isEqualTo(5);
    assertThat(proposalSwingTable.getRowCount()).isEqualTo(2);
    assertThat(proposalSwingTable.getValueAt(0, 2)).isEqualTo(getMealEntry().getName());
    assertThat(proposalSwingTable.getValueAt(0, 3)).isEqualTo(getMeal1().getName());
    assertThat(proposalSwingTable.getValueAt(0, 4)).isEqualTo(getMealDesert().getName());
    assertThat(proposalSwingTable.getValueAt(1, 2)).isEqualTo(getMealEntry2().getName());
    assertThat(proposalSwingTable.getValueAt(1, 3)).isEqualTo(getMeal2().getName());
    assertThat(proposalSwingTable.getValueAt(1, 4)).isEqualTo("");
  }

  @Test
  public void changingEntryToEmptyMealWorksCorrectly() {
    JTable proposalSwingTable = setupProposalTableWithThreeCourseMeal();

    proposalSwingTable.setValueAt("", 0, 2);

    assertThat(proposalSwingTable.getColumnCount()).isEqualTo(5);
    assertThat(proposalSwingTable.getRowCount()).isEqualTo(2);
    assertThat(proposalSwingTable.getValueAt(0, 2)).isEqualTo("");
    assertThat(proposalSwingTable.getValueAt(0, 3)).isEqualTo(getMeal1().getName());
    assertThat(proposalSwingTable.getValueAt(0, 4)).isEqualTo(getMealDesert().getName());
    assertThat(proposalSwingTable.getValueAt(1, 2)).isEqualTo("");
    assertThat(proposalSwingTable.getValueAt(1, 3)).isEqualTo(getMeal2().getName());
    assertThat(proposalSwingTable.getValueAt(1, 4)).isEqualTo("");
  }

  @Test
  public void changingDesertToEmptyMealWorksCorrectly() {
    JTable proposalSwingTable = setupProposalTableWithThreeCourseMeal();

    proposalSwingTable.setValueAt("", 0, 4);

    assertThat(proposalSwingTable.getColumnCount()).isEqualTo(5);
    assertThat(proposalSwingTable.getRowCount()).isEqualTo(2);
    assertThat(proposalSwingTable.getValueAt(0, 2)).isEqualTo(getMealEntry().getName());
    assertThat(proposalSwingTable.getValueAt(0, 3)).isEqualTo(getMeal1().getName());
    assertThat(proposalSwingTable.getValueAt(0, 4)).isEqualTo("");
    assertThat(proposalSwingTable.getValueAt(1, 2)).isEqualTo("");
    assertThat(proposalSwingTable.getValueAt(1, 3)).isEqualTo(getMeal2().getName());
    assertThat(proposalSwingTable.getValueAt(1, 4)).isEqualTo("");
  }

  private JTable setupProposalTableWithDesert() {
    MealplanerData data = setupMealplanerDataWithAllMealsAndIngredients();
    data.addMeal(getMealDesert());
    List<ProposedMenu> proposedMenu = new ArrayList<>();
    proposedMenu.add(mainAndDesert(getMeal1().getId(), getMealDesert().getId(), ONE));
    proposedMenu.add(mainOnly(getMeal2().getId(), ONE));
    Proposal proposal = Proposal.from(true, proposedMenu);

    return createProposalTable(data, proposal);
  }

  private JTable setupProposalTableWithThreeCourseMeal() {
    MealplanerData data = setupMealplanerDataWithAllMealsAndIngredients();
    data.addMeal(getMealDesert());
    data.addMeal(getMealEntry());
    data.addMeal(getMealEntry2());
    List<ProposedMenu> proposedMenu = new ArrayList<>();
    proposedMenu.add(threeCourseMeal(
        getMealEntry().getId(), getMeal1().getId(), getMealDesert().getId(), ONE));
    proposedMenu.add(mainOnly(getMeal2().getId(), ONE));
    Proposal proposal = Proposal.from(true, proposedMenu);

    return createProposalTable(data, proposal);
  }

  private JTable createProposalTable(MealplanerData data, Proposal proposal) {
    ProposalTable table = ProposalTable.proposalOutput();
    table.setupProposalTable(data, proposal);

    return table.getTable().getTable();
  }
}
