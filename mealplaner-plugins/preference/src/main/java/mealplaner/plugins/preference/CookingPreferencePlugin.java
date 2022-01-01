// SPDX-License-Identifier: MIT

package mealplaner.plugins.preference;

import static mealplaner.plugins.preference.mealextension.CookingPreference.NO_PREFERENCE;
import static mealplaner.plugins.preference.proposal.PreferenceMap.getPreferenceMap;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import mealplaner.commons.BundleUtils;
import mealplaner.commons.Pair;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.PluginDescription;
import mealplaner.plugins.preference.mealextension.CookingPreference;
import mealplaner.plugins.preference.mealextension.CookingPreferenceFact;
import mealplaner.plugins.preference.mealextension.MealEditCookingPreference;
import mealplaner.plugins.preference.mealextension.MealInputCookingPreference;
import mealplaner.plugins.preference.proposal.CookingPreferenceProposalStep;
import mealplaner.plugins.preference.settingextension.CookingPreferenceSubSetting;
import mealplaner.plugins.preference.settingextension.PreferenceSettings;
import mealplaner.plugins.preference.settingextension.SettingsCookingPreferenceSettings;

public class CookingPreferencePlugin implements PluginDescription {
  private Map<Pair<CookingPreference, PreferenceSettings>, Integer> preferenceMap;

  public CookingPreferencePlugin() {
  }

  public CookingPreferencePlugin(Map<Pair<CookingPreference, PreferenceSettings>, Integer> preferenceMap) {
    this.preferenceMap = new HashMap<>(preferenceMap);
  }

  @Override
  public void registerPlugins(PluginStore pluginStore) {
    this.preferenceMap = getPreferenceMap();
    pluginStore.registerMealExtension(
        CookingPreferenceFact.class,
        CookingPreferenceFact.class,
        () -> new CookingPreferenceFact(NO_PREFERENCE));
    pluginStore.registerSettingExtension(
        CookingPreferenceSubSetting.class,
        CookingPreferenceSubSetting.class,
        CookingPreferenceSubSetting::new);
    pluginStore.registerMealGuiExtension(
        new MealInputCookingPreference(),
        new MealEditCookingPreference());
    pluginStore.registerProposalExtension(
        new SettingsCookingPreferenceSettings(),
        new CookingPreferenceProposalStep(preferenceMap));
  }

  @Override
  public Optional<ResourceBundle> getMessageBundle(Locale locale) {
    return Optional.of(BundleUtils.loadBundle("CookingPreferenceMessagesBundle", locale));
  }

  @Override
  public Optional<ResourceBundle> getErrorBundle(Locale locale) {
    return Optional.empty();
  }
}
