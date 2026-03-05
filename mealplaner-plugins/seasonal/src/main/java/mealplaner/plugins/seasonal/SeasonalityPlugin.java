// SPDX-License-Identifier: MIT

package mealplaner.plugins.seasonal;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import mealplaner.commons.BundleUtils;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.PluginDescription;
import mealplaner.plugins.seasonal.gui.SeasonEditExtension;
import mealplaner.plugins.seasonal.gui.SeasonInputExtension;
import mealplaner.plugins.seasonal.ingredientextension.SeasonalityFact;
import mealplaner.plugins.seasonal.proposal.SeasonProposalStep;

public class SeasonalityPlugin implements PluginDescription {

  @Override
  public void registerPlugins(PluginStore pluginStore) {
    pluginStore.registerIngredientExtension(
        SeasonalityFact.class, SeasonalityFact.class, SeasonalityFact::new);
    pluginStore.registerIngredientGuiExtension(
        new SeasonInputExtension(),
        new SeasonEditExtension());
    pluginStore.registerProposalExtension(new SeasonProposalStep());
  }

  @Override
  public Optional<ResourceBundle> getMessageBundle(Locale locale) {
    return Optional.of(BundleUtils.loadBundle("SeasonalityMessagesBundle", locale));
  }

  @Override
  public Optional<ResourceBundle> getErrorBundle(Locale locale) {
    return Optional.empty();
  }
}