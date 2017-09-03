package mealplaner.gui.commons;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static mealplaner.errorhandling.ErrorKeys.ERR_HEADING;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import mealplaner.BundleStore;
import mealplaner.errorhandling.Logger;

public class MessageDialog {

	public static void errorMessages(JFrame frame, Exception exc, String errorMessage,
			BundleStore bundles) {
		JOptionPane.showMessageDialog(frame, errorMessage,
				bundles.error(ERR_HEADING), ERROR_MESSAGE);
		Logger.logError(exc);
	}

	public static void showSaveExitDialog(JFrame frame, String saveMessage, Runnable saveAction) {
		int result = JOptionPane.showConfirmDialog(frame, saveMessage);
		if (result == JOptionPane.YES_OPTION) {
			saveAction.run();
			frame.dispose();
		} else if (result == JOptionPane.NO_OPTION) {
			frame.dispose();
		}
	}
}
