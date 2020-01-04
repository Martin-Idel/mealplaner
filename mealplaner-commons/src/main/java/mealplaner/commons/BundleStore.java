// SPDX-License-Identifier: MIT

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

import mealplaner.commons.errorhandling.MealException;

public enum BundleStore {
  BUNDLES();

  private static final Logger logger = LoggerFactory.getLogger(BundleStore.class);
  private MultiResourceBundle messages;
  private MultiResourceBundle errors;
  private final Locale currentLocale;

  BundleStore() {
    currentLocale = getDefault();
    messages = new MultiResourceBundle();
    messages.addResourceBundle(loadBundle("MessagesBundle"));
    errors = new MultiResourceBundle();
    errors.addResourceBundle(loadBundle("ErrorBundle"));
  }

  public void addMessageBundle(ResourceBundle resourceBundle) {
    messages.addResourceBundle(resourceBundle);
  }

  public void addErrorBundle(ResourceBundle resourceBundle) {
    errors.addResourceBundle(resourceBundle);
  }

  public String message(String message) {
    return messages.getString(message);
  }

  public String errorMessage(String errorMessage) {
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
