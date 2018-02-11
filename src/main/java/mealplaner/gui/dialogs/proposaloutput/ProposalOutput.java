package mealplaner.gui.dialogs.proposaloutput;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.justDisposeListener;
import static mealplaner.gui.dialogs.proposaloutput.ProposalTable.proposalOutput;

import java.util.List;

import javax.swing.JFrame;

import mealplaner.commons.gui.buttonpanel.ButtonPanel;
import mealplaner.commons.gui.dialogs.DialogWindow;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;

public class ProposalOutput {
  private final JFrame parentFrame;
  private final DialogWindow dialogWindow;

  public ProposalOutput(JFrame parentFrame) {
    dialogWindow = DialogWindow.window(parentFrame, BUNDLES.message("proposalOutputDialogTitle"));
    this.parentFrame = parentFrame;
  }

  public Proposal showDialog(List<Meal> meals, Proposal lastProposal) {
    ProposalTable proposalTable = proposalOutput(meals);
    proposalTable.setupProposalTable(lastProposal);

    ButtonPanel buttonPanel = createButtonPanel(proposalTable);

    dialogWindow.addCentral(proposalTable.getTable());
    dialogWindow.addSouth(buttonPanel);
    dialogWindow.arrangeWithSize(300, 300);
    dialogWindow.setVisible();
    return proposalTable.getProposal();
  }

  private ButtonPanel createButtonPanel(ProposalTable proposalTable) {
    return builder("ProposalOutput")
        .addButton(BUNDLES.message("printButton"),
            BUNDLES.message("printButtonMnemonic"),
            action -> proposalTable.getTable().printTable(parentFrame))
        .addOkButton(justDisposeListener(dialogWindow))
        .build();
  }
}