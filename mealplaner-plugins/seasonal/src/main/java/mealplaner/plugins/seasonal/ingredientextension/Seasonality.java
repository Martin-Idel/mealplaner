package mealplaner.plugins.seasonal.ingredientextension;

import static mealplaner.commons.BundleStore.BUNDLES;

public enum Seasonality {
  NON_SEASONAL(BUNDLES.message("nonSeasonal")),
  MILDLY_SEASONAL(BUNDLES.message("mildlySeasonal")),
  VERY_SEASONAL(BUNDLES.message("verySeasonal"));

  private final String message;

  Seasonality(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return message;
  }
}