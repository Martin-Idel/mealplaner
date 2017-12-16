package mealplaner.gui.dialogs.proposaloutput;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.gui.dialogs.proposaloutput.ProposalTable.proposalOutput;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mealplaner.commons.gui.ButtonPanelBuilder;
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

    JPanel buttonPanel = createButtonPanel(proposalTable);

    dialogWindow.addCentral(proposalTable.getTable().getTableInScrollPane());
    dialogWindow.addSouth(buttonPanel);
    dialogWindow.arrangeWithSize(300, 300);
    dialogWindow.setVisible();
    return proposalTable.getProposal();
  }

  private JPanel createButtonPanel(ProposalTable proposalTable) {
    return new ButtonPanelBuilder()
        .addButton(BUNDLES.message("printButton"),
            BUNDLES.message("printButtonMnemonic"),
            action -> proposalTable.getTable().printTable(parentFrame))
        .addOkButton(ButtonPanelBuilder.justDisposeListener(dialogWindow))
        .build();
  }
}