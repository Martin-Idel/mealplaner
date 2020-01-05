package mealplaner.plugins.sidedish;

import java.util.Optional;
import java.util.ResourceBundle;

import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.PluginDescription;
import mealplaner.plugins.sidedish.mealextension.MealEditSidedish;
import mealplaner.plugins.sidedish.mealextension.MealInputSidedish;
import mealplaner.plugins.sidedish.mealextension.SidedishFact;
import mealplaner.plugins.sidedish.proposal.SideDishProposalStep;

public class SideDishPlugin implements PluginDescription {
  @Override
  public void registerPlugins(PluginStore pluginStore) {
    pluginStore.registerMealExtension(SidedishFact.class, SidedishFact.class, SidedishFact::new);
    pluginStore.registerMealGuiExtension(new MealInputSidedish(), new MealEditSidedish());
    pluginStore.registerProposalExtension(new SideDishProposalStep());
  }

  @Override
  public Optional<ResourceBundle> getMessageBundle() {
    return Optional.empty();
  }

  @Override
  public Optional<ResourceBundle> getErrorBundle() {
    return Optional.empty();
  }
}