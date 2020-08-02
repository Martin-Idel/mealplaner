// SPDX-License-Identifier: MIT

package mealplaner.plugins.vegetarian;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import mealplaner.commons.BundleUtils;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.PluginDescription;
import mealplaner.plugins.vegetarian.ingredientextension.SeasonalFact;

public class SeasonalPlugin implements PluginDescription {
  @Override
  public void registerPlugins(PluginStore pluginStore) {
    pluginStore.registerIngredientExtension(SeasonalFact.class, SeasonalFact.class, SeasonalFact::new);
    // pluginStore.registerMealGuiExtension(new MealInputVegetarian(), new MealEditVegetarian());
    // pluginStore.registerProposalExtension(new VegetarianProposalStep());
  }

  @Override
  public Optional<ResourceBundle> getMessageBundle(Locale locale) {
    return Optional.of(BundleUtils.loadBundle("SeasonalMessagesBundle", locale));
  }

  @Override
  public Optional<ResourceBundle> getErrorBundle(Locale locale) {
    return Optional.empty();
  }
}
