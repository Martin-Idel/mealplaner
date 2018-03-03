package guittests.helpers;

import static guittests.helpers.GuiMethods.create;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.io.IngredientProviderIoGui.loadIngredientProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JFrame;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Assert;

import mealplaner.MealplanerData;
import mealplaner.gui.MainGui;
import mealplaner.gui.factories.DialogFactory;
import mealplaner.io.FileIoGui;
import mealplaner.recipes.provider.IngredientProvider;

public class AssertJMealplanerTestCase extends AssertJSwingJUnitTestCase {
  protected FrameFixture window;
  protected GuiMethods windowHelpers;
  protected static final String DESTINATION_FILE_PATH = "src/test/resources/saveTemp.xml";
  protected static final String DESTINATION_INGREDIENT_FILE_PATH = "src/test/resources/ingredientsTemp.xml";
  private String originFilePath;
  private String originIngredientFilePath;

  public AssertJMealplanerTestCase() {
    this("src/test/resources/save.xml", "src/test/resources/ingredients.xml");
  }

  public AssertJMealplanerTestCase(String originFilePath, String originIngredientFilePath) {
    this.originFilePath = originFilePath;
    this.originIngredientFilePath = originIngredientFilePath;
  }

  @Override
  protected void onSetUp() {
    MainGui frame = GuiActionRunner.execute(() -> createMainApplication());
    window = new FrameFixture(robot(), frame.getFrame());
    window.show();
    windowHelpers = create(window);
  }

  @Override
  protected void onTearDown() {
    try {
      File ingredientFile = new File(DESTINATION_INGREDIENT_FILE_PATH);
      Files.delete(ingredientFile.toPath());
      File file = new File(DESTINATION_FILE_PATH);
      Files.delete(file.toPath());
    } catch (IOException ioex) {
      Assert.fail("One of the files used as save files does not exist");
    }
  }

  private MainGui createMainApplication() {
    try {
      IngredientProvider ingredientProvider = loadIngredientProvider(getIngredientsPath());
      JFrame mainFrame = new JFrame(BUNDLES.message("mainFrameTitle"));
      FileIoGui fileIoGui = new FileIoGui(mainFrame, useFilePath());
      MealplanerData data = new MealplanerData();
      DialogFactory dialogFactory = new DialogFactory(mainFrame);
      return new MainGui(mainFrame, data, ingredientProvider, dialogFactory, fileIoGui);
    } catch (IOException exception) {
      Assert.fail("One of the files to use as save files does not exist");
      return null;
    }
  }

  protected String getIngredientsPath() throws IOException {
    return copyFile(originIngredientFilePath, DESTINATION_INGREDIENT_FILE_PATH);
  }

  protected String useFilePath() throws IOException {
    return copyFile(originFilePath, DESTINATION_FILE_PATH);
  }

  private String copyFile(String filename, String destinationPath) throws IOException {
    File originalFile = new File(filename);
    File temporaryFile = new File(destinationPath);
    Files.copy(originalFile.toPath(), temporaryFile.toPath(), REPLACE_EXISTING);
    return temporaryFile.toPath().toString();
  }
}
