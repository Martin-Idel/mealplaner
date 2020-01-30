// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.ingredients;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.NonnegativeFraction.ONE;
import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withNonnegativeFractionContent;
import static mealplaner.model.recipes.Measure.GRAM;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.commons.Pair;
import mealplaner.commons.gui.tables.Table;
import mealplaner.model.recipes.Measure;

public final class MeasureTable {

  private MeasureTable() {
  }

  public static Table setupTable(List<Pair<Measure, NonnegativeFraction>> measures, Measure primaryMeasure) {
    return createNewTable()
        .withRowCount(measures::size)
        .addColumn(withContent(Measure.class)
            .isEditable()
            .withColumnName(BUNDLES.message("secondaryMeasureNameColumn"))
            .setValueToOrderedImmutableList(measures, (pair, measure) -> Pair.of(measure, pair.right))
            .getValueFromOrderedList(measures, pair -> pair.left)
            .setPreferredSize(50)
            .overwriteTableCellEditor(new DefaultCellEditor(
                new JComboBox<>(new Vector<>(removePrimary(primaryMeasure)))))
            .build())
        .addColumn(withNonnegativeFractionContent()
            .isEditable()
            .withColumnName(BUNDLES.message("secondaryMeasureConversionFactorColumn"))
            .setValueToOrderedImmutableList(measures, (pair, fraction) -> Pair.of(pair.left, fraction))
            .getValueFromOrderedList(measures, pair -> pair.right)
            .build())
        .addDefaultRowToUnderlyingModel(() -> measures.add(Pair.of(GRAM, ONE)))
        .deleteRowsOnDelete(row -> measures.remove((int) row))
        .buildDynamicTable();
  }

  public static List<Measure> removePrimary(Measure primaryMeasure) {
    return Arrays.stream(Measure.values())
        .filter(measure -> !measure.equals(primaryMeasure)).collect(Collectors.toList());
  }
}