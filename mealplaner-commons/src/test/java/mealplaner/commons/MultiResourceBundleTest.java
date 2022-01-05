// SPDX-License-Identifier: MIT

package mealplaner.commons;

import static java.util.Collections.list;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.TreeMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MultiResourceBundleTest {
  private static ResourceBundleMock firstBundle;
  private static ResourceBundleMock secondBundle;
  private static ResourceBundleMock thirdBundle;

  @BeforeAll
  static void setUp() {
    TreeMap<String, String> tm = new TreeMap<>();
    tm.put("key", "value");
    firstBundle = new ResourceBundleMock(tm);

    TreeMap<String, String> tm2 = new TreeMap<>();
    tm2.put("otherKey", "otherValue");
    secondBundle = new ResourceBundleMock(tm2);

    TreeMap<String, String> tm3 = new TreeMap<>();
    tm3.put("key", "thirdValue");
    thirdBundle = new ResourceBundleMock(tm3);
  }

  @Test
  void multiResourceBundleFindsAllKeysInAllBundles() {
    var multiResourceBundle = new MultiResourceBundle();
    multiResourceBundle.addResourceBundle(firstBundle);
    multiResourceBundle.addResourceBundle(secondBundle);

    assertThat(multiResourceBundle.containsKey("key")).isTrue();
    assertThat(multiResourceBundle.containsKey("otherKey")).isTrue();
    assertThat(multiResourceBundle.handleGetObject("key")).isEqualTo("value");
    assertThat(multiResourceBundle.handleGetObject("otherKey")).isEqualTo("otherValue");
  }

  @Test
  void multiResourceBundleTakesFirstKeyOfDuplicates() {
    var multiResourceBundle = new MultiResourceBundle();
    multiResourceBundle.addResourceBundle(firstBundle);
    multiResourceBundle.addResourceBundle(thirdBundle);

    assertThat(multiResourceBundle.containsKey("key")).isTrue();
    assertThat(multiResourceBundle.handleGetObject("key")).isEqualTo("value");
  }

  @Test
  void multiResourceBundleReturnsAllKeysOfAllBundles() {
    var multiResourceBundle = new MultiResourceBundle();
    multiResourceBundle.addResourceBundle(firstBundle);
    multiResourceBundle.addResourceBundle(secondBundle);

    assertThat(list(multiResourceBundle.getKeys())).containsExactlyInAnyOrder("key", "otherKey");
  }
}
