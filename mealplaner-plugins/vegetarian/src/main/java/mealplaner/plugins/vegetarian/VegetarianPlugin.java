// SPDX-License-Identifier: MIT

package mealplaner.plugins.vegetarian;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import mealplaner.commons.BundleUtils;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.PluginDescription;
import mealplaner.plugins.vegetarian.mealextension.MealEditVegetarian;
import mealplaner.plugins.vegetarian.mealextension.MealInputVegetarian;
import mealplaner.plugins.vegetarian.mealextension.VegetarianFact;
import mealplaner.plugins.vegetarian.proposal.VegetarianProposalStep;

public class VegetarianPlugin implements PluginDescription {
  @Override
  public void registerPlugins(PluginStore pluginStore) {
    pluginStore.registerMealExtension(VegetarianFact.class, VegetarianFact.class, VegetarianFact::new);
    pluginStore.registerMealGuiExtension(new MealInputVegetarian(), new MealEditVegetarian());
    pluginStore.registerProposalExtension(new VegetarianProposalStep());
  }

  @Override
  public Optional<ResourceBundle> getMessageBundle(Locale locale) {
    return Optional.of(BundleUtils.loadBundle("VegetarianMessagesBundle", locale));
  }

  @Override
  public Optional<ResourceBundle> getErrorBundle(Locale locale) {
    return Optional.empty();
  }
}
