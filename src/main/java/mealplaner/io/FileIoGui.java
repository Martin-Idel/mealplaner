package mealplaner.io;

import static java.util.Optional.empty;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.MessageDialog.errorMessages;

import java.util.List;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mealplaner.MealplanerData;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.Meal;
import mealplaner.xml.MealplanerDataReader;
import mealplaner.xml.MealplanerDataWriter;
import mealplaner.xml.MealsReader;
import mealplaner.xml.MealsWriter;

public class FileIoGui {
  private final JFrame frame;
  private final String savePath;
  private static final Logger logger = LoggerFactory.getLogger(FileIoGui.class);

  public FileIoGui(JFrame frame, String savePath) {
    this.frame = frame;
    this.savePath = savePath;
  }

  public MealplanerData loadDatabase() {
    MealplanerData mealPlan = new MealplanerData();
    try {
      mealPlan = MealplanerDataReader.loadXml(savePath + "save.xml");
      List<Meal> meals = MealsReader.loadXml(savePath + "meals.xml");
      mealPlan.setMeals(meals);
    } catch (MealException exc) {
      errorMessages(frame, exc, BUNDLES.errorMessage("MSG_CLASS_NOT_FOUND"));
      logger.error("Something went wrong when loading meals and mealplaner data in: ", exc);
    }
    return mealPlan;
  }

  // TODO: Implement correct backup loading
  public Optional<MealplanerData> loadBackup() {
    // String bak = showInputDialog(frame, BUNDLES.message("createLoadBackup"),
    // "*.xml");
    // if (bak != null) {
    // MealplanerData mealPlan = new MealplanerData();
    // try {
    // mealPlan = MealplanerFileLoader.load(bak);
    // } catch (FileNotFoundException exc) {
    // errorMessages(frame, exc, BUNDLES.errorMessage("MSG_BKU_FILE_NOT_FOUND"));
    // logger.error("File not found in: ", exc);
    // } catch (IOException exc) {
    // errorMessages(frame, exc, BUNDLES.errorMessage("MSG_IOEX"));
    // logger.error("I/O Exception in: ", exc);
    // } catch (MealException exc) {
    // errorMessages(frame, exc, BUNDLES.errorMessage("MSG_BKU_CLASS_NOT_FOUND"));
    // logger.error("MealException in: ", exc);
    // }
    // return of(mealPlan);
    // }
    return empty();
  }

  // TODO: Catch exceptions only here
  public void saveDatabase(MealplanerData mealPlan) {
    MealplanerDataWriter.saveXml(mealPlan, savePath + "save.xml");
    MealsWriter.saveXml(mealPlan.getMeals(), savePath + "meals.xml");
    JOptionPane.showMessageDialog(frame, BUNDLES.message("successSave"),
        BUNDLES.message("successHeading"), JOptionPane.INFORMATION_MESSAGE);
  }

  // TODO: Correct backup creation
  public void createBackup(MealplanerData mealPlan) {
    // String bak = showInputDialog(frame, BUNDLES.message("createLoadBackup"),
    // "*.xml");
    // if (bak != null) {
    // try {
    // MealplanerFileSaver.save(mealPlan, bak);
    // JOptionPane.showMessageDialog(frame, BUNDLES.message("successSave"),
    // BUNDLES.message("successHeading"), JOptionPane.INFORMATION_MESSAGE);
    // } catch (IOException exc) {
    // errorMessages(frame, exc, BUNDLES.errorMessage("MSG_IOEX"));
    // logger.error("I/O Exception in: ", exc);
    // }
    // }
  }
}
