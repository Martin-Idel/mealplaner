// SPDX-License-Identifier: MIT

package etoetests.xmlsmoketests;

import org.junit.jupiter.api.BeforeEach;

import etoetests.CommonFunctions;
import mealplaner.plugins.PluginStore;
import testcommons.XmlInteraction;

public class EtoeXmlInteraction extends XmlInteraction {
  protected PluginStore pluginStore;

  @BeforeEach
  public void setUp() {
    pluginStore = CommonFunctions.registerPlugins();
  }
}
