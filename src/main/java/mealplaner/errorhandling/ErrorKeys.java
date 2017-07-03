package mealplaner.errorhandling;
/**
 * Martin Idel
 * Only keys for error messages (other bundles than normal messages) - makes sure we have internationalised error messages.
 * Heavily based on the ideas and code here: http://www.javaworld.com/article/2075897/testing-debugging/exceptional-practices--part-3.html
 **/

public interface ErrorKeys {
	// This is the name of all error fields.
	public static String ERR_HEADING = "ERR_HEADING";
	// These are the possible inputs.
	public static String MSG_NO_NAME = "MSG_NO_NAME";
	public static String MSG_LENGTH_NOT_OK = "MSG_LENGTH_NOT_OK";
	public static String MSG_STARK_NOT_OK = "MSG_STARK_NOT_OK";
	public static String MSG_UTENSIL_NOT_OK = "MSG_UTENSIL_NOT_OK";
	public static String MSG_DAYS_PASSED_NEG = "MSG_DAYS_PASSED_NEG";
	public static String MSG_PREF_NOT_OK = "MSG_PREF_NOT_OK";
	public static String MSG_SETTINGS_CAS = "MSG_SETTINGS_CAS";
	public static String MSG_SETTINGS_PREF = "MSG_SETTINGS_PREF";
	public static String MSG_IOEX = "MSG_IOEX";
	public static String MSG_FILE_NOT_FOUND = "MSG_FILE_NOT_FOUND";
	public static String MSG_CLASS_NOT_FOUND = "MSG_CLASS_NOT_FOUND";
	public static String MSG_BKU_FILE_NOT_FOUND = "MSG_BKU_FILE_NOT_FOUND";
	public static String MSG_BKU_CLASS_NOT_FOUND = "MSG_BKU_CLASS_NOT_FOUND";
	public static String MSG_DATA_INTERNAL_ERROR = "MSG_DATA_INTERNAL_ERROR";
	public static String MSG_FAIL_PRINT = "MSG_FAIL_PRINT";
	public static String MSG_UPD_INTERNAL = "MSG_UPD_INTERNAL";
	public static String MSG_SET_DEFAULT = "MSG_SET_DEFAULT";
	public static String MSG_CREATE_SET_INTERNAL = "MSG_CREATE_SET_INTERNAL";
	public static String MSG_PROP_NULL = "MSG_PROP_NULL";
	public static String MSG_PROP_NUMBER = "MSG_PROP_NUMBER";
	public static String MSG_PROP_ARRAY = "MSG_PROP_ARRAY";
	public static String MSG_MENU_NULL = "MSG_MENU_NULL";
	public static String MSG_MENU_NUMBER = "MSG_MENU_NUMBER";
	public static String MSG_MENU_MENU = "MSG_MENU_MENU";
	public static String MSG_MISSING_RBUNDLE = "MSG_MISSING_RBUNDLE";
}