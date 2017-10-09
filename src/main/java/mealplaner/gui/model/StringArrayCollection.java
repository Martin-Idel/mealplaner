package mealplaner.gui.model;

import mealplaner.BundleStore;

// TODO: Move to TableModel and write tests to make sure we have the correct number of array elements.
public class StringArrayCollection {

	public static String[] getWeekDays(BundleStore bundles) {
		String[] weekDays = { bundles.message("sunday"),
				bundles.message("monday"),
				bundles.message("tuesday"),
				bundles.message("wednesday"),
				bundles.message("thursday"),
				bundles.message("friday"),
				bundles.message("saturday") };
		return weekDays;
	}

	public static String[] getProposalOutputColumnNames(BundleStore bundles) {
		String[] columnNames = { bundles.message("date"),
				bundles.message("weekday"),
				bundles.message("menu") };
		return columnNames;
	}

	public static String[] getSettingsInputColumnNames(BundleStore bundles) {
		String[] columnNames = { bundles.message("weekday"),
				bundles.message("date"),
				bundles.message("veryShortColumn"),
				bundles.message("shortColumn"),
				bundles.message("mediumColumn"),
				bundles.message("longColumn"),
				bundles.message("manyColumn"),
				bundles.message("casseroleColumn"),
				bundles.message("preferenceColumn") };
		return columnNames;
	}

	public static String[] getUpdatePastMealColumnNames(BundleStore bundles) {
		String[] columnNames = { bundles.message("date"),
				bundles.message("weekday"),
				bundles.message("menu") };
		return columnNames;
	}

	public static String[] getCasseroleCheckboxEntries(BundleStore bundles) {
		String[] comboboxNames = { bundles.message("allowedCasseroles"),
				bundles.message("onlyCasseroles"),
				bundles.message("noCasseroles") };
		return comboboxNames;
	}
}
