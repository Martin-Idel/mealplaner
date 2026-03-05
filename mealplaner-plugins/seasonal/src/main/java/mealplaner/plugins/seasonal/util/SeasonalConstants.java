// SPDX-License-Identifier: MIT

package mealplaner.plugins.seasonal.util;

import static mealplaner.commons.BundleStore.BUNDLES;

import java.awt.Color;

public final class SeasonalConstants {
  public static final Color MAIN_SEASON_COLOR = Color.RED;
  public static final Color OFF_SEASON_COLOR = Color.YELLOW;
  public static final Color NO_SEASON_COLOR = Color.WHITE;
  private static final String[] MONTH_KEYS = {
      "january", "february", "march", "april", "may", "june",
      "july", "august", "september", "october", "november", "december"
  };

  private SeasonalConstants() {
  }

  public static String getMonthDisplayName(int monthIndex) {
    return BUNDLES.message(MONTH_KEYS[monthIndex]);
  }

  public static String getSeasonalLegend() {
    return BUNDLES.message("seasonalLegend");
  }

  public static Color getMonthColorForMainSeason() {
    return MAIN_SEASON_COLOR;
  }

  public static Color getMonthColorForOffSeason() {
    return OFF_SEASON_COLOR;
  }

  public static Color getMonthColorForNoSeason() {
    return NO_SEASON_COLOR;
  }
}
