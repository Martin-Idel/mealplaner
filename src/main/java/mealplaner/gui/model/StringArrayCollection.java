package mealplaner.gui.model;

import java.util.ResourceBundle;

public class StringArrayCollection {

	public static String[] getDatabaseColumnNames(ResourceBundle messages) {
		String[] columnNames = { messages.getString("databankName"),
				messages.getString("databankLength"),
				messages.getString("databankExtra"),
				messages.getString("databankUtensil"),
				messages.getString("databankLasttime"),
				messages.getString("databankPopularity"),
				messages.getString("commentInsert") };
		return columnNames;
	}

	public static String[] getWeekDays(ResourceBundle messages) {
		String[] weekDays = { messages.getString("Day1"),
				messages.getString("Day2"),
				messages.getString("Day3"),
				messages.getString("Day4"),
				messages.getString("Day5"),
				messages.getString("Day6"),
				messages.getString("Day7") };
		return weekDays;
	}

	public static String[] getProposalOutputColumnNames(ResourceBundle messages) {
		String[] columnNames = { messages.getString("proposeDate"),
				messages.getString("proposeDay"),
				messages.getString("proposeName") };
		return columnNames;
	}

	public static String[] getSettingsInputColumnNames(ResourceBundle messages) {
		String[] columnNames = { messages.getString("settingsDay"),
				messages.getString("settingsDate"),
				messages.getString("settingsLength1"),
				messages.getString("settingsLength2"),
				messages.getString("settingsLength3"),
				messages.getString("settingsAmount1"),
				messages.getString("settingsAmount2"),
				messages.getString("settingsPreference") };
		return columnNames;
	}

	public static String[] getUpdatePastMealColumnNames(ResourceBundle messages) {
		String[] columnNames = { messages.getString("dateDate"),
				messages.getString("dateDay"),
				messages.getString("dateName") };
		return columnNames;
	}

	public static String[] getCasseroleCheckboxEntries(ResourceBundle messagesInner) {
		String[] comboboxNames = { messagesInner.getString("casserole1"),
				messagesInner.getString("casserole2"),
				messagesInner.getString("casserole3") };
		return comboboxNames;
	}

}
