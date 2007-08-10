package org.redcross.sar.wp.messageLog;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.redcross.sar.mso.data.IMessageLineIf;

/**
 * @author thomasl
 * Table model for displaying message lines at top level edit panel
 */
public class ListTableModel extends AbstractTableModel
{
	protected List<IMessageLineIf> m_messageLines = null;

	public ListTableModel()
	{
		m_messageLines = new LinkedList<IMessageLineIf>();
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
		return m_messageLines.size();
	}

	public Object getValueAt(int rowIndex, int coulumnIndex) 
	{
		if(m_messageLines.isEmpty())
		{
			return "";
		}
		else
		{
			IMessageLineIf line = m_messageLines.get(rowIndex);
			/*
			switch(line.getLineType())
			{
			case IMessageLineIf.MessageLineType.TEXT:
				break;
			}
			*/
			return m_messageLines.get(rowIndex).toString();
		}
	}

	public void clearMessageLines()
	{
		m_messageLines.clear();
	}

	public void addMessageLine(IMessageLineIf messageLine)
	{
		m_messageLines.add(messageLine);
	}
	
}
