package mealplaner.errorhandling;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class ErrorMessages {
	private static ResourceBundle errorBundles = ResourceBundle.getBundle("ErrorBundle",
			Locale.getDefault());

	private static String getMessageString(String messageKey) {
		return errorBundles.getString(messageKey);
	}

	public static String formatMessage(String Key) {
		MessageFormat mf = new MessageFormat(getMessageString(Key));
		return mf.format(new Object[0]);
	}
}