package org.redcross.sar.gui.renderers;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class BooleanTableCellRenderer extends JCheckBox implements
		TableCellRenderer {

	
	private static final long serialVersionUID = 1L;
	
	public BooleanTableCellRenderer() {
		super.setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		setSelected(isSelected);
		setBackground(table.getBackground());
		setForeground(table.getForeground());
		return this;
	}
}
