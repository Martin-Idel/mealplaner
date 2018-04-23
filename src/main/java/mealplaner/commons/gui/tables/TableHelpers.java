package mealplaner.commons.gui.tables;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.function.Consumer;

public class TableHelpers {
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
