package org.redcross.sar.wp.messageLog;

import javax.swing.table.AbstractTableModel;

/**
 * @author thomasl
 * Table model for displaying message lines at top level edit panel
 */
public class MessageEditTableModel extends AbstractTableModel
{
	protected String[] m_messageLines = null;
	
	public void setMessageLines(String[] messageLines)
	{
		m_messageLines = messageLines;
		fireTableDataChanged();
	}
	
	@Override
	public int getColumnCount() 
	{
		return 1;
	}
	
	@Override
	public String getColumnName(int column)
	{
		return null;
	}

	@Override
	public int getRowCount() 
	{
		if(m_messageLines == null)
		{
			return 0;
		}
		else
		{
			return m_messageLines.length;
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int coulumnIndex) 
	{
		return m_messageLines[rowIndex];
	}
	
}
