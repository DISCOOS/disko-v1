package org.redcross.sar.wp.messageLog;

import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractListModel;

import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;

/**
 * Data model for assignment lines, used to retrieve list depending on action (assign, start, complete)
 * @author thomasl
 *
 */
public class AssignmentListModel extends AbstractListModel
{
	protected IDiskoWpMessageLog m_wpMessageLog = null;
	protected MessageLineType m_lineType = null;
	protected List<IMessageLineIf> m_messageLines = null;
	
	public AssignmentListModel(IDiskoWpMessageLog wp)
	{
		m_wpMessageLog = wp;
		
		m_messageLines = new LinkedList<IMessageLineIf>();
	}
	
	public void setMessageLineType(MessageLineType lineType)
	{
		m_lineType = lineType;
		updateList();
	}
	
	/**
	 *  Rebuild message line list
	 */
	public void updateList()
	{
		m_messageLines.clear();
		
		// TODO Get all message lines of given type
		// TODO Store lines in list
		m_messageLines.add(MessageLogTopPanel.getCurrentMessage().findMessageLine(m_lineType, false));
		fireContentsChanged(this, 0, m_messageLines.size()-1);
	}
	
	public Object getElementAt(int index)
	{
		return m_messageLines.size()-1 < index ? null : m_messageLines.get(index);
	}

	public int getSize()
	{
		return m_messageLines.size();
	}

}
