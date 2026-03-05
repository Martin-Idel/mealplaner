package mealplaner.plugins.seasonal.gui;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withEnumContent;
import static mealplaner.model.recipes.IngredientBuilder.from;

import java.awt.Color;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mealplaner.commons.gui.GridPanel;
import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.model.recipes.Ingredient;
import mealplaner.plugins.api.IngredientEditExtension;
import mealplaner.plugins.seasonal.ingredientextension.Seasonality;
import mealplaner.plugins.seasonal.ingredientextension.SeasonalityFact;

public class SeasonEditExtension implements IngredientEditExtension {

  @Override
  public FlexibleTableBuilder addTableColumns(
      FlexibleTableBuilder table,
      List<Ingredient> ingredients,
      mealplaner.commons.gui.buttonpanel.ButtonPanelEnabling buttonPanelEnabling) {
    FlexibleTableBuilder tableBuilder = table.addColumn(withEnumContent(Seasonality.class)
        .withColumnName(BUNDLES.message("seasonalityColumn"))
        .getValueFromOrderedList(
            ingredients,
            ingredient -> ingredient.getTypedIngredientFact(SeasonalityFact.class).getSeasonality())
        .setValueToOrderedImmutableList(ingredients,
            (oldIngredient, newSeasonality) -> {
              SeasonalityFact fact = oldIngredient.getTypedIngredientFact(SeasonalityFact.class);
              return from(oldIngredient).changeFact(new SeasonalityFact(
                  newSeasonality,
                  fact.getMainSeasonMonths(),
                  fact.getOffSeasonMonths())).create();
            })
        .isEditable()
        .onChange(buttonPanelEnabling::enableButtons)
        .buildWithOrderNumber(90));

    return tableBuilder.addColumn(mealplaner.commons.gui.tables.TableColumnBuilder.withContent(SeasonalityFact.class)
        .withColumnName(BUNDLES.message("editSeasonalityButton"))
        .getRowValueFromUnderlyingModel(
            row -> ingredients.get(row).getTypedIngredientFact(SeasonalityFact.class))
        .overwriteTableCellRenderer(new SeasonalityBandRenderer())
        .buildWithOrderNumber(91))
        .addListenerToThisColumn(row -> {
          SeasonalityFact fact = ingredients.get(row).getTypedIngredientFact(SeasonalityFact.class);
          if (fact.getSeasonality() != Seasonality.NON_SEASONAL) {
            SeasonalityFact result = showSeasonDialog(fact);
            Ingredient newIngredient = from(ingredients.get(row)).changeFact(result).create();
            ingredients.set(row, newIngredient);
            buttonPanelEnabling.enableButtons();
          }
        });
  }

  private SeasonalityFact showSeasonDialog(SeasonalityFact fact) {
    Set<String> newMainSeason = new java.util.HashSet<>(fact.getMainSeasonMonths());
    Set<String> newOffSeason = new java.util.HashSet<>(fact.getOffSeasonMonths());
    String[] months = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
        "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};

    JPanel panel = GridPanel.gridPanel(4, 3).getComponent();
    for (String month : months) {
      JButton button = new JButton(BUNDLES.message(month.toLowerCase(Locale.ENGLISH)));
      final String monthLoop = month;
      updateButtonColor(button, newMainSeason, newOffSeason, monthLoop);
      button.addActionListener(e -> {
        if (newMainSeason.contains(monthLoop)) {
          newMainSeason.remove(monthLoop);
        } else if (newOffSeason.contains(monthLoop)) {
          newOffSeason.remove(monthLoop);
          newMainSeason.add(monthLoop);
        } else {
          newOffSeason.add(monthLoop);
        }
        updateButtonColor(button, newMainSeason, newOffSeason, monthLoop);
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