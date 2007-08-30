package org.redcross.sar.wp.messageLog;

import java.awt.BorderLayout;
import java.util.Comparator;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IMessageLineListIf;
import org.redcross.sar.util.mso.Selector;

/**
 * Panel displaying message lines in the currently selected message
 * @author thomasl
 */
public class LineListPanel extends JPanel implements IEditMessageComponentIf
{
	private static final long serialVersionUID = 1L;
	
	private JTable m_messageListTable;
	private LineListTableModel m_messageTableModel;
	private JScrollPane m_textScrollPane;
	private IDiskoWpMessageLog m_wpMessageLog;
	private MessageLineSelectionListener m_lineSelectionListener;
	
	private Comparator<IMessageLineIf> m_messageLineComparator = new Comparator<IMessageLineIf>()
	{
		public int compare(IMessageLineIf arg0, IMessageLineIf arg1)
		{
			return arg0.getLineNumber() - arg1.getLineNumber();
		} 
	};
	
	private Selector<IMessageLineIf> m_messageLineSelector = new Selector<IMessageLineIf>()
	{
		public boolean select(IMessageLineIf anObject)
		{
			return true;
		}
	};
	
	/**
	 * Constructor
	 * @param wp Message log work process
	 */
	public LineListPanel(IDiskoWpMessageLog wp)
	{
		m_wpMessageLog = wp;
		
		setLayout(new BorderLayout());
		
		m_messageTableModel = new LineListTableModel(m_wpMessageLog);
		m_messageListTable = new JTable(m_messageTableModel);
		m_textScrollPane = new JScrollPane(m_messageListTable);
		m_lineSelectionListener = new MessageLineSelectionListener(m_messageTableModel);
		m_messageListTable.getSelectionModel().addListSelectionListener(m_lineSelectionListener);
		
		m_messageListTable.setFillsViewportHeight(true);
		m_messageListTable.setColumnSelectionAllowed(false);
		m_messageListTable.setRowSelectionAllowed(true);
		
		this.add(m_textScrollPane, BorderLayout.CENTER);
	}

	/**
	 * {@link IEditMessageComponentIf#newMessageSelected(IMessageIf)}
	 */
	public void newMessageSelected(IMessageIf message)
	{
		m_messageTableModel.clearMessageLines();
		IMessageLineListIf messageLines = message.getMessageLines();
		
		if(messageLines == null)
		{
			return;
		}
		
		for(IMessageLineIf messageLine : messageLines.selectItems(m_messageLineSelector, m_messageLineComparator))
		{	
			m_messageTableModel.addMessageLine(messageLine);
		}
		m_messageTableModel.fireTableDataChanged();
	}

	/**
	 * {@link IEditMessageComponentIf#showComponent()}
	 */
	public void showComponent()
	{
		this.setVisible(true);
	}
	
	/**
	 * {@link IEditMessageComponentIf#hideComponent()}
	 */
	public void hideComponent()
	{
		this.setVisible(false);
	}

	public void clearContents()
	{
		m_messageTableModel.clearMessageLines();
	}
}