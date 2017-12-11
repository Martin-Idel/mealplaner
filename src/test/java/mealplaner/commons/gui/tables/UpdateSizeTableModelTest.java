package mealplaner.commons.gui.tables;

import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withEnumContent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import org.junit.Test;

import mealplaner.commons.gui.ButtonPanelEnabling;

public class UpdateSizeTableModelTest {

  @Test
  public void oneStringColumnOnlyReturnsCorrectSize() {
    List<String> columnContent = new ArrayList<>();
    columnContent.add("Test1");
    columnContent.add("Test2");
    JTable table = createTableWithOneNonEditableStringColumn(columnContent);

    assertThat(table.getRowCount()).isEqualTo(columnContent.size());
    assertThat(table.getColumnCount()).isEqualTo(1);
  }

  @Test
  public void oneStringColumnHasACorrectNameWhenSet() {
    List<String> columnContent = new ArrayList<>();
    columnContent.add("Test1");
    columnContent.add("Test2");
    JTable table = createTableWithOneNonEditableStringColumn(columnContent);

    assertThat(table.getColumnName(0)).isEqualTo("Column");
  }

  @Test
  public void oneStringColumnOnlyHasCorrectContent() {
    List<String> columnContent = new ArrayList<>();
    columnContent.add("Test1");
    columnContent.add("Test2");
    JTable table = createTableWithOneNonEditableStringColumn(columnContent);

    assertThat(table.getValueAt(0, 0)).isEqualTo(columnContent.get(0));
    assertThat(table.getValueAt(1, 0)).isEqualTo(columnContent.get(1));
  }

  @Test
  public void oneStringColumnCannotSetValueIfNotEditable() {
    List<String> columnContent = new ArrayList<>();
    columnContent.add("Test1");
    columnContent.add("Test2");
    JTable table = createTableWithOneNonEditableStringColumn(columnContent);

    table.setValueAt("Test3", 0, 0);

    assertThat(table.isCellEditable(0, 0)).isFalse();
    assertThat(table.getValueAt(0, 0)).isEqualTo("Test1");
    assertThat(table.getValueAt(1, 0)).isEqualTo(columnContent.get(1));
  }

  @Test
  public void oneStringColumnArrayListWorksWithConvenienceMethods() {
    List<TestClass> columnContent = new ArrayList<>();
    columnContent.add(new TestClass("Test1"));
    columnContent.add(new TestClass("Test2"));
    Table abstractedTable = createNewTable()
        .withRowCount(columnContent::size)
        .addColumn(
            withContent(String.class)
                .withColumnName("Column")
                .getValueFromOrderedList(columnContent, test -> test.getString())
                .setValueToOrderedList(columnContent,
                    (element, value) -> element.setString(value))
                .isEditable()
                .build())
        .buildTable();
    JTable table = abstractedTable.getTable();

    table.setValueAt("Test3", 0, 0);

    assertThat(table.getValueAt(0, 0)).isEqualTo("Test3");
    assertThat(table.getValueAt(1, 0)).isEqualTo("Test2");
  }

  @Test
  public void oneStringColumnArrayListOfImmutablesWorksWithConvenienceMethods() {
    List<TestClass> columnContent = new ArrayList<>();
    columnContent.add(new TestClass("Test1"));
    columnContent.add(new TestClass("Test2"));
    Table abstractedTable = createNewTable()
        .withRowCount(columnContent::size)
        .addColumn(
            withContent(String.class)
                .withColumnName("Column")
                .getValueFromOrderedList(columnContent, test -> test.getString())
                .setValueToOrderedImmutableList(columnContent,
                    (element, value) -> {
                      element.setString(value);
                      return element;
                    })
                .isEditable()
                .build())
        .buildTable();
    JTable table = abstractedTable.getTable();

    table.setValueAt("Test3", 0, 0);

    assertThat(table.getValueAt(0, 0)).isEqualTo("Test3");
    assertThat(table.getValueAt(1, 0)).isEqualTo("Test2");
  }

  @Test
  public void enablingAButtonWorksCorrectly() {
    ButtonPanelEnabling panel = mock(ButtonPanelEnabling.class);
    List<TestClass> columnContent = new ArrayList<>();
    columnContent.add(new TestClass("Test1"));
    columnContent.add(new TestClass("Test2"));
    JTable table = createNewTable()
        .withRowCount(columnContent::size)
        .addColumn(
            withContent(String.class)
                .withColumnName("Column")
                .getValueFromOrderedList(columnContent, test -> test.getString())
                .setValueToOrderedImmutableList(columnContent,
                    (element, value) -> {
                      element.setString(value);
                      return element;
                    })
                .enableButtonsOnSet(panel)
                .isEditable()
                .build())
        .buildTable()
        .getTable();

    table.setValueAt("Test3", 0, 0);

    verify(panel).enableButtons();
    assertThat(table.getValueAt(0, 0)).isEqualTo("Test3");
    assertThat(table.getValueAt(1, 0)).isEqualTo("Test2");
  }

  @Test
  public void oneStringColumnSetsValueIfEditable() {
    List<String> columnContent = new ArrayList<>();
    columnContent.add("Test1");
    columnContent.add("Test2");
    JTable table = createNewTable()
        .withRowCount(columnContent::size)
        .addColumn(
            withContent(String.class)
                .getRowValueFromUnderlyingModel(columnContent::get)
                .setRowValueToUnderlyingModel(
                    (value, row) -> columnContent.set(row, value))
                .isEditable()
                .build())
        .buildTable()
        .getTable();

    table.setValueAt("Test3", 0, 0);

    assertThat(table.isCellEditable(0, 0)).isTrue();
    assertThat(table.getValueAt(0, 0)).isEqualTo(columnContent.get(0));
    assertThat(table.getValueAt(1, 0)).isEqualTo(columnContent.get(1));
    assertThat(columnContent).element(0).isEqualTo("Test3");
  }

  @Test
  public void oneStringColumnAddingToInternalRepresentationWorksCorrectly() {
    List<String> columnContent = new ArrayList<>();
    columnContent.add("Test1");
    columnContent.add("Test2");
    JTable table = createTableWithOneNonEditableStringColumn(columnContent);

    columnContent.add("Test3");

    assertThat(table.getRowCount()).isEqualTo(columnContent.size());
    assertThat(table.getValueAt(0, 0)).isEqualTo(columnContent.get(0));
    assertThat(table.getValueAt(1, 0)).isEqualTo(columnContent.get(1));
    assertThat(table.getValueAt(2, 0)).isEqualTo(columnContent.get(2));
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
    assertThat(table.getRowCount()).isEqualTo(firstColumnContent.size());
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
  }

  @Test
  public void changingAnIntegerValueWorksCorrectly() {
    List<Integer> firstColumnContent = new ArrayList<>();
    firstColumnContent.add(1);
    firstColumnContent.add(2);
    JTable table = createNewTable()
        .withRowCount(firstColumnContent::size)
        .addColumn(withContent(Integer.class)
            .withColumnName("Column1")
            .setRowValueToUnderlyingModel(
                (value, row) -> firstColumnContent.set(row, value))
            .getRowValueFromUnderlyingModel(firstColumnContent::get)
            .isEditable()
            .build())
        .buildTable()
        .getTable();

    table.setValueAt(5, 0, 0);

    assertThat(table.getValueAt(0, 0)).isEqualTo(5);
    assertThat(table.getValueAt(1, 0)).isEqualTo(firstColumnContent.get(1));
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
            .build())
        .addColumn(withEnumContent(TestEnum.class)
            .withColumnName("Column2")
            .getRowValueFromUnderlyingModel(secondColumnContent::get)
            .setRowValueToUnderlyingModel(
                (value, row) -> secondColumnContent.set(row, value))
            .isEditable()
            .build())
        .buildTable()
        .getTable();

    table.setValueAt("Test", 0, 0);

    assertThat(table.getValueAt(0, 0)).isEqualTo("Test");
    assertThat(table.getValueAt(0, 1)).isEqualTo(TestEnum.TEST3);
    assertThat(table.getValueAt(1, 0)).isEqualTo(firstColumnContent.get(1));
    assertThat(table.getValueAt(1, 1)).isEqualTo(secondColumnContent.get(1));
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
            .build())
        .addColumn(withEnumContent(TestEnum.class)
            .withColumnName("Column2")
            .getRowValueFromUnderlyingModel(secondColumn::get)
            .setRowValueToUnderlyingModel((value, row) -> secondColumn.set(row, value))
            .isEditable()
            .build())
        .buildTable()
        .getTable();
  }

  private JTable createTableWithOneNonEditableStringColumn(List<String> tableContent) {
    return createNewTable()
        .withRowCount(tableContent::size)
        .addColumn(
            withContent(String.class)
                .withColumnName("Column")
                .getRowValueFromUnderlyingModel(tableContent::get)
                .build())
        .buildTable()
        .getTable();
  }

  private enum TestEnum {
    TEST1, TEST2, TEST3
  }

  private static class TestClass {
    private String string;

    public TestClass(String string) {
      this.string = string;
    }

    public String getString() {
      return string;
    }

    public void setString(String string) {
      this.string = string;
    }
  }
}
