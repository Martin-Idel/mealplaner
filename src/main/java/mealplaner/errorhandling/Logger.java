package mealplaner.errorhandling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Logger {
	public static void logError(Exception exception) {
		try {
			PrintStream ps = new PrintStream(new File("error.log"));
			exception.printStackTrace(ps);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
	}
}