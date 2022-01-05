// SPDX-License-Identifier: MIT

package mealplaner.plugins.cookingtime;

import static bundletests.BundleCommons.allMessageTests;

import org.junit.jupiter.api.Test;

class CookingTimeBundleTest {
  @Test
  void testBundle() {
    allMessageTests("CookingTimeMessagesBundle", "cookingtime");
  }
}
