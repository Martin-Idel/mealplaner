package mealplaner.commons;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import mealplaner.commons.errorhandling.MealException;

public enum BundleStore {
  BUNDLES();

  private ResourceBundle messages;
  private ResourceBundle errors;
  private Locale currentLocale;

  private BundleStore() {
  }

  public void setMessageBundle(ResourceBundle messages) {
    this.messages = messages;
  }

  public void setErrorBundle(ResourceBundle errors) {
    this.errors = errors;
  }

  public void setLocale(Locale currentLocale) {
    this.currentLocale = currentLocale;
  }

  public String message(String message) {
    if (messages == null) {
      throw new MealException("No message bundle provided");
    }
    return messages.getString(message);
  }

  public String errorMessage(String errorMessage) {
    if (errors == null) {
      throw new MealException("No error bundle provided");
    }
    MessageFormat mf = new MessageFormat(errors.getString(errorMessage));
    return mf.format(new Object[0]);
  }

  public Locale locale() {
    return currentLocale == null ? Locale.getDefault() : currentLocale;
  }
}
