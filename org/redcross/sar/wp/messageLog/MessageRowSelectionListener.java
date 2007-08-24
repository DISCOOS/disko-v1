package org.redcross.sar.wp.messageLog;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author Thomas
 *	This class listens for row selections in {@link MessageLogPanel#m_logTable} and updates the top panel GUI accordingly
 */
public class MessageRowSelectionListener implements ListSelectionListener
{
	protected MessageLogTopPanel m_topPanel;
	protected JTable m_messageTable;
	protected LogTableModel m_tableMode;
	protected boolean m_msoUpdate = false;
	
	public MessageRowSelectionListener(MessageLogTopPanel panel) 
	{
		m_topPanel = panel;
	}

	public void valueChanged(ListSelectionEvent event) 
	{		
		ListSelectionModel lsm = (ListSelectionModel)event.getSource();
		
		if(lsm.isSelectionEmpty())
			return;
		
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
		
		// Toggle expanded
		Boolean expanded = m_tableMode.isMessageExtended(messageNr);
		m_tableMode.setMessageExtended(messageNr, !expanded);
		
		// Set row height
		if(expanded)
		{
			setRowExpanded(rowIndex);
		}
		else
		{
			setRowCollapsed(rowIndex);
		}
		
		// Update top message panel
		m_topPanel.newMessageSelected(messageNr);
		MessageLogTopPanel.showListDialog();
	}

	public void setTable(JTable table) 
	{
		m_messageTable = table;
	}

	public void setModel(LogTableModel model) 
	{
		m_tableMode = model;
	}
	
	/**
	 * Expands a row so that it encompasses all text in message lines
	 * @param rowIndex Row identifier
	 */
	public void setRowExpanded(int rowIndex)
	{
		// Calculate row height so that all text is visible in cell without changing column width
		int defaultRowHeight = 18; //m_messageTable.getRowHeight();
		int numRows = numRows(rowIndex);
		int rowHeight = defaultRowHeight * numRows + (numRows - 1) * 2 + 4;
		m_messageTable.setRowHeight(rowIndex, rowHeight);
	}
	
	/**
	 * Collapses a row to the default size
	 * @param rowIndex Row identifier
	 */
	public void setRowCollapsed(int rowIndex)
	{
		m_messageTable.setRowHeight(rowIndex, m_messageTable.getRowHeight());
	}
	
	/**
	 * @param rowIndex Identifies the message line
	 * @return Number of rows need to display the entire contents of the message lines
	 */
	public int numRows(int rowIndex)
	{
		int numRows = 0;
		String[] strings = (String[])m_messageTable.getValueAt(rowIndex, 4);
		
		
		for(int i=0; i<strings.length; i++)
		{
			String[] multiline = strings[i].split("\n");
			int numLinesString = multiline.length;
			// TODO handle long single lines as well.
			numRows += Math.max(1, numLinesString);
		}
		
		return numRows;
	}
}
