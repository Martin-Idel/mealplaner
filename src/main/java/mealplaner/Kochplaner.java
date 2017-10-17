package mealplaner;

import static java.util.ResourceBundle.getBundle;
import static javax.swing.JOptionPane.ERROR_MESSAGE;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mealplaner.errorhandling.MealException;
import mealplaner.gui.MainGUI;
import mealplaner.gui.commons.MessageDialog;
import mealplaner.gui.factories.DialogFactory;
import mealplaner.io.IngredientIO;
import mealplaner.recipes.provider.IngredientProvider;

// TODO: load IngredientProvider in MainGUI or somewhere else
// TODO: better error handling and logging
public class Kochplaner {
	private static final Logger logger = LoggerFactory.getLogger(Kochplaner.class);

	public static void main(String args[]) {
		try {
			MealplanerData data = new MealplanerData();
			BundleStore bundles = loadResourceBundle();
			IngredientProvider ingredientProvider;
			try {
				ingredientProvider = IngredientIO.readXml();
			} catch (MealException exc) {
				MessageDialog.errorMessages(null, exc,
						bundles.errorMessage("INGREDIENT_PROVIDER_NOT_FOUND"), bundles);
				ingredientProvider = new IngredientProvider();
			}
			SwingUtilities.invokeLater(createMainGUI(data, ingredientProvider, bundles));
		} catch (MissingResourceException exc) {
			logger.error(
					"Fatal error: Resource bundles could not be found. No localisation possible.",
					exc);
			JOptionPane.showMessageDialog(null, "Resource Bundles not found", "Error",
					ERROR_MESSAGE);
		}
	}

	private static Runnable createMainGUI(MealplanerData data,
			IngredientProvider ingredientProvider, BundleStore bundles) {
		return () -> {
			JFrame mainFrame = new JFrame("Meal planer");
			DialogFactory dialogFactory = new DialogFactory(mainFrame, bundles);
			new MainGUI(mainFrame, data, dialogFactory, bundles, ingredientProvider);
		};
	}

	private static BundleStore loadResourceBundle() {
		Locale currentLocale = Locale.getDefault();
		ResourceBundle messages = getBundle("MessagesBundle", currentLocale);
		ResourceBundle errors = getBundle("ErrorBundle", currentLocale);
		return new BundleStore(messages, errors, currentLocale);
	}
}