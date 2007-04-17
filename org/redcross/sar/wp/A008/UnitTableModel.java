package org.redcross.sar.wp.A008;

import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IUnitIf;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 12.apr.2007
 * To change this template use File | Settings | File Templates.
 */

/**
 *
 */
public class UnitTableModel extends AbstractTableModel
{
    public UnitTableModel()
    {
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

    ArrayList<IUnitIf> units = new ArrayList<IUnitIf>();


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
