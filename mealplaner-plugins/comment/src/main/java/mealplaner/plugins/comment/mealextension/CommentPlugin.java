// SPDX-License-Identifier: MIT

package mealplaner.plugins.comment.mealextension;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.PluginDescription;

public class CommentPlugin implements PluginDescription {
  @Override
  public void registerPlugins(PluginStore pluginStore) {
    pluginStore.registerMealExtension(CommentFact.class, CommentFact.class, CommentFact::new);
    pluginStore.registerMealGuiExtension(new MealInputComment(), new MealEditComment());
  }

  @Override
  public Optional<ResourceBundle> getMessageBundle(Locale locale) {
    return Optional.empty();
  }

  @Override
  public Optional<ResourceBundle> getErrorBundle(Locale locale) {
    return Optional.empty();
  }
}
