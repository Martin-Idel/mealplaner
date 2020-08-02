package mealplaner.plugins.vegetarian.ingredientextension;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.GridPanel.gridPanel;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.commons.gui.dialogs.DialogWindow.window;
import static mealplaner.model.recipes.Recipe.createRecipe;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import mealplaner.commons.gui.GridPanel;
import mealplaner.commons.gui.buttonpanel.ButtonPanel;
import mealplaner.commons.gui.dialogs.DialogWindow;
import mealplaner.commons.gui.inputfields.CheckboxInputField;
import mealplaner.commons.gui.inputfields.InputField;
import mealplaner.gui.dialogs.DialogEditing;
import mealplaner.gui.dialogs.recepies.RecipeTable;
import mealplaner.model.DataStore;
import mealplaner.plugins.PluginStore;

public class SeasonsInput implements DialogEditing<SeasonalFact> {

  private final DialogWindow dialogWindow;
  private InputField<Boolean> strongSeasonalAffinity;
  private SeasonalFact seasonalFact;

  SeasonsInput(JDialog parentDialog) {
    dialogWindow = window(parentDialog, BUNDLES.message("seasonInputDialogTitle"),
        "SeasonInputDialog");
  }

  @Override
  public SeasonalFact showDialog(SeasonalFact toEdit, DataStore data, PluginStore pluginStore) {
    seasonalFact = toEdit;
    dialogWindow.dispose();
    return seasonalFact;
  }


  private void display(SeasonalFact toEdit, DataStore store, PluginStore pluginStore) {
    strongSeasonalAffinity = new CheckboxInputField(
        BUNDLES.message("strongSeasonalAffinity"), "StrongSeasonalAffinity",1);
    GridPanel inputFieldPanel = gridPanel(1, 2);
    strongSeasonalAffinity.addToPanel(inputFieldPanel);

    // Need to have a real "Table" here
    recipeTable = new RecipeTable(recipe.orElse(createRecipe()), store.getIngredients());
    table = recipeTable.setupTable();

    ButtonPanel buttonPanel = displayButtons(store, pluginStore);

    dialogWindow.addNorth(inputFieldPanel);
    dialogWindow.addCentral(table);
    dialogWindow.addSouth(buttonPanel);
    dialogWindow.arrangeWithSize(400, 300);
    dialogWindow.setVisible();
  }

  private ButtonPanel displayButtons(DataStore store, PluginStore pluginStore) {
    return builder("RecipeInput")
        .addCancelDialogButton(dialogWindow)
        .addOkButton(getSaveListener())
        .build();
  }

  private ActionListener getSaveListener() {
    return action -> {
      seasonalFact = new SeasonalFact(strongSeasonalAffinity.getUserInput(), table.getContent());
      dialogWindow.dispose();
    };
  }
}
