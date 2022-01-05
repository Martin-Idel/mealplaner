// SPDX-License-Identifier: MIT

package mealplaner.plugins.utensil;

import static bundletests.BundleCommons.allMessageTests;

import java.io.File;

import org.junit.jupiter.api.Test;

class ObligatoryUtensilBundleTest {
  @Test
  void testBundle() {
    allMessageTests("ObligatoryUtensilMessagesBundle", "plugins" + File.separator + "utensil");
  }
}
