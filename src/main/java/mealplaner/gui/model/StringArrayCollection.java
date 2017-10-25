package mealplaner.gui.model;

import static mealplaner.BundleStore.BUNDLES;

// TODO: Move to TableModel and write tests to make sure we have the correct number of array elements.
public class StringArrayCollection {

	public static String[] getWeekDays() {
		String[] weekDays = { BUNDLES.message("sunday"),
				BUNDLES.message("monday"),
				BUNDLES.message("tuesday"),
				BUNDLES.message("wednesday"),
				BUNDLES.message("thursday"),
				BUNDLES.message("friday"),
				BUNDLES.message("saturday") };
		return weekDays;
	}

	public static String[] getProposalOutputColumnNames() {
		String[] columnNames = { BUNDLES.message("date"),
				BUNDLES.message("weekday"),
				BUNDLES.message("menu") };
		return columnNames;
	}

	public static String[] getSettingsInputColumnNames() {
		String[] columnNames = { BUNDLES.message("weekday"),
				BUNDLES.message("date"),
				BUNDLES.message("veryShortColumn"),
				BUNDLES.message("shortColumn"),
				BUNDLES.message("mediumColumn"),
				BUNDLES.message("longColumn"),
				BUNDLES.message("manyColumn"),
				BUNDLES.message("casseroleColumn"),
				BUNDLES.message("preferenceColumn") };
		return columnNames;
	}

	public static String[] getUpdatePastMealColumnNames() {
		String[] columnNames = { BUNDLES.message("date"),
				BUNDLES.message("weekday"),
				BUNDLES.message("menu") };
		return columnNames;
	}

	public static String[] getCasseroleCheckboxEntries() {
		String[] comboboxNames = { BUNDLES.message("allowedCasseroles"),
				BUNDLES.message("onlyCasseroles"),
				BUNDLES.message("noCasseroles") };
		return comboboxNames;
	}
}
