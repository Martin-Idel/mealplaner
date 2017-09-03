package mealplaner.gui.model;

import mealplaner.BundleStore;

public class StringArrayCollection {

	public static String[] getDatabaseColumnNames(BundleStore bundles) {
		String[] columnNames = { bundles.message("mealNameColumn"),
				bundles.message("cookingLengthColumn"),
				bundles.message("sidedishColumn"),
				bundles.message("utensilColumn"),
				bundles.message("cookedLastTimeColumn"),
				bundles.message("popularityColumn"),
				bundles.message("commentInsertColumn") };
		return columnNames;
	}

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
				bundles.message("shortColumn"),
				bundles.message("short"),
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
