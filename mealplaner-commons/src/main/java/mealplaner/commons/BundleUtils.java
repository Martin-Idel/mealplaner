// SPDX-License-Identifier: MIT

package mealplaner.commons;

import static java.util.ResourceBundle.Control.getControl;
import static java.util.ResourceBundle.getBundle;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static mealplaner.commons.BundleStore.BUNDLES;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mealplaner.commons.errorhandling.MealException;

public final class BundleUtils {
  private static final Logger logger = LoggerFactory.getLogger(BundleUtils.class);

  private BundleUtils() {
  }

  public static ResourceBundle loadBundle(String bundleName, Locale locale) {
    try {
      return getBundle(bundleName, locale);
    } catch (MissingResourceException exc) {
      logger.error(
          "Fatal error: Resource bundles could not be found. No localisation possible.",
          exc);
      JOptionPane.showMessageDialog(null, "Fatal error: Resource Bundles not found", "Error",
          ERROR_MESSAGE);
      throw new MealException("Fatal error: Resource Bundles not found", exc);
    }
  }

  public static <T> Predicate<T> not(Predicate<T> p) {
    return t -> !p.test(t);
  }

  public static String getLocalizedResource(String documentationName, String suffix) {
    Locale wantedLocale = BUNDLES.locale();
    return getLocalizedResource(documentationName, suffix, wantedLocale);
  }

  static String getLocalizedResource(String documentationName, String suffix,
                                     Locale wantedLocale) {
    ResourceBundle.Control control = getControl(ResourceBundle.Control.FORMAT_DEFAULT);
    List<Locale> possibleLocales = control.getCandidateLocales(documentationName, wantedLocale);

    for (Locale locale : possibleLocales) {
      String resourceName = control.toResourceName(
          control.toBundleName(documentationName, locale), suffix);
      URL url = BundleUtils.class.getResource(resourceName);
      if (url != null) {
        return resourceName;
      }
    }
    return documentationName + "." + suffix;
  }
}
