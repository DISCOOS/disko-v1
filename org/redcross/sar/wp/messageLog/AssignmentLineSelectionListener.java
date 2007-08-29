package org.redcross.sar.wp.messageLog;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Selection listener used when modifying assignment lines
 * @author thomasl
 */
public class AssignmentLineSelectionListener implements ListSelectionListener
{
	AbstractAssignmentPanel m_dialog = null;
	JList m_list = null;
	
	public AssignmentLineSelectionListener(JList list, AbstractAssignmentPanel dialog)
	{
		m_list = list;
		m_dialog = dialog;
	}
	
	public void valueChanged(ListSelectionEvent e)
	{
		int selectedLine = m_list.getSelectedIndex();
		if(selectedLine < 0)
		{
			return;
		}
		
		m_list.clearSelection();
		
		// Show selected assignment in edit mode
		m_dialog.showEditAssignment(selectedLine);
	}
}
