package mealplaner;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class BundleStore {
	ResourceBundle messages;
	ResourceBundle errors;
	Locale currentLocale;

	public BundleStore(ResourceBundle messages, ResourceBundle errors, Locale currentLocale) {
		super();
		this.messages = messages;
		this.errors = errors;
	}

	public String message(String message) {
		return messages.getString(message);
	}

	public String error(String errorMessage) {
		MessageFormat mf = new MessageFormat(errors.getString(errorMessage));
		return mf.format(new Object[0]);
	}

	public Locale locale() {
		return currentLocale;
	}
}
