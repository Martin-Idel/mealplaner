package mealplaner.plugins;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.plugins.api.DatabaseEditExtension;
import mealplaner.plugins.api.IngredientEditExtension;
import mealplaner.plugins.api.IngredientFact;
import mealplaner.plugins.api.IngredientFactXml;
import mealplaner.plugins.api.IngredientInputDialogExtension;
import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealFactXml;
import mealplaner.plugins.api.MealInputDialogExtension;
import mealplaner.plugins.api.ProposalBuilderStep;
import mealplaner.plugins.api.Setting;
import mealplaner.plugins.api.SettingXml;
import mealplaner.plugins.api.SettingsInputDialogExtension;

public class PluginStore {
  private ModelExtension<MealFact, MealFactXml> registeredMealExtensions = new ModelExtension<>();
  private ModelExtension<IngredientFact, IngredientFactXml> registeredIngredientExtensions = new ModelExtension<>();
  private ModelExtension<Setting, SettingXml> registeredSettingExtensions = new ModelExtension<>();
  private GuiExtension<MealInputDialogExtension, DatabaseEditExtension> registeredMealGuiExtensions
      = new GuiExtension<>();
  private GuiExtension<IngredientInputDialogExtension, IngredientEditExtension> registeredIngredientGuiExtensions
      = new GuiExtension<>();
  private Map<
      Class<? extends SettingsInputDialogExtension>,
      Class<? extends ProposalBuilderStep>>
      registeredProposalExtensions = new HashMap<>();

  public PluginStore() {
  }

  public <T extends MealFact> void registerMealExtension(
      Class<T> mealFact,
      Class<? extends MealFactXml> mealFactXml,
      Supplier<T> mealFactFactory) {
    if (registeredMealExtensions.containsFact(mealFact)) {
      throw new MealException("Class name " + mealFact + " already known or plugin already registered");
    }
    registeredMealExtensions.registerClass(mealFact, mealFactXml, mealFactFactory);
  }

  public <T extends IngredientFact> void registerIngredientExtension(
      Class<T> ingredientFact,
      Class<? extends IngredientFactXml> ingredientFactXmls,
      Supplier<T> ingredientFactory) {
    if (registeredIngredientExtensions.containsFact(ingredientFact)) {
      throw new MealException("Class name " + ingredientFact + " already known or plugin already registered");
    }
    registeredIngredientExtensions.registerClass(ingredientFact, ingredientFactXmls, ingredientFactory);
  }

  public <T extends Setting> void registerSettingExtension(
      Class<T> setting,
      Class<? extends SettingXml> settingXml,
      Supplier<T> settingFactory) {
    if (registeredSettingExtensions.containsFact(setting)) {
      throw new MealException("Class name " + setting + " already known or plugin already registered");
    }
    registeredSettingExtensions.registerClass(setting, settingXml, settingFactory);
  }

  public void registerMealGuiExtension(
      MealInputDialogExtension mealInputDialogExtension,
      DatabaseEditExtension databaseEditExtension) {
    registeredMealGuiExtensions.registerGuiExtension(
        mealInputDialogExtension.getClass(), mealInputDialogExtension,
        databaseEditExtension.getClass(), databaseEditExtension);
  }

  public void registerIngredientGuiExtension(
      IngredientInputDialogExtension ingredientInputDialogExtension,
      IngredientEditExtension ingredientEditExtension) {
    registeredIngredientGuiExtensions.registerGuiExtension(
        ingredientInputDialogExtension.getClass(), ingredientInputDialogExtension,
        ingredientEditExtension.getClass(), ingredientEditExtension
    );
  }

  public void registerProposalExtension(
      Class<? extends SettingsInputDialogExtension> settingsInputDialogExtension,
      Class<? extends ProposalBuilderStep> proposalBuilderStep) {
    if (registeredProposalExtensions.containsKey(settingsInputDialogExtension)) {
      throw new MealException("Class name already known or plugin already registered");
    }
    registeredProposalExtensions.putIfAbsent(settingsInputDialogExtension, proposalBuilderStep);
  }

  public ModelExtension<MealFact, MealFactXml> getRegisteredMealExtensions() {
    return registeredMealExtensions;
  }

  public ModelExtension<IngredientFact, IngredientFactXml> getRegisteredIngredientExtensions() {
    return registeredIngredientExtensions;
  }

  public ModelExtension<Setting, SettingXml> getRegisteredSettingExtensions() {
    return registeredSettingExtensions;
  }

  public Collection<MealInputDialogExtension> getRegisteredMealInputGuiExtensions() {
    return registeredMealGuiExtensions.getInputExtensions();
  }

  public Collection<DatabaseEditExtension> getRegisteredMealEditGuiExtensions() {
    return registeredMealGuiExtensions.getEditExtensions();
  }

  public Collection<IngredientInputDialogExtension> getRegisteredIngredientInputGuiExtensions() {
    return registeredIngredientGuiExtensions.getInputExtensions();
  }

  public Collection<IngredientEditExtension> getRegisteredIngredientEditGuiExtensions() {
    return registeredIngredientGuiExtensions.getEditExtensions();
  }

  public Map<
      Class<? extends SettingsInputDialogExtension>,
      Class<? extends ProposalBuilderStep>>
      getRegisteredProposalExtensions() {
    return registeredProposalExtensions;
  }

  public Class<?>[] getAllKnownClassesForXmlConversion() {
    Set<Class<?>> knownClasses = new HashSet<>();
    knownClasses.addAll(registeredMealExtensions.getAllRegisteredFacts());
    knownClasses.addAll(registeredMealExtensions.getAllRegisteredFactXmls());
    knownClasses.addAll(registeredIngredientExtensions.getAllRegisteredFacts());
    knownClasses.addAll(registeredIngredientExtensions.getAllRegisteredFactXmls());
    knownClasses.addAll(registeredSettingExtensions.getAllRegisteredFacts());
    knownClasses.addAll(registeredSettingExtensions.getAllRegisteredFactXmls());
    var knownClassesArray = new Class<?>[knownClasses.size()];
    return knownClasses.toArray(knownClassesArray);
  }
}
