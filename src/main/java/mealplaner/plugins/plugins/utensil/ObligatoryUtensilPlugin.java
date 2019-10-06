package mealplaner.plugins.plugins.utensil;

import java.util.Optional;
import java.util.ResourceBundle;

import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.PluginDescription;
import mealplaner.plugins.plugins.utensil.mealextension.MealEditObligatoryUtensil;
import mealplaner.plugins.plugins.utensil.mealextension.MealInputObligatoryUtensil;
import mealplaner.plugins.plugins.utensil.mealextension.ObligatoryUtensilFact;
import mealplaner.plugins.plugins.utensil.proposal.UtensilProposalStep;
import mealplaner.plugins.plugins.utensil.settingextension.CasseroleSubSetting;
import mealplaner.plugins.plugins.utensil.settingextension.SettingsCasseroleSetting;

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
  public Optional<ResourceBundle> getMessageBundle() {
    return Optional.empty();
  }

  @Override
  public Optional<ResourceBundle> getErrorBundle() {
    return Optional.empty();
  }
}