package mealplaner.plugins;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import mealplaner.commons.errorhandling.MealException;

public class GuiExtension<ExtensionInputT, ExtensionEditT> {
  private Map<Class<? extends ExtensionInputT>, ExtensionInputT> inputClasses;
  private Map<Class<? extends ExtensionEditT>, ExtensionEditT> editClasses;

  public GuiExtension() {
    inputClasses = new HashMap<>();
    editClasses = new HashMap<>();
  }

  public void registerGuiExtension(
      Class<? extends ExtensionInputT> inputDialogExtensionType, ExtensionInputT inputDialogExtension,
      Class<? extends ExtensionEditT> editDialogExtensionType, ExtensionEditT editDialogExtension) {
    if (inputClasses.containsKey(inputDialogExtensionType)) {
      throw new MealException(inputDialogExtensionType + " is already registered.");
    }
    if (editClasses.containsKey(editDialogExtensionType)) {
      throw new MealException(editDialogExtensionType + " is already registered.");
    }
    inputClasses.putIfAbsent(inputDialogExtensionType, inputDialogExtension);
    editClasses.putIfAbsent(editDialogExtensionType, editDialogExtension);
  }

  public Collection<ExtensionInputT> getInputExtensions() {
    return inputClasses.values();
  }

  public Collection<ExtensionEditT> getEditExtensions() {
    return editClasses.values();
  }

}
