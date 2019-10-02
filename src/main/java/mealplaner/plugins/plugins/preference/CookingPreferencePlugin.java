package mealplaner.plugins.plugins.preference;

import static mealplaner.plugins.plugins.preference.mealextension.CookingPreference.NO_PREFERENCE;
import static mealplaner.plugins.plugins.preference.proposal.PreferenceMap.getPreferenceMap;

import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import mealplaner.commons.Pair;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.PluginDescription;
import mealplaner.plugins.plugins.preference.mealextension.CookingPreference;
import mealplaner.plugins.plugins.preference.mealextension.CookingPreferenceFact;
import mealplaner.plugins.plugins.preference.mealextension.MealEditCookingPreference;
import mealplaner.plugins.plugins.preference.mealextension.MealInputCookingPreference;
import mealplaner.plugins.plugins.preference.proposal.CookingPreferenceProposalStep;
import mealplaner.plugins.plugins.preference.setting.CookingPreferenceSetting;
import mealplaner.plugins.plugins.preference.setting.PreferenceSettings;
import mealplaner.plugins.plugins.preference.setting.SettingsCookingPreferenceSettings;

public class CookingPreferencePlugin implements PluginDescription {
  private final Map<Pair<CookingPreference, PreferenceSettings>, Integer> preferenceMap;

  public CookingPreferencePlugin() {
    this.preferenceMap = getPreferenceMap();
  }

  public CookingPreferencePlugin(Map<Pair<CookingPreference, PreferenceSettings>, Integer> preferenceMap) {
    this.preferenceMap = preferenceMap;
  }

  @Override
  public void registerPlugins(PluginStore pluginStore) {
    pluginStore.registerMealExtension(
        CookingPreferenceFact.class,
        CookingPreferenceFact.class,
        () -> new CookingPreferenceFact(NO_PREFERENCE));
    pluginStore.registerSettingExtension(
        CookingPreferenceSetting.class,
        CookingPreferenceSetting.class,
        CookingPreferenceSetting::new);
    pluginStore.registerMealGuiExtension(
        new MealInputCookingPreference(),
        new MealEditCookingPreference());
    pluginStore.registerProposalExtension(
        new SettingsCookingPreferenceSettings(),
        new CookingPreferenceProposalStep(preferenceMap));
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
