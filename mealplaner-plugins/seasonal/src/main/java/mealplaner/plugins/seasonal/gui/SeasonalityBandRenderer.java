package mealplaner.plugins.seasonal.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import mealplaner.plugins.seasonal.ingredientextension.Seasonality;
import mealplaner.plugins.seasonal.ingredientextension.SeasonalityFact;

public class SeasonalityBandRenderer extends DefaultTableCellRenderer {
  private static final long serialVersionUID = 1L;
  private static final int NUM_MONTHS = 12;
  private static final int BAND_HEIGHT = 16;
  private static final int BAND_WIDTH_PER_MONTH = 18;
  private static final String[] MONTHS = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
      "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};

  @Override
  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    SeasonalityFact fact = (SeasonalityFact) value;
    Color tableBackground = isSelected ? table.getSelectionBackground() : table.getBackground();

    SeasonalityBandPanel panel = new SeasonalityBandPanel(fact, tableBackground);
    panel.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
    return panel;
  }

  private static class SeasonalityBandPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final transient SeasonalityFact fact;
    private final transient Color tableBackground;

    SeasonalityBandPanel(SeasonalityFact fact, Color tableBackground) {
      this.fact = fact;
      this.tableBackground = tableBackground;
      setPreferredSize(new Dimension(BAND_WIDTH_PER_MONTH * NUM_MONTHS, BAND_HEIGHT));
      setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);

      if (fact.getSeasonality() == Seasonality.NON_SEASONAL) {
        g.setColor(tableBackground);
        g.fillRect(0, 0, getWidth(), getHeight());
        return;
      }

      for (int i = 0; i < NUM_MONTHS; i++) {
        String month = MONTHS[i];
        Color color;

        if (fact.getMainSeasonMonths().contains(month)) {
          color = new Color(220, 80, 80);
        } else if (fact.getOffSeasonMonths().contains(month)) {
          color = new Color(240, 220, 100);
        } else {
          color = Color.LIGHT_GRAY;
        }

        g.setColor(color);
        g.fillRect(i * BAND_WIDTH_PER_MONTH, 0, BAND_WIDTH_PER_MONTH - 1, BAND_HEIGHT);
      }
    }
  }
}