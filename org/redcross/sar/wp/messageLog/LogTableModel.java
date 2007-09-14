package org.redcross.sar.wp.messageLog;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.ICommunicatorIf;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IMessageLineListIf;
import org.redcross.sar.mso.data.IMessageLogIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.data.ITaskIf;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;
import org.redcross.sar.mso.data.IPOIIf.POIType;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.Update;
import org.redcross.sar.util.mso.DTG;
import org.redcross.sar.util.mso.Position;
import org.redcross.sar.util.mso.Selector;
import org.redcross.sar.wp.messageLog.ChangeTasksDialog.TaskSubType;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

/**
 * Table model providing log table with data
 */
public class LogTableModel extends AbstractTableModel implements IMsoUpdateListenerIf
{
	private static final long serialVersionUID = 1L;
	
	IMessageLogIf m_messageLog;
    List<IMessageIf> m_messageList;
    JTable m_table;

    private IMsoEventManagerIf m_eventManager;
    IDiskoWpMessageLog m_wpModule;

    private HashMap<Integer, Boolean> m_rowExpandedMap;
    
//    private MessageRowSelectionListener m_selectionListener = null;

    /**
     * @param aTable Log table
     * @param aModule Message log work process
     * @param aMessageLog Message log reference
     * @param listener Selection listener
     */
    public LogTableModel(JTable aTable, IDiskoWpMessageLog aModule, IMessageLogIf aMessageLog, MessageRowSelectionListener listener)
    {
        m_table = aTable;
        m_wpModule = aModule;
        m_eventManager = aModule.getMmsoEventManager();
        m_eventManager.addClientUpdateListener(this);
        m_messageLog = aMessageLog;
        m_rowExpandedMap = new HashMap<Integer, Boolean>();
//        m_selectionListener = listener;
    }

    /**
     * 
     */
    public int getRowCount()
    {
        return m_messageList.size();
    }

    /**
     *
     */
    public int getColumnCount()
    {
        return 7;
    }

    /**
     * Get messages, update expanded hash map 
     */
    void buildTable()
    {
        m_messageList = m_messageLog.selectItems(m_messageSelector, m_lineNumberComparator);
        
        // Update hash map
        HashMap<Integer, Boolean> tempMap = new HashMap<Integer, Boolean>(m_rowExpandedMap);
        m_rowExpandedMap.clear();
        int numMessages = m_messageList.size();
        for(int i=0; i<numMessages; i++)
        {
        	int messageNr = m_messageList.get(i).getNumber();
        	if(tempMap.containsKey(messageNr))
        	{
        		Boolean expanded = tempMap.get(messageNr);
        		m_rowExpandedMap.put(messageNr, expanded);
        	}
        	else
        	{
        		m_rowExpandedMap.put(messageNr, new Boolean(false));
        	}
        }
    }

    /**
     * 
     */
    @Override
	public Class<?> getColumnClass(int c)
    {
    	switch(c)
    	{
    		case 4:
    			return JTextPane.class;
    		default:
    			return String.class;

    	}
    }

    /**
     * Get value of message field
     * @param rowIndex Message number
     * @param columnIndex Index of field
     */
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        IMessageIf message = m_messageList.get(rowIndex);

        switch (columnIndex)
        {
            case 0:
            	Boolean expanded = isMessageExpanded(message.getNumber());
            	StringBuilder string = new StringBuilder(Integer.toString(message.getNumber()));
            	if(numRows(rowIndex) > 1)
            	{
            		if(expanded)
                	{
                		string.append(" -");
                	}
                	else
                	{
                		string.append(" +");
                	}
            	}
            	
                return string.toString();
            case 1:
                return DTG.CalToDTG(message.getOccuredTime());
            case 2:
            	ICommunicatorIf sender = message.getSender();
            	if(sender == null)
            	{
            		sender = (ICommunicatorIf)m_wpModule.getMsoManager().getCmdPost();
            	}
            	return sender.getCommunicatorNumberPrefix() + " " + sender.getCommunicatorNumber();
            case 3:
            	if(message.isBroadcast())
            	{
            		return m_wpModule.getText("BroadcastLabel.text");
            	}
            	else
            	{
            		ICommunicatorIf receiver = message.getSingleReceiver();
            		if(receiver == null)
            		{
            			receiver = (ICommunicatorIf)m_wpModule.getMsoManager().getCmdPost();
            		}
            		return receiver.getCommunicatorNumberPrefix() + " " + receiver.getCommunicatorNumber();
            	}
            case 4:
            	StringBuilder stringBuilder = new StringBuilder();
            	IMessageLineListIf lines = message.getMessageLines();
            	ICommunicatorIf singleReceiver = message.getSingleReceiver();
            	for(IMessageLineIf line : lines.getItems())
            	{
            		String lineText = "";
            		switch(line.getLineType())
        			{
        			case TEXT:
        			{
        				lineText = String.format(m_wpModule.getText("ListItemText.text"),
        						line.getLineText());
        			}
        			break;
        			case POI:
        			{
        				IPOIIf poi = line.getLinePOI();
        				if(poi != null)
        				{
        					String receiver = message.isBroadcast() || singleReceiver == null ? m_wpModule.getText("Unit.text") 
        							: singleReceiver.getCommunicatorNumberPrefix() + " " + singleReceiver.getCommunicatorNumber();
        					Position pos = poi.getPosition();
        					lineText = String.format(m_wpModule.getText("ListItemPOI.text"), 
        							receiver, 
        							String.format("%1$.3g", pos.getPosition().x), String.format("%1$.3g", pos.getPosition().y),
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
        					lineText = String.format(m_wpModule.getText("ListItemFinding.text"),
        							type, String.format("%1$.3g", pos.getPosition().x), String.format("%1$.3g", pos.getPosition().y));
        				}
        			}
        			break;
        			case ASSIGNED:
        			{
        				IAssignmentIf assignment = line.getLineAssignment();
        				lineText = String.format(m_wpModule.getText("ListItemAssigned.text"),
        						assignment.getTypeAndNumber(), DTG.CalToDTG(line.getOperationTime()));

        			}
        			break;
        			case STARTED:
        			{
        				IAssignmentIf assignment = line.getLineAssignment();
        				lineText = String.format(m_wpModule.getText("ListItemStarted.text"),
        						assignment.getTypeAndNumber(), DTG.CalToDTG(line.getOperationTime()));
        			}
        			break;
        			case COMPLETE:
        			{
        				IAssignmentIf assignment = line.getLineAssignment();
        				lineText = String.format(m_wpModule.getText("ListItemCompleted.text"),
        						assignment.getTypeAndNumber(), DTG.CalToDTG(line.getOperationTime()));
        			}
        			break;
        			}
            		
            		stringBuilder.append(lineText + "LINEEND");
            	}
                return stringBuilder.toString().split("LINEEND");
            case 5:
            	StringBuilder taskBuilder = new StringBuilder();
            	for(ITaskIf task : message.getMessageTasksItems())
            	{
            		if(ChangeTasksDialog.getSubType(task) == TaskSubType.FINDING)
            		{
            			String taskString = null;
            			IMessageLineIf line = message.findMessageLine(MessageLineType.FINDING, false);
            			if(line != null)
            			{
            				
            				IPOIIf poi = line.getLinePOI();
            				if(poi != null && poi.getType() == POIType.SILENT_WITNESS)
            				{
            					taskString = String.format(m_wpModule.getText("TaskSubType.FINDING.text"),
            							m_wpModule.getText("SilentWitness.text"));
            				}
            				else
            				{
            					taskString = String.format(m_wpModule.getText("TaskSubType.FINDING.text"),
            							m_wpModule.getText("Finding.text"));
            				}
            			}
            			else
        				{
            				// Set task finding to finding if no message line added
        					taskString = String.format(m_wpModule.getText("TaskSubType.FINDING.text"),
        							m_wpModule.getText("Finding.text"));
        				}
        				taskBuilder.append(taskString);
            		}
            		else
            		{
            			taskBuilder.append(task.getTaskText());
            		}
            		
            		taskBuilder.append("\n");
            	}
                return taskBuilder.toString().split("\\n");
            default:
                return message.getStatusText();
        }
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
     * Rebuild table data model when MSO changes
     */
    public void handleMsoUpdateEvent(Update e)
    {
        buildTable();
        fireTableDataChanged();
        updateRowHeights();
    }

    private final EnumSet<IMsoManagerIf.MsoClassCode> myInterests = EnumSet.of(
    		IMsoManagerIf.MsoClassCode.CLASSCODE_MESSAGE,
    		IMsoManagerIf.MsoClassCode.CLASSCODE_MESSAGELINE);

    /**
     * Interested in when messages and message lines updates
     */
    public boolean hasInterestIn(IMsoObjectIf aMsoObject)
    {
        return myInterests.contains(aMsoObject.getMsoClassCode());
    }

    private final Selector<IMessageIf> m_messageSelector = new Selector<IMessageIf>()
    {
        public boolean select(IMessageIf aMessage)
        {
            return true;
        }
    };

    private final static Comparator<IMessageIf> m_lineNumberComparator = new Comparator<IMessageIf>()
    {
        public int compare(IMessageIf m1, IMessageIf m2)
        {
            return m1.getNumber() - m2.getNumber();
        }
    };
    
    /**
     * @param messageNr
     * @return Whether or not the message is extended in the message log table, i.e. display entire message in log
     */
    public Boolean isMessageExpanded(int messageNr)
    {
    	return m_rowExpandedMap.get(messageNr);
    }
    
    /**
     * Sets whether the message is extended in log view or not
     * @param messageNr
     * @param expanded
     */
    public void setMessageExpanded(int messageNr, boolean expanded)
    {
    	m_rowExpandedMap.put(new Integer(messageNr), new Boolean(expanded));
    }

	public void updateRowHeights()
	{
		for(int i = 0; i < m_messageList.size(); i++)
		{
			IMessageIf message = m_messageList.get(i);
			Boolean expanded = m_rowExpandedMap.get(message.getNumber());
			
			if(expanded)
			{
				setRowExpanded(i);
			}
			else
			{
				setRowCollapsed(i);
			}
		}
	}
	
	/**
	 * Expands a row so that it encompasses all text in message lines
	 * @param rowIndex Row identifier
	 */
	public void setRowExpanded(int rowIndex)
	{
		// Calculate row height so that all text is visible in cell without changing column width
		int defaultRowHeight = 18; //m_messageTable.getRowHeight();
		int numRows = numRows(rowIndex);
		int rowHeight = defaultRowHeight * numRows + (numRows - 1) * 2 + 4;
		m_table.setRowHeight(rowIndex, rowHeight);
	}
	
	/**
	 * Collapses a row to the default size
	 * @param rowIndex Row identifier
	 */
	public void setRowCollapsed(int rowIndex)
	{
		m_table.setRowHeight(rowIndex, m_table.getRowHeight());
	}
	
	/**
	 * @param rowIndex Identifies the message line
	 * @return Number of rows in the table need to display the entire contents of the message lines
	 *		or task, whichever is longer
	 */
	public int numRows(int rowIndex)
	{
		JTextArea textArea = new JTextArea();
		Font font = textArea.getFont();
		FontMetrics fm = textArea.getFontMetrics(font);
		
		// Message lines
		int columnWidth = m_table.getColumnModel().getColumn(4).getWidth();
		int numMessageLines = 0;
		String[] messageLineStrings = (String[])getValueAt(rowIndex, 4);
		for(String line : messageLineStrings)
		{
			int lineWidth = fm.stringWidth(line);
			numMessageLines += (lineWidth/columnWidth + 1);
		}
		
		// Tasks
		columnWidth = m_table.getColumnModel().getColumn(5).getWidth();
		String[] taskStrings = (String[])getValueAt(rowIndex, 5);
		int numTaskLines = 0;
		for(String task : taskStrings)
		{
			int lineWidth = fm.stringWidth(task);
			numTaskLines += (lineWidth/columnWidth + 1);
		}
		
		return Math.max(numMessageLines, numTaskLines);
	}
}