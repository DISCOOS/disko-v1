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
            return TmpUnit.class;
        }
        else if(columnIndex==5)
        {
            return String.class;
        }
        else
        {
            return TmpAssignment[].class;
        }
    }

    ArrayList<TmpUnit> units = new ArrayList();


    public int getRowCount()
    {
        return units.size();
    }

    public int getColumnCount()
    {
        return 6;
    }

    public void addUnit (TmpUnit aUnit)
    {
        units.add(aUnit);
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        TmpUnit un=units.get(rowIndex);
        switch(columnIndex)
        {
            case 0:
                return un;
            case 1:
                return un.getNeste();
            case 2:
                return un.getTildelt();
            case 3:
                return un.getStartet();
            case 4:
                return un.getSøkt();
            case 5:
                return un.getInfo();
            default: return "";
        }
    }

    public static class TmpUnit
    {
        int enhnr;
        IUnitIf.UnitType type;
        String info;

        public String getInfo()
        {
            return info;
        }

        List neste = new ArrayList();
        List tildelt = new ArrayList();
        List startet = new ArrayList();
        List søkt = new ArrayList();

        public TmpUnit(int aNr, IUnitIf.UnitType aType)
        {
            enhnr = aNr;
            type = aType;
        }

        public int getEnhnr()
        {
            return enhnr;
        }

        public IUnitIf.UnitType getType()
        {
            return type;
        }

        public TmpAssignment[] getNeste()
        {
            return (TmpAssignment[])neste.toArray(new TmpAssignment[neste.size()]);
        }

        public TmpAssignment[] getTildelt()
        {
            return (TmpAssignment[])tildelt.toArray(new TmpAssignment[tildelt.size()]);
        }

        public TmpAssignment[] getStartet()
        {
            return (TmpAssignment[])startet.toArray(new TmpAssignment[startet.size()]);
        }

        public TmpAssignment[] getSøkt()
        {
            return (TmpAssignment[])søkt.toArray(new TmpAssignment[søkt.size()]);
        }

         public List<TmpAssignment> getNesteList()
        {
            return neste;
        }

        public List<TmpAssignment> getTildeltList()
        {
            return tildelt;
        }

        public List<TmpAssignment> getStartetList()
        {
            return startet;
        }

        public List<TmpAssignment> getSøktList()
        {
            return søkt;
        }
       


    }

    public static class TmpAssignment
    {
        int assgng;
        int type;
        IAssignmentIf.AssignmentStatus status;

        public TmpAssignment(int assgng, int type, IAssignmentIf.AssignmentStatus status)
        {
            this.assgng = assgng;
            this.type = type;
            this.status = status;
        }

        public int getAssgng()
        {
            return assgng;
        }

        public int getType()
        {
            return type;
        }

        public IAssignmentIf.AssignmentStatus getStatus()
        {
            return status;
        }
    }
}
