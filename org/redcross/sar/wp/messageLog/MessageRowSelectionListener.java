package org.redcross.sar.wp.messageLog;

import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author Thomas Landvik
 *	This class listens for row selections in {@link MessageLogPanel#m_logTable} and updates the GUI accordingly
 */
public class MessageRowSelectionListener implements ListSelectionListener
{
	protected MessageEditPanel m_editPanel;
	protected JTable m_messageTable;
	protected LogTableModel m_tableMode;
	protected HashMap m_rowMap;
	
	public MessageRowSelectionListener(MessageEditPanel panel) 
	{
		m_editPanel = panel;
	}

	public void valueChanged(ListSelectionEvent event) 
	{		
		ListSelectionModel lsm = (ListSelectionModel)event.getSource();
		
		Integer rowIndex = lsm.getMinSelectionIndex();
		
		// Update row status
		Boolean expanded = (Boolean)m_rowMap.get(rowIndex);
		m_rowMap.put(rowIndex, !expanded);
		
		// Set row height
		int rowHeight = m_messageTable.getRowHeight();
		if(expanded)
		{
			// Calculate row height so that all text is visible in cell, without changing column width
			rowHeight = 100;
		}
		m_messageTable.setRowHeight(rowIndex, rowHeight);
		
		// Fill values in edit panel
		m_editPanel.setNr((Integer)m_messageTable.getValueAt(rowIndex, 0));
		
		// Clear selection
		
	}

	public void setTable(JTable table) 
	{
		m_messageTable = table;
	}

	public void setModel(LogTableModel model) 
	{
		m_tableMode = model;
	}

	public void setRowMap(HashMap rowMap) 
	{
		m_rowMap = rowMap;
	}

}
