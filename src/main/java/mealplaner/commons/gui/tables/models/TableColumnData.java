// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.tables.models;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public final class TableColumnData<T> {
  private final String name;
  private final Class<T> classType;
  private final T defaultValue;

  private final BiFunction<T, Integer, Optional<Integer[]>> setValue;
  private final Function<Integer, T> getValue;
  private final Predicate<Integer> isEditableIf;

  private final int preferredSize;
  private final Optional<TableCellEditor> tableCellEditor;
  private final Optional<TableCellRenderer> tableCellRenderer;

  private TableColumnData(
      Class<T> classType,
      String name,
      T defaultValue,
      BiFunction<T, Integer, Optional<Integer[]>> setValue,
      Function<Integer, T> getValue,
      Predicate<Integer> isEditableIf,
      int preferredSize,
      Optional<TableCellEditor> tableCellEditor,
      Optional<TableCellRenderer> tableCellRenderer) {
    this.classType = classType;
    this.name = name;
    this.defaultValue = defaultValue;
    this.setValue = setValue;
    this.getValue = getValue;
    this.isEditableIf = isEditableIf;
    this.preferredSize = preferredSize;
    this.tableCellEditor = tableCellEditor;
    this.tableCellRenderer = tableCellRenderer;
  }

  public static <S> TableColumnData<S> createTableColumn(
      Class<S> classType,
      String name,
      S defaultValue,
      BiFunction<S, Integer, Optional<Integer[]>> setValue,
      Function<Integer, S> getValue,
      Predicate<Integer> isEditableIf,
      int preferredSize,
      Optional<TableCellEditor> tableCellEditor,
      Optional<TableCellRenderer> tableCellRenderer) {
    return new TableColumnData<>(classType, name, defaultValue, setValue, getValue,
        isEditableIf,
        preferredSize, tableCellEditor, tableCellRenderer);
  }

  public String columnName() {
    return name;
  }

  public Class<T> getColumnClass() {
    return classType;
  }

  public Optional<Integer[]> setValue(Object value, int row) {
    return setValue.apply(classType.cast(value), row);
  }

  public T getValue(int row) {
    return getValue.apply(row);
  }

  public boolean isEditable(int row) {
    return isEditableIf.test(row);
  }

  public int getPreferredSize() {
    return preferredSize;
  }

  public Optional<TableCellEditor> getTableCellEditor() {
    return tableCellEditor;
  }

  public Optional<TableCellRenderer> getTableCellRenderer() {
    return tableCellRenderer;
  }

  public T getDefaultValue() {
    return defaultValue;
  }
}
