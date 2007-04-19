package org.redcross.sar.wp.A008;

import org.redcross.sar.mso.data.*;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.util.mso.Selector;

import javax.swing.table.AbstractTableModel;
import java.util.*;
/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 12.apr.2007
 * To change this template use File | Settings | File Templates.
 */

/**
 *
 */
public class UnitTableModel extends AbstractTableModel implements IMsoUpdateListenerIf
{
    private IMsoEventManagerIf m_eventManager;
    private IUnitListIf m_unitList;
    private ArrayList<IUnitIf> units = new ArrayList<IUnitIf>();


    public UnitTableModel(IMsoEventManagerIf anEventManager, IUnitListIf aUnitList)
    {
        m_eventManager = anEventManager;
        m_eventManager.addClientUpdateListener(this);
        m_unitList = aUnitList;
    }


    public void handleMsoUpdateEvent(MsoEvent.Update e)
    {
        System.out.println(this.getClass() + " " + e + " " + e.getEventTypeMask());
        if (e.getSource() instanceof IUnitIf)
        {
            buildTable();
        }
        this.fireTableDataChanged();
    }

    private final EnumSet<IMsoManagerIf.MsoClassCode> myInterests =  EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_UNIT,IMsoManagerIf.MsoClassCode.CLASSCODE_ASSIGNMENT);

    public boolean hasInterestIn(IMsoObjectIf aMsoObject)
    {
        return myInterests.contains(aMsoObject.getMsoClassCode());
    }

    private final static AbstractMsoObject.StatusSetSelector<IUnitIf,IUnitIf.UnitStatus> m_activeUnitSelector =
            new AbstractMsoObject.StatusSetSelector<IUnitIf, IUnitIf.UnitStatus>(EnumSet.range(IUnitIf.UnitStatus.READY, IUnitIf.UnitStatus.WORKING));

    private final static Comparator<IUnitIf> m_unitNumberComparator = new Comparator<IUnitIf>()
    {
        public int compare(IUnitIf u1, IUnitIf u2)
        {
            return u1.getNumber() - u2.getNumber();
        }
    };

    void buildTable()
    {
        units.clear();
        for (IUnitIf unit : m_unitList.selectItems(m_activeUnitSelector, m_unitNumberComparator))
        {
            addUnit(unit);
        }


    }

    @Override
    public String getColumnName(int column)
    {
        switch (column)
        {
            case 0:
                return "Enhet";
            case 1:
                return "Neste";
            case 2:
                return "Tildelt";
            case 3:
                return "Startet";
            case 4:
                return "Søkt";
            default:
                return "Info";
        }

    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        if(columnIndex==0)
        {
            return IUnitIf.class;
        }
        else if(columnIndex==5)
        {
            return String.class;
        }
        else
        {
            return List.class;
        }
    }

    public int getRowCount()
    {
        return units.size();
    }

    public int getColumnCount()
    {
        return 6;
    }

    public void addUnit (IUnitIf aUnit)
    {
        units.add(aUnit);
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        IUnitIf un=units.get(rowIndex);
        switch(columnIndex)
        {
            case 0:
                return un;
            case 1:
                return un.getAllocatedAssignments();
            case 2:
                return un.getAssignedAssignments();
            case 3:
                return un.getExecutingAssigment();
            case 4:
                return un.getFinishedAssigment();
            case 5:
                return un.getRemarks();  // todo getInfo
            default: return "";
        }
    }

}
