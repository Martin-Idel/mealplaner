package mealplaner.gui.dialogs.proposaloutput;

import javax.swing.table.DefaultTableModel;

public class ProposalTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;

	public ProposalTableModel(String[][] proposal, String[] columnNames) {
		super(proposal, columnNames);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}