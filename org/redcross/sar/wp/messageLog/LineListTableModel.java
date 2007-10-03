package org.redcross.sar.wp.messageLog;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.ICommunicatorIf;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.util.mso.DTG;
import org.redcross.sar.util.mso.Position;

/**
 * Table model for displaying message lines at top level edit panel
 * 
 * @author thomasl
 */
public class LineListTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;
	
	protected List<IMessageLineIf> m_messageLines = null;
	protected IDiskoWpMessageLog m_wpMessageLog = null;

	/**
	 * @param wp Message log work process
	 */
	public LineListTableModel(IDiskoWpMessageLog wp)
	{
		m_messageLines = new LinkedList<IMessageLineIf>();
		
		m_wpMessageLog = wp;
	}
	
	/**
	 * Returns number of columns, always 1
	 */
	public int getColumnCount() 
	{
		return 1;
	}
	
	/**
	 * 
	 */
	@Override
	public String getColumnName(int column)
	{
		return null;
	}

	/**
	 * Returns number of rows, which is the number of message lines in the model
	 */
	public int getRowCount() 
	{
		return m_messageLines.size();
	}

	/**
	 * {@link TableModel#getValueAt(int, int)}
	 */
	public Object getValueAt(int rowIndex, int coulumnIndex) 
	{
		if(m_messageLines.isEmpty())
		{
			return "";
		}
		else
		{
			IMessageIf message = MessageLogTopPanel.getCurrentMessage(true);
			IMessageLineIf line = m_messageLines.get(rowIndex);
			ICommunicatorIf singleReceiver = message.getSingleReceiver();
			String lineText = null;
			switch(line.getLineType())
			{
			case TEXT:
			{
				lineText = String.format(m_wpMessageLog.getText("ListItemText.text"),
						line.getLineText());
			}
			break;
			case POI:
			{
				IPOIIf poi = line.getLinePOI();
				if(poi != null)
				{
					String receiver = message.isBroadcast() || singleReceiver == null ? m_wpMessageLog.getText("Unit.text") :
						singleReceiver.getCommunicatorNumberPrefix() + " " + singleReceiver.getCommunicatorNumber();
					Position pos = poi.getPosition();
					lineText = String.format(m_wpMessageLog.getText("ListItemPOI.text"), 
							receiver, String.format("%1$.3g", pos.getPosition().x), String.format("%1$.3g", pos.getPosition().y), // TODO
							DTG.CalToDTG(line.getOperationTime()));
				}
			}
			break;
			case FINDING:
			{
				IPOIIf poi = line.getLinePOI();
				if(poi != null)
				{
					String type = poi.getTypeText();
					Position pos = line.getLinePOI().getPosition();
					lineText = String.format(m_wpMessageLog.getText("ListItemFinding.text"),
							type, String.format("%1$.3g", pos.getPosition().x), String.format("%1$.3g", pos.getPosition().y));
				}
			}
			break;
			case ASSIGNED:
			{
				IAssignmentIf assignment = line.getLineAssignment();
				lineText = String.format(m_wpMessageLog.getText("ListItemAssigned.text"),
						assignment.getTypeAndNumber(), DTG.CalToDTG(line.getOperationTime()));

			}
			break;
			case STARTED:
			{
				IAssignmentIf assignment = line.getLineAssignment();
				lineText = String.format(m_wpMessageLog.getText("ListItemStarted.text"),
						assignment.getTypeAndNumber(), DTG.CalToDTG(line.getOperationTime()));
			}
			break;
			case COMPLETE:
			{
				IAssignmentIf assignment = line.getLineAssignment();
				lineText = String.format(m_wpMessageLog.getText("ListItemCompleted.text"),
						assignment.getTypeAndNumber(), DTG.CalToDTG(line.getOperationTime()));
			}
			break;
			}
			
			return lineText;
		}
	}

	/**
	 * Remove all message lines from line list model
	 */
	public void clearMessageLines()
	{
		m_messageLines.clear();
	}

	/**
	 * Add a message line to the list model
	 * @param messageLine
	 */
	public void addMessageLine(IMessageLineIf messageLine)
	{
		m_messageLines.add(messageLine);
	}
	
	/**
	 * Get a message line from the list model
	 * @param index Line number
	 * @return The message line
	 */
	public IMessageLineIf getMessageLine(int index)
	{
		return m_messageLines.get(index);
	}
	
}
