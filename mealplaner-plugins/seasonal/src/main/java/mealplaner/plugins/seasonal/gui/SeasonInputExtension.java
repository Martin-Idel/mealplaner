package mealplaner.plugins.seasonal.gui;

import static java.util.Arrays.asList;
import static mealplaner.commons.BundleStore.BUNDLES;

import java.awt.Color;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mealplaner.commons.gui.GridPanel;
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
        Seasonality.class,
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
    Set<String> newMainSeason = new HashSet<>(fact.getMainSeasonMonths());
    Set<String> newOffSeason = new HashSet<>(fact.getOffSeasonMonths());
    String[] months = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
        "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};

    JPanel panel = GridPanel.gridPanel(4, 3).getComponent();
    for (String month : months) {
      JButton button = new JButton(BUNDLES.message(month.toLowerCase(Locale.ENGLISH)));
      updateButtonColor(button, newMainSeason, newOffSeason, month);
      button.addActionListener(e -> {
        if (newMainSeason.contains(month)) {
          newMainSeason.remove(month);
        } else if (newOffSeason.contains(month)) {
          newOffSeason.remove(month);
          newMainSeason.add(month);
        } else {
          newOffSeason.add(month);
        }
        updateButtonColor(button, newMainSeason, newOffSeason, month);
      });
      panel.add(button);
    }

    String legend = "Red: Main season, Yellow: Off season, White: No season";
    panel.add(new javax.swing.JLabel(legend));

    Object[] options = {"OK", "Cancel"};
    int result = JOptionPane.showOptionDialog(null,
        panel,
        BUNDLES.message("seasonEditDialogTitle"),
        JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.PLAIN_MESSAGE,
        null,
        options,
        options[0]);

    if (result == JOptionPane.OK_OPTION) {
      return new SeasonalityFact(fact.getSeasonality(), newMainSeason, newOffSeason);
    }
    return fact;
  }

  private void updateButtonColor(JButton button, Set<String> mainSeason,
      Set<String> offSeason, String month) {
    if (mainSeason.contains(month)) {
      button.setBackground(Color.RED);
    } else if (offSeason.contains(month)) {
      button.setBackground(Color.YELLOW);
    } else {
      button.setBackground(Color.WHITE);
    }
  }
}