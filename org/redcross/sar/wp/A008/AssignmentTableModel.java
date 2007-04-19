package org.redcross.sar.wp.A008;

import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.MsoEvent;
import org.redcross.sar.mso.data.*;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.util.mso.Selector;

import javax.swing.table.AbstractTableModel;
import java.util.EnumSet;
import java.util.Comparator;
import java.util.List;
/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 18.apr.2007
 * To change this template use File | Settings | File Templates.
 */

/**
 *
 */
public class AssignmentTableModel extends AbstractTableModel implements IMsoUpdateListenerIf
{
    private IMsoEventManagerIf m_eventManager;
    private IAssignmentListIf m_AssignmentList;

    public AssignmentTableModel(IMsoEventManagerIf anEventManager, IAssignmentListIf anAssignmentList)
    {
        m_eventManager = anEventManager;
        m_eventManager.addClientUpdateListener(this);
        m_AssignmentList = anAssignmentList;
    }

    public void handleMsoUpdateEvent(MsoEvent.Update e)
    {
        System.out.println(this.getClass() + " " + e + " " + e.getEventTypeMask());
        this.fireTableDataChanged();
    }

    private final EnumSet<IMsoManagerIf.MsoClassCode> myInterests = EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_ASSIGNMENT);

    public boolean hasInterestIn(IMsoObjectIf aMsoObject)
    {
        return myInterests.contains(aMsoObject.getMsoClassCode());
    }

    private final static AbstractMsoObject.StatusSelector<IAssignmentIf, IAssignmentIf.AssignmentStatus> m_readyAssignmentSelector = new AbstractMsoObject.StatusSelector<IAssignmentIf, IAssignmentIf.AssignmentStatus>(IAssignmentIf.AssignmentStatus.READY);

    private final static Comparator<IAssignmentIf> m_assignmentNumberComparator = new Comparator<IAssignmentIf>()
    {
        public int compare(IAssignmentIf u1, IAssignmentIf u2)
        {
            return u1.getNumber() - u2.getNumber();
        }
    };

    public int getRowCount()
    {
        return 1;
    }

    public int getColumnCount()
    {
        return 2;
    }

    private String readyAssignments()
    {
        String retVal = "";
        for (IAssignmentIf ass : m_AssignmentList.selectItems(m_readyAssignmentSelector, m_assignmentNumberComparator))
        {
            if (retVal.length() > 0)
            {
                retVal = retVal + ", ";
            }
            retVal = retVal + Integer.toString(ass.getNumber());
        }
        return retVal;
    }


    public Object getValueAt(int rowIndex, int columnIndex)
    {
        switch (columnIndex)
        {
            case 0:
                return readyAssignments();
            default:
                return "";
        }
    }

    @Override
    public String getColumnName(int column)
    {
        switch (column)
        {
            case 0:
                return "Klar";
            default:
                return "Pri";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        if (columnIndex == 0)
        {
            return String.class;
        } else
        {
            return String.class;
        }
    }


}
