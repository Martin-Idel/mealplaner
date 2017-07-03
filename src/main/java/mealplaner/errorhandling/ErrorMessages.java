package mealplaner.errorhandling;
/**
 * Martin Idel, 02.02.2016 (last update)
 * ErrorMessages: class to format messages (keys given by ErrorKeys) for internationalised error messages.
 * Heavily based on the ideas and code here: http://www.javaworld.com/article/2075897/testing-debugging/exceptional-practices--part-3.html
 **/

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class ErrorMessages {
	private static ResourceBundle errorBundles = ResourceBundle.getBundle("ErrorBundle", Locale.getDefault());

	private static String getMessageString(String messageKey) {
		return errorBundles.getString(messageKey);
	}

	public static String formatMessage(String Key) {
		MessageFormat mf = new MessageFormat(getMessageString(Key));
		return mf.format(new Object[0]);
	}
}