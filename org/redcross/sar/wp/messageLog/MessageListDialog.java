package org.redcross.sar.wp.messageLog;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.IMessageIf;

public class MessageListDialog extends DiskoDialog implements IEditMessageDialogIf
{
	private JTable m_messageListTable;
	private MessageListTableModel m_messageTableModel;
	private JScrollPane m_textScrollPane;
	
	public MessageListDialog(IDiskoWpMessageLog wp)
	{
		super(wp.getApplication().getFrame());
		setLayout(new BorderLayout());
		m_messageTableModel = new MessageListTableModel();
		m_messageListTable = new JTable(m_messageTableModel);
		m_textScrollPane = new JScrollPane(m_messageListTable);
		m_messageListTable.setFillsViewportHeight(true);
		m_messageListTable.setColumnSelectionAllowed(false);
		m_messageListTable.setRowSelectionAllowed(false);
		this.add(m_textScrollPane, BorderLayout.CENTER);
	}

	/*
	public void updateContents(IMessageIf message)
	{
		m_messageTableModel.setMessageLines(message.getLines());
	}

	public void clearPanelContents()
	{
		m_messageTableModel.setMessageLines(null);
	}
	*/

	public void setMessageLines(String[] lines)
	{
		m_messageTableModel.setMessageLines(lines);
	}

	@Override
	public void hideDialog()
	{
		this.setVisible(false);
	}

	@Override
	public void newMessageSelected(IMessageIf message)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showDialog()
	{
		this.setVisible(true);
	}
}
