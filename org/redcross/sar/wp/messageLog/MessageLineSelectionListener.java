package org.redcross.sar.wp.messageLog;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.redcross.sar.mso.data.IMessageLineIf;

public class MessageLineSelectionListener implements ListSelectionListener
{
	protected LineListTableModel m_tableModel;
	
	public MessageLineSelectionListener(LineListTableModel listTableModel)
	{
		m_tableModel = listTableModel;
	}
	
	public void valueChanged(ListSelectionEvent e)
	{
		ListSelectionModel lsm = (ListSelectionModel)e.getSource();
		
		// Get selected row index
		Integer rowIndex = new Integer(lsm.getMinSelectionIndex());
		if(rowIndex > -1)
		{
			IMessageLineIf line = m_tableModel.getMessageLine(rowIndex);
			
			switch(line.getLineType())
			{
			case TEXT:
				MessageLogTopPanel.showTextDialog();
				break;
			case POI:
				MessageLogTopPanel.showPositionDialog();
				break;
			case FINDING:
				MessageLogTopPanel.showFindingDialog();
				break;
			case ASSIGNED:
				MessageLogTopPanel.showAssignDialog();
				break;
			case STARTED:
				MessageLogTopPanel.showStartDialog();
				break;
			case COMPLETE:
				MessageLogTopPanel.showCompleteDialog();
				break;
			}
		}
	}
}
