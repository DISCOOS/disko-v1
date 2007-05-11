package org.redcross.sar.gui.renderers;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.redcross.sar.app.Utils;

public class CheckableListCellRenderer extends JCheckBox implements ListCellRenderer {

	
	private static final long serialVersionUID = 1L;
	
	public CheckableListCellRenderer() {
		super.setOpaque(true);
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		
		setText(Utils.translate(value));
		setSelected(isSelected);
		setBackground(list.getBackground());
		setForeground(list.getForeground());
		return this;
	}
}
