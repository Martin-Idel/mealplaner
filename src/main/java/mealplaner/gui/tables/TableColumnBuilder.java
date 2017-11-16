package mealplaner.gui.tables;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static mealplaner.gui.tables.TableColumnData.createTableColumn;

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

import mealplaner.commons.NonnegativeInteger;
import mealplaner.gui.editing.PositiveIntegerCellEditor;

public class TableColumnBuilder<T> {
	private Class<T> classType;
	private String name = "";

	private BiFunction<T, Integer, Optional<Integer[]>> setValue = (value, row) -> empty();
	private Function<Integer, T> getValue = (integer) -> null;
	private Predicate<Integer> isEditableIf = (bool) -> false;
	private int preferredSize = 100;
	private Optional<TableCellEditor> editor = empty();
	private Optional<TableCellRenderer> renderer = empty();

	private TableColumnBuilder(Class<T> classType) {
		this.classType = classType;
	}

	public static <S> TableColumnBuilder<S> withContent(Class<S> type) {
		return new TableColumnBuilder<>(type);
	}

	public static <S extends Enum<S>> TableColumnBuilder<S> newColumnWithEnumContent(
			Class<S> type) {
		JComboBox<S> comboBox = new JComboBox<S>(type.getEnumConstants());
		return new TableColumnBuilder<S>(type)
				.setPreferredSize(50)
				.overwriteTableCellEditor(new DefaultCellEditor(comboBox));
	}

	public static TableColumnBuilder<NonnegativeInteger> withNonnegativeIntegerContent() {
		return new TableColumnBuilder<>(NonnegativeInteger.class)
				.setPreferredSize(50)
				.overwriteTableCellEditor(new PositiveIntegerCellEditor());
	}

	public static TableColumnBuilder<Boolean> withBooleanContent() {
		return new TableColumnBuilder<>(Boolean.class)
				.setPreferredSize(50);
	}

	public TableColumnBuilder<T> withColumnName(String name) {
		this.name = name;
		return this;
	}

	public <S> TableColumnBuilder<T> setValueToOrderedList(List<S> orderedList,
			BiConsumer<S, T> setValue) {
		this.setValue = (value, row) -> {
			setValue.accept(orderedList.get(row), value);
			return empty();
		};
		return this;
	}

	public <S> TableColumnBuilder<T> setValueToOrderedImmutableList(List<S> orderedList,
			BiFunction<S, T, S> setValue) {
		this.setValue = (value, row) -> {
			orderedList.set(row, setValue.apply(orderedList.get(row), value));
			return empty();
		};
		return this;
	}

	public TableColumnBuilder<T> setRowValueToUnderlyingModel(BiConsumer<T, Integer> setValue) {
		this.setValue = (value, row) -> {
			setValue.accept(value, row);
			return empty();
		};
		return this;
	}

	public <S> TableColumnBuilder<T> getValueFromOrderedList(List<S> orderedList,
			Function<S, T> getValue) {
		this.getValue = row -> getValue.apply(orderedList.get(row));
		return this;
	}

	public TableColumnBuilder<T> alsoUpdatesCellsOfColumns(Integer... cells) {
		// Need to copy the setValue-Function, as we modify it in the lambda
		final BiFunction<T, Integer, Optional<Integer[]>> oldSetValue = this.setValue;
		this.setValue = (value, row) -> {
			oldSetValue.apply(value, row);
			return of(cells);
		};
		return this;
	}

	public TableColumnBuilder<T> getRowValueFromUnderlyingModel(Function<Integer, T> getValue) {
		this.getValue = getValue;
		return this;
	}

	public TableColumnBuilder<T> isEditable() {
		this.isEditableIf = (bool) -> true;
		return this;
	}

	public TableColumnBuilder<T> isEditableIf(Predicate<Integer> predicate) {
		this.isEditableIf = predicate;
		return this;
	}

	public TableColumnBuilder<T> setPreferredSize(int preferredSize) {
		this.preferredSize = preferredSize;
		return this;
	}

	public TableColumnBuilder<T> overwriteTableCellEditor(TableCellEditor editor) {
		this.editor = of(editor);
		return this;
	}

	public TableColumnBuilder<T> overwriteTableCellRenderer(TableCellRenderer renderer) {
		this.renderer = of(renderer);
		return this;
	}

	public TableColumnData<T> build() {
		return createTableColumn(classType, name, setValue, getValue, isEditableIf, preferredSize,
				editor, renderer);
	}
}
