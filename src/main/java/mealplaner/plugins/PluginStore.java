package mealplaner.plugins;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import mealplaner.plugins.api.IngredientEditExtension;
import mealplaner.plugins.api.IngredientFact;
import mealplaner.plugins.api.IngredientFactXml;
import mealplaner.plugins.api.IngredientInputDialogExtension;
import mealplaner.plugins.api.MealEditExtension;
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
  private GuiExtension<MealInputDialogExtension, MealEditExtension> registeredMealGuiExtensions
      = new GuiExtension<>();
  private GuiExtension<IngredientInputDialogExtension, IngredientEditExtension> registeredIngredientGuiExtensions
      = new GuiExtension<>();
  private GuiExtension<SettingsInputDialogExtension, ProposalBuilderStep> registeredProposalExtensions
      = new GuiExtension<>();

  public PluginStore() {
  }

  public <T extends MealFact> void registerMealExtension(
      Class<T> mealFact,
      Class<? extends MealFactXml> mealFactXml,
      Supplier<T> mealFactFactory) {
    registeredMealExtensions.registerClass(mealFact, mealFactXml, mealFactFactory);
  }

  public <T extends IngredientFact> void registerIngredientExtension(
      Class<T> ingredientFact,
      Class<? extends IngredientFactXml> ingredientFactXmls,
      Supplier<T> ingredientFactory) {
    registeredIngredientExtensions.registerClass(ingredientFact, ingredientFactXmls, ingredientFactory);
  }

  public <T extends Setting> void registerSettingExtension(
      Class<T> setting,
      Class<? extends SettingXml> settingXml,
      Supplier<T> settingFactory) {
    registeredSettingExtensions.registerClass(setting, settingXml, settingFactory);
  }

  public void registerMealGuiExtension(
      MealInputDialogExtension mealInputDialogExtension,
      MealEditExtension mealEditExtension) {
    registeredMealGuiExtensions.registerGuiExtension(
        mealInputDialogExtension.getClass(), mealInputDialogExtension,
        mealEditExtension.getClass(), mealEditExtension);
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
      SettingsInputDialogExtension settingsInputDialogExtension,
      ProposalBuilderStep proposalBuilderStep) {
    registeredProposalExtensions.registerGuiExtension(
        settingsInputDialogExtension.getClass(), settingsInputDialogExtension,
        proposalBuilderStep.getClass(), proposalBuilderStep
    );
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

  public Collection<MealEditExtension> getRegisteredMealEditGuiExtensions() {
    return registeredMealGuiExtensions.getEditExtensions();
  }

  public Collection<IngredientInputDialogExtension> getRegisteredIngredientInputGuiExtensions() {
    return registeredIngredientGuiExtensions.getInputExtensions();
  }

  public Collection<IngredientEditExtension> getRegisteredIngredientEditGuiExtensions() {
    return registeredIngredientGuiExtensions.getEditExtensions();
  }

  public Collection<SettingsInputDialogExtension> getRegisteredSettingsInputGuiExtensions() {
    return registeredProposalExtensions.getInputExtensions();
  }

  public Collection<ProposalBuilderStep> getRegisteredProposalBuilderSteps() {
    return registeredProposalExtensions.getEditExtensions();
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
