package mealplaner.gui.model;

import java.util.ResourceBundle;

public class StringArrayCollection {

	public static String[] getDatabaseColumnNames(ResourceBundle messages) {
		String[] columnNames = { messages.getString("mealNameColumn"),
				messages.getString("cookingLengthColumn"),
				messages.getString("sidedishColumn"),
				messages.getString("utensilColumn"),
				messages.getString("cookedLastTimeColumn"),
				messages.getString("popularityColumn"),
				messages.getString("commentInsertColumn") };
		return columnNames;
	}

	public static String[] getWeekDays(ResourceBundle messages) {
		String[] weekDays = { messages.getString("sunday"),
				messages.getString("monday"),
				messages.getString("tuesday"),
				messages.getString("wednesday"),
				messages.getString("thursday"),
				messages.getString("friday"),
				messages.getString("saturday") };
		return weekDays;
	}

	public static String[] getProposalOutputColumnNames(ResourceBundle messages) {
		String[] columnNames = { messages.getString("date"),
				messages.getString("weekday"),
				messages.getString("menu") };
		return columnNames;
	}

	public static String[] getSettingsInputColumnNames(ResourceBundle messages) {
		String[] columnNames = { messages.getString("weekday"),
				messages.getString("shortColumn"),
				messages.getString("short"),
				messages.getString("mediumColumn"),
				messages.getString("longColumn"),
				messages.getString("manyColumn"),
				messages.getString("casseroleColumn"),
				messages.getString("preferenceColumn") };
		return columnNames;
	}

	public static String[] getUpdatePastMealColumnNames(ResourceBundle messages) {
		String[] columnNames = { messages.getString("date"),
				messages.getString("weekday"),
				messages.getString("menu") };
		return columnNames;
	}

	public static String[] getCasseroleCheckboxEntries(ResourceBundle messagesInner) {
		String[] comboboxNames = { messagesInner.getString("allowedCasseroles"),
				messagesInner.getString("onlyCasseroles"),
				messagesInner.getString("noCasseroles") };
		return comboboxNames;
	}
}
