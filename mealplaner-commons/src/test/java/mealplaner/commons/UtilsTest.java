// SPDX-License-Identifier: MIT

package mealplaner.commons;

import static mealplaner.commons.Utils.getLocalizedResource;

import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilsTest {

  @Test
  public void getLocalizedResourceWithGermanLocaleReturnsGermanFile() {
    String localizedResource = getLocalizedResource("/help/DatabaseEditHelp", "txt",
        Locale.GERMANY);

    Assertions.assertThat(localizedResource)
        .contains("help/DatabaseEditHelp_de.txt");
  }

  @Test
  public void getLocalizedResourceWithDifferentLocaleReturnsDefaultEnglishFile() {
    String localizedResource = getLocalizedResource("/help/DatabaseEditHelp", "txt", Locale.FRENCH);

    Assertions.assertThat(localizedResource)
        .contains("help/DatabaseEditHelp.txt");
  }
}
