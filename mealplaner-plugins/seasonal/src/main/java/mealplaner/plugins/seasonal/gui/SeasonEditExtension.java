// SPDX-License-Identifier: MIT

package mealplaner.plugins.seasonal.gui;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withEnumContent;
import static mealplaner.model.recipes.IngredientBuilder.from;

import java.util.List;
import javax.swing.JFrame;

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
            JFrame frame = javax.swing.JOptionPane.getRootFrame() instanceof JFrame
                ? (JFrame) javax.swing.JOptionPane.getRootFrame()
                : new JFrame();
            SeasonalityFact result = SeasonDialog.seasonDialog(frame).showDialog(fact);
            Ingredient newIngredient = from(ingredients.get(row)).changeFact(result).create();
            ingredients.set(row, newIngredient);
            buttonPanelEnabling.enableButtons();
          }
        });
  }
}