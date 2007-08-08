package org.redcross.sar.wp.messageLog;

import javax.swing.table.AbstractTableModel;

/**
 * @author thomasl
 * Table model for displaying message lines at top level edit panel
 */
public class ListTableModel extends AbstractTableModel
{
	protected String[] m_messageLines = null;
	
	public void setMessageLines(String[] messageLines)
	{
		m_messageLines = messageLines;
		fireTableDataChanged();
	}
	
	public int getColumnCount() 
	{
		return 1;
	}
	
	@Override
	public String getColumnName(int column)
	{
		return null;
	}

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

	public Object getValueAt(int rowIndex, int coulumnIndex) 
	{
		if(m_messageLines == null)
		{
			return "";
		}
		else
		{
			return m_messageLines[rowIndex];
		}
	}
	
}
