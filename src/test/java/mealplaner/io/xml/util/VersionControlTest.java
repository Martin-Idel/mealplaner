package mealplaner.io.xml.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import mealplaner.io.xml.util.VersionControl;
import testcommons.XmlInteraction;

public class VersionControlTest extends XmlInteraction {

  private static final String VERSION_IS_NOT_FIRST_CHILD = "src/test/resources/versionIsNotFirstChild.xml";
  private static final String VERSION_IS_NOT_TOP_LEVEL = "src/test/resources/versionIsNotTopLevel.xml";
  private static final String VERSION_FIVE_FILE = "src/test/resources/unknownVersion.xml";

  @Test
  public void versionNumberIsReadCorrectly() {
    loadFileWithName(VERSION_FIVE_FILE);

    int version = VersionControl.getVersion(DESTINATION_FILE_PATH);

    assertThat(version).isEqualTo(5);
  }

  @Test
  public void versionNumberIsReadCorrectlyEvenIfNotFirstEntry() {
    loadFileWithName(VERSION_IS_NOT_FIRST_CHILD);

    int version = VersionControl.getVersion(DESTINATION_FILE_PATH);

    assertThat(version).isEqualTo(5);
  }

  @Test
  public void versionNumberIsZeroedIfNoVersionOrNotTopLevel() {
    loadFileWithName(VERSION_IS_NOT_TOP_LEVEL);

    int version = VersionControl.getVersion(DESTINATION_FILE_PATH);

    assertThat(version).isEqualTo(0);
  }
}
