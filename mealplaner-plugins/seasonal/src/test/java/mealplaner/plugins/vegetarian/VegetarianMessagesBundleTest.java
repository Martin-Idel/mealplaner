// SPDX-License-Identifier: MIT

package mealplaner.plugins.vegetarian;

import static bundletests.BundleCommons.allMessageTests;

import java.io.File;

import org.junit.jupiter.api.Test;

public class VegetarianMessagesBundleTest {
  @Test
  public void testBundle() {
    allMessageTests("VegetarianMessagesBundle", "plugins" + File.separator + "vegetarian");
  }
}
