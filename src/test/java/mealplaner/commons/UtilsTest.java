package mealplaner.commons;

import static mealplaner.commons.Utils.getLocalizedResource;

import java.net.URL;
import java.util.Locale;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class UtilsTest {

  @Test
  public void getLocalizedResourceWithGermanLocaleReturnsGermanFile() {
    URL localizedResource = getLocalizedResource("/help/DatabaseEditHelp", "txt", Locale.GERMANY);

    Assertions.assertThat(localizedResource.toString())
        .contains("bin/help/DatabaseEditHelp_de.txt");
  }

  @Test
  public void getLocalizedResourceWithDifferentLocaleReturnsDefaultEnglishFile() {
    URL localizedResource = getLocalizedResource("/help/DatabaseEditHelp", "txt", Locale.FRENCH);

    Assertions.assertThat(localizedResource.toString())
        .contains("bin/help/DatabaseEditHelp.txt");
  }
}
