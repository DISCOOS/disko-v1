package org.redcross.sar.wp.messageLog;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLineIf;

public class ListDialog extends DiskoDialog implements IEditMessageDialogIf
{
	private JTable m_messageListTable;
	private ListTableModel m_messageTableModel;
	private JScrollPane m_textScrollPane;
	
	public ListDialog(IDiskoWpMessageLog wp)
	{
		super(wp.getApplication().getFrame());
		
		setLayout(new BorderLayout());
		
		m_messageTableModel = new ListTableModel();
		m_messageListTable = new JTable(m_messageTableModel);
		m_textScrollPane = new JScrollPane(m_messageListTable);
		
		m_messageListTable.setFillsViewportHeight(true);
		m_messageListTable.setColumnSelectionAllowed(false);
		m_messageListTable.setRowSelectionAllowed(true);
		
		this.add(m_textScrollPane, BorderLayout.CENTER);
	}

	public void newMessageSelected(IMessageIf message)
	{
		m_messageTableModel.clearMessageLines();
		for(IMessageLineIf messageLine : message.getMessageLineItems())
		{	
			m_messageTableModel.addMessageLine(messageLine);
		}
		m_messageTableModel.fireTableDataChanged();
	}

	public void showDialog()
	{
		this.setVisible(true);
	}
	
	public void hideDialog()
	{
		this.setVisible(false);
	}

	public void clearContents()
	{
		m_messageTableModel.clearMessageLines();
	}
}
