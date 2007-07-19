package org.redcross.sar.wp.messageLog;

import java.awt.Font;
import java.awt.FontMetrics;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author Thomas
 *	This class listens for row selections in {@link MessageLogPanel#m_logTable} and updates the GUI accordingly
 */
public class MessageRowSelectionListener implements ListSelectionListener
{
	protected MessageLogTopPanel m_topPanel;
	protected JTable m_messageTable;
	protected LogTableModel m_tableMode;
	protected HashMap<Integer, Boolean> m_rowMap;
	
	public MessageRowSelectionListener(MessageLogTopPanel panel) 
	{
		m_topPanel = panel;
	}

	public void valueChanged(ListSelectionEvent event) 
	{		
		ListSelectionModel lsm = (ListSelectionModel)event.getSource();
		
		// Get selected row index
		Integer rowIndex = new Integer(lsm.getMinSelectionIndex());
		
		// Clear selection
		lsm.clearSelection();
		
		// Check for empty selection
		if(rowIndex == -1)
		{
			return;
		}
		
		// Get message number
		String messageNrString = (String)m_tableMode.getValueAt(rowIndex, 0);
		int messageNr = Integer.valueOf(messageNrString.split("\\s")[0]);
		
		// Update row status
		Boolean expanded = (Boolean)m_rowMap.get(messageNr);
		if(expanded == null)
		{
			expanded = new Boolean(true);
		}
		m_rowMap.put(messageNr, !expanded);
		
		// Set row height
		int defaultRowHeight = m_messageTable.getRowHeight();
		int rowHeight;
		if(!expanded)
		{
			// Calculate row height so that all text is visible in cell without changing column width
			Font font = m_messageTable.getFont();
			String[] strings = (String[])m_messageTable.getValueAt(rowIndex, 4);
			StringBuilder stringBuilder = new StringBuilder();
			for(int i=0; i<strings.length; i++)
			{
				stringBuilder.append(strings[i]);
			}
			int columnWidth = m_messageTable.getColumnModel().getColumn(4).getWidth();
			FontMetrics metrics = m_messageTable.getFontMetrics(font);
			int stringWidth = metrics.stringWidth(stringBuilder.toString());
			int numRows = Math.max(strings.length, stringWidth / columnWidth + 1);
			rowHeight = defaultRowHeight * numRows;
		}
		else
		{
			rowHeight = defaultRowHeight;
		}
		m_messageTable.setRowHeight(rowIndex, rowHeight);
		
		// Update top message panel
		m_topPanel.newMessageSelected(messageNr);
	}

	public void setTable(JTable table) 
	{
		m_messageTable = table;
	}

	public void setModel(LogTableModel model) 
	{
		m_tableMode = model;
	}

	public void setRowMap(HashMap<Integer, Boolean> rowMap) 
	{
		m_rowMap = rowMap;
	}

}
