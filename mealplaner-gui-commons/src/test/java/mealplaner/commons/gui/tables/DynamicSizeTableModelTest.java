// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.tables;

import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withEnumContent;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import org.junit.Test;

public class DynamicSizeTableModelTest {
  @Test
  public void oneStringColumnOnlyHasCorrectContent() {
    List<String> columnContent = new ArrayList<>();
    columnContent.add("Test1");
    columnContent.add("Test2");
    JTable table = createTableWithOneNonEditableStringColumn(columnContent);

    assertThat(table.getValueAt(0, 0)).isEqualTo(columnContent.get(0));
    assertThat(table.getValueAt(1, 0)).isEqualTo(columnContent.get(1));
    assertThat(table.getValueAt(2, 0)).isEqualTo("defaultString");
  }

  @Test
  public void oneStringAndOneEnumColumnResultInCorrectScale() {
    List<String> firstColumnContent = new ArrayList<>();
    firstColumnContent.add("Test1");
    firstColumnContent.add("Test2");
    List<TestEnum> secondColumnContent = new ArrayList<>();
    secondColumnContent.add(TestEnum.TEST1);
    secondColumnContent.add(TestEnum.TEST2);

    JTable table = createTableWithTwoEditableColumns(firstColumnContent, secondColumnContent);

    assertThat(table.getColumnCount()).isEqualTo(2);
    assertThat(table.getRowCount()).isEqualTo(firstColumnContent.size() + 1);
  }

  @Test
  public void oneStringAndOneEnumColumnResultValuesCanBeChangedCorrectly() {
    List<String> firstColumnContent = new ArrayList<>();
    firstColumnContent.add("Test1");
    firstColumnContent.add("Test2");
    List<TestEnum> secondColumnContent = new ArrayList<>();
    secondColumnContent.add(TestEnum.TEST1);
    secondColumnContent.add(TestEnum.TEST2);
    JTable table = createTableWithTwoEditableColumns(firstColumnContent, secondColumnContent);

    table.setValueAt("Test", 0, 0);
    table.setValueAt(TestEnum.TEST3, 1, 1);

    assertThat(table.getValueAt(0, 0)).isEqualTo("Test");
    assertThat(table.getValueAt(0, 1)).isEqualTo(secondColumnContent.get(0));
    assertThat(table.getValueAt(1, 0)).isEqualTo(firstColumnContent.get(1));
    assertThat(table.getValueAt(1, 1)).isEqualTo(TestEnum.TEST3);
    assertThat(table.getValueAt(2, 0)).isEqualTo("defaultString");
    assertThat(table.getValueAt(2, 1)).isEqualTo(TestEnum.TEST1);
  }

  @Test
  public void twoColumnsOneChangingTheOtherWorksCorrectly() {
    List<String> firstColumnContent = new ArrayList<>();
    firstColumnContent.add("Test1");
    firstColumnContent.add("Test2");
    List<TestEnum> secondColumnContent = new ArrayList<>();
    secondColumnContent.add(TestEnum.TEST1);
    secondColumnContent.add(TestEnum.TEST2);
    JTable table = createNewTable()
        .withRowCount(firstColumnContent::size)
        .addColumn(withContent(String.class)
            .withColumnName("Column1")
            .setRowValueToUnderlyingModel(
                (value, row) -> {
                  firstColumnContent.set(row, value);
                  secondColumnContent.set(row, TestEnum.TEST3);
                })
            .alsoUpdatesCellsOfColumns(1)
            .getRowValueFromUnderlyingModel(firstColumnContent::get)
            .isEditable()
            .setDefaultValueForEmptyRow("defaultString")
            .build())
        .addColumn(withEnumContent(TestEnum.class)
            .withColumnName("Column2")
            .getRowValueFromUnderlyingModel(secondColumnContent::get)
            .setRowValueToUnderlyingModel(
                (value, row) -> secondColumnContent.set(row, value))
            .setDefaultValueForEmptyRow(TestEnum.TEST1)
            .isEditable()
            .build())
        .buildDynamicTable()
        .getTable();

    table.setValueAt("Test", 0, 0);

    assertThat(table.getValueAt(0, 0)).isEqualTo("Test");
    assertThat(table.getValueAt(0, 1)).isEqualTo(TestEnum.TEST3);
    assertThat(table.getValueAt(1, 0)).isEqualTo(firstColumnContent.get(1));
    assertThat(table.getValueAt(1, 1)).isEqualTo(secondColumnContent.get(1));
    assertThat(table.getValueAt(2, 0)).isEqualTo("defaultString");
    assertThat(table.getValueAt(2, 1)).isEqualTo(TestEnum.TEST1);
  }

  @Test
  public void addingARowAddsARowToArrayList() {
    List<String> firstColumnContent = new ArrayList<>();
    firstColumnContent.add("Test1");
    firstColumnContent.add("Test2");
    List<TestEnum> secondColumnContent = new ArrayList<>();
    secondColumnContent.add(TestEnum.TEST1);
    secondColumnContent.add(TestEnum.TEST2);
    JTable table = createTableWithTwoEditableColumns(firstColumnContent, secondColumnContent);

    table.setValueAt("newString", 2, 0);

    assertThat(firstColumnContent).size().isEqualTo(3);
    assertThat(secondColumnContent).size().isEqualTo(3);
    assertThat(firstColumnContent).element(2).isEqualTo("newString");
    assertThat(secondColumnContent).element(2).isEqualTo(TestEnum.TEST1);

    assertThat(table.getRowCount()).isEqualTo(4);
    assertThat(table.getValueAt(0, 0)).isEqualTo(firstColumnContent.get(0));
    assertThat(table.getValueAt(0, 1)).isEqualTo(secondColumnContent.get(0));
    assertThat(table.getValueAt(1, 0)).isEqualTo(firstColumnContent.get(1));
    assertThat(table.getValueAt(1, 1)).isEqualTo(secondColumnContent.get(1));
    assertThat(table.getValueAt(2, 0)).isEqualTo(firstColumnContent.get(2));
    assertThat(table.getValueAt(2, 1)).isEqualTo(secondColumnContent.get(2));
    assertThat(table.getValueAt(3, 0)).isEqualTo("defaultString");
    assertThat(table.getValueAt(3, 1)).isEqualTo(TestEnum.TEST1);
  }

  private JTable createTableWithTwoEditableColumns(List<String> firstColumn,
      List<TestEnum> secondColumn) {
    return createNewTable()
        .withRowCount(firstColumn::size)
        .addColumn(withContent(String.class)
            .withColumnName("Column1")
            .setRowValueToUnderlyingModel((value, row) -> firstColumn.set(row, value))
            .getRowValueFromUnderlyingModel(firstColumn::get)
            .isEditable()
            .setDefaultValueForEmptyRow("defaultString")
            .build())
        .addColumn(withEnumContent(TestEnum.class)
            .withColumnName("Column2")
            .getRowValueFromUnderlyingModel(secondColumn::get)
            .setRowValueToUnderlyingModel((value, row) -> secondColumn.set(row, value))
            .isEditable()
            .setDefaultValueForEmptyRow(TestEnum.TEST1)
            .build())
        .addDefaultRowToUnderlyingModel(() -> {
          firstColumn.add("defaultString");
          secondColumn.add(TestEnum.TEST1);
        })
        .buildDynamicTable()
        .getTable();
  }

  private JTable createTableWithOneNonEditableStringColumn(List<String> tableContent) {
    return createNewTable()
        .withRowCount(tableContent::size)
        .addColumn(
            withContent(String.class)
                .withColumnName("Column")
                .getRowValueFromUnderlyingModel(tableContent::get)
                .setDefaultValueForEmptyRow("defaultString")
                .build())
        .buildDynamicTable()
        .getTable();
  }

  private enum TestEnum {
    TEST1, TEST2, TEST3
  }
}
