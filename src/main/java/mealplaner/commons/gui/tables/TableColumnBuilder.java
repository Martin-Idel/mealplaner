// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.tables;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static mealplaner.commons.gui.tables.models.TableColumnData.createTableColumn;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.gui.buttonpanel.ButtonPanelEnabling;
import mealplaner.commons.gui.editing.NonnegativeFractionCellEditor;
import mealplaner.commons.gui.editing.NonnegativeIntegerCellEditor;
import mealplaner.commons.gui.tables.models.TableColumnData;

/**
 * TableColumnBuilder to provide type safe access to an underlying data
 * structure.
 *
 * @param <T>
 *          Content type of the column to be constructed
 */
public final class TableColumnBuilder<T> {
  private final Class<T> classType;
  private String name = "";
  private T defaultValue = null;

  private BiFunction<T, Integer, Optional<Integer[]>> setValue = (value, row) -> empty();
  private Function<Integer, T> getValue = (integer) -> null;
  private Predicate<Integer> isEditableIf = (bool) -> false;
  private int preferredSize = 100;
  private Optional<TableCellEditor> editor = empty();
  private Optional<TableCellRenderer> renderer = empty();

  private TableColumnBuilder(Class<T> classType) {
    this.classType = classType;
  }

  /**
   * Obtain a builder for a column with content of type "type".
   */
  public static <S> TableColumnBuilder<S> withContent(Class<S> type) {
    return new TableColumnBuilder<>(type);
  }

  /**
   * Obtain a builder for a column with enum content. This column will
   * automatically come with a ComobBox as CellEditor containing all possible
   * values of the enum.
   */
  public static <S extends Enum<S>> TableColumnBuilder<S> withEnumContent(
      Class<S> type) {
    JComboBox<S> comboBox = new JComboBox<>(type.getEnumConstants());
    return new TableColumnBuilder<>(type)
        .setPreferredSize(50)
        .overwriteTableCellEditor(new DefaultCellEditor(comboBox));
  }

  /**
   * Obtain a builder for a column with nonnegativeInteger type. This column will
   * automatically come with an editor which only allows nonnegative Integers as
   * input.
   */
  public static TableColumnBuilder<NonnegativeInteger> withNonnegativeIntegerContent() {
    return new TableColumnBuilder<>(NonnegativeInteger.class)
        .setPreferredSize(50)
        .overwriteTableCellEditor(new NonnegativeIntegerCellEditor());
  }

  /**
   * Obtain a builder for a column with nonnegativeFraction type. This column will
   * automatically come with an editor which only allows nonnegative fractions as
   * input.
   */
  public static TableColumnBuilder<NonnegativeFraction> withNonnegativeFractionContent() {
    return new TableColumnBuilder<>(NonnegativeFraction.class)
        .setPreferredSize(50)
        .overwriteTableCellEditor(new NonnegativeFractionCellEditor());
  }

  /**
   * Obtain a builder for a column with boolean type. This column will
   * automatically come with a checkbox as editor.
   */
  public static TableColumnBuilder<Boolean> withBooleanContent() {
    return new TableColumnBuilder<>(Boolean.class)
        .setPreferredSize(50);
  }

  /**
   * Add a name to the column
   */
  public TableColumnBuilder<T> withColumnName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Call this function if your table's data structure is an ordered
   * java.utils.List (ordered in the sense that row i will be entry i in the
   * list). Note that the data structure cannot be reassigned.
   *
   * @param orderedList
   *          The ordered list which provides the data structure of this column
   * @param setValue
   *          A function which given an element of your list and an object of the
   *          TableColumn's type, sets the corresponding element to the element of
   *          your list. In the table, when the user sets an entry, this function
   *          ensures that this will set the corresponding data inside the list at
   *          the correct row.
   * @return the current builder
   */
  public <S> TableColumnBuilder<T> setValueToOrderedList(List<S> orderedList,
      BiConsumer<S, T> setValue) {
    this.setValue = (value, row) -> {
      setValue.accept(orderedList.get(row), value);
      return empty();
    };
    return this;
  }

  /**
   * Call this function if your table's data structure is an ordered
   * java.utils.List (ordered in the sense that row i will be entry i in the list)
   * where the entries are immutable. Note that the list data structure cannot be
   * reassigned and must be mutable.
   *
   * @param orderedList
   *          The ordered list of immutables which provides the data structure of
   *          this column
   * @param setValue
   *          A bifunction which given an element of your list and an object of
   *          the TableColumn's type, returns an element of type of your list
   *          where the corresponding aspect is changed. In the table, when the
   *          user sets an entry, this function ensures that this will set the
   *          corresponding data inside the list at the correct row.
   * @return the current builder
   */
  public <S> TableColumnBuilder<T> setValueToOrderedImmutableList(List<S> orderedList,
      BiFunction<S, T, S> setValue) {
    this.setValue = (value, row) -> {
      orderedList.set(row, setValue.apply(orderedList.get(row), value));
      return empty();
    };
    return this;
  }

  /**
   * Call this function if you want to use an arbitrary data structure. Provide a
   * function which when the user changes this column at row i changes the
   * corresponding entry in your data.
   *
   * @param setValue
   *          Function which, when given a row number and the new value of that
   *          row, sets the data structure of the table correspondingly.
   * @return the current builder
   */
  public TableColumnBuilder<T> setRowValueToUnderlyingModel(BiConsumer<T, Integer> setValue) {
    this.setValue = (value, row) -> {
      setValue.accept(value, row);
      return empty();
    };
    return this;
  }

  /**
   * Call this function if your table's data structure is an ordered
   * java.utils.List (ordered in the sense that row i will be entry i in the
   * list). Note that the data structure cannot be reassigned.
   *
   * @param orderedList
   *          The ordered list which provides the data structure of this column
   * @param getValue
   *          A function which given an element of your list, returns the
   *          corresponding entry in this column. In the table, this will make
   *          sure that row i will contain the content of the ith entry of your
   *          list.
   * @return the current builder
   */
  public <S> TableColumnBuilder<T> getValueFromOrderedList(List<S> orderedList,
      Function<S, T> getValue) {
    this.getValue = row -> getValue.apply(orderedList.get(row));
    return this;
  }

  /**
   * Call this function for an arbitrary data structure. This function provides
   * the link between the data structure and the representation in the table.
   *
   * @param getValue
   *          A function which given the row number, provides a value to be shown
   *          in the table.
   * @return the current builder
   */
  public TableColumnBuilder<T> getRowValueFromUnderlyingModel(Function<Integer, T> getValue) {
    this.getValue = getValue;
    return this;
  }

  /**
   * Call this function if you want to enable buttons when a value of the table is
   * set to a new value.
   *
   * @param onSet
   *          An object of type ButtonPanelEnabling, which contains buttons which
   *          can be enabled or disabled.
   * @return the current builder
   */
  public TableColumnBuilder<T> enableButtonsOnSet(ButtonPanelEnabling onSet) {
    // Need to copy the setValue-Function, as we modify it in the lambda
    final BiFunction<T, Integer, Optional<Integer[]>> oldSetValue = this.setValue;
    this.setValue = (value, row) -> {
      onSet.enableButtons();
      return oldSetValue.apply(value, row);
    };
    return this;
  }

  /**
   * Call this function when setting the value in one cell also affects the values
   * in other cells of the same row. This might be the case if e.g. some columns
   * are read only and depend on other aspects of the list.
   *
   * @param cells
   *          Additional cells in the same row to repaint when a row of this
   *          column is changed.
   * @return the current builder
   */
  public TableColumnBuilder<T> alsoUpdatesCellsOfColumns(Integer... cells) {
    // Need to copy the setValue-Function, as we modify it in the lambda
    final BiFunction<T, Integer, Optional<Integer[]>> oldSetValue = this.setValue;
    this.setValue = (value, row) -> {
      oldSetValue.apply(value, row);
      return of(cells);
    };
    return this;
  }

  /**
   * Make the row editable. It is necessary to call any of the functions above
   * providing a setter for the data structure.
   *
   * @return the current builder
   */
  public TableColumnBuilder<T> isEditable() {
    this.isEditableIf = (bool) -> true;
    return this;
  }

  /**
   * Make the row editable if a condition is fulfiled. This can be used to
   * activate/deactivate editing based on other cells content
   *
   * @param predicate
   *          The predicate to test against.
   * @return the current builder
   */
  public TableColumnBuilder<T> isEditableIf(Predicate<Integer> predicate) {
    this.isEditableIf = predicate;
    return this;
  }

  /**
   * Set the preferred horizontal size of the column
   *
   * @param preferredSize
   *          The preferred size in pixels
   * @return the current builder
   */
  public TableColumnBuilder<T> setPreferredSize(int preferredSize) {
    this.preferredSize = preferredSize;
    return this;
  }

  /**
   * Add a custom TableCellEditor to this column.
   *
   * @param editor
   *          The table cell editor for this column
   * @return the current builder
   */
  public TableColumnBuilder<T> overwriteTableCellEditor(TableCellEditor editor) {
    this.editor = of(editor);
    return this;
  }

  /**
   * Add a custom TableCellRenderer to this column.
   *
   * @param renderer
   *          The renderer for this column
   * @return the current builder
   */
  public TableColumnBuilder<T> overwriteTableCellRenderer(TableCellRenderer renderer) {
    this.renderer = of(renderer);
    return this;
  }

  /**
   * Set a default value for an empty row. This is necessary if the column is part
   * of an UpdateSizeTableModel as this value gets displayed for the last "empty"
   * row. See the table model for further clarification.
   *
   * @param defaultValue
   *          Value to display for the last row of an UpdateSizeTableModel
   * @return the current builder
   */
  public TableColumnBuilder<T> setDefaultValueForEmptyRow(T defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }

  /**
   * Perform another action when a column is changed, e.g. run a save action, etc.
   * N.B: This function must be called after calling one of the functions above
   * setting values to the table.
   *
   * @param changeAction
   *          Runnable containing the action to be processed when changing a row's
   *          entry.
   * @return the current builder
   */
  public TableColumnBuilder<T> onChange(Runnable changeAction) {
    final BiFunction<T, Integer, Optional<Integer[]>> oldSetValue = this.setValue;
    this.setValue = (value, row) -> {
      if (!this.getValue.apply(row).equals(value)) {
        changeAction.run();
        return oldSetValue.apply(value, row);
      }
      return Optional.empty();
    };
    return this;
  }

  /**
   * Build a TableColumn.
   *
   * @return The table column with all aspects chosen for this builder
   */
  public TableColumnData<T> build() {
    return createTableColumn(
        classType,
        name,
        defaultValue,
        setValue,
        getValue,
        isEditableIf,
        preferredSize,
        editor,
        renderer,
        Optional.empty());
  }

  /**
   * Build a TableColumn with an order number. This order number will be used to order the table deterministically.
   * The higher the order number, the more the column will appear to the right.
   *
   * @return A pair of the table column with all aspects chosen for this builder and the order number
   */
  public TableColumnData<T> buildWithOrderNumber(int orderNumber) {
    return createTableColumn(
        classType,
        name,
        defaultValue,
        setValue,
        getValue,
        isEditableIf,
        preferredSize,
        editor,
        renderer,
        Optional.of(orderNumber));
  }
}
