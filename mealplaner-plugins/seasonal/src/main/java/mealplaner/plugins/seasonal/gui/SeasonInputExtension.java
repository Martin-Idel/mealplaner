// SPDX-License-Identifier: MIT

package mealplaner.plugins.seasonal.gui;

import static java.util.Arrays.asList;
import static mealplaner.commons.BundleStore.BUNDLES;

import java.util.HashSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import mealplaner.commons.gui.GuiPanel;
import mealplaner.commons.gui.inputfields.InputField;
import mealplaner.plugins.api.IngredientFact;
import mealplaner.plugins.api.IngredientInputDialogExtension;
import mealplaner.plugins.seasonal.ingredientextension.Seasonality;
import mealplaner.plugins.seasonal.ingredientextension.SeasonalityFact;

public class SeasonInputExtension implements IngredientInputDialogExtension {
  private SeasonalityFact seasonalityFact;
  private SeasonalityComboBoxWithHook seasonalityComboBox;
  private JButton editMonthsButton;

  @Override
  public java.util.List<InputField<IngredientFact>> createInputElements() {
    resetState();
    seasonalityComboBox = createSeasonalityComboBox();
    return asList(
        new InputField<>() {
          @Override
          public void addToPanel(GuiPanel panel) {
            seasonalityComboBox.addToPanel(panel);
          }

          @Override
          public IngredientFact getUserInput() {
            seasonalityFact.setSeasonality(seasonalityComboBox.getUserInput());
            return seasonalityFact;
          }

          @Override
          public void resetField() {
            seasonalityComboBox.resetField();
            resetState();
            updateButtonState();
          }

          @Override
          public int getOrdering() {
            return seasonalityComboBox.getOrdering();
          }
        },
        createSeasonMonthsButton());
  }

  public void resetState() {
    seasonalityFact = new SeasonalityFact(Seasonality.NON_SEASONAL, new HashSet<>(), new HashSet<>());
  }

  private SeasonalityComboBoxWithHook createSeasonalityComboBox() {
    SeasonalityComboBoxWithHook comboBox = new SeasonalityComboBoxWithHook(
        BUNDLES.message("insertSeasonality"),
        "Seasonality",
        Seasonality.NON_SEASONAL,
        4);
    comboBox.setOnChange(v -> updateButtonState());
    return comboBox;
  }

  private InputField<IngredientFact> createSeasonMonthsButton() {
    String addButtonLabel = BUNDLES.message("addEditButton");
    return new InputField<>() {
      @Override
      public void addToPanel(GuiPanel panel) {
        editMonthsButton = new JButton(addButtonLabel);
        editMonthsButton.setName("InputFieldButtonSeasonMonths");
        editMonthsButton.addActionListener(action -> {
          seasonalityFact.setSeasonality(seasonalityComboBox.getUserInput());
          if (seasonalityFact.getSeasonality() != Seasonality.NON_SEASONAL) {
            SeasonalityFact dialogResult = showSeasonDialog(seasonalityFact);
            seasonalityFact.setMainSeasonMonths(dialogResult.getMainSeasonMonths());
            seasonalityFact.setOffSeasonMonths(dialogResult.getOffSeasonMonths());
          }
        });
        panel.getComponent().add(new JLabel(BUNDLES.message("editSeasonalityMonths")));
        panel.getComponent().add(editMonthsButton);
      }

      @Override
      public IngredientFact getUserInput() {
        return seasonalityFact;
      }

      @Override
      public void resetField() {
        updateButtonState();
      }

      @Override
      public int getOrdering() {
        return 5;
      }
    };
  }

  private void updateButtonState() {
    if (editMonthsButton == null) {
      return;
    }
    Seasonality currentSeasonality = seasonalityComboBox.getUserInput();
    editMonthsButton.setEnabled(currentSeasonality != Seasonality.NON_SEASONAL);
  }

  private SeasonalityFact showSeasonDialog(SeasonalityFact fact) {
    JFrame frame = javax.swing.JOptionPane.getRootFrame() instanceof JFrame
        ? (JFrame) javax.swing.JOptionPane.getRootFrame()
        : new JFrame();
    return SeasonDialog.seasonDialog(frame).showDialog(fact);
  }
}
