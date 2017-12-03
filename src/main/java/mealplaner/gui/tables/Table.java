package mealplaner.gui.tables;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import mealplaner.gui.dialogs.proposaloutput.TablePrinter;

public class Table {
	private FlexibleTableModel tableModel;
	private JTable table;

	private Table(FlexibleTableModel tableModel, JTable table) {
		this.tableModel = tableModel;
		this.table = table;
	}

	public static Table from(FlexibleTableModel tableModel, JTable table) {
		return new Table(tableModel, table);
	}

	public void addToPane(JPanel panel) {
		panel.add(table, BorderLayout.CENTER);
	}

	public void addScrollingTableToPane(JPanel panel) {
		JScrollPane scrollPanel = new JScrollPane(table);
		panel.add(scrollPanel, BorderLayout.CENTER);
	}

	public JTable getTable() {
		return table;
	}

	public void printTable(JFrame frame) {
		TablePrinter.printTable(table, frame);
	}

	public void update() {
		tableModel.updateTable();
	}

	public void insertRows(int firstRow, int lastRow) {
		tableModel.insertRows(firstRow, lastRow);
	}

	public void deleteRows(int firstRow, int lastRow) {
		tableModel.deleteRows(firstRow, lastRow);
	}

	public int[] getSelectedRows() {
		return table.getSelectedRows();
	}
}
