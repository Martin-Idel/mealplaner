// SPDX-License-Identifier: MIT

package mealplaner.plugins.cookingtime;

import static bundletests.BundleCommons.allMessageTests;

import org.junit.jupiter.api.Test;

public class CookingTimeBundleTest {
  @Test
  public void testBundle() {
    allMessageTests("CookingTimeMessagesBundle", "cookingtime");
  }
}
