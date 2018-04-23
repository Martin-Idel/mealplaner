package guitests.pageobjects;

import org.assertj.swing.fixture.FrameFixture;

public final class GuiMethods {
  private final FrameFixture window;

  private GuiMethods(FrameFixture window) {
    this.window = window;
  }

  public static GuiMethods create(FrameFixture window) {
    return new GuiMethods(window);
  }

  public ProposalSummaryPageObject getProposalPane() {
    return new ProposalSummaryPageObject(window).proposalTabbedPane();
  }

  public MealsEditPageObject getMealsPane() {
    return new MealsEditPageObject(window).mealsTabbedPane();
  }

  public IngredientsEditPageObject getIngredientsPane() {
    return new IngredientsEditPageObject(window).ingredientsTabbedPane();
  }
}
