// SPDX-License-Identifier: MIT

package mealplaner.commons;

import static mealplaner.commons.BundleUtils.getLocalizedResource;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;

public class BundleUtilsTest {

  @Test
  public void getLocalizedResourceWithGermanLocaleReturnsGermanFile() {
    String localizedResource = getLocalizedResource("/help/DatabaseEditHelp", "txt",
        Locale.GERMANY);

    assertThat(localizedResource)
        .contains("help/DatabaseEditHelp_de.txt");
  }

  @Test
  public void getLocalizedResourceWithDifferentLocaleReturnsDefaultEnglishFile() {
    String localizedResource = getLocalizedResource("/help/DatabaseEditHelp", "txt", Locale.FRENCH);

    assertThat(localizedResource)
        .contains("help/DatabaseEditHelp.txt");
  }
}
