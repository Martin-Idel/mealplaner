// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.ingredients;

import static java.util.stream.Collectors.toMap;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.commons.gui.dialogs.DialogWindow.window;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.JDialog;
import javax.swing.JFrame;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.commons.Pair;
import mealplaner.commons.gui.MessageDialog;
import mealplaner.commons.gui.buttonpanel.ButtonPanel;
import mealplaner.commons.gui.dialogs.DialogWindow;
import mealplaner.gui.dialogs.DialogEditing;
import mealplaner.model.DataStore;
import mealplaner.model.recipes.Measure;
import mealplaner.plugins.PluginStore;

public class MeasureInputDialog implements DialogEditing<Map<Measure, NonnegativeFraction>> {
  private final DialogWindow dialogWindow;
  private Measure primaryMeasure;
  private Map<Measure, NonnegativeFraction> oldMeasures;
  private List<Pair<Measure, NonnegativeFraction>> tableMeasures;

  private MeasureInputDialog(JFrame parentFrame, Measure primaryMeasure) {
    dialogWindow = window(parentFrame, BUNDLES.message("measureInputDialogTitle"), "MeasureInputDialog");
    this.primaryMeasure = primaryMeasure;
  }

  private MeasureInputDialog(JDialog parentDialog, Measure primaryMeasure) {
    dialogWindow = window(parentDialog, BUNDLES.message("measureInputDialogTitle"), "MeasureInputDialog");
    this.primaryMeasure = primaryMeasure;
  }

  public static MeasureInputDialog measureInput(JFrame parentFrame, Measure primaryMeasure) {
    return new MeasureInputDialog(parentFrame, primaryMeasure);
  }

  public static MeasureInputDialog measureInput(JDialog parentDialog, Measure primaryMeasure) {
    return new MeasureInputDialog(parentDialog, primaryMeasure);
  }

  @Override
  public Map<Measure, NonnegativeFraction> showDialog(
      Map<Measure, NonnegativeFraction> toEdit, DataStore data, PluginStore pluginStore) {
    display(toEdit);
    dialogWindow.dispose();
    return tableMeasures
        .stream()
        .collect(toMap(
            pair -> pair.left,
            pair -> pair.right,
            (l, r) -> l,
            () -> new EnumMap<>(Measure.class)));
  }

  private void display(Map<Measure, NonnegativeFraction> toEdit) {
    oldMeasures = toEdit;
    tableMeasures = toList(toEdit);
    var table = MeasureTable.setupTable(tableMeasures, primaryMeasure);
    dialogWindow.addCentral(table);
    dialogWindow.arrangeWithSize(400, 300);
    dialogWindow.addSouth(displayButtons());
    dialogWindow.setVisible();
  }

  private ButtonPanel displayButtons() {
    return builder("MeasuresInput")
        .addCancelButton(action -> {
          tableMeasures = toList(oldMeasures);
          dialogWindow.dispose();
        })
        .addOkButton(action -> {
          if (validate()) {
            dialogWindow.dispose();
          } else {
            MessageDialog.errorMessages(dialogWindow,
                BUNDLES.message("measuresNotZeroConversion"));
          }
        })
        .build();
  }

  private List<Pair<Measure, NonnegativeFraction>> toList(Map<Measure, NonnegativeFraction> toEdit) {
    return toEdit
        .entrySet()
        .stream()
        .filter(entry -> !entry.getKey().equals(primaryMeasure))
        .map(entry -> Pair.of(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }

  private boolean validate() {
    return tableMeasures.stream()
        .noneMatch(pair -> NonnegativeFraction.ZERO.equals(pair.right));
  }
}
