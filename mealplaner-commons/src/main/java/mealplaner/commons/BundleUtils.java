// SPDX-License-Identifier: MIT

package mealplaner.commons;

import static java.util.ResourceBundle.getBundle;
import static javax.swing.JOptionPane.ERROR_MESSAGE;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mealplaner.commons.errorhandling.MealException;

public class BundleUtils {
  private static final Logger logger = LoggerFactory.getLogger(BundleUtils.class);

  public static ResourceBundle loadBundle(String bundleName, Locale locale) {
    ResourceBundle bundle;
    try {
      bundle = getBundle(bundleName, locale);
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
