package org.redcross.sar.wp.messageLog;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.redcross.sar.mso.data.IMessageIf;

public class MessageListPanel extends AbstractMessagePanelContets
{
	private JTable m_messageListTable;
	private MessageListTableModel m_messageTableModel;
	private JScrollPane m_textScrollPane;
	
	public MessageListPanel()
	{
		setLayout(new BorderLayout());
		m_messageTableModel = new MessageListTableModel();
		m_messageListTable = new JTable(m_messageTableModel);
		m_textScrollPane = new JScrollPane(m_messageListTable);
		m_messageListTable.setFillsViewportHeight(true);
		m_messageListTable.setColumnSelectionAllowed(false);
		m_messageListTable.setRowSelectionAllowed(false);
		this.add(m_textScrollPane, BorderLayout.CENTER);
	}

	@Override
	public void updateContents(IMessageIf message)
	{
		m_messageTableModel.setMessageLines(message.getLines());
	}
}
