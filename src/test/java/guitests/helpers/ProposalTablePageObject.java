package guitests.helpers;

import static java.time.LocalDate.now;
import static java.time.format.DateTimeFormatter.ofLocalizedDate;
import static java.time.format.FormatStyle.SHORT;
import static java.time.format.TextStyle.FULL;
import static mealplaner.commons.BundleStore.BUNDLES;

import java.time.LocalDate;
import java.util.List;

import org.assertj.swing.fixture.FrameFixture;

import mealplaner.model.meal.Meal;

public class ProposalTablePageObject {
  private final FrameFixture window;

  ProposalTablePageObject(FrameFixture window) {
    this.window = window;
  }

  public ShoppingListPageObject requireProposalFrom(List<Meal> mealOutput) {
    window.dialog().table().requireContents(generateProposalOutputContent(mealOutput));
    window.dialog().button("ButtonPanelProposalOutput1").click();
    return new ShoppingListPageObject(window);
  }

  private String[][] generateProposalOutputContent(List<Meal> mealOutput) {
    String[][] contents = new String[mealOutput.size()][3];
    LocalDate date = now().plusDays(1);
    for (int row = 0; row < mealOutput.size(); row++) {
      contents[row][0] = ofLocalizedDate(SHORT).withLocale(BUNDLES.locale()).format(date);
      contents[row][1] = date.getDayOfWeek().getDisplayName(FULL, BUNDLES.locale());
      contents[row][2] = mealOutput.get(row).getName();
      date = date.plusDays(1);
    }
    return contents;
  }
}
