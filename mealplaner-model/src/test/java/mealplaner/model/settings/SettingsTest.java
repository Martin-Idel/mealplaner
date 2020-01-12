// SPDX-License-Identifier: MIT

package mealplaner.model.settings;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class SettingsTest {
  @Test
  public void equalsContract() {
    EqualsVerifier.forClass(Settings.class)
        .suppress(Warning.NULL_FIELDS)
        .verify();
  }
}
