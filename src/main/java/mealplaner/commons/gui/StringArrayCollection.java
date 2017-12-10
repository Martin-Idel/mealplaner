package mealplaner.commons.gui;

import static mealplaner.commons.BundleStore.BUNDLES;

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
}
