package mealplaner.gui.dialogs.proposaloutput;

import static mealplaner.BundleStore.BUNDLES;

import java.awt.print.PrinterException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TablePrinter {
	private static final Logger logger = LoggerFactory.getLogger(TablePrinter.class);

	private TablePrinter() {
	}

	public static void printTable(JTable table, JFrame frame) {
		try {
			table.print();
		} catch (PrinterException exc) {
			JOptionPane.showMessageDialog(frame, BUNDLES.errorMessage("MSG_FAIL_PRINT"),
					BUNDLES.errorMessage("ERR_HEADING"), JOptionPane.ERROR_MESSAGE);
			logger.error("Printing impossible: ", exc);
		}
	}
}
