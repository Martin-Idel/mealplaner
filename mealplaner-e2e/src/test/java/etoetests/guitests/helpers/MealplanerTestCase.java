// SPDX-License-Identifier: MIT

package etoetests.guitests.helpers;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static mealplaner.commons.BundleStore.BUNDLES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import etoetests.CommonFunctions;
import etoetests.guitests.pageobjects.GuiMethods;
import mealplaner.gui.MainGui;
import mealplaner.gui.factories.DialogFactory;
import mealplaner.io.FileIo;
import mealplaner.model.MealplanerData;
import mealplaner.plugins.PluginStore;
import mealplaner.proposal.ProposalBuilderFactoryImpl;

public class MealplanerTestCase {
  protected GuiMethods windowHelpers;
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

  protected MealplanerTestCase() {
    this("src/test/resources/meals.xml",
        "src/test/resources/save.xml",
        "src/test/resources/ingredients.xml");
  }

  protected MealplanerTestCase(String originMealFilePath, String originMealplanerFilePath,
                               String originIngredientFilePath) {
    this.originMealsFilePath = originMealFilePath;
    this.originMealplanerFilePath = originMealplanerFilePath;
    this.originIngredientFilePath = originIngredientFilePath;
  }

  @BeforeEach
  protected void onSetUp() throws Exception {
    SwingUtilities.invokeAndWait(() -> createMainApplication());
    SwingUtilities.invokeLater(() -> mainFrame.setVisible(true));
    waitForFrameVisible();
    windowHelpers = GuiMethods.create(mainFrame);
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

  protected void waitForFrameVisible() throws Exception {
    waitForCondition(() -> {
      Boolean[] visible = new Boolean[1];
      try {
        SwingUtilities.invokeAndWait(() -> visible[0] = mainFrame.isVisible());
      } catch (Exception e) {
        return false;
      }
      return visible[0];
    }, 5000, "Frame did not become visible");
  }

  protected void waitForCondition(java.util.function.BooleanSupplier condition, long timeoutMillis, String errorMessage) throws Exception {
    long startTime = System.currentTimeMillis();
    while (System.currentTimeMillis() - startTime < timeoutMillis) {
      if (condition.getAsBoolean()) {
        return;
      }
      Thread.sleep(50);
    }
    fail(errorMessage);
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

  protected <T extends Component> T findComponentByName(JFrame frame, String name, Class<T> type) throws Exception {
    Component component = findComponentByName(frame, name);
    if (!type.isInstance(component)) {
      throw new AssertionError("Component " + name + " is not of type " + type.getSimpleName() +
                             " but " + component.getClass().getSimpleName());
    }
    return type.cast(component);
  }

  protected <T extends Component> T findComponentByName(JFrame frame, String name, Class<T> type, long timeoutMillis) throws Exception {
    final Component[] result = new Component[1];
    waitForCondition(() -> {
      try {
        SwingUtilities.invokeAndWait(() -> {
          result[0] = findComponentByName(frame.getContentPane(), name);
        });
        return result[0] != null;
      } catch (Exception e) {
        return false;
      }
    }, timeoutMillis, "Component not found: " + name + " within " + timeoutMillis + "ms");

    if (!type.isInstance(result[0])) {
      throw new AssertionError("Component " + name + " is not of type " + type.getSimpleName() +
                             " but " + result[0].getClass().getSimpleName());
    }
    return type.cast(result[0]);
  }

  protected void waitForButtonToBeDisabled(JFrame frame, String buttonName) throws Exception {
    final boolean[] isDisabled = new boolean[1];
    waitForCondition(() -> {
      try {
        SwingUtilities.invokeAndWait(() -> {
          JButton button = (JButton) findComponentByName(frame.getContentPane(), buttonName);
          isDisabled[0] = button != null && !button.isEnabled();
        });
        return isDisabled[0];
      } catch (Exception e) {
        return false;
      }
    }, 3000, "Button " + buttonName + " did not become disabled within 3000ms");
  }

  protected void waitForButtonToBeEnabled(JFrame frame, String buttonName) throws Exception {
    final boolean[] isEnabled = new boolean[1];
    waitForCondition(() -> {
      try {
        SwingUtilities.invokeAndWait(() -> {
          JButton button = (JButton) findComponentByName(frame.getContentPane(), buttonName);
          isEnabled[0] = button != null && button.isEnabled();
        });
        return isEnabled[0];
      } catch (Exception e) {
        return false;
      }
    }, 3000, "Button " + buttonName + " did not become enabled within 3000ms");
  }

  protected void assertButtonIsDisabled(JFrame frame, String buttonName) throws Exception {
    try {
      SwingUtilities.invokeAndWait(() -> {
        JButton button = (JButton) findComponentByName(frame.getContentPane(), buttonName);
        assertThat(button != null && !button.isEnabled())
            .withFailMessage("Button " + buttonName + " should be disabled")
            .isTrue();
      });
    } catch (Exception e) {
      throw new AssertionError("Failed to check button state: " + e.getMessage(), e);
    }
  }

  protected void assertButtonIsEnabled(JFrame frame, String buttonName) throws Exception {
    try {
      SwingUtilities.invokeAndWait(() -> {
        JButton button = (JButton) findComponentByName(frame.getContentPane(), buttonName);
        assertThat(button != null && button.isEnabled())
            .withFailMessage("Button " + buttonName + " should be enabled")
            .isTrue();
      });
    } catch (Exception e) {
      throw new AssertionError("Failed to check button state: " + e.getMessage(), e);
    }
  }

  protected void clickButtonAndWaitForDisabled(JFrame frame, String buttonName) throws Exception {
    SwingUtilities.invokeLater(() -> {
      JButton button = (JButton) findComponentByName(frame.getContentPane(), buttonName);
      if (button != null) {
        button.doClick();
      }
    });
    waitForButtonToBeDisabled(frame, buttonName);
  }

  protected void clickButtonAndWaitForDisabledThenAssert(JFrame frame, String buttonName) throws Exception {
    clickButtonAndWaitForDisabled(frame, buttonName);
    assertButtonIsDisabled(frame, buttonName);
  }

  protected void waitForFileToExist(String filePath) throws Exception {
    waitForCondition(() -> {
      File file = new File(filePath);
      return file.exists() && file.length() > 0;
    }, 5000, "File " + filePath + " did not exist within 5000ms");
  }
}
