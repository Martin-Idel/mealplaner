package mealplaner.commons.gui;

import static mealplaner.commons.BundleStore.BUNDLES;

public final class StringArrayCollection {
  private StringArrayCollection() {
  }

  public static String[] getWeekDays() {
    return new String[] { BUNDLES.message("sunday"),
        BUNDLES.message("monday"),
        BUNDLES.message("tuesday"),
        BUNDLES.message("wednesday"),
        BUNDLES.message("thursday"),
        BUNDLES.message("friday"),
        BUNDLES.message("saturday") };
  }
}
