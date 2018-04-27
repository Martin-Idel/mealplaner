package mealplaner.commons.gui.tables;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.function.Consumer;

public final class TableHelpers {
  private TableHelpers() {
  }

  /**
   * Helper functions to correctly delete selected rows from a table. Given a
   * table and a function to delete one row from the table's underlying data
   * structure, delete all rows currently selected.
   * 
   * @param table
   *          The table to get selected rows from.
   * @param remove
   *          A consumer that when given an integer deletes the row corresponding
   *          to this integer from the underlying model.
   */
  public static void deleteSelectedRows(Table table, Consumer<Integer> remove) {
    Arrays.stream(table.getSelectedRows())
        .collect(ArrayDeque<Integer>::new, ArrayDeque<Integer>::add,
            ArrayDeque<Integer>::addAll)
        .descendingIterator()
        .forEachRemaining(number -> {
          remove.accept(number);
          table.deleteRows(number, number);
        });
  }
}
