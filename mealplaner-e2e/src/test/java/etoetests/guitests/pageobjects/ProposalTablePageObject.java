// SPDX-License-Identifier: MIT

package etoetests.guitests.pageobjects;

import static etoetests.guitests.constants.ComponentNames.BUTTON_PROPOSALOUTPUT_PROCEED;
import static java.time.LocalDate.now;
import static java.time.format.DateTimeFormatter.ofLocalizedDate;
import static java.time.format.FormatStyle.SHORT;
import static java.time.format.TextStyle.FULL;

import java.time.LocalDate;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;

import etoetests.guitests.helpers.SwingTestHelper;
import org.assertj.core.api.Assertions;

import mealplaner.model.meal.Meal;

public class ProposalTablePageObject extends BasePageObject {
  ProposalTablePageObject(JFrame frame) {
    super(frame);
  }

  public ShoppingListPageObject requireProposalFrom(List<Meal> mealOutput) throws Exception {
    JDialog dialog = helper.findDialogContaining(BUTTON_PROPOSALOUTPUT_PROCEED, 3000);
    JTable table = findTableInDialog(dialog);
    helper.invokeAndWaitVoid(() -> {
      String[][] expectedContent = generateProposalOutputContent(mealOutput);
      Assertions.assertThat(table.getColumnCount()).isEqualTo(3);
      Assertions.assertThat(table.getRowCount()).isEqualTo(mealOutput.size());
      assertTableContents(table, expectedContent);
    });

    JButton proceedButton = helper.findComponentByName(dialog, BUTTON_PROPOSALOUTPUT_PROCEED);
    helper.clickButtonOnEdt(proceedButton);
    helper.waitForDialogToClose(dialog, 3000);

    return new ShoppingListPageObject(frame);
  }

  public void assertMissingRecipes() throws Exception {
    helper.handleOptionPaneWithOptionIfExists(javax.swing.JOptionPane.YES_OPTION, 1000);
  }

  private String[][] generateProposalOutputContent(List<Meal> mealOutput) {
    String[][] contents = new String[mealOutput.size()][3];
    LocalDate date = now().plusDays(1);
    for (int row = 0; row < mealOutput.size(); row++) {
      contents[row][0] = ofLocalizedDate(SHORT).withLocale(mealplaner.commons.BundleStore.BUNDLES.locale()).format(date);
      contents[row][1] = date.getDayOfWeek().getDisplayName(FULL, mealplaner.commons.BundleStore.BUNDLES.locale());
      contents[row][2] = mealOutput.get(row).getName();
      date = date.plusDays(1);
    }
    return contents;
  }

  @Override
  protected void assertTableContents(JTable table, String[][] expectedContent) {
    for (int row = 0; row < expectedContent.length; row++) {
      for (int col = 0; col < expectedContent[row].length; col++) {
        Object actualValue = table.getValueAt(row, col);
        String expectedValue = expectedContent[row][col];
        if (actualValue instanceof Meal) {
          Assertions.assertThat(((Meal) actualValue).getName()).isEqualTo(expectedValue);
        } else {
          Assertions.assertThat(actualValue.toString()).isEqualTo(expectedValue);
        }
      }
    }
  }

  @Override
  protected JTable findTableInDialog(JDialog dialog) {
    return findNamedTableInDialogOrFallback(dialog, "ProposalOutputTable");
  }
}