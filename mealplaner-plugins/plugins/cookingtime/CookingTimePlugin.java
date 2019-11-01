package mealplaner.plugins.plugins.cookingtime;

import static mealplaner.plugins.plugins.cookingtime.mealextension.CookingTime.MEDIUM;

import java.util.Optional;
import java.util.ResourceBundle;

import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.PluginDescription;
import mealplaner.plugins.plugins.cookingtime.mealextension.CookingTimeFact;
import mealplaner.plugins.plugins.cookingtime.mealextension.MealEditCookingTime;
import mealplaner.plugins.plugins.cookingtime.mealextension.MealInputCookingTime;
import mealplaner.plugins.plugins.cookingtime.proposal.CookingTimeProposalStep;
import mealplaner.plugins.plugins.cookingtime.settingextension.CookingTimeSubSetting;
import mealplaner.plugins.plugins.cookingtime.settingextension.CookingTimeSubSettingXml;
import mealplaner.plugins.plugins.cookingtime.settingextension.SettingsCookingTimeSetting;

public class CookingTimePlugin implements PluginDescription {
  @Override
  public void registerPlugins(PluginStore pluginStore) {
    pluginStore.registerMealExtension(
        CookingTimeFact.class, CookingTimeFact.class, () -> new CookingTimeFact(MEDIUM));
    pluginStore.registerSettingExtension(
        CookingTimeSubSetting.class, CookingTimeSubSettingXml.class, CookingTimeSubSetting::defaultCookingTime);
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
