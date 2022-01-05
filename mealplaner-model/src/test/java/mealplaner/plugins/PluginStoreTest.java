// SPDX-License-Identifier: MIT

package mealplaner.plugins;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import mealplaner.commons.errorhandling.MealException;
import testcommonsmodel.HiddenMealFact;
import testcommonsmodel.TestIngredientFact;
import testcommonsmodel.TestSubSetting;

class PluginStoreTest {
  @Test
  void pluginStoreRegistrationIsPossible() {
    PluginStore store = new PluginStore();
    store.registerMealExtension(HiddenMealFact.class, HiddenMealFact.class, HiddenMealFact::new);
    store.registerSettingExtension(TestSubSetting.class, TestSubSetting.class, TestSubSetting::new);
    store.registerIngredientExtension(TestIngredientFact.class, TestIngredientFact.class, TestIngredientFact::new);

    assertThat(store.getAllKnownClassesForXmlConversion())
        .containsExactlyInAnyOrder(HiddenMealFact.class, TestSubSetting.class, TestIngredientFact.class);
    assertThat(store.getRegisteredIngredientExtensions().containsFact(TestIngredientFact.class)).isTrue();
    assertThat(store.getRegisteredMealExtensions().containsFact(HiddenMealFact.class)).isTrue();
    assertThat(store.getRegisteredSettingExtensions().containsFact(TestSubSetting.class)).isTrue();
  }

  @Test
  void pluginStoreThrowsIfClassIsRegisteredTwice() {
    PluginStore store = new PluginStore();
    store.registerMealExtension(HiddenMealFact.class, HiddenMealFact.class, HiddenMealFact::new);

    Assertions.assertThrows(MealException.class, () ->
        store.registerMealExtension(HiddenMealFact.class, HiddenMealFact.class, HiddenMealFact::new));
  }
}
