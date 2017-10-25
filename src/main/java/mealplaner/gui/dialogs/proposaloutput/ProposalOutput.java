package mealplaner.gui.dialogs.proposaloutput;

import static mealplaner.BundleStore.BUNDLES;
import static mealplaner.gui.dialogs.proposaloutput.TablePrinter.printTable;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import mealplaner.gui.commons.ButtonPanelBuilder;
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

		JTable proposalTable = new ProposalTableFactory().createTable(lastProposal);
		JScrollPane tablescroll = new JScrollPane(proposalTable);

		JPanel buttonPanel = new ButtonPanelBuilder()
				.addButton(BUNDLES.message("printButton"),
						BUNDLES.message("printButtonMnemonic"),
						action -> printTable(proposalTable, parentFrame))
				.addOkButton(ButtonPanelBuilder.justDisposeListener(this))
				.build();

		arrangeGui(dataPanel, parentFrame, tablescroll, buttonPanel);
		setVisible(true);
	}

	private JPanel setupDataPanel() {
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new BorderLayout());
		return dataPanel;
	}

	private void arrangeGui(JPanel dataPanel, JFrame parentFrame, JScrollPane tablescroll,
			JPanel buttonPanel) {
		dataPanel.add(tablescroll, BorderLayout.CENTER);
		dataPanel.add(buttonPanel, BorderLayout.SOUTH);
		getContentPane().add(dataPanel);
		setSize(300, 300);
		setLocationRelativeTo(parentFrame);
	}
}