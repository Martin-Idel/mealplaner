// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.tables;

import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withNonnegativeIntegerContent;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import mealplaner.commons.NonnegativeInteger;

class FlexibleTableBuilderTest {

  @Test
  void buildingATableWithCustomTableOrderNumbersCorrectlyOrdersTableColumns() {
    var table = createNewTable()
        .addColumn(withContent(String.class)
            .withColumnName("new Column")
            .buildWithOrderNumber(2))
        .addColumn(withNonnegativeIntegerContent()
            .withColumnName("new Column 2")
            .buildWithOrderNumber(1))
        .buildTable();

    assertThat(table.getTable().getColumnClass(0)).isEqualTo(NonnegativeInteger.class);
    assertThat(table.getTable().getColumnClass(1)).isEqualTo(String.class);
  }

  @Test
  void theDefaultSystemCreatesAStableColumnOrder() {
    var table = createNewTable()
        .addColumn(withContent(String.class)
            .withColumnName("new Column")
            .build())
        .addColumn(withNonnegativeIntegerContent()
            .withColumnName("new Column 2")
            .build())
        .buildTable();

    assertThat(table.getTable().getColumnClass(0)).isEqualTo(String.class);
    assertThat(table.getTable().getColumnClass(1)).isEqualTo(NonnegativeInteger.class);
  }

  @Test
  void columnListenersGetRedirectedCorrectly() {
    var table = createNewTable()
        .addColumn(withContent(String.class)
            .withColumnName("new Column")
            .buildWithOrderNumber(2))
        .addListenerToThisColumn(click -> {
        })
        .addColumn(withNonnegativeIntegerContent()
            .withColumnName("new Column 2")
            .buildWithOrderNumber(1))
        .buildTable();

    assertThat(table.getTable().getColumnClass(0)).isEqualTo(NonnegativeInteger.class);
    assertThat(table.getTable().getMouseListeners()[2]).isInstanceOf(ColumnListener.class);
    assertThat(((ColumnListener) table.getTable().getMouseListeners()[2]).getColumnNumber()).isEqualTo(1);
    assertThat(table.getTable().getColumnClass(1)).isEqualTo(String.class);
  }
}
