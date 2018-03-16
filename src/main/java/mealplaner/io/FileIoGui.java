package mealplaner.io;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static javax.swing.JOptionPane.showInputDialog;
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
import mealplaner.recipes.model.Ingredient;
import mealplaner.xml.IngredientsReader;
import mealplaner.xml.IngredientsWriter;
import mealplaner.xml.MealplanerDataReader;
import mealplaner.xml.MealplanerDataWriter;
import mealplaner.xml.MealsReader;
import mealplaner.xml.MealsWriter;
import mealplaner.xml.ProposalSummaryDataReader;
import mealplaner.xml.ProposalSummaryDataWriter;

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
      mealPlan = ProposalSummaryDataReader.loadXml(savePath + "save.xml");
      List<Meal> meals = MealsReader.loadXml(savePath + "meals.xml");
      List<Ingredient> ingredients = IngredientsReader.loadXml(savePath + "ingredients.xml");
      mealPlan.setMeals(meals);
      mealPlan.setIngredients(ingredients);
    } catch (MealException exc) {
      errorMessages(frame, exc, BUNDLES.errorMessage("MSG_LOAD_ERROR"));
      logger.error("Could not load database: ", exc);
    }
    return mealPlan;
  }

  public Optional<MealplanerData> loadBackup() {
    String bak = JOptionPane.showInputDialog(frame, BUNDLES.message("createLoadBackup"),
        "*.xml");
    if (bak != null) {
      MealplanerData mealPlan = new MealplanerData();
      try {
        mealPlan = MealplanerDataReader.loadXml(bak);
      } catch (MealException exc) {
        errorMessages(frame, exc, BUNDLES.errorMessage("MSG_BACKUP_NOT_LOADED"));
        logger.error("Could not load backup: ", exc);
      }
      return of(mealPlan);
    }
    return empty();
  }

  public void saveDatabase(MealplanerData mealPlan) {
    try {
      ProposalSummaryDataWriter.saveXml(mealPlan, savePath + "save.xml");
      MealsWriter.saveXml(mealPlan.getMeals(), savePath + "meals.xml");
      IngredientsWriter.saveXml(mealPlan.getIngredients(), savePath + "ingredients.xml");
    } catch (MealException exc) {
      errorMessages(frame, exc, BUNDLES.errorMessage("MSG_SAVING_ERROR"));
      logger.error("Could not save database: ", exc);
    }
  }

  public void savePart(MealplanerData mealPlan, DataParts part) {
    try {
      switch (part) {
      case INGREDIENTS:
        IngredientsWriter.saveXml(mealPlan.getIngredients(), savePath + "ingredients.xml");
        break;
      case MEALS:
        MealsWriter.saveXml(mealPlan.getMeals(), savePath + "meals.xml");
        break;
      case PROPOSAL:
        ProposalSummaryDataWriter.saveXml(mealPlan, savePath + "save.xml");
        break;
      default: // do nothing
      }
    } catch (MealException exc) {
      errorMessages(frame, exc, BUNDLES.errorMessage("MSG_SAVING_ERROR"));
      logger.error("Could not save " + part.toString() + " of database: ", exc);
    }

  }

  public void saveMeals(List<Meal> meals) {
    MealsWriter.saveXml(meals, savePath + "meals.xml");
  }

  public void createBackup(MealplanerData mealPlan) {
    String bak = showInputDialog(frame, BUNDLES.message("createLoadBackup"),
        "*.xml");
    if (bak != null) {
      try {
        MealplanerDataWriter.saveXml(mealPlan, bak);
        JOptionPane.showMessageDialog(frame, BUNDLES.message("successSave"),
            BUNDLES.message("successHeading"), JOptionPane.INFORMATION_MESSAGE);
      } catch (MealException exc) {
        errorMessages(frame, exc, BUNDLES.errorMessage("MSG_BACKUP_NOT_SAVED"));
        logger.error("Could not load backup: ", exc);
      }
    }
  }
}
