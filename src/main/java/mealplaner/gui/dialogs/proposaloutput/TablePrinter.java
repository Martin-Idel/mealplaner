package mealplaner.gui.dialogs.proposaloutput;

import static mealplaner.errorhandling.ErrorKeys.ERR_HEADING;
import static mealplaner.errorhandling.ErrorKeys.MSG_FAIL_PRINT;

import java.awt.print.PrinterException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import mealplaner.errorhandling.ErrorMessages;
import mealplaner.errorhandling.Logger;

public class TablePrinter {

	public static void printTable(JTable table, JFrame frame) {
		try {
			table.print();
		} catch (PrinterException exc) {
			JOptionPane.showMessageDialog(frame, ErrorMessages.formatMessage(MSG_FAIL_PRINT),
					ErrorMessages.formatMessage(ERR_HEADING), JOptionPane.ERROR_MESSAGE);
			Logger.logError(exc);
		}
	}
}