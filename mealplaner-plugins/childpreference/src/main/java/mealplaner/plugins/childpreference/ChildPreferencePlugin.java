// SPDX-License-Identifier: MIT

package mealplaner.plugins.childpreference;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import mealplaner.commons.BundleUtils;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.PluginDescription;
import mealplaner.plugins.childpreference.mealextension.ChildPreferenceFact;
import mealplaner.plugins.childpreference.mealextension.MealEditChildPreference;
import mealplaner.plugins.childpreference.mealextension.MealInputChildPreference;
import mealplaner.plugins.childpreference.proposal.ChildPreferenceProposalStep;
import mealplaner.plugins.childpreference.settingextension.ChildPreferenceSubSetting;
import mealplaner.plugins.childpreference.settingextension.SettingsChildPreferenceSettings;

public class ChildPreferencePlugin implements PluginDescription {
  @Override
  public void registerPlugins(PluginStore pluginStore) {
    pluginStore.registerMealExtension(ChildPreferenceFact.class, ChildPreferenceFact.class,
        ChildPreferenceFact::new);
    pluginStore.registerSettingExtension(ChildPreferenceSubSetting.class,
        ChildPreferenceSubSetting.class, ChildPreferenceSubSetting::new);
    pluginStore.registerMealGuiExtension(new MealInputChildPreference(), new MealEditChildPreference());
    pluginStore.registerProposalExtension(new SettingsChildPreferenceSettings(), new ChildPreferenceProposalStep());
  }

  @Override
  public Optional<ResourceBundle> getMessageBundle(Locale locale) {
    return Optional.of(BundleUtils.loadBundle("ChildPreferenceMessagesBundle", locale));
  }

  @Override
  public Optional<ResourceBundle> getErrorBundle(Locale locale) {
    return Optional.empty();
  }
}