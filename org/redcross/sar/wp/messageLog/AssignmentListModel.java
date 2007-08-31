package org.redcross.sar.wp.messageLog;

import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;

/**
 * Data model for assignment lines, used to retrieve list depending on action (assign, start, complete)
 * @author thomasl
 */
public class AssignmentListModel extends AbstractListModel
{
	private static final long serialVersionUID = 1L;
	
	protected IDiskoWpMessageLog m_wpMessageLog = null;
	protected MessageLineType m_lineType = null;
	protected List<IMessageLineIf> m_messageLines = null;
	
	/**
	 * @param wp Message log work process
	 */
	public AssignmentListModel(IDiskoWpMessageLog wp)
	{
		m_wpMessageLog = wp;
		
		m_messageLines = new LinkedList<IMessageLineIf>();
	}
	
	/**
	 * Sets the assignment line type and updates assignment list
	 * @param lineType The line type
	 */
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
	
	/**
	 * Returns list element
	 * @param index Index of element to retrieve
	 */
	public Object getElementAt(int index)
	{
		return m_messageLines.size()-1 < index ? null : m_messageLines.get(index);
	}

	/**
	 * Return length of list
	 */
	public int getSize()
	{
		return m_messageLines.size();
	}

}
