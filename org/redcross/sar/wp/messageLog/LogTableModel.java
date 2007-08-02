package org.redcross.sar.wp.messageLog;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLogIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent;
import org.redcross.sar.util.mso.DTG;
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

    private HashMap<Integer, Boolean> m_rowSelectionMap;

    public LogTableModel(JTable aTable, IDiskoWpMessageLog aModule, IMessageLogIf aMessageLog)
    {
        m_table = aTable;
        m_wpModule = aModule;
        m_eventManager = aModule.getMmsoEventManager();
        m_eventManager.addClientUpdateListener(this);
        m_messageLog = aMessageLog;
        m_rowSelectionMap = new HashMap<Integer, Boolean>();
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

        // Update hashmap
        int numMessages = m_messageList.size();
        for(int i=0; i<numMessages; i++)
        {
        	int messageNr = m_messageList.get(i).getNumber();
        	if(!m_rowSelectionMap.containsKey(Integer.valueOf(messageNr)))
        	{
        		m_rowSelectionMap.put(Integer.valueOf(messageNr), Boolean.valueOf(false));
        	}
        }
    }

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
            	Boolean expand = (Boolean)m_rowSelectionMap.get(message.getNumber());
            	StringBuilder string = new StringBuilder(Integer.toString(message.getNumber()));
            	string.append(" ");
            	if(expand)
            	{
            		string.append("-");
            	}
            	else
            	{
            		string.append("+");
            	}
                return string.toString();
            case 1:
                return DTG.CalToDTG(message.getOccuredTime());
            case 2:
            	//return message.getFrom();
                return "";
            case 3:
            	// return message.getTo();
                return "";
            case 4:
                return message.getLines();
            case 5:
                return "";
            default:
                return message.getStatus().name();
        }
    }

    @Override
    public String getColumnName(int column)
    {
    	return null;
        //return m_wpModule.getText(MessageFormat.format("LogTable_hdr_{0}.text", column));
    }

    public void handleMsoUpdateEvent(MsoEvent.Update e)
    {
        buildTable();
        fireTableDataChanged();
    }

    private final EnumSet<IMsoManagerIf.MsoClassCode> myInterests = EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_MESSAGE, IMsoManagerIf.MsoClassCode.CLASSCODE_MESSAGELINE);


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

	public HashMap getRowMap()
	{
		return m_rowSelectionMap;
	}

}



