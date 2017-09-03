package mealplaner.gui.dialogs.proposaloutput;

import static mealplaner.gui.dialogs.proposaloutput.TablePrinter.printTable;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import mealplaner.BundleStore;
import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.model.Proposal;

public class ProposalOutput extends JDialog {
	private static final long serialVersionUID = 1L;
	private JFrame parentFrame;
	private BundleStore bundles;

	public ProposalOutput(JFrame parentFrame, BundleStore bundles) {
		super(parentFrame, bundles.message("proposalOutputDialogTitle"), true);
		this.parentFrame = parentFrame;
		this.bundles = bundles;
	}

	public void showDialog(Proposal lastProposal) {
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