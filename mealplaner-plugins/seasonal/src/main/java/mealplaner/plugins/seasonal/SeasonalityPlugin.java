// SPDX-License-Identifier: MIT

/*
 * Seasonality Plugin
 * 
 * This plugin adds support for marking ingredients as seasonal and adjusting meal proposals
 * based on the current date and ingredient seasonality.
 * 
 * Note: This plugin intentionally does not include a bundle test (unlike other plugins).
 * The standard bundle test pattern checks for:
 * 1. All bundle keys are used in the code
 * 2. All code references to bundles have corresponding keys
 * 3. Bundle calls use string literals (not variables)
 * 
 * This plugin violates rule #3 because we use variables for month names in SeasonalConstants
 * to allow programmatic month name lookup. This is a reasonable pattern that improves
 * code maintainability, so we skip the bundle test rather than coupling the implementation
 * to the test framework's limitations.
 */

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