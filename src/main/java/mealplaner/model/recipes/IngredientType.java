// SPDX-License-Identifier: MIT

package mealplaner.model.recipes;

import static mealplaner.commons.BundleStore.BUNDLES;

public enum IngredientType {
  WHITE_MEAT(BUNDLES.message("WHITE_MEAT")),
  RED_MEAT(BUNDLES.message("RED_MEAT")),
  MEAT_PRODUCTS(BUNDLES.message("MEAT_PRODUCTS")),
  MILK_EGG_PRODUCTS(BUNDLES.message("MILK_EGG_PRODUCTS")),
  FRESH_FRUIT(BUNDLES.message("FRESH_FRUIT")),
  CANNED_FRUIT(BUNDLES.message("CANNED_FRUIT")),
  FRESH_VEGETABLES(BUNDLES.message("FRESH_VEGETABLES")),
  CANNED_VEGETABLES(BUNDLES.message("CANNED_VEGETABLES")),
  SPICE(BUNDLES.message("SPICE")),
  FLUID(BUNDLES.message("FLUID")),
  BAKING_GOODS(BUNDLES.message("BAKING_GOODS")),
  DRY_GOODS(BUNDLES.message("DRY_GOODS")),
  OTHER(BUNDLES.message("OTHER"));

  private String message;

  IngredientType(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return message;
  }
}
