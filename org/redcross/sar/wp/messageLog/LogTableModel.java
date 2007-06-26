package org.redcross.sar.wp.messageLog;

import org.redcross.sar.mso.data.IMessageLogIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 25.jun.2007
 * To change this template use File | Settings | File Templates.
 */

/**
 *
 */
public class LogTableModel  extends AbstractTableModel implements IMsoUpdateListenerIf
{
    public LogTableModel(JTable aTable, DiskoWpMessageLogImpl aModule, IMessageLogIf aMessageLog)
    {

    }

    public int getRowCount()
    {
        return 7;
    }

    public int getColumnCount()
    {
        return 7;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return "cell " + rowIndex + " " + columnIndex;
    }

    public void handleMsoUpdateEvent(MsoEvent.Update e)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean hasInterestIn(IMsoObjectIf aMsoObject)
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
