// SPDX-License-Identifier: MIT

package mealplaner.commons;

import static java.util.Locale.getDefault;
import static mealplaner.commons.BundleUtils.loadBundle;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public enum BundleStore {
  BUNDLES();

  private MultiResourceBundle messages;
  private MultiResourceBundle errors;
  private final Locale currentLocale;

  BundleStore() {
    currentLocale = getDefault();
    messages = new MultiResourceBundle();
    messages.addResourceBundle(loadBundle("MessagesBundle", currentLocale));
    errors = new MultiResourceBundle();
    errors.addResourceBundle(loadBundle("ErrorBundle", currentLocale));
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
}
