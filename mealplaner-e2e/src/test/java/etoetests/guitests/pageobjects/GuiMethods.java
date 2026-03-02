// SPDX-License-Identifier: MIT

package etoetests.guitests.pageobjects;

import javax.swing.JFrame;

public final class GuiMethods {
  private final JFrame frame;

  private GuiMethods(JFrame frame) {
    this.frame = frame;
  }

  public static GuiMethods create(JFrame frame) {
    return new GuiMethods(frame);
  }

  public IngredientsEditPageObject getIngredientsPane() {
    return new IngredientsEditPageObject(frame).ingredientsTabbedPane();
  }

  public MealsEditPageObject getMealsPane() {
    return new MealsEditPageObject(frame).mealsTabbedPane();
  }

  public ProposalSummaryPageObject getProposalPane() throws Exception {
    return new ProposalSummaryPageObject(frame).proposalTabbedPane();
  }
}