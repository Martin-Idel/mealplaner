package mealplaner.gui.dialogs.proposaloutput;

import static mealplaner.gui.dialogs.proposaloutput.TablePrinter.printTable;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import mealplaner.BundleStore;
import mealplaner.errorhandling.ErrorKeys;
import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.model.Proposal;

public class ProposalOutput extends JDialog implements ErrorKeys {
	private static final long serialVersionUID = 1L;

	public ProposalOutput(JFrame parentFrame, Proposal lastProposal, BundleStore bundles) {
		super(parentFrame, bundles.message("proposalOutputDialogTitle"), true);

		JPanel dataPanel = setupDataPanel();

		JTable proposalTable = new ProposalTableFactory(bundles)
				.createTable(lastProposal);
		JScrollPane tablescroll = new JScrollPane(proposalTable);
		JPanel buttonPanel = new ButtonPanelBuilder(bundles)
				.addButton(bundles.message("printButton"),
						bundles.message("printButtonMnemonic"),
						action -> printTable(proposalTable, parentFrame, bundles))
				.addOkButton(ButtonPanelBuilder.justDisposeListener(this))
				.build();

		displayGUI(dataPanel, parentFrame, tablescroll, buttonPanel);
	}

	private JPanel setupDataPanel() {
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new BorderLayout());
		return dataPanel;
	}

	private void displayGUI(JPanel dataPanel, JFrame parentFrame, JScrollPane tablescroll,
			JPanel buttonPanel) {
		dataPanel.add(tablescroll, BorderLayout.CENTER);
		dataPanel.add(buttonPanel, BorderLayout.SOUTH);
		getContentPane().add(dataPanel);
		setSize(300, 300);
		setLocationRelativeTo(parentFrame);
		setVisible(true);
	}
}