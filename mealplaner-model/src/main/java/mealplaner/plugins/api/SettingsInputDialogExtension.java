// SPDX-License-Identifier: MIT

package mealplaner.plugins.api;

import java.util.List;

import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.model.settings.Settings;

public interface SettingsInputDialogExtension {
  FlexibleTableBuilder addTableColumns(FlexibleTableBuilder table, List<Settings> settings);
}
