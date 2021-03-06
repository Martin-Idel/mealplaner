// SPDX-License-Identifier: MIT

package mealplaner.gui.tabbedpanes.ingredientsedit;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.HelpPanel.mealPlanerHelpScrollPane;
import static mealplaner.commons.gui.JMenuBuilder.builder;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mealplaner.gui.MainContainer;
import mealplaner.ioapi.FileIoInterface;
import mealplaner.model.MealplanerData;
import mealplaner.plugins.PluginStore;

public class IngredientsEditPanel {
  private final MealplanerData mealPlan;
  private final JFrame frame;
  private final FileIoInterface fileIo;
  private IngredientsEdit ingredientsEdit;

  public IngredientsEditPanel(MealplanerData mealPlan,
      JFrame frame, FileIoInterface fileIo) {
    this.mealPlan = mealPlan;
    this.frame = frame;
    this.fileIo = fileIo;
  }

  public void addElements(MainContainer container, PluginStore pluginStore) {
    container.addTabbedPane(BUNDLES.message("ingredientsPanelName"), setupIngredientsPanel(pluginStore));
    container.addToHelpMenu(helpIngredientsMenu());
  }

  private JPanel setupIngredientsPanel(PluginStore pluginStore) {
    JPanel databasePanel = new JPanel();
    ingredientsEdit = new IngredientsEdit(this.mealPlan, frame, databasePanel,
        fileIo);
    ingredientsEdit.setupPane(pluginStore);
    return databasePanel;
  }

  public void saveDatabase() {
    ingredientsEdit.saveIngredients();
  }

  private JMenuItem helpIngredientsMenu() {
    return builder("HelpDataBase").addLabelText(BUNDLES.message("menuHelpIngredients"))
        .addMnemonic(BUNDLES.message("menuHelpIngredientsMnemonic"))
        .addActionListener(action -> JOptionPane.showMessageDialog(frame,
            mealPlanerHelpScrollPane("IngredientsEditHelp"),
            BUNDLES.message("menuHelp"), JOptionPane.PLAIN_MESSAGE))
        .build();
  }

}
