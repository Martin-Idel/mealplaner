package mealplaner.plugins.vegetarian.ingredientextension;

import static mealplaner.commons.BundleStore.BUNDLES;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import mealplaner.commons.BundleStore;
import mealplaner.commons.gui.GuiComponent;

public class SeasonsInputTable implements GuiComponent {
  private JTable table;
  private int rowClicked = 0;
  private int columnClicked = 0;
  private Map<Month, AvailabilityLevel> data = new HashMap<>();

  public static SeasonsInputTable setupTable(SeasonalFact oldInput) {
    return new SeasonsInputTable().setup(oldInput);
  }

  private SeasonsInputTable setup(SeasonalFact oldInput) {
    Arrays.asList(Month.values())
        .forEach(month -> data.put(month,
            oldInput.inSeason(month)
                ? (oldInput.inHighSeason(month) ? AvailabilityLevel.FULLY : AvailabilityLevel.PARTIALLY)
                : AvailabilityLevel.UNAVAILABLE));
    table = new JTable();
    table.getTableHeader().setUI(null);
    return this;
  }

  @Override
  public Component getComponent() {
    return table;
  }

  public enum AvailabilityLevel {
    UNAVAILABLE, PARTIALLY, FULLY
  }

  public class ColorCodingMouseListener extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent arg0) {
      super.mouseClicked(arg0);
      rowClicked = table.getSelectedRow();
      columnClicked = table.getSelectedColumn();
      table.repaint();
    }
  }

  public class ColorCodingCell extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      var label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      var currentColour = label.getBackground();
      label.setToolTipText(BUNDLES.message("seasonsTooltip"));
      if (row == rowClicked && column == columnClicked) {
        setColor(label, currentColour);
      }
      return label;
    }

    private void setColor(JLabel label, Color currentColour) {
      if (Color.WHITE.equals(currentColour)) {
        label.setBackground(Color.GREEN);
      } else if (Color.GREEN.equals(label.getBackground())) {
        label.setBackground(Color.YELLOW);
      } else {
        label.setBackground(Color.WHITE);
      }
    }
  }
}
