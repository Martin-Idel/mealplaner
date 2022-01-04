// SPDX-License-Identifier: MIT

package mealplaner.plugins.recipephoto;

import static mealplaner.plugins.recipephoto.mealedit.RecipePhotoEdit.copyFileToNewLocation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class RecipePhotoEditTest {
  public static final String TEST_FILE = "photo.png";
  public static final String TEST_CONTENT = "best photo ever";

  @Test
  public void copyFileToNewLocationCopiesFileCorrectly(@TempDir Path saveLocation, @TempDir Path copyLocation)
      throws IOException {
    Path photo = saveLocation.resolve(TEST_FILE);
    Files.write(photo, List.of(TEST_CONTENT));

    copyFileToNewLocation(saveLocation.resolve(TEST_FILE), copyLocation);

    assertThat(Files.exists(copyLocation.resolve(TEST_FILE))).isTrue();
    assertThat(Files.readAllLines(copyLocation.resolve(TEST_FILE))).containsExactly(TEST_CONTENT);
  }

  @Test
  public void copyFileToNewLocationThrowsOnNonexistentFile(@TempDir Path saveLocation, @TempDir Path copyLocation) {
    assertThatThrownBy(() -> copyFileToNewLocation(saveLocation.resolve(TEST_FILE), copyLocation))
        .isInstanceOf(IOException.class);
  }
}
