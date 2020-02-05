// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.tables.models;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public final class TableColumnData<T> {
  private final String name;
  private final Class<T> classType;

  private final BiFunction<T, Integer, Optional<Integer[]>> setValueFunction;
  private final IntFunction<T> getValueFunction;
  private final IntPredicate isEditableIf;

  private final int preferredSize;
  private final Optional<TableCellEditor> tableCellEditor;
  private final Optional<TableCellRenderer> tableCellRenderer;
  private final Optional<Integer> orderNumber;

  private TableColumnData(
      Class<T> classType,
      String name,
      BiFunction<T, Integer, Optional<Integer[]>> setValueFunction,
      IntFunction<T> getValueFunction,
      IntPredicate isEditableIf,
      int preferredSize,
      Optional<TableCellEditor> tableCellEditor,
      Optional<TableCellRenderer> tableCellRenderer,
      Optional<Integer> orderNumber) {
    this.classType = classType;
    this.name = name;
    this.setValueFunction = setValueFunction;
    this.getValueFunction = getValueFunction;
    this.isEditableIf = isEditableIf;
    this.preferredSize = preferredSize;
    this.tableCellEditor = tableCellEditor;
    this.tableCellRenderer = tableCellRenderer;
    this.orderNumber = orderNumber;
  }

  public static <S> TableColumnData<S> createTableColumn(
      Class<S> classType,
      String name,
      BiFunction<S, Integer, Optional<Integer[]>> setValue,
      IntFunction<S> getValue,
      IntPredicate isEditableIf,
      int preferredSize,
      Optional<TableCellEditor> tableCellEditor,
      Optional<TableCellRenderer> tableCellRenderer,
      Optional<Integer> orderNumber) {
    return new TableColumnData<>(classType, name, setValue, getValue,
        isEditableIf, preferredSize, tableCellEditor, tableCellRenderer, orderNumber);
  }

  public TableColumnData<T> addOrderNumber(int orderNumber) {
    return new TableColumnData<>(classType, name, setValueFunction, getValueFunction,
        isEditableIf, preferredSize, tableCellEditor, tableCellRenderer, Optional.of(orderNumber));
  }

  public String columnName() {
    return name;
  }

  public Class<T> getColumnClass() {
    return classType;
  }

  public Optional<Integer[]> setValue(Object value, int row) {
    return setValueFunction.apply(classType.cast(value), row);
  }

  public T getValue(int row) {
    return getValueFunction.apply(row);
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

  public int getOrderNumber() {
    return orderNumber.orElse(-1);
  }

  public boolean hasOrderNumber() {
    return orderNumber.isPresent();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TableColumnData<?> that = (TableColumnData<?>) o;
    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
