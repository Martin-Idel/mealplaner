package mealplaner.gui.dialogs.proposaloutput;
/**
 * Martin Idel, 
 * ProposalOutput: Present Proposal in table form. It is also possible to print the proposal directly.
 * It is called by ProposalSummary once entering the settings is completed.
 **/

import static mealplaner.gui.dialogs.proposaloutput.TablePrinter.printTable;

import java.awt.BorderLayout;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import mealplaner.errorhandling.ErrorKeys;
import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.model.Proposal;

public class ProposalOutput extends JDialog implements ErrorKeys {
	private static final long serialVersionUID = 1L;

	public ProposalOutput(JFrame parentFrame, Proposal lastProposal, Locale parentLocale, ResourceBundle parentMes) {
		super(parentFrame, parentMes.getString("proposeWindow"), true);

		JPanel dataPanel = setupDataPanel();

		JTable proposalTable = new ProposalTableFactory(parentMes, parentLocale).createTable(lastProposal);
		JScrollPane tablescroll = new JScrollPane(proposalTable);
		JPanel buttonPanel = new ButtonPanelBuilder(parentMes)
				.addButton("proposeButton1", "proposeButton1Mnemonic",
						action -> printTable(proposalTable, parentFrame))
				.addOkButton(ButtonPanelBuilder.justDisposeListener(this))
				.build();

		displayGUI(dataPanel, parentFrame, tablescroll, buttonPanel);
	}

	private JPanel setupDataPanel() {
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new BorderLayout());
		return dataPanel;
	}

	private void displayGUI(JPanel dataPanel, JFrame parentFrame, JScrollPane tablescroll, JPanel buttonPanel) {
		dataPanel.add(tablescroll, BorderLayout.CENTER);
		dataPanel.add(buttonPanel, BorderLayout.SOUTH);
		getContentPane().add(dataPanel);
		setSize(300, 300);
		setLocationRelativeTo(parentFrame);
		setVisible(true);
	}
}