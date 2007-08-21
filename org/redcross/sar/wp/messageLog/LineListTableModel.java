package org.redcross.sar.wp.messageLog;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.util.mso.DTG;
import org.redcross.sar.util.mso.Position;

/**
 * @author thomasl
 * Table model for displaying message lines at top level edit panel
 */
public class LineListTableModel extends AbstractTableModel
{
	protected List<IMessageLineIf> m_messageLines = null;
	protected IDiskoWpMessageLog m_wpMessageLog = null;

	public LineListTableModel(IDiskoWpMessageLog wp)
	{
		m_messageLines = new LinkedList<IMessageLineIf>();
		
		m_wpMessageLog = wp;
	}
	
	public int getColumnCount() 
	{
		return 1;
	}
	
	public String getColumnName(int column)
	{
		return null;
	}

	public int getRowCount() 
	{
		return m_messageLines.size();
	}

	public Object getValueAt(int rowIndex, int coulumnIndex) 
	{
		if(m_messageLines.isEmpty())
		{
			return "";
		}
		else
		{
			IMessageLineIf line = m_messageLines.get(rowIndex);
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
				Position pos = poi.getPosition();
				lineText = String.format(m_wpMessageLog.getText("ListItemPOI.text"), 
						"Enhet", String.format("%1$.3g", pos.getPosition().x), String.format("%1$.3g", pos.getPosition().y),
						DTG.CalToDTG(line.getOperationTime()));
			}
			break;
			case FINDING:
			{
				String type = line.getLinePOI().getTypeText();
				Position pos = line.getLinePOI().getPosition();
				lineText = String.format(m_wpMessageLog.getText("ListItemFinding.text"),
						type, String.format("%1$.3g", pos.getPosition().x), String.format("%1$.3g", pos.getPosition().y));
				
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

	public void clearMessageLines()
	{
		m_messageLines.clear();
	}

	public void addMessageLine(IMessageLineIf messageLine)
	{
		m_messageLines.add(messageLine);
	}
	
	public IMessageLineIf getMessageLine(int index)
	{
		return m_messageLines.get(index);
	}
	
}
