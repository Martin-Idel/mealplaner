package guittests.helpers;

import static guittests.helpers.GuiMethods.create;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.io.IngredientProviderIoGui.loadIngredientProvider;

import javax.swing.JFrame;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;

import mealplaner.MealplanerData;
import mealplaner.gui.MainGui;
import mealplaner.gui.factories.DialogFactory;
import mealplaner.recipes.provider.IngredientProvider;

public class AssertJMealplanerTestCase extends AssertJSwingJUnitTestCase {
  protected FrameFixture window;
  protected GuiMethods windowHelpers;

  @Override
  protected void onSetUp() {
    MainGui frame = GuiActionRunner.execute(() -> createMainApplication());
    window = new FrameFixture(robot(), frame.getFrame());
    window.show();
    windowHelpers = create(window);
  }

  private MainGui createMainApplication() {
    MealplanerData data = new MealplanerData();
    IngredientProvider ingredientProvider = loadIngredientProvider();
    JFrame mainFrame = new JFrame(BUNDLES.message("mainFrameTitle"));
    DialogFactory dialogFactory = new DialogFactory(mainFrame);
    return new MainGui(mainFrame, data, dialogFactory, ingredientProvider);
  }

}
