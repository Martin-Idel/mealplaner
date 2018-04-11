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

import mealplaner.commons.errorhandling.MealException;
import mealplaner.io.xml.IngredientsReader;
import mealplaner.io.xml.IngredientsWriter;
import mealplaner.io.xml.MealplanerDataReader;
import mealplaner.io.xml.MealplanerDataWriter;
import mealplaner.io.xml.MealsReader;
import mealplaner.io.xml.MealsWriter;
import mealplaner.io.xml.ProposalSummaryDataReader;
import mealplaner.io.xml.ProposalSummaryDataWriter;
import mealplaner.io.xml.ProposalSummaryModel;
import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;
import mealplaner.model.recipes.Ingredient;

public class FileIoGui {
  private final JFrame frame;
  private final String savePath;
  private static final Logger logger = LoggerFactory.getLogger(FileIoGui.class);

  public FileIoGui(JFrame frame, String savePath) {
    this.frame = frame;
    this.savePath = savePath;
  }

  public MealplanerData loadDatabase() {
    MealplanerData mealPlan = MealplanerData.getInstance();
    try {
      mealPlan.clear();
      List<Ingredient> ingredients = IngredientsReader.loadXml(savePath + "ingredients.xml");
      mealPlan.setIngredients(ingredients);
      List<Meal> meals = MealsReader.loadXml(mealPlan, savePath + "meals.xml");
      mealPlan.setMeals(meals);
      ProposalSummaryModel proposalData = ProposalSummaryDataReader.loadXml(mealPlan,
          savePath + "save.xml");
      mealPlan.setDefaultSettings(proposalData.defaultSettings);
      mealPlan.setLastProposal(proposalData.lastProposal);
      mealPlan.setTime(proposalData.time);
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
      MealplanerData mealPlan = MealplanerData.getInstance();
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

  // TODO: use function above
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
