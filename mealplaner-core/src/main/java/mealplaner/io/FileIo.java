// SPDX-License-Identifier: MIT

package mealplaner.io;

import static java.util.Optional.empty;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.MessageDialog.errorMessages;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.io.xml.IngredientsReader;
import mealplaner.io.xml.IngredientsWriter;
import mealplaner.io.xml.MealsReader;
import mealplaner.io.xml.MealsWriter;
import mealplaner.io.xml.ProposalSummaryDataReader;
import mealplaner.io.xml.ProposalSummaryDataWriter;
import mealplaner.io.xml.ProposalSummaryModel;
import mealplaner.ioapi.DataParts;
import mealplaner.ioapi.FileIoInterface;
import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;
import mealplaner.model.recipes.Ingredient;
import mealplaner.plugins.PluginStore;

public class FileIo implements FileIoInterface {
  private static final String INGREDIENT_FILE = "ingredients.xml";
  private static final String MEAL_FILE = "meals.xml";
  private static final String SAVE_FILE = "save.xml";

  private final JFrame frame;
  private final String basePath;
  private final String regularSavePath;
  private final PluginStore knownPlugins;
  private static final Logger logger = LoggerFactory.getLogger(FileIo.class);

  public FileIo(JFrame frame, String basePath, PluginStore knownPlugins) {
    this.frame = frame;
    this.basePath = basePath;
    this.knownPlugins = knownPlugins;
    this.regularSavePath = basePath + File.separator + "savefiles" + File.separator;
  }

  @Override
  public MealplanerData loadDatabase(PluginStore pluginStore) {
    return load(pluginStore, regularSavePath);
  }

  @Override
  public Optional<MealplanerData> loadBackup(PluginStore pluginStore) {
    JFileChooser chooser = new JFileChooser(basePath);
    FileNameExtensionFilter filter = new FileNameExtensionFilter("XML documents", "xml");
    chooser.setFileFilter(filter);
    int returnVal = chooser.showOpenDialog(frame);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      var bak = chooser.getSelectedFile();
      load(pluginStore, bak.getParent() + File.separator);
    }
    return empty();
  }

  @Override
  public void saveDatabase(MealplanerData mealPlan) {
    save(mealPlan, regularSavePath);
  }

  @Override
  public void createBackup(MealplanerData mealPlan) {
    var timestamp = new SimpleDateFormat("yyyyMMdd", BUNDLES.locale()).format(new Date());
    save(mealPlan, basePath + File.separator + timestamp + File.separator);
    JOptionPane.showMessageDialog(
        frame,
        BUNDLES.message("successSave"),
        BUNDLES.message("successHeading"),
        JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void savePart(MealplanerData mealPlan, DataParts part) {
    createDirectoryIfMissing(regularSavePath);
    try {
      switch (part) {
        case INGREDIENTS:
          IngredientsWriter.saveXml(mealPlan.getIngredients(), regularSavePath + INGREDIENT_FILE, knownPlugins);
          break;
        case MEALS:
          MealsWriter.saveXml(mealPlan.getMeals(), regularSavePath + MEAL_FILE, knownPlugins);
          break;
        case PROPOSAL:
          ProposalSummaryDataWriter.saveXml(mealPlan, regularSavePath + SAVE_FILE, knownPlugins);
          break;
        default: // do nothing
      }
    } catch (MealException exc) {
      errorMessages(frame, BUNDLES.errorMessage("MSG_SAVING_ERROR"));
      logger.error("Could not save {} of database: {}", part, exc);
    }
  }

  private void save(MealplanerData mealPlan, String savePath) {
    createDirectoryIfMissing(savePath);
    try {
      ProposalSummaryDataWriter.saveXml(mealPlan, savePath + SAVE_FILE, knownPlugins);
      MealsWriter.saveXml(mealPlan.getMeals(), savePath + MEAL_FILE, knownPlugins);
      IngredientsWriter.saveXml(mealPlan.getIngredients(), savePath + INGREDIENT_FILE, knownPlugins);
    } catch (MealException exc) {
      errorMessages(frame, BUNDLES.errorMessage("MSG_SAVING_ERROR"));
      logger.error("Could not save database: ", exc);
    }
  }

  private void createDirectoryIfMissing(String regularSavePath) {
    if (!new File(regularSavePath).exists()) {
      boolean created = new File(regularSavePath).mkdirs();
      if (!created) {
        errorMessages(frame, BUNDLES.errorMessage("MSG_SAVING_ERROR"));
        logger.error("Could not create savegame folder and folder does not exist");
      }
    }
  }

  private MealplanerData load(PluginStore pluginStore, String savePath) {
    MealplanerData mealPlan = MealplanerData.getInstance(pluginStore);
    try {
      mealPlan.clear();
      List<Ingredient> ingredients = IngredientsReader.loadXml(savePath + INGREDIENT_FILE, knownPlugins);
      mealPlan.setIngredients(ingredients);
      List<Meal> meals = MealsReader.loadXml(mealPlan, savePath + MEAL_FILE, knownPlugins);
      mealPlan.setMeals(meals);
      ProposalSummaryModel proposalData = ProposalSummaryDataReader.loadXml(
          savePath + SAVE_FILE, knownPlugins);
      mealPlan.setDefaultSettings(proposalData.defaultSettings);
      mealPlan.setLastProposal(proposalData.lastProposal);
      mealPlan.setTime(proposalData.time);
    } catch (MealException exc) {
      errorMessages(frame, BUNDLES.errorMessage("MSG_LOAD_ERROR"));
      logger.error("Could not load database: ", exc);
    }
    return mealPlan;
  }
}
