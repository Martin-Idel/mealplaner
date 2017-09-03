package mealplaner;

import static java.util.ResourceBundle.getBundle;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static mealplaner.errorhandling.Logger.logError;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import mealplaner.gui.MainGUI;

public class Kochplaner {
	public static void main(String args[]) {
		try {
			MealplanerData bla = new MealplanerData();
			BundleStore bundles = loadResourceBundle();
			SwingUtilities.invokeLater(() -> new MainGUI(bla, bundles));
		} catch (MissingResourceException exc) {
			logError(exc);
			JOptionPane.showMessageDialog(null, "Resource Bundles not found", "Error",
					ERROR_MESSAGE);
		}
	}

	private static BundleStore loadResourceBundle() {
		Locale currentLocale = Locale.getDefault();
		ResourceBundle messages = getBundle("MessagesBundle", currentLocale);
		ResourceBundle errors = getBundle("ErrorBundle", currentLocale);
		return new BundleStore(messages, errors, currentLocale);
	}
}