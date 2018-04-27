// SPDX-License-Identifier: MIT

package mealplaner.commons;

import static java.util.ResourceBundle.Control.getControl;
import static mealplaner.commons.BundleStore.BUNDLES;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public final class Utils {
  private Utils() {
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
      URL url = Utils.class.getResource(resourceName);
      if (url != null) {
        return resourceName;
      }
    }
    return documentationName + "." + suffix;
  }
}
