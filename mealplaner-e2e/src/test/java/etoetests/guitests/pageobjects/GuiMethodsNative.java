// SPDX-License-Identifier: MIT

package etoetests.guitests.pageobjects;

import javax.swing.JFrame;

public final class GuiMethodsNative {
  private final JFrame frame;

  private GuiMethodsNative(JFrame frame) {
    this.frame = frame;
  }

  public static GuiMethodsNative create(JFrame frame) {
    return new GuiMethodsNative(frame);
  }

  public IngredientsEditPageObjectNative getIngredientsPane() {
    return new IngredientsEditPageObjectNative(frame).ingredientsTabbedPane();
  }

  public MealsEditPageObjectNative getMealsPane() {
    return new MealsEditPageObjectNative(frame).mealsTabbedPane();
  }

  public ProposalSummaryPageObjectNative getProposalPane() throws Exception {
    return new ProposalSummaryPageObjectNative(frame).proposalTabbedPane();
  }
}