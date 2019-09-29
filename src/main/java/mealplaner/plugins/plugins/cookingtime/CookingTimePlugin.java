package mealplaner.plugins.plugins.cookingtime;

import static mealplaner.plugins.plugins.cookingtime.CookingTime.MEDIUM;

import java.util.Optional;
import java.util.ResourceBundle;

import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.PluginDescription;

public class CookingTimePlugin implements PluginDescription {
  @Override
  public void registerPlugins(PluginStore pluginStore) {
    pluginStore.registerMealExtension(
        CookingTimeFact.class, CookingTimeFact.class, () -> new CookingTimeFact(MEDIUM));
    pluginStore.registerSettingExtension(
        CookingTimeSetting.class, CookingTimeSettingXml.class, CookingTimeSetting::defaultCookingTime);
    pluginStore.registerMealGuiExtension(new MealInputCookingTime(), new MealEditCookingTime());
    pluginStore.registerProposalExtension(new SettingsCookingTimeSetting(), new CookingTimeProposalStep());
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
