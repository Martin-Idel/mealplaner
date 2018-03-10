package mealplaner.xml;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Test;

import mealplaner.xml.VersionControl;

public class VersionControlTest {
  private static final String DESTINATION_FILE_PATH = "src/test/resources/saveTemp.xml";

  private static final String VERSION_IS_NOT_FIRST_CHILD = "src/test/resources/versionIsNotFirstChild.xml";
  private static final String VERSION_IS_NOT_TOP_LEVEL = "src/test/resources/versionIsNotTopLevel.xml";
  private static final String VERSION_FIVE_FILE = "src/test/resources/unknownVersion.xml";

  @After
  public void tearDown() {
    try {
      File file = new File(DESTINATION_FILE_PATH);
      if (file.exists()) {
        Files.delete(file.toPath());
      }
    } catch (IOException ioex) {
      fail("Something went wrong with the TearDown");
    }
  }

  @Test
  public void versionNumberIsReadCorrectly() {
    File originalFile = new File(VERSION_FIVE_FILE);
    moveFileToWorkingFile(originalFile);

    int version = VersionControl.getVersion(DESTINATION_FILE_PATH);

    assertThat(version).isEqualTo(5);
  }

  @Test
  public void versionNumberIsReadCorrectlyEvenIfNotFirstEntry() {
    File originalFile = new File(VERSION_IS_NOT_FIRST_CHILD);
    moveFileToWorkingFile(originalFile);

    int version = VersionControl.getVersion(DESTINATION_FILE_PATH);

    assertThat(version).isEqualTo(5);
  }

  @Test
  public void versionNumberIsZeroedIfNoVersionOrNotTopLevel() {
    File originalFile = new File(VERSION_IS_NOT_TOP_LEVEL);
    moveFileToWorkingFile(originalFile);

    int version = VersionControl.getVersion(DESTINATION_FILE_PATH);

    assertThat(version).isEqualTo(0);
  }

  private void moveFileToWorkingFile(File originalFile) {
    File temporaryFile = new File(DESTINATION_FILE_PATH);
    try {
      Files.copy(originalFile.toPath(), temporaryFile.toPath(), REPLACE_EXISTING);
    } catch (IOException exc) {
      fail("Could not load file");
    }
  }
}
