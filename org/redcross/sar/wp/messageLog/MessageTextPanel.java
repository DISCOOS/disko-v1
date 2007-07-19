package org.redcross.sar.wp.messageLog;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.redcross.sar.mso.data.IMessageIf;

/**
 * @author thomasl
 *
 * Displays the message text in the top panel in the message log
 */
public class MessageTextPanel extends AbstractMessagePanelContets
{
	private JTable m_messageTextTable;
	private MessageTextTableModel m_messageTableModel;
	private JScrollPane m_textScrollPane;
	
	public MessageTextPanel() 
	{
		setLayout(new BorderLayout());
		m_messageTableModel = new MessageTextTableModel();
		m_messageTextTable = new JTable(m_messageTableModel);
		m_textScrollPane = new JScrollPane(m_messageTextTable);
		m_messageTextTable.setFillsViewportHeight(true);
		m_messageTextTable.setColumnSelectionAllowed(false);
		m_messageTextTable.setRowSelectionAllowed(false);
		this.add(m_textScrollPane, BorderLayout.CENTER);
	}

	@Override
	public void updateContents(IMessageIf message) 
	{
		m_messageTableModel.setMessageLines(message.getLines());
	}
}
