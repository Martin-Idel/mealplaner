package mealplaner.gui.commons;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import mealplaner.BundleStore;

public class MessageDialog {

	public static void errorMessages(JFrame frame, Exception exc, String errorMessage,
			BundleStore bundles) {
		JOptionPane.showMessageDialog(frame, errorMessage,
				bundles.errorMessage("ERR_HEADING"), ERROR_MESSAGE);
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
