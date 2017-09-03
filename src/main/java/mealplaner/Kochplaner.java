package mealplaner;

import static java.util.ResourceBundle.getBundle;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static mealplaner.errorhandling.Logger.logError;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import mealplaner.gui.MainGUI;
import mealplaner.gui.factories.DialogFactory;

public class Kochplaner {
	public static void main(String args[]) {
		try {
			MealplanerData data = new MealplanerData();
			BundleStore bundles = loadResourceBundle();
			SwingUtilities.invokeLater(extracted(data, bundles));
		} catch (MissingResourceException exc) {
			logError(exc);
			JOptionPane.showMessageDialog(null, "Resource Bundles not found", "Error",
					ERROR_MESSAGE);
		}
	}

	private static Runnable extracted(MealplanerData data, BundleStore bundles) {
		return () -> {
			JFrame mainFrame = new JFrame("Meal planer");
			DialogFactory dialogFactory = new DialogFactory(mainFrame, bundles);
			new MainGUI(mainFrame, data, dialogFactory, bundles);
		};
	}

	private static BundleStore loadResourceBundle() {
		Locale currentLocale = Locale.getDefault();
		ResourceBundle messages = getBundle("MessagesBundle", currentLocale);
		ResourceBundle errors = getBundle("ErrorBundle", currentLocale);
		return new BundleStore(messages, errors, currentLocale);
	}
}