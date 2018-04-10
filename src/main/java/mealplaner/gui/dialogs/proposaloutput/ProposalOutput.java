package mealplaner.gui.dialogs.proposaloutput;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.justDisposeListener;
import static mealplaner.gui.dialogs.proposaloutput.ProposalTable.proposalOutput;

import javax.swing.JFrame;

import mealplaner.commons.gui.buttonpanel.ButtonPanel;
import mealplaner.commons.gui.dialogs.DialogWindow;
import mealplaner.gui.dialogs.DialogCreating;
import mealplaner.model.DataStore;
import mealplaner.model.proposal.Proposal;

public class ProposalOutput implements DialogCreating<Proposal> {
  private final JFrame parentFrame;
  private final DialogWindow dialogWindow;

  public ProposalOutput(JFrame parentFrame) {
    dialogWindow = DialogWindow.window(parentFrame, BUNDLES.message("proposalOutputDialogTitle"));
    this.parentFrame = parentFrame;
  }

  @Override
  public Proposal showDialog(DataStore store) {
    ProposalTable proposalTable = proposalOutput();
    proposalTable.setupProposalTable(store, store.getLastProposal());

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