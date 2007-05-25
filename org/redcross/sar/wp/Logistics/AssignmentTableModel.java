package org.redcross.sar.wp.logistics;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.AbstractMsoObject;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IAssignmentListIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent;

import javax.swing.table.AbstractTableModel;
import java.util.Comparator;
import java.util.EnumSet;

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
        fireTableDataChanged();
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

    private java.util.List readyAssignments()
    {
        return m_AssignmentList.selectItems(m_readyAssignmentSelector, m_assignmentNumberComparator);
    }


    public Object getValueAt(int rowIndex, int columnIndex)
    {
        switch (columnIndex)
        {
            case 0:
                return readyAssignments();
            default:
                return new java.util.ArrayList();
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
            return java.util.List.class;
        } else
        {
            return java.util.List.class;
        }
    }


}
