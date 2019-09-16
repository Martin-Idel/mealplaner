package mealplaner;

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
  private MealExtensions registeredMealExtensions = new MealExtensions();
  private Map<
      Class<? extends IngredientFact>,
      Class<? extends IngredientFactXml>>
      registeredIngredientExtensions = new HashMap<>();
  private Map<Class<? extends Setting>, Class<? extends SettingXml>>
      registeredSettingExtensions = new HashMap<>();
  private Map<
      Class<? extends MealInputDialogExtension>,
      Class<? extends DatabaseEditExtension>>
      registeredMealGuiExtensions = new HashMap<>();
  private Map<
      Class<? extends IngredientInputDialogExtension>,
      Class<? extends IngredientEditExtension>>
      registeredIngredientGuiExtensions = new HashMap<>();
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
    if (registeredMealExtensions.containsMealFact(mealFact)) {
      throw new MealException("Class name already known or plugin already registered");
    }
    registeredMealExtensions.registerClass(mealFact, mealFactXml, mealFactFactory);
  }

  public void registerIngredientExtension(
      Class<? extends IngredientFact> ingredientFact,
      Class<? extends IngredientFactXml> ingredientFactXmls) {
    if (registeredIngredientExtensions.containsKey(ingredientFact)) {
      throw new MealException("Class name already known or plugin already registered");
    }
    registeredIngredientExtensions.putIfAbsent(ingredientFact, ingredientFactXmls);
  }

  public void registerSettingExtension(
      Class<? extends Setting> setting,
      Class<? extends SettingXml> settingXml) {
    if (registeredSettingExtensions.containsKey(setting)) {
      throw new MealException("Class name already known or plugin already registered");
    }
    registeredSettingExtensions.putIfAbsent(setting, settingXml);
  }

  public void registerMealGuiExtension(
      Class<? extends MealInputDialogExtension> mealInputDialogExtension,
      Class<? extends DatabaseEditExtension> databaseEditExtension) {
    if (registeredMealGuiExtensions.containsKey(mealInputDialogExtension)) {
      throw new MealException("Class name already known or plugin already registered");
    }
    registeredMealGuiExtensions.putIfAbsent(mealInputDialogExtension, databaseEditExtension);
  }

  public void registerIngredientGuiExtension(
      Class<? extends IngredientInputDialogExtension> ingredientInputDialogExtension,
      Class<? extends IngredientEditExtension> ingredientEditExtension) {
    if (registeredIngredientGuiExtensions.containsKey(ingredientInputDialogExtension)) {
      throw new MealException("Class name already known or plugin already registered");
    }
    registeredIngredientGuiExtensions.putIfAbsent(ingredientInputDialogExtension, ingredientEditExtension);
  }

  public void registerProposalExtension(
      Class<? extends SettingsInputDialogExtension> settingsInputDialogExtension,
      Class<? extends ProposalBuilderStep> proposalBuilderStep) {
    if (registeredProposalExtensions.containsKey(settingsInputDialogExtension)) {
      throw new MealException("Class name already known or plugin already registered");
    }
    registeredProposalExtensions.putIfAbsent(settingsInputDialogExtension, proposalBuilderStep);
  }

  public MealExtensions getRegisteredMealExtensions() {
    return registeredMealExtensions;
  }

  public Map<Class<? extends IngredientFact>, Class<? extends IngredientFactXml>>
      getRegisteredIngredientExtensions() {
    return registeredIngredientExtensions;
  }

  public Map<Class<? extends Setting>, Class<? extends SettingXml>>
      getRegisteredSettingExtensions() {
    return registeredSettingExtensions;
  }

  public Map<Class<? extends MealInputDialogExtension>, Class<? extends DatabaseEditExtension>>
      getRegisteredMealGuiExtensions() {
    return registeredMealGuiExtensions;
  }

  public Map<
      Class<? extends IngredientInputDialogExtension>,
      Class<? extends IngredientEditExtension>>
      getRegisteredIngredientGuiExtensions() {
    return registeredIngredientGuiExtensions;
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
    knownClasses.addAll(registeredIngredientExtensions.keySet());
    knownClasses.addAll(registeredIngredientExtensions.values());
    knownClasses.addAll(registeredSettingExtensions.keySet());
    knownClasses.addAll(registeredSettingExtensions.values());
    var knownClassesArray = new Class<?>[knownClasses.size()];
    return knownClasses.toArray(knownClassesArray);
  }
}
