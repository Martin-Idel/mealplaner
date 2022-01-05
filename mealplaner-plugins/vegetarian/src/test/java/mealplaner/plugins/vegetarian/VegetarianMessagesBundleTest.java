// SPDX-License-Identifier: MIT

package mealplaner.plugins.vegetarian;

import static bundletests.BundleCommons.allMessageTests;

import java.io.File;

import org.junit.jupiter.api.Test;

class VegetarianMessagesBundleTest {
  @Test
  void testBundle() {
    allMessageTests("VegetarianMessagesBundle", "plugins" + File.separator + "vegetarian");
  }
}
