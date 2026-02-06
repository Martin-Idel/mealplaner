// SPDX-License-Identifier: MIT

package etoetests.guitests.helpers;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static mealplaner.commons.BundleStore.BUNDLES;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import etoetests.CommonFunctions;
import etoetests.guitests.pageobjects.GuiMethodsNative;
import mealplaner.gui.MainGui;
import mealplaner.gui.factories.DialogFactory;
import mealplaner.io.FileIo;
import mealplaner.model.MealplanerData;
import mealplaner.plugins.PluginStore;
import mealplaner.proposal.ProposalBuilderFactoryImpl;

public class NonAssertJMealplanerTestCase {
  protected GuiMethodsNative windowHelpersNative;
  protected JFrame mainFrame;
  private static final String WORKING_DIRECTORY = "src/test/resources/temp/";
  private static final String SAVEFILES_DIRECTORY = WORKING_DIRECTORY + "savefiles/";
  private static final String DESTINATION_MEALS_FILE_PATH = SAVEFILES_DIRECTORY + "meals.xml";
  private static final String DESTINATION_MEALPLANER_FILE_PATH = SAVEFILES_DIRECTORY
      + "save.xml";
  protected static final String DESTINATION_INGREDIENT_FILE_PATH = SAVEFILES_DIRECTORY
      + "ingredients.xml";
  private final String originMealsFilePath;
  private final String originMealplanerFilePath;
  private final String originIngredientFilePath;

  protected NonAssertJMealplanerTestCase() {
    this("src/test/resources/meals.xml",
        "src/test/resources/save.xml",
        "src/test/resources/ingredients.xml");
  }

  protected NonAssertJMealplanerTestCase(String originMealFilePath, String originMealplanerFilePath,
                                      String originIngredientFilePath) {
    this.originMealsFilePath = originMealFilePath;
    this.originMealplanerFilePath = originMealplanerFilePath;
    this.originIngredientFilePath = originIngredientFilePath;
  }

  @BeforeEach
  protected void onSetUp() throws Exception {
    SwingUtilities.invokeAndWait(() -> createMainApplication());
    SwingUtilities.invokeLater(() -> mainFrame.setVisible(true));
    Thread.sleep(200);
    windowHelpersNative = GuiMethodsNative.create(mainFrame);
  }

  @AfterEach
  protected void onTearDown() {
    try {
      SwingUtilities.invokeAndWait(() -> {
        if (mainFrame != null) {
          mainFrame.dispose();
        }
      });
      File mealsFile = new File(DESTINATION_MEALS_FILE_PATH);
      Files.delete(mealsFile.toPath());
      File saveFile = new File(DESTINATION_MEALPLANER_FILE_PATH);
      Files.delete(saveFile.toPath());
      File ingredientFile = new File(DESTINATION_INGREDIENT_FILE_PATH);
      Files.delete(ingredientFile.toPath());
    } catch (Exception ex) {
      fail("One of the files used as save files does not exist");
    }
  }

  private MainGui createMainApplication() {
    try {
      JFrame frame = new JFrame(BUNDLES.message("mainFrameTitle"));
      PluginStore pluginStore = CommonFunctions.registerPlugins();
      FileIo fileIo = new FileIo(frame, useFilePath(), pluginStore);
      MealplanerData data = fileIo.loadDatabase(pluginStore);
      DialogFactory dialogFactory = new DialogFactory(frame);
      mainFrame = frame;
      return new MainGui(frame, data, dialogFactory, fileIo, new ProposalBuilderFactoryImpl(), pluginStore);
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

  protected Component findComponentByName(JFrame frame, String name) throws Exception {
    final Component[] result = new Component[1];
    SwingUtilities.invokeAndWait(() -> {
      result[0] = findComponentByName(frame.getContentPane(), name);
    });
    return result[0];
  }

  private Component findComponentByName(Component parent, String name) {
    if (name.equals(parent.getName())) {
      return parent;
    }
    if (parent instanceof Container) {
      Component[] components = ((Container) parent).getComponents();
      for (Component component : components) {
        Component result = findComponentByName(component, name);
        if (result != null) {
          return result;
        }
      }
    }
    return null;
  }
}