// SPDX-License-Identifier: MIT

package mealplaner.commons;

import static mealplaner.commons.BundleStore.BUNDLES;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.TreeMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class BundleStoreTest {
  private static ResourceBundleMock firstBundle;
  private static ResourceBundleMock secondBundle;

  @BeforeAll
  public static void setUp() {
    TreeMap<String, String> tm = new TreeMap<>();
    tm.put("key", "value");
    firstBundle = new ResourceBundleMock(tm);

    TreeMap<String, String> tm2 = new TreeMap<>();
    tm2.put("otherKey", "otherValue");
    secondBundle = new ResourceBundleMock(tm2);
  }

  @Test
  public void bundleStore() {
    BUNDLES.addMessageBundle(firstBundle);
    BUNDLES.addErrorBundle(secondBundle);

    assertThat(BUNDLES.message("key")).isEqualTo("value");
    assertThat(BUNDLES.errorMessage("otherKey")).isEqualTo("otherValue");
  }
}
