package mealplaner.commons;

import static java.util.Locale.getDefault;
import static java.util.ResourceBundle.getBundle;
import static javax.swing.JOptionPane.ERROR_MESSAGE;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mealplaner.Kochplaner;
import mealplaner.commons.errorhandling.MealException;

public enum BundleStore {
  BUNDLES();

  private static final Logger logger = LoggerFactory.getLogger(Kochplaner.class);
  private ResourceBundle messages;
  private ResourceBundle errors;
  private Locale currentLocale;

  private BundleStore() {
    currentLocale = getDefault();
  }

  public String message(String message) {
    if (messages == null) {
      messages = loadBundle("MessagesBundle");
    }
    return messages.getString(message);
  }

  public String errorMessage(String errorMessage) {
    if (errors == null) {
      errors = loadBundle("ErrorBundle");
    }
    MessageFormat mf = new MessageFormat(errors.getString(errorMessage));
    return mf.format(new Object[0]);
  }

  public Locale locale() {
    return currentLocale;
  }

  private ResourceBundle loadBundle(String bundleName) {
    ResourceBundle bundle;
    try {
      bundle = getBundle(bundleName, currentLocale);
    } catch (MissingResourceException exc) {
      logger.error(
          "Fatal error: Resource bundles could not be found. No localisation possible.",
          exc);
      JOptionPane.showMessageDialog(null, "Fatal error: Resource Bundles not found", "Error",
          ERROR_MESSAGE);
      throw new MealException("Fatal error: Resource Bundles not found", exc);
    }
    return bundle;
  }
}
