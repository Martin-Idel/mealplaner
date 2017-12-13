package mealplaner.gui.dialogs.proposaloutput;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.gui.dialogs.proposaloutput.ProposalTable.proposalOutput;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mealplaner.commons.gui.ButtonPanelBuilder;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;

public class ProposalOutput extends JDialog {
  private static final long serialVersionUID = 1L;
  private final JFrame parentFrame;

  public ProposalOutput(JFrame parentFrame) {
    super(parentFrame, BUNDLES.message("proposalOutputDialogTitle"), true);
    this.parentFrame = parentFrame;
  }

  public Proposal showDialog(List<Meal> meals, Proposal lastProposal) {
    JPanel dataPanel = setupDataPanel();

    ProposalTable proposalTable = proposalOutput(meals);
    proposalTable.createProposalTable(lastProposal);
    proposalTable.addToScrollingPane(dataPanel);

    JPanel buttonPanel = new ButtonPanelBuilder()
        .addButton(BUNDLES.message("printButton"),
            BUNDLES.message("printButtonMnemonic"),
            action -> proposalTable.printTable(parentFrame))
        .addOkButton(ButtonPanelBuilder.justDisposeListener(this))
        .build();

    arrangeGui(dataPanel, parentFrame, buttonPanel);
    setVisible(true);
    return proposalTable.getProposal();
  }

  private JPanel setupDataPanel() {
    JPanel dataPanel = new JPanel();
    dataPanel.setLayout(new BorderLayout());
    return dataPanel;
  }

  private void arrangeGui(JPanel dataPanel, JFrame parentFrame, JPanel buttonPanel) {
    dataPanel.add(buttonPanel, BorderLayout.SOUTH);
    getContentPane().add(dataPanel);
    setSize(300, 300);
    setLocationRelativeTo(parentFrame);
  }
}