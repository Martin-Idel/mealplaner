package mealplaner.gui.editing;

import java.util.EnumMap;

import javax.swing.table.DefaultTableCellRenderer;

public class EnumListTableCellRenderer<E extends Enum<E>> extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	private EnumMap<E, String> nameMap;

	public EnumListTableCellRenderer(EnumMap<E, String> nameMap) {
		super();
		this.nameMap = nameMap;
	}

	@Override
	public void setValue(Object value) {
		setText((nameMap.get(value) == null) ? "" : nameMap.get(value));
	}

}
