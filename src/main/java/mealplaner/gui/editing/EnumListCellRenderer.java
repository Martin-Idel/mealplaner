package mealplaner.gui.editing;

import java.awt.Component;
import java.util.EnumMap;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

public class EnumListCellRenderer<E extends Enum<E>> extends BasicComboBoxRenderer {
	private static final long serialVersionUID = 1L;
	private EnumMap<E, String> nameMap;

	public EnumListCellRenderer(EnumMap<E, String> nameMap) {
		this.nameMap = nameMap;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Component getListCellRendererComponent(
			JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		setText((nameMap.get(value) == null) ? "" : nameMap.get(value));
		return this;
	}
}
