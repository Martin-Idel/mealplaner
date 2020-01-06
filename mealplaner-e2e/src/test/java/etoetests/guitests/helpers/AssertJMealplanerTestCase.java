// SPDX-License-Identifier: MIT

package etoetests.guitests.helpers;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static mealplaner.commons.BundleStore.BUNDLES;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.JFrame;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;

import etoetests.CommonFunctions;
import etoetests.guitests.pageobjects.GuiMethods;
import mealplaner.gui.MainGui;
import mealplaner.gui.factories.DialogFactory;
import mealplaner.io.FileIoGui;
import mealplaner.model.MealplanerData;
import mealplaner.plugins.PluginStore;

public class AssertJMealplanerTestCase extends AssertJSwingJUnitTestCase {
  protected GuiMethods windowHelpers;
  private static final String WORKING_DIRECTORY = "src/test/resources/temp/";
  private static final String DESTINATION_MEALS_FILE_PATH = WORKING_DIRECTORY + "meals.xml";
  private static final String DESTINATION_MEALPLANER_FILE_PATH = WORKING_DIRECTORY
      + "save.xml";
  protected static final String DESTINATION_INGREDIENT_FILE_PATH = WORKING_DIRECTORY
      + "ingredients.xml";
  private final String originMealsFilePath;
  private final String originMealplanerFilePath;
  private final String originIngredientFilePath;

  protected AssertJMealplanerTestCase() {
    this("src/test/resources/meals.xml",
        "src/test/resources/save.xml",
        "src/test/resources/ingredients.xml");
  }

  protected AssertJMealplanerTestCase(String originMealFilePath, String originMealplanerFilePath,
                                      String originIngredientFilePath) {
    this.originMealsFilePath = originMealFilePath;
    this.originMealplanerFilePath = originMealplanerFilePath;
    this.originIngredientFilePath = originIngredientFilePath;
  }

  @Override
  protected void onSetUp() {
    MainGui frame = GuiActionRunner.execute(this::createMainApplication);
    FrameFixture window = new FrameFixture(robot(), frame.getFrame());
    window.show();
    windowHelpers = GuiMethods.create(window);
  }

  @Override
  protected void onTearDown() {
    try {
      File mealsFile = new File(DESTINATION_MEALS_FILE_PATH);
      Files.delete(mealsFile.toPath());
      File saveFile = new File(DESTINATION_MEALPLANER_FILE_PATH);
      Files.delete(saveFile.toPath());
      File ingredientFile = new File(DESTINATION_INGREDIENT_FILE_PATH);
      Files.delete(ingredientFile.toPath());
    } catch (IOException ioex) {
      fail("One of the files used as save files does not exist");
    }
  }

  private MainGui createMainApplication() {
    try {
      JFrame mainFrame = new JFrame(BUNDLES.message("mainFrameTitle"));
      PluginStore pluginStore = CommonFunctions.registerPlugins();
      FileIoGui fileIoGui = new FileIoGui(mainFrame, useFilePath(), pluginStore);
      MealplanerData data = fileIoGui.loadDatabase(pluginStore);
      DialogFactory dialogFactory = new DialogFactory(mainFrame);
      return new MainGui(mainFrame, data, dialogFactory, fileIoGui, pluginStore);
    } catch (IOException exception) {
      fail("One of the files to use as save files does not exist");
      return null;
    }
  }

  private String useFilePath() throws IOException {
    copyFile(originMealsFilePath, DESTINATION_MEALS_FILE_PATH);
    copyFile(originMealplanerFilePath, DESTINATION_MEALPLANER_FILE_PATH);
    copyFile(originIngredientFilePath, DESTINATION_INGREDIENT_FILE_PATH);
    return new File(WORKING_DIRECTORY).getPath() + "/";
  }

  private void copyFile(String filename, String destinationPath) throws IOException {
    File originalFile = new File(filename);
    File temporaryFile = new File(destinationPath);
    if (!temporaryFile.getParentFile().exists()) {
      var mkdirs = temporaryFile.getParentFile().mkdirs();
      if (!mkdirs) {
        fail("Could not create temporary directories");
      }
    }
    Files.copy(originalFile.toPath(), temporaryFile.toPath(), REPLACE_EXISTING);
  }
}
