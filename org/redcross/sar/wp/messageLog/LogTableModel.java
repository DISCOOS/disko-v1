package org.redcross.sar.wp.messageLog;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLogIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent;
import org.redcross.sar.util.mso.Selector;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 25.jun.2007
 * To change this template use File | Settings | File Templates.
 */

/**
 *
 */
public class LogTableModel extends AbstractTableModel implements IMsoUpdateListenerIf
{
    IMessageLogIf m_messageLog;
    List<IMessageIf> m_messageList;
    JTable m_table;

    private IMsoEventManagerIf m_eventManager;
    DiskoWpMessageLogImpl m_wpModule;


    public LogTableModel(JTable aTable, DiskoWpMessageLogImpl aModule, IMessageLogIf aMessageLog)
    {
        m_table = aTable;
        m_wpModule = aModule;
        m_eventManager = aModule.getMmsoEventManager();
        m_eventManager.addClientUpdateListener(this);
        m_messageLog = aMessageLog;
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
    }


    public Object getValueAt(int rowIndex, int columnIndex)
    {
        IMessageIf message = m_messageList.get(rowIndex);
        switch (columnIndex)
        {
            case 0:
                return Integer.toString(message.getNumber());
            case 1:
                return message.getDTG();
            case 2:
                return "";
            case 3:
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
        return m_wpModule.getText(MessageFormat.format("LogTable_hdr_{0}.text", column));
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

}



