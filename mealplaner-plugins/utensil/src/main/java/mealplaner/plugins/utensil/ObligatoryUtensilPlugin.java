// SPDX-License-Identifier: MIT

package mealplaner.plugins.utensil;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.PluginDescription;
import mealplaner.plugins.utensil.mealextension.MealEditObligatoryUtensil;
import mealplaner.plugins.utensil.mealextension.MealInputObligatoryUtensil;
import mealplaner.plugins.utensil.mealextension.ObligatoryUtensilFact;
import mealplaner.plugins.utensil.proposal.UtensilProposalStep;
import mealplaner.plugins.utensil.settingextension.CasseroleSubSetting;
import mealplaner.plugins.utensil.settingextension.SettingsCasseroleSetting;

public class ObligatoryUtensilPlugin implements PluginDescription {
  @Override
  public void registerPlugins(PluginStore pluginStore) {
    pluginStore.registerMealExtension(
        ObligatoryUtensilFact.class, ObligatoryUtensilFact.class, ObligatoryUtensilFact::new);
    pluginStore.registerSettingExtension(
        CasseroleSubSetting.class, CasseroleSubSetting.class, CasseroleSubSetting::new);
    pluginStore.registerMealGuiExtension(new MealInputObligatoryUtensil(), new MealEditObligatoryUtensil());
    pluginStore.registerProposalExtension(new SettingsCasseroleSetting(), new UtensilProposalStep());
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
