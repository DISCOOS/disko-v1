package org.redcross.sar.wp.messageLog;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

public class MessageTableRenderer extends JTextArea implements TableCellRenderer
{
	public MessageTableRenderer()
	{
		setLineWrap(true);
		setWrapStyleWord(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
	{
		StringBuilder messageString = new StringBuilder();
		String[] messageLines = (String[])value;
		for(int i=0; i<messageLines.length; i++)
		{
			messageString.append(messageLines[i]);
		}
		setText(messageString.toString());
		setSize(table.getColumnModel().getColumn(column).getWidth(),
	               getPreferredSize().height);
		return this;
	}

}
