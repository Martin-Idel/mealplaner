// SPDX-License-Identifier: MIT

package mealplaner.plugins.recipephoto;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import mealplaner.commons.BundleUtils;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.PluginDescription;
import mealplaner.plugins.recipephoto.mealedit.MealEditRecipePhoto;
import mealplaner.plugins.recipephoto.mealedit.MealInputRecipePhoto;
import mealplaner.plugins.recipephoto.mealedit.RecipePhotoFact;

public class RecipePhotoPlugin implements PluginDescription {
  @Override
  public void registerPlugins(PluginStore pluginStore) {
    pluginStore.registerMealExtension(RecipePhotoFact.class, RecipePhotoFact.class, RecipePhotoFact::new);
    pluginStore.registerMealGuiExtension(new MealInputRecipePhoto(), new MealEditRecipePhoto());
  }

  @Override
  public Optional<ResourceBundle> getMessageBundle(Locale locale) {
    return Optional.of(BundleUtils.loadBundle("RecipePhotoMessagesBundle", locale));
  }

  @Override
  public Optional<ResourceBundle> getErrorBundle(Locale locale) {
    return Optional.of(BundleUtils.loadBundle("RecipePhotoErrorBundle", locale));
  }
}
