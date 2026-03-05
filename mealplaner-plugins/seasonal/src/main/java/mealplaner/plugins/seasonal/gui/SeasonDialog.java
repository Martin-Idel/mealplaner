// SPDX-License-Identifier: MIT

package mealplaner.plugins.seasonal.gui;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.commons.gui.dialogs.DialogWindow.window;

import javax.swing.JButton;
import javax.swing.JFrame;

import mealplaner.commons.gui.GridPanel;
import mealplaner.commons.gui.buttonpanel.ButtonPanel;
import mealplaner.commons.gui.dialogs.DialogWindow;
import mealplaner.plugins.seasonal.ingredientextension.SeasonalityFact;
import mealplaner.plugins.seasonal.util.SeasonalConstants;

public class SeasonDialog {
  private final DialogWindow dialogWindow;
  private final String[] months = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
      "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
  private java.util.Set<String> mainSeasonMonths;
  private java.util.Set<String> offSeasonMonths;
  private boolean confirmed = false;

  private SeasonDialog(JFrame parentFrame) {
    dialogWindow = window(parentFrame, BUNDLES.message("seasonEditDialogTitle"), "SeasonDialog");
  }

  public static SeasonDialog seasonDialog(JFrame parentFrame) {
    return new SeasonDialog(parentFrame);
  }

  public SeasonalityFact showDialog(SeasonalityFact fact) {
    mainSeasonMonths = new java.util.HashSet<>(fact.getMainSeasonMonths());
    offSeasonMonths = new java.util.HashSet<>(fact.getOffSeasonMonths());
    confirmed = false;
    display();
    if (confirmed) {
      return new SeasonalityFact(fact.getSeasonality(), mainSeasonMonths, offSeasonMonths);
    }
    return fact;
  }

  private void display() {
    var gridPanel = GridPanel.gridPanel(4, 3);
    for (int i = 0; i < 12; i++) {
      String monthKey = months[i];
      JButton button = new JButton(SeasonalConstants.getMonthDisplayName(i));
      updateButtonColor(button, monthKey);
      final int monthIndex = i;
      button.addActionListener(e -> toggleMonth(monthIndex, button));
      gridPanel.add(button);
    }

    gridPanel.add(new javax.swing.JLabel(SeasonalConstants.getSeasonalLegend()));
    dialogWindow.addCentral(gridPanel);

    ButtonPanel buttonPanel = displayButtons();
    dialogWindow.addSouth(buttonPanel);

    dialogWindow.arrangeWithSize(500, 200);
    dialogWindow.setVisible();
  }

  private void toggleMonth(int monthIndex, JButton button) {
    String month = months[monthIndex];
    if (mainSeasonMonths.contains(month)) {
      mainSeasonMonths.remove(month);
    } else if (offSeasonMonths.contains(month)) {
      offSeasonMonths.remove(month);
      mainSeasonMonths.add(month);
    } else {
      offSeasonMonths.add(month);
    }
    updateButtonColor(button, month);
  }

  private void updateButtonColor(JButton button, String month) {
    if (mainSeasonMonths.contains(month)) {
      button.setBackground(SeasonalConstants.getMonthColorForMainSeason());
    } else if (offSeasonMonths.contains(month)) {
      button.setBackground(SeasonalConstants.getMonthColorForOffSeason());
    } else {
      button.setBackground(SeasonalConstants.getMonthColorForNoSeason());
    }
  }

  private ButtonPanel displayButtons() {
    return builder("SeasonDialog")
        .addOkButton(pressed -> {
          confirmed = true;
          dialogWindow.dispose();
        })
        .addCancelButton(pressed -> {
          dialogWindow.dispose();
        })
        .build();
  }
}