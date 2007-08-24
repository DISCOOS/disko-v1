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
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.Update;
import org.redcross.sar.util.mso.DTG;
import org.redcross.sar.util.mso.Position;
import org.redcross.sar.util.mso.Selector;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

/**
 *
 */

public class LogTableModel extends AbstractTableModel implements IMsoUpdateListenerIf
{
    IMessageLogIf m_messageLog;
    List<IMessageIf> m_messageList;
    JTable m_table;

    private IMsoEventManagerIf m_eventManager;
    IDiskoWpMessageLog m_wpModule;

    private HashMap<Integer, Boolean> m_rowExpandedMap;
    
    private MessageRowSelectionListener m_selectionListener = null;

    public LogTableModel(JTable aTable, IDiskoWpMessageLog aModule, IMessageLogIf aMessageLog, MessageRowSelectionListener listener)
    {
        m_table = aTable;
        m_wpModule = aModule;
        m_eventManager = aModule.getMmsoEventManager();
        m_eventManager.addClientUpdateListener(this);
        m_messageLog = aMessageLog;
        m_rowExpandedMap = new HashMap<Integer, Boolean>();
        m_selectionListener = listener;
    }

    public int getRowCount()
    {
        return m_messageList.size();
    }

    public int getColumnCount()
    {
        return 7;
    }

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
//        	else
//        	{
//        		m_rowExpandedMap.put(new Integer(messageNr), new Boolean(false));
//        	}	
        }
    }

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

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        IMessageIf message = m_messageList.get(rowIndex);

        switch (columnIndex)
        {
            case 0:
            	Boolean expanded = isMessageExtended(message.getNumber());
            	StringBuilder string = new StringBuilder(Integer.toString(message.getNumber()));
            	if(m_selectionListener.numRows(rowIndex) > 1)
            	{
            		if(!expanded)
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
        					String receiver = singleReceiver == null ? m_wpModule.getText("Unit.text") 
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
                return ""; // TODO return tasks
            default:
                return message.getStatusText();
        }
    }

    @Override
    public String getColumnName(int column)
    {
    	return null;
    }

    public void handleMsoUpdateEvent(Update e)
    {
        buildTable();
        fireTableDataChanged();
    }

    private final EnumSet<IMsoManagerIf.MsoClassCode> myInterests = EnumSet.of(
    		IMsoManagerIf.MsoClassCode.CLASSCODE_MESSAGE,
    		IMsoManagerIf.MsoClassCode.CLASSCODE_MESSAGELINE);

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
     * Get whether or not the message is extended in the message log table, i.e. display entire message in log
     * @param messageNr
     * @return
     */
    public boolean isMessageExtended(int messageNr)
    {
    	if(!m_rowExpandedMap.containsKey(messageNr))
    	{
    		m_rowExpandedMap.put(messageNr, true);
    	}
    	
    	return m_rowExpandedMap.get(messageNr);
    }
    
    /**
     * Sets whether the message is extended in log view or not
     * @param messageNr
     * @param extended
     */
    public void setMessageExtended(int messageNr, boolean extended)
    {
    	m_rowExpandedMap.put(new Integer(messageNr), new Boolean(extended));
    }
}