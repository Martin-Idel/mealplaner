package mealplaner.gui.dialogs.proposaloutput;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.gui.dialogs.proposaloutput.ProposalTableFactory.proposalOutput;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mealplaner.commons.gui.ButtonPanelBuilder;
import mealplaner.commons.gui.tables.Table;
import mealplaner.model.Proposal;

public class ProposalOutput extends JDialog {
	private static final long serialVersionUID = 1L;
	private JFrame parentFrame;

	public ProposalOutput(JFrame parentFrame) {
		super(parentFrame, BUNDLES.message("proposalOutputDialogTitle"), true);
		this.parentFrame = parentFrame;
	}

	public void showDialog(Proposal lastProposal) {
		JPanel dataPanel = setupDataPanel();

		Table proposalTable = proposalOutput().createProposalTable(lastProposal);
		proposalTable.addScrollingTableToPane(dataPanel);

		JPanel buttonPanel = new ButtonPanelBuilder()
				.addButton(BUNDLES.message("printButton"),
						BUNDLES.message("printButtonMnemonic"),
						action -> proposalTable.printTable(parentFrame))
				.addOkButton(ButtonPanelBuilder.justDisposeListener(this))
				.build();

		arrangeGui(dataPanel, parentFrame, buttonPanel);
		setVisible(true);
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